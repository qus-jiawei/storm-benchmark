package lib;

import java.util.List;

import com.google.common.collect.Lists;

public class ChartLine {
	private String name = "default";
	private List<Integer> data = Lists.newArrayList();
	public ChartLine(String name){
		this.name = name;
	}
	public void addData(Integer d){
		data.add(d);
	}
	public String getName(){
		return name;
	}
	public int getAvg(){
		int sum = 0;
		for(Integer i:data){
			sum+=i;
		}
		return sum/=data.size();
	}
}
