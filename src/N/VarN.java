package N;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Data.TK;
import Data.Variance;
import Uti.InputFile;
import Uti.OutputFile;

public class VarN {
	int NN = 500;
	int TT = 40;
	int KK = 100;
	int LL = 20000;
	int CC = 10;
	int BIN = 5;
	double EPS = 1e-5;
	// int FLG = 1;

	StrOrder current = null, pre = null;
	List<List<Double>> ld3 = new ArrayList<List<Double>>();
	List<TK> li = new ArrayList<TK>();
	List<TK> li2 = new ArrayList<TK>();
	List<TK> geo = new ArrayList<TK>();
	List<ITK> neq = new ArrayList<ITK>();
	List<Variance> lv = new ArrayList<Variance>();
	int flg[] = new int[1000];
	// double vol[] = new double[NN];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VarN pu = new VarN();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Double.valueOf(args[3]));
	}

	void init() {
		for (int i = 0; i < NN; i++) {
			ld3.add(new ArrayList<Double>());
		}
		for (int i = 0; i < NN; i++) {
			li.add(new TK(0, 0));
			li2.add(new TK(0, 0));
			lv.add(new Variance());
			neq.add(new ITK(0, 0));
		}
	}

	void process(String inputfile, int limit, int bin, double eps) {
		this.BIN = bin;
		this.EPS = eps;
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
		outputExpect("VarN_" + "B" + BIN + "E" + EPS);

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
		for (int i = 0; i < NN; i++)
			flg[i] = 0;
		for (int i = 0; i < li.size(); i++) {
			li.get(i).t1++;
			li2.get(i).t1++;
		}
		for (int i = 0; i < p.bid.length; i++) {
			int index = -(int) (Math.log(p.bid[i].t1 / t) * 100000 / BIN);
			if (index >= 0 && index < NN) {
				// if (FLG == 0 && p.bid[i].t2 <= 0)
				// continue;
				// li.get(index).t1++;
				li.get(index).t2 += p.bid[i].t2;
				flg[index] += p.bid[i].t2;
			}
		}
		for (int i = 0; i < NN; i++) {
			li2.get(i).t2 += 1 / (flg[i] / 1000.0 + EPS);
			lv.get(i).add(flg[i] / 1000.0);
			// System.out.print(flg[i] / 1000.0 + " ");
		}
		// System.out.println();
	}

	void outputExpect(String file) {
		OutputFile output = new OutputFile();
		output.setFileName(file + "Expect.txt");
		output.openFile();
		for (int i = 0; i < li.size(); i++)
			output.write(li.get(i).t2 / (li.get(i).t1 + 1e-8) / 1000.0 + " ");
		output.write("\n");
		for (int i = 0; i < li2.size(); i++)
			output.write(li2.get(i).t2 / (li2.get(i).t1 + 1e-8) + " ");
		output.write("\n");
		for (int i = 0; i < li.size(); i++)
			output.write(lv.get(i).cal() + " ");
		output.write("\n");
		output.closeFile();
	}

}
