package DeltaN;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;

public class ProDeltaN {
	int NN = 500;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 10;
	int FF = 1;
	int FLG = 0;
	int BIN = 5;
	int VLEN = 100;
	int NUM = 10000;

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
	List<List<List<Double>>> tltk = new ArrayList<List<List<Double>>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProDeltaN pu = new ProDeltaN();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]), Integer.valueOf(args[7]),
				Integer.valueOf(args[8]));
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
			List<List<Double>> ti = new ArrayList<List<Double>>();
			for (int j = 0; j < NN / CC; j++) {
				ti.add(new ArrayList<Double>());
			}
			tltk.add(ti);
		}
	}

	void process(String inputfile, int limit, int bin, int cc, int ff, int num, int tt, int vlen, int flg) {
		this.TT = tt;
		this.BIN = bin;
		this.CC = cc;
		this.FF = ff;
		this.VLEN = vlen;
		this.FLG = flg;
		this.NUM = num;
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
		outputPdf("ProDeltaN_" + "B" + BIN + "C" + CC + "F" + FF + "NUM" + NUM + "T" + TT + "VL" + VLEN + "FLG" + FLG);
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

		for (int i = 0; i < gg.length; i++) {
			for (int j = 0; j < pt.length; j += CC)
				sum[i][j] = sum[i][j] - a[(index + TT - gg[i]) % TT][j] + pt[j];

			if (p1.time / 1000 - time[(index + TT - gg[i]) % TT] / 1000 <= gg[i] * (1 + 0.05)) {
				for (int j = 0; j < pt.length; j += CC) {
					if (sum[i][j] == 0)
						continue;

					if (tltk.get(i).get(j / CC).size() < NUM)
						tltk.get(i).get(j / CC).add((double) (sum[i][j] / 1000.0));
					// tltk.get(i).get(j / CC)..t1++;
				}
			}
		}
		for (int i = 0; i < pt.length; i += CC)
			a[index][i] = pt[i];
		mid[index] = p1.mid;
		time[index] = p1.time;
	}

	void outputPdf(String file) {
		PdfOutput pdf1 = new PdfOutput();
		pdf1.setParam(100, -100, 100);
		pdf1.openFile(file + ".txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(80, -2, 2);
		pdf2.openFile(file + "Log.txt");
		OutputFile output = new OutputFile();
		output.setFileName(file + "Num.txt");
		output.openFile();

		for (int i = 0; i < tltk.size(); i++) {
			for (int j = 0; j < tltk.get(i).size(); j++) {
				output.write(tltk.get(i).get(j).size() + " ");
				pdf1.calPdf(tltk.get(i).get(j));
				pdf2.calLogPdf(tltk.get(i).get(j));
			}
			output.write("\n");
		}
		output.closeFile();
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
