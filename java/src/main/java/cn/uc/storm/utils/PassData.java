package cn.uc.storm.utils;

import java.util.Map;

public class PassData {
	public Byte[] data;
	
	public PassData(){
		
	}
	
	private PassData(int size){
		data = new Byte[size];
	}
	
	static public PassData getPassData(Map conf){
		Integer kSize = Integer.valueOf(conf.get(Env.dataSize).toString());
		if(kSize == null) kSize = 1;
		return new PassData(kSize*1024);
	}
	static public int getPassDataSize(Map conf){
		Integer kSize = Integer.valueOf(conf.get(Env.dataSize).toString());
		if(kSize == null) kSize = 1;
		return kSize;
	}
}
