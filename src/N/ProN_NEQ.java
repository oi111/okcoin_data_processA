package N;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;

public class ProN_NEQ {
	int BT = 100000;
	int NN = 100000;
	int TT = 40;
	int KK = 100;
	int LL = 20000;
	int CC = 10;
	int BIN = 5;
	// int FLG = 1;

	StrOrder current = null, pre = null;
	List<ITK> neq = new ArrayList<ITK>();
	// double vol[] = new double[NN];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProN_NEQ pu = new ProN_NEQ();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]));
	}

	void init() {

		for (int i = 0; i < NN; i++) {
			neq.add(new ITK(0, 0));
		}
	}

	void process(String inputfile, int limit, int bin) {
		// this.FLG = flg;
		this.BIN = bin;
		init();
		InputFile input = new InputFile();
		input.setFileName(inputfile);
		input.openFile();
		String line = null;
		List<StrOrder> lls = new ArrayList<StrOrder>();
		int n = 0;
		while ((line = input.read()) != null) {
			addOne(line);
			processX(current, pre);
			if ((++n) % 10000 == 0)
				System.out.println(n);
			if (n > limit)
				break;
		}
		input.closeFile();
		outputNeq("ProN_NEQ_" + "B" + BIN);
		outputNumber("ProN_NEQ_" + "B" + BIN);
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
		double t = Math.max(p.mid, q.mid);
		for (int i = 0; i < neq.size(); i++)
			neq.get(i).t1++;
		for (int i = 0; i < p.bid.length; i++) {
			int index = -(int) (Math.log(p.bid[i].t1 / (t + 0.0)) * BT / BIN);
			// System.out.println(index);
			if (p.bid[i].t2 != 0 && index < neq.size()) {
				neq.get(index).t2++;
			}
		}
	}

	void outputNeq(String file) {
		OutputFile output = new OutputFile();
		output.setFileName(file + "Neq.txt");
		output.openFile();
		for (int i = 0; i < neq.size(); i++)
			output.write(neq.get(i).t2 / (neq.get(i).t1 + 1e-8) + " ");
		output.closeFile();
	}

	void outputNumber(String file) {
		OutputFile output = new OutputFile();
		output.setFileName(file + "Number.txt");
		output.openFile();
		for (int i = 0; i < neq.size(); i++)
			output.write(i * BIN / (BT + 0.0) + " ");
		output.write("\n");
		for (int i = 0; i < neq.size(); i++)
			output.write(neq.get(i).t2 + " ");
		output.closeFile();
	}

}
