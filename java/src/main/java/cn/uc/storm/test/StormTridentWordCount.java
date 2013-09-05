package cn.uc.storm.test;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.builtin.Count;
import storm.trident.testing.MemoryMapState;
import storm.trident.tuple.TridentTuple;
import backtype.storm.Config;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import cn.uc.storm.spouts.TridentSpout;
import cn.uc.storm.utils.Env;
import cn.uc.storm.utils.Helper;
import cn.uc.storm.utils.PassData;

public class StormTridentWordCount implements BaseTest {
	public static class Split extends BaseFunction {
        @Override
        public void execute(TridentTuple tuple, TridentCollector collector) {
            String sentence = tuple.getString(0);
            for(String word: sentence.split(" ")) {
                collector.emit(new Values(word));                
            }
        }
    }
	private int batch = 100;
	private int spout = 1;
	private int agg = 1;
	private int worker = 5;
	@Override
	public void init(Map conf) {
		batch = Helper.getInteger(conf, "batch", 100);
		spout = Helper.getInteger(conf, "spout", 1);
		agg = Helper.getInteger(conf, "agg", 1);
		worker = Helper.getInteger(conf, "worker", 5);
	}

	@Override
	public String run() {
		String testState = "StormTridentWordCount"+"_batch-"+batch+"_spout-"+spout+"_agg-"+agg+"_worker-"+worker;

		TridentTopology topology = new TridentTopology();        
        TridentState wordCounts =
              topology.newStream("spout1", new TridentSpout(batch))
                .parallelismHint(spout)
                .each(new Fields("sentence"), new Split(), new Fields("word"))
                .groupBy(new Fields("word"))
                .persistentAggregate(new MemoryMapState.Factory(),
                                     new Count(), new Fields("count"))         
                .parallelismHint(agg);
        
        Config conf = new Config();
		//***
		List<String> list = Lists.newArrayList();
		list.add(PassData.class.getName());
		conf.put(Config.TOPOLOGY_KRYO_REGISTER, list);
		
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 100);
		//***
		conf.put(Config.TOPOLOGY_WORKERS, worker);
		conf.put(Env.logPrefix, testState+"_");
		String topologyId = testState;
		Env.sumbitTopology(topologyId, conf , topology.build());
		Helper.printTopology(topologyId);
		return topologyId;
	}

	@Override
	public int waitForComplete() {
		Utils.sleep(30000);
		return 0;
	}
	
	static public void main(String[] args){
		Map map = Helper.changeMap(args);
		StormTridentWordCount stormTest = new StormTridentWordCount();
		stormTest.init(map);
		stormTest.run();
	}
}
