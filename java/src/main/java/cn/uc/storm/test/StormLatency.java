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
public class StormLatency implements BaseTest {
	private int step;
	private int size;
	private int worker;
	private int parallelism;
	private int pending;
	private int acker;
	
	@Override
	public void init(Map conf){
		step = Helper.getInteger(conf, "step", 1);
		size = Helper.getInteger(conf, "size", 1);
		worker = Helper.getInteger(conf, "worker", 1);
		parallelism = Helper.getInteger(conf, "para", 1);
		pending = Helper.getInteger(conf, "pending", 100);
		acker = Helper.getInteger(conf, "acker", 1);
	}
	@Override
	public String run(){
		String testState = "StormLatency"+"_step-"+step+"_size-"+size+"_worker-"+worker
				+"_para-"+parallelism+"_pending-"+pending+"_acker-"+acker;
//		int step = params.getInt("step",1);
		
		TopologyBuilder builder = new TopologyBuilder();
		String spoutId = "memspout";
		builder.setSpout(spoutId, new MemSpout(),1);
		String pre = spoutId;
		for(int i =0;i<step;i++){
			String nowId = "passBolt"+i;
			builder.setBolt(nowId, new PassBolt(),parallelism).shuffleGrouping(pre);
			pre = nowId;
		}
		
		Config conf = new Config();
		//***
		List<String> list = Lists.newArrayList();
		list.add(PassData.class.getName());
		conf.put(Config.TOPOLOGY_KRYO_REGISTER, list);
		
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, pending);
		//***
		conf.put(Env.dataSize, size);
		conf.put(Config.TOPOLOGY_WORKERS, worker);
		conf.put(Env.logPrefix, testState+"_");
		conf.put(Config.TOPOLOGY_ACKER_EXECUTORS, acker);
		
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
		StormLatency stormLatency = new StormLatency();
		stormLatency.init(map);
		stormLatency.run();
	}
	
}
