package Trade;

import java.util.ArrayList;
import java.util.List;

import Data.TCor;
import Data.THR;
import Uti.InputFile;
import Uti.OutputFile;

public class AutoCorrelationJ {

	List<THR> lt = new ArrayList<THR>();
	int LEN = 1000;
	int BIN = 5;
	int FLG = 0;
	OutputFile output = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AutoCorrelationJ ac = new AutoCorrelationJ();
		ac.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
	}

	void init() {
		output = new OutputFile();
		output.setFileName("AutoCorrelationJ_" + "B" + BIN + "FLG" + FLG + "L" + LEN + ".txt");
		output.openFile();
		lt.clear();
	}

	void read(String file) {
		InputFile input = new InputFile();
		input.setFileName(file);
		input.openFile();
		String line = null;
		int i = 0;
		while ((line = input.read()) != null) {
			String pt[] = line.split(" ");
			THR t = new THR(Double.valueOf(pt[0]), Double.valueOf(pt[1]), Double.valueOf(pt[2]));
			lt.add(t);
			if ((++i) % 10000 == 0)
				System.out.println(i);
		}
		input.closeFile();
	}

	void calAutoCorrelation() {
		for (int i = 0; i < LEN; i += BIN) {
			TCor tc = new TCor();
			int num = 0;
			for (int j = LEN; j < lt.size(); j++)
				if (lt.get(j).t1 / 1000 - lt.get(j - i).t1 / 1000 <= i * (1 + 0.05)) {
					if (this.FLG == 0)
						tc.add(lt.get(j).t2, lt.get(j - i).t2);
					else
						tc.add(Math.abs(lt.get(j).t2), Math.abs(lt.get(j - i).t2));
					num++;
				}
			output.write(tc.calCorrelation() + " " + num + "\n");
			System.out.println(i);
		}
	}

	void process(String file, int bin, int flg, int len) {
		this.BIN = bin;
		this.LEN = len;
		this.FLG = flg;
		init();
		read(file);
		calAutoCorrelation();
		output.closeFile();
	}

}
