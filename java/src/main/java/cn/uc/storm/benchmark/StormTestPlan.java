package cn.uc.storm.benchmark;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.apache.thrift7.TException;

import backtype.storm.generated.NotAliveException;
import backtype.storm.utils.Utils;
import cn.uc.storm.test.BaseTestManager;
import cn.uc.storm.utils.Conf;


/**
 * storm测试计划的类，通过读取配置文件，循环提交不同维度的测试任务。
 * 测试任务会保存测试结果到Env的getLogPath的路径中。
 * 测试完成后，使用项目中的webPython部分查看，测试结果.
 * @author qiujw@ucweb.com
 *
 */
public class StormTestPlan {
	static private Logger LOG = Logger.getLogger(StormTestPlan.class);
	static public void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NotAliveException, TException{
		Map plan = Utils.findAndReadConfigFile("plan.yaml", false);
		List<String> planList =  (List<String>)plan.get(Conf.planList);
		for(String className:planList){
			BaseTestManager manager = new BaseTestManager();
			manager.runAllTest(className,plan);
			//wait for 30 second to give the split
			LOG.info("finish testing"+className+" wait for one minute to run");
			Utils.sleep(60000);
		}
	}
}
