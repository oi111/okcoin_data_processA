package Velocity;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;

public class ProUnderLogVelocityAllSAndTime {
	int NN = 500;
	int TT = 40;
	int VLEN = 101;
	int LL = 10000;
	int CC = 20;
	int FF = 2;
	int BEISHU = 1;
	String OTHER = "";

	int index = -1;
	int a[][];
	int sum[][];
	long time[];
	int mid[];
	StrOrder pre = null, current = null;
	List<List<List<List<Double>>>> ld3 = new ArrayList<List<List<List<Double>>>>();
	int gg[];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProUnderLogVelocityAllSAndTime pu = new ProUnderLogVelocityAllSAndTime();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]), args[7]);
	}

	void init() {
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
		for (int i = 0; i < VLEN; i++) {
			List<List<List<Double>>> tmp = new ArrayList<List<List<Double>>>();
			tmp.clear();
			for (int j = 0; j < TT / FF; j++) {
				List<List<Double>> u = new ArrayList<List<Double>>();
				for (int k = 0; k < NN / CC; k++)
					u.add(new ArrayList<Double>());
				tmp.add(u);
			}
			ld3.add(tmp);
		}
	}

	void process(String inputfile, int limit, int beishu, int cc, int ff, int tt, int vlen, String other) {
		this.BEISHU = beishu;
		this.TT = tt;
		this.CC = cc;
		this.FF = ff;
		this.VLEN = vlen;
		this.OTHER = other;
		this.init();
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
		outputData("ProUnderLogVelocityAllSAndTime_" + "B" + BEISHU + "C" + CC + "F" + FF + "T" + TT + "VL" + VLEN + "O"
				+ OTHER);
		outputPro("ProUnderLogVelocityAllSAndTime_" + "B" + BEISHU + "C" + CC + "F" + FF + "T" + TT + "VL" + VLEN + "O"
				+ OTHER);
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
			for (int j = 0; j < pt.length; j += CC)
				sum[i][j] = sum[i][j] - a[(index + TT - gg[i]) % TT][j] + pt[j];

			int v = (int) (Math.log10(Math.abs(p1.mid - mid[(index + TT - gg[i]) % TT])) * 100.0 / Math.log10(gg[i])
					/ this.BEISHU * (p1.mid >= mid[(index + TT - gg[i]) % TT] ? 1 : -1));
			if (v + VLEN / 2 >= 0 && v + VLEN / 2 < VLEN
					&& p1.time / 1000 - time[(index + TT - gg[i]) % TT] / 1000 <= gg[i] * (1 + 0.05)) {
				for (int j = 0; j < pt.length; j += CC)
					if (ld3.get(v + VLEN / 2).get(i).get(j / CC).size() < LL && sum[i][j] != 0)
						ld3.get(v + VLEN / 2).get(i).get(j / CC).add((double) (sum[i][j] / 1000.0));
			}
		}
		for (int i = 0; i < pt.length; i += CC)
			a[index][i] = pt[i];
		mid[index] = p1.mid;
		time[index] = p1.time;
	}

	void outputData(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + ".txt");
		output.openFile();
		for (int i = 0; i < ld3.size(); i++)
			for (int j = 0; j < ld3.get(i).size(); j++) {
				for (int g = 0; g < ld3.get(i).get(j).size(); g++) {
					output.write(ld3.get(i).get(j).get(g).size() + " ");
					for (int k = 0; k < ld3.get(i).get(j).get(g).size(); k++)
						output.write(ld3.get(i).get(j).get(g).get(k) + " ");
					for (int k = ld3.get(i).get(j).get(g).size(); k < LL; k++)
						output.write("0" + " ");
					output.write("\n");
				}
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
				for (int k = 0; k < ld3.get(i).get(j).size(); k++) {
					pdf1.calPdf(ld3.get(i).get(j).get(k));
					pdf2.calLogPdf(ld3.get(i).get(j).get(k));
				}
			}
		}
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
