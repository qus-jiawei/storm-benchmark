package lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public abstract class ChartDataBuilder {
	public ChartData getChartData(String title, String suffix,
			String[] fileList, int fieldNumber, List<String> xAxis) {
		ChartData cd = new ChartData(title, suffix, xAxis);
		if (fileList != null) {
			for (String fileName : fileList) {
				ChartLine oneLine = new ChartLine(fileName);
				String filePath = Helper.getDataDir() + File.separatorChar
						+ fileName;
				changeCsvToLine(filePath, oneLine, fieldNumber, xAxis.size());
				cd.addChartLine(oneLine);
				// System.out.println(oneLine);
			}
		}
		return cd;
	}

	private void changeCsvToLine(String filePath, ChartLine oneLine,
			int fieldNumber, int xAxisNumber) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null && i < xAxisNumber) {
				String[] fields = line.split(",");
				if (i >= Helper.skip) {
					if (fieldNumber < fields.length) {
						oneLine.addData(this.valueOf(fields[fieldNumber]));
					} else {
						oneLine.addData(null);
					}
				}
				i++;
			}
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}
	}

	public abstract Integer valueOf(String temp);
}
