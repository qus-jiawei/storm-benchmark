package lib;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Helper {
	
	/** define config here
	 * 
	 */
	static private final String dataDir = "/home/qiujw/storm_test/collect_data";
//	static private final String dataDir = "C:/Users/Administrator/Desktop/storm/测试/测试结果";
	
	static public String getDataDir() {
		return dataDir;
	}
	static public List<String> getDataFileList(){
		File dir = new File(getDataDir());
		if(dir.exists()){
			return Arrays.asList(dir.list());
		}
		return null;
	}
}
