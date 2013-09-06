package cn.uc.storm.test;

import java.util.Map;

public interface BaseTest {
	/**
	 * 测试初始化
	 * @param conf
	 */
	public void init(Map conf);
	/**
	 * 提交拓扑的地方
	 * @return
	 */
	public String run();
	/**
	 * 每单个测试完成后等待的时间
	 * @return
	 */
	public int waitForComplete();
}
