package cn.uc.storm.utils;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

/**
 * use jstat to get gc stat,
 * 
 * unuse
 * @author qiujw@ucweb.com
 *
 */
class GCStat {
	private int YGC;
	private int YGCT;
	private int FGC;
	private int FGCT;
	private int GCT;
	
	static private void GCStats() {
	    long totalGarbageCollections = 0;
	    long garbageCollectionTime = 0;

	    for(GarbageCollectorMXBean gc :
	            ManagementFactory.getGarbageCollectorMXBeans()) {

	        long count = gc.getCollectionCount();
	        if(count >= 0) {
	            totalGarbageCollections += count;
	        }

	        long time = gc.getCollectionTime();

	        if(time >= 0) {
	            garbageCollectionTime += time;
	        }
	    }

	    System.out.println("Total Garbage Collections: "
	        + totalGarbageCollections);
	    System.out.println("Total Garbage Collection Time (ms): "
	        + garbageCollectionTime);
	}
}
