package controllers;

import java.util.Arrays;
import java.util.List;

import lib.ChartData;
import lib.ChartDataBuilder;
import lib.Helper;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.playhelp;

import com.google.common.collect.Lists;

public class Application extends Controller {

	public static Result index() {
		String[] chosenFileList = request().queryString().get("csv");
		if (chosenFileList != null) {
			for (int i = 0; i < chosenFileList.length; i++) {
				chosenFileList[i] = Helper.displayToFile(chosenFileList[i]);
			}
		}
		// ChartData
		ChartDataBuilder builder = new ChartDataBuilder() {
			@Override
			public Integer valueOf(String temp) {
				try{
					return Integer.valueOf(temp);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return 0;
			}
		};
		List<String> xAxis = Lists.newArrayList();
		for (int i = Helper.skip; i < 200; i++) {
			xAxis.add((i * 100) + "ms");
		}
		ChartData tupleChartData = builder.getChartData("tuple处理量", "条",
				chosenFileList, 1, xAxis);
		ChartData sizeChartData = builder.getChartData("数据处理量", "K",
				chosenFileList, 2, xAxis);
		ChartData avgChartData = builder.getChartData("平均处理延迟", "ms",
				chosenFileList, 3, xAxis);
		ChartData maxChartData = builder.getChartData("最大处理延迟", "ms",
				chosenFileList, 4, xAxis);
		ChartData minChartData = builder.getChartData("最小处理延迟", "ms",
				chosenFileList, 5, xAxis);
		ChartData msgidChartData = builder.getChartData("当前的msgId", "ms",
				chosenFileList, 6, xAxis);
		return ok(index.render(Helper.getDataFileList(),
				Arrays.toString(chosenFileList), tupleChartData, sizeChartData,
				avgChartData, maxChartData, minChartData,msgidChartData));
	}

	public static Result playhelp() {
		return ok(playhelp.render("Your new application is ready."));
	}

}
