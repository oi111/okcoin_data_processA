package Trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Data.Trade;
import Uti.InputFile;
import Uti.PdfOutput;

public class ProTradeSecondExcept {
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
	HashMap<Long, ITK> hm = new HashMap<Long, ITK>();

	Trade tcurrent = null, tpre = null;
	List<Trade> tlt = new ArrayList<Trade>();
	List<Double> tld = new ArrayList<Double>();
	int k1 = 0, k2 = 0;

	// List<TK> ld = new ArrayList<TK>();
	List<Double> ld1 = new ArrayList<Double>();
	List<Double> ld2 = new ArrayList<Double>();
	List<Double> ld3 = new ArrayList<Double>();
	List<Double> ld0 = new ArrayList<Double>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProTradeSecondExcept pu = new ProTradeSecondExcept();
		pu.process(args[0], args[1], Integer.valueOf(args[2]));
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

	}

	void process(String inputfile, String tradefile, int limit) {

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
		read(tradefile);
		System.out.println(k1 + " " + k2);
		outputExpect("ProTradeSecondExcept");
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
		if (p1.time / 1000 - p2.time / 1000 == 1)
			hm.put(p1.time / 1000, new ITK(p2.t1, p2.t2));
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
		int ask, bid;
		k1++;
		if (!hm.containsKey(tlt.get(0).date_ms / 1000))
			return;
		k2++;
		ITK tmp = hm.get(tlt.get(0).date_ms / 1000);
		bid = tmp.t1;
		ask = tmp.t2;
		// System.out.println(bid + " " + ask);
		for (int i = 0; i < tlt.size(); i++) {
			// System.out.print(tlt.get(i).lprice + " ");
			if (tlt.get(i).lprice <= bid || tlt.get(i).lprice >= ask)
				continue;
			if (tlt.get(i).type == 1)
				t1 += tlt.get(i).amount;
			if (tlt.get(i).type == 0)
				t0 += tlt.get(i).amount;
		}
		// System.out.println();
		ld1.add(t1);
		ld0.add(t0);
		ld2.add(t0 + t1);
		ld3.add(t0 - t1);
		// System.out.println(t0 + " " + t1 + " " + (t0 + t1) + " " + (t0 -
		// t1));
		// System.out.println("=========================");
	}

	void outputExpect(String outputfile) {
		PdfOutput pdf1 = new PdfOutput();
		pdf1.setParam(100, -1000, 1000);
		pdf1.openFile(outputfile + ".txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(100, -3, 3);
		pdf2.openFile(outputfile + "Log.txt");

		pdf1.calPdf(ld0);
		pdf1.calPdf(ld1);
		pdf1.calPdf(ld2);
		pdf1.calPdf(ld3);
		pdf2.calLogPdf(ld0);
		pdf2.calLogPdf(ld1);
		pdf2.calLogPdf(ld2);
		pdf2.calLogPdf(ld3);

		pdf1.closeFile();
		pdf2.closeFile();
	}

}
