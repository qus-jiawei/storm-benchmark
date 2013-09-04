package cn.uc.storm.bolts;

import org.apache.log4j.Logger;

import backtype.storm.daemon.worker;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class PassBolt extends BaseBasicBolt{
	static public Logger LOG = Logger.getLogger(worker.class);
	public void execute(Tuple input, BasicOutputCollector collector) {
		collector.emit(new Values(input.getValue(0)));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));
	}

}
