package lib;

public class ResultTableData {
	private String name;
	private int sum;
	private int avg;
	private double variance;

	public ResultTableData(String name,int sum, int avg, double variance) {
		this.name = name;
		this.sum = sum;
		this.avg = avg;
		this.variance = variance;
	}
	public String getName(){
		return name;
	}
	public int getSum(){
		return sum;
	}
	public int getAvg(){
		return avg;
	}
	public double getVariance(){
		return variance;
	}

}
