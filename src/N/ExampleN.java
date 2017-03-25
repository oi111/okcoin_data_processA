package N;

import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;

public class ExampleN {
	int NN = 600;
	int TT = 40;
	int KK = 100;
	int LL = 10000;
	int CC = 10;
	int PRO = 1;
	int BIN = 1;

	int index = -1;

	double b[][] = new double[100][NN];
	StrOrder pre = null, current = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExampleN pu = new ExampleN();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
	}

	void init() {

		for (int i = 0; i < b.length; i++)
			for (int j = 0; j < b[i].length; j++)
				b[i][j] = 0;
		index = -1;

	}

	void process(String inputfile, int limit, int bin, int pro) {
		this.PRO = pro;
		this.BIN = bin;
		init();
		InputFile input = new InputFile();
		input.setFileName(inputfile);
		input.openFile();
		String line = null;
		int n = 0;
		while ((line = input.read()) != null) {
			addOne(line);
			processX(current, pre);
			if ((++n) % 10000 == 0)
				System.out.println(n);
			if (n > limit)
				break;
			if (index > b.length)
				break;
		}
		input.closeFile();
		outputN("ExampleN_B" + BIN + "P" + PRO);

	}

	void addOne(String line) {
		String pt[] = line.split(" ");
		StrOrder tmp = new StrOrder(pt[0], pt[2], pt[1]);
		pre = current;
		current = tmp;
	}

	void processX(StrOrder p, StrOrder q) {
		if (p == null || q == null)
			return;
		if (p.time / 1000 - q.time / 1000 != 1)
			return;
		if (Math.random() > PRO * 0.01)
			return;
		index++;
		if (index >= b.length)
			return;
		int t = Math.max(p.t1, q.t1);

		for (int i = 0; i < p.bid.length; i++) {
			if (t - p.bid[i].t1 >= 0 && t - p.bid[i].t1 < b[index].length) {
				// if (ld3.get(t - p.bid[i].t1).size() < LL)
				// ld3.get(t - p.bid[i].t1).add(p.bid[i].t2 / 1000.0);
				b[index][t - p.bid[i].t1] = p.bid[i].t2 / 1000.0;
				// vol[t - p.bid[i].t1] += p.bid[i].t2;
			}
		}

	}

	void outputN(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + ".txt");
		output.openFile();
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length;) {
				double tot = 0;
				for (int k = 0; k < BIN; k++, j++)
					tot += b[i][j];

				output.write(tot + " ");
			}
			output.write("\n");
		}
		output.write("\n");
		output.closeFile();
	}

}
