package cn.uc.storm.test;

import java.util.List;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import cn.uc.storm.bolts.PassBolt;
import cn.uc.storm.spouts.MemSpout;
import cn.uc.storm.utils.Env;
import cn.uc.storm.utils.Helper;
import cn.uc.storm.utils.PassData;

import com.google.common.collect.Lists;

/**
 * 框架基础延迟测试:
 * 目标:测试storm基础框架在数据传输大小,通过的网络节点数(worker数量),
 * 		数据流经过的结点数(step+2),spout最大pending数4个因素如何影响，
 *     tuple的处理延迟，总体吞吐，平均tuple处理数，GC状况等指标.
 * 要求:每个Supervisor上只配置一个worker.
 * 由此,保证本地没有回环.
 * 
 * @author qiujw@ucweb.com
 *
 */
public class StormBoltMax implements BaseTest {
	private int spout;
	private int worker;
	private int parallelism;
	private int pending;
	
	@Override
	public void init(Map conf){
		spout = Helper.getInteger(conf, "spout", 1);
		worker = Helper.getInteger(conf, "worker", 1);
		parallelism = Helper.getInteger(conf, "para", 1);
		pending = Helper.getInteger(conf, "pending", 100);
	}
	@Override
	public String run(){
		String testState = "StormBoltMax"+"_spout-"+spout+"_worker-"+worker+"_para-"+parallelism+"_pending-"+pending;
//		int step = params.getInt("step",1);
		
		TopologyBuilder builder = new TopologyBuilder();
		String spoutId = "memspout";
		builder.setSpout(spoutId, new MemSpout(),spout);
		String pre = spoutId;
		builder.setBolt("bolt1", new PassBolt(),parallelism).shuffleGrouping(pre);
		
		Config conf = new Config();
		//***
		List<String> list = Lists.newArrayList();
		list.add(PassData.class.getName());
		conf.put(Config.TOPOLOGY_KRYO_REGISTER, list);
		
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, pending);
		//***
		conf.put(Env.dataSize, 1);
		conf.put(Config.TOPOLOGY_WORKERS, worker);
		conf.put(Env.logPrefix, testState+"_");
		String topologyId = testState;
		Env.sumbitTopology(topologyId, conf , builder.createTopology());
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
		StormBoltMax stormBoltMax = new StormBoltMax();
		stormBoltMax.init(map);
		stormBoltMax.run();
	}
	
}
