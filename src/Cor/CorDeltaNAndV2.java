package Cor;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Data.TCor;
import Uti.InputFile;
import Uti.OutputFile;

public class CorDeltaNAndV2 {
	int NN = 500;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 10;
	int FF = 1;
	int FLG = 0;
	int LN = 0;
	// int BIN = 5;
	// int VLEN = 100;

	int index = -1;
	int a[][];
	int sum[][];
	int q[];
	long time[];
	double mid[];
	StrOrder pre = null, current = null;
	// List<List<List<Double>>> ld3 = new ArrayList<List<List<Double>>>();
	int gg[];// = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 120, 140, 160, 180,
				// 200 };
	int GIVENS = 10;
	int pn = 0;
	List<List<TCor>> tltk = new ArrayList<List<TCor>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CorDeltaNAndV2 pu = new CorDeltaNAndV2();
		// pu.process("333.txt", 100000, 1, 10, 0, 200, 0);
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]));
	}

	void init() {
		int f = 1;
		gg = new int[TT / FF];
		a = new int[TT][NN];
		sum = new int[TT / FF][NN];
		q = new int[NN];
		time = new long[TT];
		mid = new double[TT];
		for (int i = 0; i < gg.length; i++)
			gg[i] = (i + 1) * FF;
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				a[i][j] = 0;
		for (int i = 0; i < sum.length; i++)
			for (int j = 0; j < sum[i].length; j++)
				sum[i][j] = 0;
		for (int i = 0; i < time.length; i++)
			time[i] = 0;
		for (int i = 0; i < mid.length; i++)
			mid[i] = 0;
		index = (TT - 1);
		for (int i = 0; i < TT / FF; i++) {
			List<TCor> ti = new ArrayList<TCor>();
			for (int j = 0; j < NN / CC; j++) {
				ti.add(new TCor());
			}
			tltk.add(ti);
		}
	}

	void process(String inputfile, int limit, int cc, int ff, int ln, int tt, int flg) {
		this.TT = tt;
		this.CC = cc;
		this.FF = ff;
		this.FLG = flg;
		this.LN = ln;
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
		outputCor("CorDeltaNAndV_" + "C" + CC + "F" + FF + "LN" + LN + "T" + TT + "FLG" + FLG);
	}

	void addOne(String line) {
		String pt[] = line.split(" ");
		StrOrder tmp = new StrOrder(pt[0], pt[2], pt[1]);
		pre = current;
		current = tmp;
	}

	void processX(StrOrder p1, StrOrder p2) {
		if (p2 == null || p1 == null)
			return;
		int pt[] = StrOrder.getBidDiff(p1, p2, NN, 5);

		index = (index + 1) % TT;

		int pn = find(p1.time, index);
		if (pn != -1) {
			for (int j = 0; j < pt.length; j += CC)
				q[j] = pt[j] - a[(index + TT - pn) % TT][j];

			double tmp = Math.log(p1.mid) - Math.log(mid[(index + TT - pn) % TT]);
			double v = (tmp / (20 + 0.0));
			for (int j = 0; j < pt.length; j += CC) {
				// if (FLG == 0 && sum[i][j] == 0)
				// continue;
				tltk.get(0).get(j / CC).add(q[j] / 1000.0, v);
			}
		}
		for (int i = 0; i < pt.length; i += CC)
			a[index][i] = pt[i];
		mid[index] = p1.mid;
		time[index] = p1.time;
	}

	int find(long ans, int index) {
		for (int i = 1; i <= 20; i++)
			if (ans / 1000 - time[(index + TT - i) % TT] / 1000 >= 20
					&& ans / 1000 - time[(index + TT - i) % TT] / 1000 <= 21)
				return i;
		return -1;
	}

	void outputCor(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + "Cor.txt");
		output.openFile();
		for (int i = 0; i < tltk.size(); i++) {
			for (int j = 0; j < tltk.get(i).size(); j++) {
				output.write(tltk.get(i).get(j).calCorrelation() + " ");
			}
			output.write("\n");
		}
		output.closeFile();
	}

}