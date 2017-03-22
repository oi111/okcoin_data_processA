package process;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;

public class ProUnderVelocityGivenSAndGamma {
	int NN = 500;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 10;
	int FF = 1;
	int BINSIZE = 1;
	int GAMMA = 0;

	int index = -1;
	int a[][];
	int sum[][];
	long time[];
	int mid[];
	StrOrder pre = null, current = null;
	List<List<Double>> ld3 = new ArrayList<List<Double>>();
	int gg[];// = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 120, 140, 160, 180,
				// 200 };
	int GIVENS = 10;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProUnderVelocityGivenSAndGamma pu = new ProUnderVelocityGivenSAndGamma();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]));
	}

	void init() {
		int f = 1;
		gg = new int[TT / FF];
		a = new int[TT][NN];
		sum = new int[TT / FF][NN];
		time = new long[TT];
		mid = new int[TT];
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
			List<List<Double>> tmp = new ArrayList<List<Double>>();
			tmp.clear();
			for (int j = 0; j < NN / CC; j++)
				tmp.add(new ArrayList<Double>());
			ld3.add(new ArrayList<Double>());
		}
	}

	void process(String inputfile, int limit, int tt, int givens, int binsize, int ff, int gamma) {
		this.TT = tt;
		this.GIVENS = givens;
		this.BINSIZE = binsize;
		this.GAMMA = gamma;
		this.FF = ff;
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
		outputData("ProUnderVelocityGivenSAndGamma_T" + TT + "S" + GIVENS + "F" + FF + "B" + BINSIZE + "G" + GAMMA);
		outputPro("ProUnderVelocityGivenSAndGamma_T" + TT + "S" + GIVENS + "F" + FF + "B" + BINSIZE + "G" + GAMMA);
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

		for (int i = 0; i < gg.length; i++) {
			sum[i][GAMMA] = sum[i][GAMMA] - a[(index + TT - gg[i]) % TT][GAMMA] + pt[GAMMA];

			int v = p1.mid - mid[(index + TT - gg[i]) % TT];
			if (v >= GIVENS - BINSIZE && v <= GIVENS + BINSIZE
					&& p1.time / 1000 - time[(index + TT - gg[i]) % TT] / 1000 <= gg[i] * (1 + 0.05)) {

				if (ld3.get(i).size() < LL && sum[i][GAMMA] != 0)
					ld3.get(i).add((double) (sum[i][GAMMA] / 1000.0));
			}
		}
		a[index][GAMMA] = pt[GAMMA];
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
			{
				pdf1.calPdf(ld3.get(i));
				pdf2.calLogPdf(ld3.get(i));
			}
		}
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
