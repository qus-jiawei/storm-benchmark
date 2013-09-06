#Storm Benchmark#


支持以下几种测试：

- StormLatency：可以测试程序的tuple的处理延迟，总体吞吐，平均tuple处理数等指标。

		包括启动参数：
		step:bolt的数量
		size:数据量的大小
		worker：程序的worker数量
		para：bolt的并行数量
		pending：Spout可积压的Tuple数
		acker：程序启动的acker认证的任务，

- StormBoltMax：可以测试Bolt的极限性能。
		
		包括启动参数：
		spout:spout的数量
		worker：程序的worker数量
		pending：Spout可积压的Tuple数

- StormTridentWordCount:
		
		包括启动参数：
		batch:一次batch发送的数量
		spout：程序的Spout的数量
		agg：程序的Aggregate阶段的数量
		worker：程序的worker数量



> 提示：在作者的测试中，每个Supervisor的Worker数量设置为1。可以通过步骤数控制通过的网络节点数。
		
以上3种测试都会通过cn.uc.storm.utils.AutoFlush这个类将测试结果打印到cn.uc.storm.utils.Env.linuxLogPath中。
以上测试都可以使用对应的main函数直接启动。也可以通过StormTestPlan进行自动化测试。

##使用##

	git clone https://github.com/qus-jiawei/storm-benchmark
	cd storm-benchmark/java
	mvn package
	storm jar target/storm-benchmark-0.1-jar-with-dependencies.jar cn.uc.storm.test.StormLatency worker 4 step 2

##怎样使用StormTestPlan?##
StormTestPlan可以自动读取所有参数的可能值。并组合出不同的参数组合，启动程序并测试。

	- 修改plan.yaml中的storm.test.plan.list。添加要测试的程序的主类。
	- 添加对应的测试的参数启动列表。	
	- 使用cn.uc.storm.benchmark.StormTestPlan启动自动化测试。

	
##数据查看##
所有的测试都会采集前20秒的测试结果，测试程序每100MS打印一侧统计数据。
打印出来的数据可以通过collect.sh收集，并使用web部分程序查看。