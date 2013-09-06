package lib;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class Helper {

	/**
	 * define config here
	 * 
	 */
	static private final String dataDir = "/home/qiujw/storm_test/collect_data";
	static public final int skip = 100;

	static public String getDataDir() {
		return dataDir;
	}

	static public List<String> getDataFileList() {
		File dir = new File(getDataDir());
		if (dir.exists()) {
			List<String> re = Lists.newArrayList();
			String[] temp = dir.list();
			if (temp != null) {
				for (String t : temp) {
					re.add(fileToDisplay(t));
				}
			}
			Collections.sort(re);
			return re;
		}
		return null;
	}
	
	static public String fileToDisplay(String file){
		return file.replaceAll("_", " ").substring(0,file.length()-4);
	}
	static public String displayToFile(String display){
		return display.replaceAll(" ", "_")+".csv";
	}
}
