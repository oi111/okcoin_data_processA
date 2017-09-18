package DeltaN;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Data.TCor;
import Data.TK;
import Uti.InputFile;
import Uti.OutputFile;

public class CorDeltaN {
	int NN = 200;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 10;
	int FF = 1;
	int FLG = 0;
	int BIN = 5;

	int index = -1;
	int a[][];
	int sum[][];
	long time[];
	double mid[];
	StrOrder pre = null, current = null;
	// List<List<List<Double>>> ld3 = new ArrayList<List<List<Double>>>();
	int gg[];// = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 120, 140, 160, 180,
				// 200 };
	int GIVENS = 10;
	List<List<TK>> tltk = new ArrayList<List<TK>>();
	TCor tc[][];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CorDeltaN pu = new CorDeltaN();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]), Integer.valueOf(args[7]));
	}

	void init() {
		int f = 1;
		gg = new int[TT / FF];
		a = new int[TT][NN];
		sum = new int[TT / FF][NN];
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
			List<TK> ti = new ArrayList<TK>();
			for (int j = 0; j < NN / CC; j++) {
				ti.add(new TK(0, 0));
			}
			tltk.add(ti);
		}
		tc = new TCor[NN][NN];
		for (int i = 0; i < tc.length; i++)
			for (int j = 0; j < tc[i].length; j++)
				tc[i][j] = new TCor();

	}

	void process(String inputfile, int from, int to, int cc, int ff, int nn, int tt, int flg) {
		this.TT = tt;
		// this.BINSIZE = binsize;
		this.CC = cc;
		this.FF = ff;
		this.FLG = flg;
		this.NN = nn;
		init();
		InputFile input = new InputFile();
		input.setFileName(inputfile);
		input.openFile();
		String line = null;
		List<StrOrder> lls = new ArrayList<StrOrder>();
		int n = 0;
		while ((line = input.read()) != null) {
			++n;
			if (n > from && n < to) {
				addOne(line);
				processX(current, pre);
			}
			if ((n) % 10000 == 0)
				System.out.println(n);
			if (n > to)
				break;
		}
		input.closeFile();
		outputExpect("CorDeltaN_" + "C" + CC + "F" + FF + "NN" + NN + "T" + TT + "FLG" + FLG);

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
		int pt[] = StrOrder.getBidDiff(p1, p2, NN, BIN);

		index = (index + 1) % TT;

		for (int i = 0; i < gg.length; i++) {
			for (int j = 0; j < pt.length; j += CC)
				sum[i][j] = sum[i][j] - a[(index + TT - gg[i]) % TT][j] + pt[j];

			// int v = p1.mid - mid[(index + TT - gg[i]) % TT];
			if (p1.time / 1000 - time[(index + TT - gg[i]) % TT] / 1000 <= gg[i] * (1 + 0.05)) {
				for (int j = 0; j < pt.length; j += CC)
					for (int k = 0; k < pt.length; k += CC) {
						// if (FLG == 0 && sum[i][j] == 0)
						// continue;
						tc[j][k].add(sum[i][j] / 1000.0, sum[i][k] / 1000.0);
					}
			}
		}
		for (int i = 0; i < pt.length; i += CC)
			a[index][i] = pt[i];
		mid[index] = p1.mid;
		time[index] = p1.time;
	}

	void outputExpect(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + "Time.txt");
		output.openFile();
		for (int i = 0; i < tc.length; i++) {
			for (int j = 0; j < tc[i].length; j++)
				output.write(tc[i][j].calCorrelation() + " ");
			output.write("\n");
		}
		output.closeFile();
	}

}
