package cn.uc.storm.utils;

import java.util.Map;

import org.apache.thrift7.TException;

import backtype.storm.generated.Bolt;
import backtype.storm.generated.ExecutorSummary;
import backtype.storm.generated.NotAliveException;
import backtype.storm.generated.StormTopology;
import backtype.storm.generated.Nimbus.Client;
import backtype.storm.generated.TopologyInfo;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;

import com.google.common.collect.Maps;

public class Helper {
	static public final int _1k = 1024;
	static public final int _10k = 1024*10;
	static public final int _100k = 1024*100;

	static public Map changeMap(String[] args){
		Map map = null;
		if( args== null) return map;
		map = Maps.newHashMap();
		int now =0;
		while(now+1 < args.length){
			map.put(args[now], args[now+1]);
			now+=2;
		}
		if("true".equals(map.get("local")))Env.local = true;
		return map;
	}
	static public int getInteger(Map conf,String key,int defaultValue){
		Object temp = conf.get(key);
		if( temp!=null ){
			if(temp instanceof String) return Integer.valueOf((String)temp);
			else if(temp instanceof Integer) return (Integer)temp;
		}
		return defaultValue;
	}
	static public void printTopology(String topologyId){
		Utils.sleep(1000);
		Map stormConf = Utils.readStormConfig();
		Client client = NimbusClient.getConfiguredClient(stormConf).getClient();
		TopologyInfo temp= null;
		try {
			temp = client.getTopologyInfo(topologyId);
		} catch (NotAliveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( temp!=null){
			System.out.println(temp.toString());
		}
		else{
			System.out.println("not found this"+topologyId);
		}
	}
}
