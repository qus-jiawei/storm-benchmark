package cn.uc.storm.spouts;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.daemon.worker;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import cn.uc.storm.utils.AutoFlush;
import cn.uc.storm.utils.Env;
import cn.uc.storm.utils.PassData;

import com.google.common.collect.Maps;

public class MemSpout extends BaseRichSpout {
	static public Logger LOG = Logger.getLogger(worker.class);
	private SpoutOutputCollector _collector;
	private long msgId = 0;
	private Map<Long,Long> begin ;
	private final String fileName = "MemSpout.csv";
	private Map _conf;
	private PassData pd;
	private int kSize;
	private AutoFlush autoFlush;


	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		begin = Maps.newHashMap();
		_conf = conf;
		pd = PassData.getPassData(_conf);
		kSize = PassData.getPassDataSize(_conf);
		try {
			autoFlush = new AutoFlush(Env.getLogPath(_conf, fileName));
		} catch (IOException e) {
			LOG.error("autoflush init false");
		}
	}

	@Override
	public void nextTuple() {
		long now = System.currentTimeMillis();
		begin.put(msgId, now);
		_collector.emit(new Values(pd), msgId);
		msgId++;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));
	}

	@Override
	public void ack(Object msgId) {
		long now = System.currentTimeMillis();
		long beginTime = begin.get(msgId);
		autoFlush.count((Long)msgId,now-beginTime,kSize);
	}

	@Override
	public void fail(Object msgId) {
	}
}
