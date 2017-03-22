package process;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;

public class ProUnderVelocityGivenTimeAndDeltaT {
	int NN = 500;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 1;
	int DeltaT = 1;
	int BIN = 1;

	int a[][][];
	int sum[][];
	long time[][];
	int mid[][];
	int pindex[];
	StrOrder pre = null, current = null;
	List<List<List<Double>>> ld3 = new ArrayList<List<List<Double>>>();
	StrOrder lso[];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProUnderVelocityGivenTimeAndDeltaT pu = new ProUnderVelocityGivenTimeAndDeltaT();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]));
	}

	void init() {
		a = new int[DeltaT][TT][NN];
		sum = new int[DeltaT][NN];
		time = new long[DeltaT][TT];
		mid = new int[DeltaT][TT];
		lso = new StrOrder[DeltaT];
		pindex = new int[DeltaT];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				for (int k = 0; k < a[i][j].length; k++)
					a[i][j][k] = 0;
		for (int i = 0; i < sum.length; i++)
			for (int j = 0; j < sum[i].length; j++)
				sum[i][j] = 0;
		for (int i = 0; i < time.length; i++)
			for (int j = 0; j < time[i].length; j++)
				time[i][j] = 0;
		for (int i = 0; i < mid.length; i++)
			for (int j = 0; j < mid[i].length; j++)
				mid[i][j] = 0;
		for (int i = 0; i < pindex.length; i++)
			pindex[i] = (TT - 1);
		for (int i = 0; i < KK; i++) {
			List<List<Double>> tmp = new ArrayList<List<Double>>();
			tmp.clear();
			for (int j = 0; j < NN / CC; j++)
				tmp.add(new ArrayList<Double>());
			ld3.add(tmp);
		}
	}

	void process(String inputfile, int limit, int tt, int ll, int deltat, int bin) {
		this.TT = tt;
		this.LL = ll;
		this.DeltaT = deltat;
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
			processX(current, lso[n % DeltaT], n % DeltaT);
			lso[n % DeltaT] = current;
			if ((++n) % 10000 == 0)
				System.out.println(n);
			if (n > limit)
				break;
		}
		input.closeFile();
		outputData("ProUnderVelocityGivenTimeAndDeltaT_" + TT + "_" + LL + "_" + BIN + "_" + DeltaT);
		outputPro("ProUnderVelocityGivenTimeAndDeltaT_" + TT + "_" + LL + "_" + BIN + "_" + DeltaT);
	}

	void addOne(String line) {
		String pt[] = line.split(" ");
		StrOrder tmp = new StrOrder(pt[0], pt[2], pt[1]);
		current = tmp;
	}

	void processX(StrOrder p1, StrOrder p2, int p) {
		if (p2 == null || p1 == null)
			return;
		int pt[] = StrOrder.getBidDiff(p1, p2, NN);

		pindex[p] = (pindex[p] + 1) % TT;
		int index = pindex[p];
		int v = (p1.mid - mid[p][index]) / BIN;

		for (int i = 0; i < pt.length; i++) {
			sum[p][i] = sum[p][i] - a[p][index][i] + pt[i];
			a[p][index][i] = pt[i];
		}
		if (v + KK / 2 >= 0 && v + KK / 2 < KK && p1.time / 1000 - time[p][index] / 1000 <= TT * DeltaT * (1 + 0.05)) {
			for (int i = 0; i < pt.length; i += CC)
				if (ld3.get(v + KK / 2).get(i / CC).size() < LL && sum[p][i] != 0)
					ld3.get(v + KK / 2).get(i / CC).add((double) (sum[p][i] / 1000.0));
		}
		mid[p][index] = p1.mid;
		time[p][index] = p1.time;
	}

	void outputData(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + ".txt");
		output.openFile();
		for (int i = 0; i < ld3.size(); i++)
			for (int j = 0; j < ld3.get(i).size(); j++) {
				output.write(ld3.get(i).get(j).size() + " ");
				for (int k = 0; k < ld3.get(i).get(j).size(); k++)
					output.write(ld3.get(i).get(j).get(k) + " ");
				for (int k = ld3.get(i).get(j).size(); k < LL; k++)
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
			for (int j = 0; j < ld3.get(i).size(); j++) {
				pdf1.calPdf(ld3.get(i).get(j));
				pdf2.calLogPdf(ld3.get(i).get(j));
			}
		}
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
