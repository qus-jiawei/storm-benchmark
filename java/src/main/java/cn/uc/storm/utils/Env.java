package cn.uc.storm.utils;

import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.utils.Utils;

public class Env {
	//local
	static public boolean local = false;
	static public final String localLogPath = "D:/storm/test/";
	
	//linux
	static public final String linuxLogPath = "/home/qiujw/storm_test/test_data/";
	
	static public final String logPrefix = "storm.test.log.prefix";
	static public final String dataSize = "storm.test.data.size";
	
	static public String getLogPath(Map conf,String fileName){
		String temp = (String) conf.get(logPrefix);
		if( temp != null) fileName = temp+fileName;
		return local? (localLogPath+fileName): (linuxLogPath+fileName);
	}

	public static void sumbitTopology(String string, Config conf,StormTopology createTopology) {
		if(local){
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(string,conf,createTopology);
			Utils.sleep(10000);
//			cluster.shutdown();
		}
		else{
			try {
				StormSubmitter.submitTopology(string, conf, createTopology);
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		}
	}
}
