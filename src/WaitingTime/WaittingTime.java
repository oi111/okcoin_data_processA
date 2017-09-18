package WaitingTime;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Data.TK;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;
import Uti.Uti;

public class WaittingTime {
	int NN = 500;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 10;
	int FF = 1;
	int BIN = 1;
	int BINPRO = 18;
	int BINNORPRO = 20;

	int index = -1;
	long b[];

	StrOrder pre = null, current = null;
	List<List<Double>> ld2 = new ArrayList<List<Double>>();
	int GIVENS = 10;
	List<TK> ltk = new ArrayList<TK>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WaittingTime pu = new WaittingTime();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]), Integer.valueOf(args[7]));

	}

	void init() {
		int f = 1;
		b = new long[NN];

		for (int i = 0; i < b.length; i++)
			b[i] = 0;
		index = (TT - 1);
		for (int i = 0; i < b.length / CC; i++) {
			ld2.add(new ArrayList<Double>());
			ltk.add(new TK(0, 0));
		}
	}

	void process(String inputfile, int limit, int bin, int bp, int bnp, int cc, int kk, int nn) {
		this.NN = nn;
		this.CC = cc;
		this.BIN = bin;
		this.BINPRO = bp;
		this.BINNORPRO = bnp;
		this.KK = kk;
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
		outputExpect("WaittingTime_" + "B" + BIN + "BP" + BINPRO + "BNP" + BINNORPRO + "C" + CC + "K" + kk + "N" + nn);
		outputPro("WaittingTime_" + "B" + BIN + "BP" + BINPRO + "BNP" + BINNORPRO + "C" + CC + "K" + kk + "N" + nn);
		outputNormalPro(
				"WaittingTime_" + "B" + BIN + "BP" + BINPRO + "BNP" + BINNORPRO + "C" + CC + "K" + kk + "N" + nn);
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
		double delt = (p1.time / 1000.0 - p2.time / 1000.0);
		for (int j = 0; j < pt.length; j += CC) {
			if (pt[j] != 0) {
				if (ld2.get(j).size() < 1000000)
					ld2.get(j).add(b[j] + delt);

				ltk.get(j).t2 += b[j] + delt;
				ltk.get(j).t1++;
				b[j] = 0;
			} else
				b[j] += delt;
		}
	}

	void outputExpect(String file) {
		OutputFile output = new OutputFile();
		output.setFileName(file + "Expect.txt");
		output.openFile();
		for (int i = 0; i < ltk.size(); i++) {
			output.write(ltk.get(i).t2 / (ltk.get(i).t1 + 1e-8) + " ");
		}
		output.write("\n");
		output.closeFile();
	}

	void outputNormalPro(String file) {
		PdfOutput pdf1 = new PdfOutput();
		pdf1.setParam(100, 0, KK);
		pdf1.openFile(file + "NorPro.txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(BINNORPRO, -2, Math.log10(KK));
		pdf2.openFile(file + "NorLogPro.txt");

		for (int i = 0; i < ld2.size(); i++) {
			double avg = Uti.calAverage(ld2.get(i));
			for (int j = 0; j < ld2.get(i).size(); j++)
				ld2.get(i).set(j, ld2.get(i).get(j) / avg);
			pdf1.calPdf(ld2.get(i));
			pdf2.calLogPdf(ld2.get(i));
		}

		pdf1.closeFile();
		pdf2.closeFile();
	}

	void outputPro(String file) {
		PdfOutput pdf1 = new PdfOutput();
		pdf1.setParam(100, 0, 1000);
		pdf1.openFile(file + "Pro.txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(BINPRO, -1, Math.log10(KK));
		pdf2.openFile(file + "LogPro.txt");

		for (int i = 0; i < ld2.size(); i++) {
			for (int j = 0; j < ld2.get(i).size(); j++)
				ld2.get(i).set(j, ld2.get(i).get(j));
			pdf1.calPdf(ld2.get(i));
			pdf2.calLogPdf(ld2.get(i));
		}

		pdf1.closeFile();
		pdf2.closeFile();
	}

}
