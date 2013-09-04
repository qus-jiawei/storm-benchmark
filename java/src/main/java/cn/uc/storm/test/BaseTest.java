package cn.uc.storm.test;

import java.util.Map;

public interface BaseTest {
	public void init(Map conf);
	public String run();
	public int waitForComplete();
}
