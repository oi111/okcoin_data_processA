package Velocity;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;

public class PdfReturn {
	int NN = 500;
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
	List<Double> lv = new ArrayList<Double>();
	OutputFile output;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PdfReturn pu = new PdfReturn();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]));
	}

	void init() {

		time = new long[TT];
		mid = new double[TT];
		for (int i = 0; i < time.length; i++)
			time[i] = 0;
		for (int i = 0; i < mid.length; i++)
			mid[i] = 0;
		index = (TT - 1);
		output = new OutputFile();
		output.setFileName("PdfReturn.txt");
		output.openFile();
	}

	void process(String inputfile, int from, int to, int ff, int tt) {
		this.TT = tt;
		this.FF = ff;

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
				System.out.println(n + " " + lv.size());
			if (n > to)
				break;
		}
		input.closeFile();
		output.closeFile();
		outputExpect("PdfReturn_" + "F" + FF + "T" + TT);
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
		output.write(p1.time + " " + p1.mid / 100.0 + "\n");
		index = (index + 1) % TT;
		// System.out.println(p1.time / 1000 - time[(index + TT - FF) % TT] /
		// 1000);
		if (p1.time / 1000 - time[(index + TT - FF) % TT] / 1000 <= FF * (1 + 0.05)) {
			// if (index % FF == 0)
			lv.add((Math.log(p1.mid / 1000.0) - Math.log(mid[(index + TT - FF) % TT] / 1000.0)));
		}
		mid[index] = p1.mid;
		time[index] = p1.time;
	}

	void outputExpect(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + "Velocity.txt");
		output.openFile();
		for (int i = 0; i < lv.size(); i++) {
			output.write(lv.get(i) + " ");
		}
		output.write("\n");
		output.closeFile();
	}

}
