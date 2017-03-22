package N;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;

public class ProUnderN {
	int NN = 500;
	int TT = 40;
	int KK = 100;
	int LL = 10000;
	int CC = 10;
	int LOGLEN = 20;

	int index = -1;
	int a[][] = new int[TT][NN];
	int sum[] = new int[NN];
	int time[] = new int[TT];
	StrOrder pre = null, current = null;
	List<List<List<Double>>> ld3 = new ArrayList<List<List<Double>>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProUnderN pu = new ProUnderN();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]));
	}

	void init() {
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				a[i][j] = 0;
		for (int i = 0; i < sum.length; i++)
			sum[i] = 0;
		for (int i = 0; i < time.length; i++)
			time[i] = 0;
		index = -1;
		for (int i = 0; i < NN / CC; i++) {
			List<List<Double>> tmp = new ArrayList<List<Double>>();
			tmp.clear();
			for (int j = 0; j < KK; j++)
				tmp.add(new ArrayList<Double>());
			ld3.add(tmp);
		}
	}

	void process(String inputfile, int limit, int loglen) {
		this.LOGLEN = loglen;
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
		outputData("ProUnderN_" + LOGLEN);
		outputPro("ProUnderN_" + LOGLEN);
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
		ITK pt[] = StrOrder.getBidDiffITK(p1, p2, NN);
		index = (index + 1) % TT;

		time[index] = (int) (p1.time / 1000);
		for (int i = 0; i < pt.length; i++) {
			a[index][i] = pt[i].t1;

		}
		if (p1.time / 1000 - p2.time / 1000 == 1) {
			for (int i = 0; i < pt.length; i += CC)
				if (pt[i].t2 / 100 < KK)
					if (ld3.get(i / CC).get(pt[i].t2 / 100).size() < LL && a[index][i] != 0)
						ld3.get(i / CC).get(pt[i].t2 / 100).add((double) (a[index][i] / 1000.0));
		}
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

		pdf1.openFile(file + "Pro.txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(100, -3, 2);
		pdf2.openFile(file + "LogPro.txt");

		for (int i = 0; i < ld3.size(); i++) {
			for (int j = 0; j < ld3.get(i).size(); j++) {
				pdf1.setParam(100, -j * 0.1 - 1, j * 0.1 + 1);
				pdf1.calPdf(ld3.get(i).get(j));
				pdf2.setParam(LOGLEN, -1, 1);
				pdf2.calLogPdf(ld3.get(i).get(j));
			}
		}
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
