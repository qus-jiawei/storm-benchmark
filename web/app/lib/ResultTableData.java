package lib;

public class ResultTableData {
	private int sum;
	private int avg;
	private double variance;

	public ResultTableData(int sum, int avg, double variance) {
		this.sum = sum;
		this.avg = avg;
		this.variance = variance;
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
