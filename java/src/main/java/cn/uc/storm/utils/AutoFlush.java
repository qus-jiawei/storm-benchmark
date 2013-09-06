package cn.uc.storm.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TODO:use log4j to print
 * it don't need to write this class.
 * @author qiujw
 *
 */
public class AutoFlush extends TimerTask {
	private long usingTime;
	private long tupleNumber;
	private long max, min;
	private long maxMsgId;
	private int totalSize;
	private AtomicBoolean flushing = new AtomicBoolean();
	private Timer timer;
	private String file;
	private final String logSplit = ",";
	private final int maxPrint = 200; 
	private int nowPrint = 0; 
	private boolean finish=false;

	public AutoFlush(String file) throws IOException {
		this.file = file;
		flushing.set(false);
		timer = new Timer();
		timer.schedule(this, 1000L, 100L);
		initCounter();
	}

	private void initCounter() {
		max = -1;
		min = Long.MAX_VALUE;
		usingTime = 0;
		tupleNumber = 0;
		totalSize = 0;
	}

	private void _counter(long using, int size) {
		usingTime += using;
		totalSize += size;
		tupleNumber++;
		if (using > max)
			max = using;
		if (using < min)
			min = using;
	}
	public void updateMsgId(long msgId) {
		maxMsgId = msgId;
	}
	
	public void count(long msgId, long using, int size) {
		if(finish) return;
		if (flushing.get()) {
			synchronized (this) {
				_counter(using, size);
			}
		} else {
			_counter(using, size);
		}
	}

	private void flush() throws IOException {
		if(nowPrint<maxPrint){
			nowPrint++;
		}
		else{
			finish=true;
			return;
		}
		flushing.set(true);
		long now = System.currentTimeMillis();
		long tupleNumberFlush, totalSizeFlush, avgDelay, maxDelay, minDelay,nowMsgId;
		synchronized (this) {
			tupleNumberFlush = this.tupleNumber;
			totalSizeFlush = this.totalSize;
			if (this.tupleNumber != 0)
				avgDelay = this.usingTime / this.tupleNumber;
			else
				avgDelay = 0;
			maxDelay = this.max;
			minDelay = this.min;
			nowMsgId = this.maxMsgId;
			initCounter();
		}
		if(maxDelay == - 1) maxDelay=0;
		if(minDelay == Long.MAX_VALUE) minDelay=0;
		flushing.set(false);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(file, true));
			StringBuilder sb = new StringBuilder();
			
			sb.append(now).append(logSplit).append(tupleNumberFlush).append(logSplit)
					.append(totalSizeFlush).append(logSplit).append(avgDelay)
					.append(logSplit).append(maxDelay).append(logSplit).append(minDelay)
					.append(logSplit).append(nowMsgId)
					.append("\n");
			out.write(sb.toString());
			out.flush();
		} catch (Exception e) {

		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	@Override
	public void run() {
		try {
			flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
