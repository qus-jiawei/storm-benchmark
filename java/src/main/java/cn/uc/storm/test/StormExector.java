package cn.uc.storm.test;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;
import cn.uc.storm.bolts.PassBolt;
import cn.uc.storm.spouts.MemSpout;
import cn.uc.storm.utils.Env;
import cn.uc.storm.utils.Helper;
import cn.uc.storm.utils.PassData;

public class StormExector implements BaseTest{

	@Override
	public void init(Map conf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String run() {
		// TODO Auto-generated method stub
		
		TopologyBuilder builder = new TopologyBuilder();
		String spoutId = "memspout";
		builder.setSpout(spoutId, new MemSpout(),1);
		String pre = spoutId;
		
		builder.setBolt("bolt2",new PassBolt(),1).shuffleGrouping("bolt1");
		builder.setBolt("bolt1",new PassBolt(),1).shuffleGrouping("memspout");
		
		Config conf = new Config();
		
		Env.sumbitTopology("top", conf , builder.createTopology());
//		Helper.printTopology(topologyId);
		return "top";
	}

	@Override
	public int waitForComplete() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	static public void main(String[] args){
		Map map = Helper.changeMap(args);
		StormExector stormExector = new StormExector();
		stormExector.init(map);
		stormExector.run();
	}
	
}
