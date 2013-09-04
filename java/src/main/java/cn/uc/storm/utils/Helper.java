package cn.uc.storm.utils;

import java.util.Map;

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
}
