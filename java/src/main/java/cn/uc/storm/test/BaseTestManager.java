package cn.uc.storm.test;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift7.TException;

import backtype.storm.generated.Nimbus.Client;
import backtype.storm.generated.NotAliveException;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BaseTestManager {
	private static Logger LOG = Logger.getLogger(BaseTestManager.class);
	class BaseTestParam{
		public String paramsName;
		public List list;
		public BaseTestParam(String paramsName,List list){
			this.paramsName = paramsName;
			this.list = list;
		}
	}
	private List<BaseTestParam> params = Lists.newArrayList();
	private int[] now;
	public int max = 1;
	private boolean hasNext(){
		int index = 0;
		now[index]++;
		while(now[index]>=params.get(index).list.size()){
			now[index] = 0;
			//进位
			index++;
			if(index<now.length){
				now[index]++;
			}
			else break;
		}
		return index < now.length;
	}
	public void runAllTest(String className,Map<String,Object> conf) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NotAliveException, TException{
		//init all params
		for(Map.Entry<String,Object> entry:conf.entrySet()){
			String key = entry.getKey();
			if(key.startsWith(className) && key.endsWith("list")){
				String paramsName = key.substring(className.length()+1,key.length()-"list".length()-1);
				BaseTestParam param = new BaseTestParam(paramsName,(List)entry.getValue());
				params.add(param);
			}
		}
		now = new int[params.size()];
		now[0]=-1;
		Class c = Class.forName(className);
		
		//init nimbus client
		Map stormConf = Utils.readStormConfig();
        conf.putAll(stormConf);
		Client client = NimbusClient.getConfiguredClient(stormConf).getClient();
		
		while(hasNext()){
			Map map = Maps.newHashMap();
			for(int i =0;i<now.length;i++){
				map.put(params.get(i).paramsName,params.get(i).list.get(now[i]));
			}
			BaseTest test = (BaseTest) c.newInstance();
			test.init(map);
			String topologyId = test.run();
			test.waitForComplete();
			LOG.info(topologyId+" is finish and kill");
			client.killTopology(topologyId);
			Utils.sleep(60000);
		}
		//
	}
}
