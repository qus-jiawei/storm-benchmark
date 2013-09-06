package lib;

import java.util.List;

import com.google.common.collect.Lists;

public class ChartLine {
	private String name = "default";
	private List<Integer> data = Lists.newArrayList();

	public ChartLine(String name) {
		this.name = name;
	}

	public void addData(Integer d) {
		data.add(d);
	}

	public String getName() {
		return name;
	}

	public int getSum() {
		int sum = 0;
		for (Integer i : data) {
			sum += i;
		}
		return sum;
	}

	public int getAvg() {
		int sum = 0;
		int count = 0;
		for (Integer i : data) {
			if (i != 0) {
				sum += i;
				count++;
			}
		}
		if (count != 0)
			return sum / count;
		else
			return 0;
	}

	public double getVariance() {
		int avg = getAvg();
		int count = 0;
		double tempSum = 0.0;
		for (Integer i : data) {
			if (i != 0) {
				int temp = (i - avg) * (i - avg);
				tempSum += temp;
			}
		}
		if (count != 0) {
			tempSum /= (double) (count);
			return Math.sqrt(tempSum);
		} else
			return 0;
	}

	public ResultTableData getTableData() {
		return new ResultTableData(getSum(), getAvg(), getVariance());
	}
}
