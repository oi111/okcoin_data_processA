package process;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;

public class ProUnderVelocityGivenTimeAndS {
	int NN = 500;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 1;
	int BIN = 1;
	int S = 100;

	int index = -1;
	int a[][];
	int sum[];
	long time[];
	int mid[];
	StrOrder pre = null, current = null;
	List<List<Double>> ld3 = new ArrayList<List<Double>>();
	int ak1 = 0, ak2 = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProUnderVelocityGivenTimeAndS pu = new ProUnderVelocityGivenTimeAndS();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]));
	}

	void init() {
		a = new int[TT][NN];
		sum = new int[NN];
		time = new long[TT];
		mid = new int[TT];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				a[i][j] = 0;
		for (int i = 0; i < sum.length; i++)
			sum[i] = 0;
		for (int i = 0; i < time.length; i++)
			time[i] = 0;
		for (int i = 0; i < mid.length; i++)
			mid[i] = 0;
		index = (TT - 1);
		for (int i = 0; i < NN / CC; i++) {

			ld3.add(new ArrayList<Double>());
		}
	}

	void process(String inputfile, int limit, int tt, int ll, int S, int bin) {
		this.S = S;
		this.TT = tt;
		this.LL = ll;
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
		System.out.println(ak1 + " " + ak2);
		input.closeFile();
		outputData("ProUnderVelocityGivenTimeAndS_" + TT + "_" + LL + "_" + BIN);
		outputPro("ProUnderVelocityGivenTimeAndS_" + TT + "_" + LL + "_" + BIN);
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
		int pt[] = StrOrder.getBidDiff(p1, p2, NN);

		index = (index + 1) % TT;
		int v = (p1.mid - mid[index]);

		for (int i = 0; i < pt.length; i++) {
			sum[i] = sum[i] - a[index][i] + pt[i];
			a[index][i] = pt[i];
		}
		if (v >= S - BIN && v <= S + BIN)
			ak1++;
		if (v >= S - BIN && v <= S + BIN && p1.time / 1000 - time[index] / 1000 <= TT * (1 + 0.05)) {
			for (int i = 0; i < pt.length; i += CC)
				if (ld3.get(i / CC).size() < LL && sum[i] != 0)
					ld3.get(i / CC).add((double) (sum[i] / 1000.0));
			ak2++;
		}
		mid[index] = p1.mid;
		time[index] = p1.time;
	}

	void outputData(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + ".txt");
		output.openFile();
		for (int i = 0; i < ld3.size(); i++) {
			output.write(ld3.get(i).size() + " ");
			for (int k = 0; k < ld3.get(i).size(); k++)
				output.write(ld3.get(i).get(k) + " ");
			for (int k = ld3.get(i).size(); k < LL; k++)
				output.write("0" + " ");
			output.write("\n");
		}
		output.closeFile();
	}

	void outputPro(String file) {
		PdfOutput pdf1 = new PdfOutput();
		pdf1.setParam(100, -25, 25);
		pdf1.openFile(file + "Pro.txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(100, -3, 2);
		pdf2.openFile(file + "LogPro.txt");

		for (int i = 0; i < ld3.size(); i++) {
			pdf1.calPdf(ld3.get(i));
			pdf2.calLogPdf(ld3.get(i));
		}
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
