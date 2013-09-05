package cn.uc.storm.spouts;

import backtype.storm.Config;
import backtype.storm.daemon.worker;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.uc.storm.utils.AutoFlush;
import cn.uc.storm.utils.Env;

import com.google.common.collect.Maps;

import storm.trident.operation.TridentCollector;
import storm.trident.spout.IBatchSpout;


public class TridentSpout implements IBatchSpout {
	static public Logger LOG = Logger.getLogger(worker.class);
	private String sentence= "Sport (or sports) is all forms of usually competitive physical activity which,[1] through casual or organised participation, aim to use, maintain or improve physical ability and skills while providing entertainment to participants, and in some cases, spectators.[2] Hundreds of sports exist, from those requiring only two participants, through to those with hundreds of simultaneous participants, either in teams or competing as individuals.Sport is generally recognised as activities which are based in physical athleticism or physical dexterity, with the largest major competitions such as the Olympic Games admitting only sports meeting this definition,[3] and other organisations such as the Council of Europe using definitions precluding activities without a physical element from classification as sports.[2] However, a number of competitive, but non-physical, activities claim recognition as mind sports. The International Olympic Committee (through ARISF) recognises both chess and bridge as bona fide sports";
	private int maxBatchSize;
	private Map<Long,Long> begin ;
	private AutoFlush autoFlush;
	private Map _conf;
	private Fields fields = new Fields("sentence");
   
    public TridentSpout(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }
    
    int index = 0;
    
    @Override
    public void open(Map conf, TopologyContext context) {
    	_conf = conf;
    	begin = Maps.newHashMap();
        index = 0;
        String fileName = "TridentSpout"+context.getThisTaskId()+".csv";
		try {
			autoFlush = new AutoFlush(Env.getLogPath(_conf, fileName));
		} catch (IOException e) {
			LOG.error("autoflush init false");
		}
    }

    @Override
    public void emitBatch(long batchId, TridentCollector collector) {
    	long now = System.currentTimeMillis();
		begin.put(batchId, now);
        for(int i=0; i < maxBatchSize; index++, i++) {
            collector.emit(new Values(sentence));
        }
    }

    @Override
    public void ack(long batchId) {
    	long now = System.currentTimeMillis();
		long beginTime = begin.get(batchId);
		autoFlush.count((Long)batchId,now-beginTime,1);
    }

    @Override
    public void close() {
    }

    @Override
    public Map getComponentConfiguration() {
        Config conf = new Config();
        conf.setMaxTaskParallelism(1);
        return conf;
    }

    @Override
    public Fields getOutputFields() {
        return fields;
    }
    
}
