package Statistics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import Uti.InputFile;
import Uti.OutputFile;

public class StatisticsData {
	int a[] = new int[400];
	String b[] = new String[400];
	HashMap<String, Integer> hm = new HashMap<String, Integer>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StatisticsData s = new StatisticsData();
		s.work(args[0], args[1]);
	}

	void init() {
		for (int i = 0; i < a.length; i++)
			a[i] = 0;
	}

	void work(String inputfile, String outputfile) {
		init();
		read(inputfile);
		outputDate(outputfile + "Date.txt");
		outputValue(outputfile + "Value.txt");
	}

	void buildTime(long time) {
		hm.clear();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < 350; i++) {
			Date date = new Date(time + 24 * 60 * 60 * 1000 * i);
			hm.put(sdf.format(date), i);
			b[i] = sdf.format(date);
		}

	}

	void read(String file) {
		InputFile input = new InputFile();
		input.setFileName(file);
		input.openFile();
		String line = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		int index = 0;
		line = input.read();
		String tmp[] = line.split(" ");
		buildTime(Long.valueOf(tmp[0]));
		while ((line = input.read()) != null) {
			String pt[] = line.split(" ");
			Date date = new Date(Long.valueOf(pt[0]));
			String str = sdf.format(date);
			a[hm.get(str)]++;
		}
		input.closeFile();
	}

	void outputDate(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile);
		output.openFile();
		for (int i = 0; i < hm.size(); i++)
			output.write(b[i] + "\n");
		output.closeFile();
	}

	void outputValue(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile);
		output.openFile();
		for (int i = 0; i < hm.size(); i++)
			output.write(a[i] + "\n");
		output.closeFile();
	}

}
