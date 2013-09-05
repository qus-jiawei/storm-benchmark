package lib;

import java.lang.reflect.Type;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ChartData {
	private String title;
	private List<ChartLine> chartLineList = Lists.newArrayList();
	
	private List<String> xAxis;
	private String suffix;
	
	public ChartData(String title,String suffix,List<String> xAxis) {
		this.title = title;
		this.suffix = suffix;
		this.xAxis = xAxis;
	}

	public String getSeriesJson() {
		Gson gson = new Gson();
		Type chartLineType = new TypeToken<List<ChartLine>>() {}.getType();
		return gson.toJson(chartLineList,chartLineType);
	}
	public void addChartLine(ChartLine oneLine) {
		chartLineList.add(oneLine);
	}

	public String getValueSuffix() {
		return suffix;
	}

	public String getXAxisJson() {
		Gson gson = new Gson();
		return gson.toJson(xAxis);
	}

	public String getTitle() {
		return title;
	}
	
	public List<String> getAllAvg(){
		List<String> re = Lists.newArrayList();
		for(ChartLine cl: chartLineList){
			re.add(cl.getName()+":"+cl.getSum()+" "+cl.getAvg()+" , "+cl.getVariance());
		}
		return re;
	}
}
