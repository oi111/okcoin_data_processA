package Trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Data.TK;
import Data.Trade;
import Data.TradeSecond;
import Uti.InputFile;
import Uti.OutputFile;

public class ExpectJGivenN {
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
	HashMap<Long, TradeSecond> hm = new HashMap<Long, TradeSecond>();

	Trade tcurrent = null, tpre = null;
	List<Trade> tlt = new ArrayList<Trade>();
	List<Double> tld = new ArrayList<Double>();
	int k1 = 0, k2 = 0;

	// List<TK> ld = new ArrayList<TK>();
	List<Double> ld1 = new ArrayList<Double>();
	List<Double> ld2 = new ArrayList<Double>();
	List<Double> ld3 = new ArrayList<Double>();
	List<Double> ld0 = new ArrayList<Double>();

	List<List<TK>> ld = new ArrayList<List<TK>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExpectJGivenN pu = new ExpectJGivenN();
		pu.process(args[0], args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]));
	}

	void init() {
		int f = 1;
		a = new int[TT][NN];
		sum = new int[TT / FF][NN];
		time = new long[TT];
		mid = new double[TT];
		hm.clear();
		ld1.clear();
		ld2.clear();
		ld3.clear();
		ld0.clear();

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
		for (int j = 0; j < 10; j++) {
			List<TK> tmp = new ArrayList<TK>();
			tmp.clear();
			for (int i = 0; i < this.VLEN; i++)
				tmp.add(new TK(0, 0));
			ld.add(tmp);
		}
	}

	void process(String inputfile, String tradefile, int limit, int vbin, int vlen) {
		this.VLEN = vlen;
		this.VBIN = vbin;
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

		input.closeFile();

		System.out.println(k1 + " " + k2);
		outputExpect("ExpectJGivenN_" + "VB" + this.VBIN + "VL" + this.VLEN);
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
		// ITK pt[] = StrOrder.getBidDiffITK(p1, p2, NN, 5);
		if (p1.time / 1000 - p2.time / 1000 == 1 && hm.containsKey(p2.time / 1000)) {

			TradeSecond ts = hm.get(p2.time / 1000);
			if (ts.max - ts.min > 10000)
				return;
			List<Double> ld1 = new ArrayList<Double>();
			List<Double> ld2 = new ArrayList<Double>();

			double t0 = dog(ts, 0, p2.bid, p2.mid, ld1);
			double t1 = dog(ts, 1, p2.ask, p2.mid, ld2);

			// ld0.add(t0);
			// ld1.add(t1);
			// ld2.add(t0 - t1);
			for (int i = 0; i < 10; i++) {
				int index = ((int) ((ld1.get(i) + ld2.get(i)) * 1000 / VBIN));
				if (index >= 0 && index < VLEN) {
					ld.get(i).get(index).t1++;
					ld.get(i).get(index).t2 += t0 - t1;
				}
			}

			System.out.println(t0 + " " + t1 + " " + (t1 - t0));
		}
	}

	double dog(TradeSecond ts, int type, ITK p[], double mid, List<Double> ld) {
		ld.clear();
		for (int i = 0; i < 10; i++)
			ld.add(0.0);
		double tmp[] = new double[ts.max - ts.min + 1];
		// System.out.println(ts.max - ts.min);
		double tot = 0;
		for (int i = 0; i < tmp.length; i++)
			tmp[i] = 0;
		for (int i = 0; i < ts.lt.size(); i++) {
			if (ts.lt.get(i).type != type)
				continue;
			tmp[ts.lt.get(i).lprice - ts.min] += ts.lt.get(i).amount;
			// tot += ts.lt.get(i).amount;
		}
		// System.out.print(tot + " ");
		for (int i = 0; i < p.length; i++) {
			ITK t = p[i];
			if (t.t1 >= ts.min && t.t1 <= ts.max) {
				if (tmp[t.t1 - ts.min] != 0)
					tot += Math.min(t.t2 / 1000.0, tmp[t.t1 - ts.min]);
				int tx = Math.abs((int) (Math.log(t.t1 / mid) * 100000));
				if (tx < 10 && tx >= 0)
					ld.set(tx, ld.get(tx) + t.t2 / 1000.0);
			}
		}
		for (int i = 1; i < 10; i++)
			ld.set(i, ld.get(i) + ld.get(i - 1));
		return tot;
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
		if (tcurrent.date_ms / 1000 != tpre.date_ms / 1000 && tpre.date_ms / 1000 > 0) {
			// process();
			TradeSecond ts = new TradeSecond();
			for (int i = 0; i < tlt.size(); i++)
				ts.add(tlt.get(i));
			hm.put(ts.lt.get(0).date_ms / 1000, ts);
			tlt.clear();
		}
		tlt.add(tcurrent);
	}

	void outputExpect(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + "Expect.txt");
		output.openFile();
		for (int i = 0; i < ld.size(); i++) {
			for (int j = 0; j < ld.get(i).size(); j++)
				output.write(ld.get(i).get(j).t2 / (ld.get(i).get(j).t1 + 1e-8) + " ");
			output.write("\n");
		}

		for (int i = 0; i < ld.size(); i++) {
			for (int j = 0; j < ld.get(i).size(); j++)
				output.write(ld.get(i).get(j).t1 + " ");
			output.write("\n");
		}

		output.closeFile();
	}

}
