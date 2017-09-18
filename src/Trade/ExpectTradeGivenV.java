package Trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Data.TK;
import Data.Trade;
import Uti.InputFile;
import Uti.OutputFile;

public class ExpectTradeGivenV {
	int NN = 500;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 10;
	int FF = 1;
	int FLG = 0;
	int BIN = 5;
	int VLEN = 100;
	int VBIN = 5;
	int type = 2;

	int index = -1;
	int a[][];
	int sum[][];
	long time[];
	double mid[];
	StrOrder pre = null, current = null;
	HashMap<Long, Double> hm = new HashMap<Long, Double>();

	Trade tcurrent = null, tpre = null;
	List<Trade> tlt = new ArrayList<Trade>();
	List<Double> tld = new ArrayList<Double>();
	int k1 = 0, k2 = 0;
	// List<List<List<Double>>> ld3 = new ArrayList<List<List<Double>>>();
	int gg[];// = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 120, 140, 160, 180,
				// 200 };
	int GIVENS = 10;
	List<TK> ld = new ArrayList<TK>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExpectTradeGivenV pu = new ExpectTradeGivenV();
		pu.process(args[0], args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]),
				Integer.valueOf(args[5]));
	}

	void init() {
		int f = 1;
		gg = new int[TT / FF];
		a = new int[TT][NN];
		sum = new int[TT / FF][NN];
		time = new long[TT];
		mid = new double[TT];
		hm.clear();
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
		for (int i = 0; i < this.VLEN; i++)
			ld.add(new TK(0, 0));

	}

	void process(String inputfile, String tradefile, int limit, int type, int vbin, int vlen) {
		this.type = type;
		this.VBIN = vbin;
		this.VLEN = vlen;
		init();
		read(tradefile);
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
		System.out.println(k1 + " " + k2);
		input.closeFile();
		outputExpect("ExpectTradeGivenV_" + "TP" + this.type + "VB" + this.VBIN + "VL" + VLEN);
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
		ITK pt[] = StrOrder.getBidDiffITK(p1, p2, NN, 5);
		index = (index + 1) % TT;

		int v = (int) ((Math.log(p1.mid) - Math.log(p2.mid)) * 1e6 / VBIN) + VLEN / 2;
		if (hm.containsKey(p1.time / 1000) && v < this.VLEN && v >= 0) {
			ld.get(v).t1++;
			ld.get(v).t2 += hm.get(p1.time / 1000);
			k2++;
		}
	}

	void read(String file) {
		tpre = new Trade(0, 0, 0, 0, 0, 0);
		InputFile input = new InputFile();
		input.setFileName(file);
		input.openFile();
		String line = null;
		int n = 0;
		while ((line = input.read()) != null) {
			processData(line);
			tpre = tcurrent;
			if ((++n) % 100000 == 0)
				System.out.println(n);
		}
		input.closeFile();
	}

	void processData(String str) {
		String pt[] = str.split(" ");
		tcurrent = new Trade(Long.valueOf(pt[0]), Long.valueOf(pt[1]), Long.valueOf(pt[2]), Integer.valueOf(pt[3]),
				Double.valueOf(pt[4]), Double.valueOf(pt[5]));
		if (tcurrent.date != tpre.date && tpre.date > 0) {
			process();
			tlt.clear();
		}
		tlt.add(tcurrent);
	}

	void process() {
		double t1 = 0, t0 = 0;
		for (int i = 0; i < tlt.size(); i++) {
			if (tlt.get(i).type == 1)
				t1 += tlt.get(i).amount;
			if (tlt.get(i).type == 0)
				t0 += tlt.get(i).amount;
		}
		hm.put(tlt.get(0).date, t0 - t1);
		k1++;
	}

	void outputExpect(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + "Expect.txt");
		output.openFile();
		for (int i = 0; i < ld.size(); i++) {
			output.write(ld.get(i).t2 / (ld.get(i).t1 + 1e-8) + " ");
		}
		output.write("\n");
		for (int i = 0; i < ld.size(); i++) {
			output.write(ld.get(i).t1 + " ");
		}
		output.write("\n");
		output.closeFile();
	}

}
