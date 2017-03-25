package ProcessGivenS;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;
import Uti.Uti;

public class ProUnderVelocityGivenS {
	int NN = 500;
	int TT = 40;
	int KK = 81;
	int LL = 10000;
	int CC = 10;
	int FF = 1;
	int BINSIZE = 1;
	int NORMAL = 1;

	int index = -1;
	int a[][];
	int sum[][];
	long time[];
	int mid[];
	StrOrder pre = null, current = null;
	List<List<List<Double>>> ld3 = new ArrayList<List<List<Double>>>();
	int gg[];
	int GIVENS = 10;
	List<List<ITK>> tltk = new ArrayList<List<ITK>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProUnderVelocityGivenS pu = new ProUnderVelocityGivenS();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]), Integer.valueOf(args[7]));
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
			List<ITK> ti = new ArrayList<ITK>();
			tmp.clear();
			for (int j = 0; j < NN / CC; j++) {
				tmp.add(new ArrayList<Double>());
				ti.add(new ITK(0, 0));
			}
			tltk.add(ti);
			ld3.add(tmp);
		}
	}

	void process(String inputfile, int limit, int binsize, int cc, int ff, int normal, int givens, int tt) {
		this.TT = tt;
		this.GIVENS = givens;
		this.BINSIZE = binsize;
		this.CC = cc;
		this.FF = ff;
		this.NORMAL = normal;
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
		outputTime("ProUnderVelocityGivenS_" + "B" + BINSIZE + "C" + CC + "F" + FF + "NORMAL" + NORMAL + "S" + GIVENS
				+ "T" + TT);
		outputSigma("ProUnderVelocityGivenS_" + "B" + BINSIZE + "C" + CC + "F" + FF + "NORMAL" + NORMAL + "S" + GIVENS
				+ "T" + TT);
		outputData("ProUnderVelocityGivenS_" + "B" + BINSIZE + "C" + CC + "F" + FF + "NORMAL" + NORMAL + "S" + GIVENS
				+ "T" + TT);
		outputPro("ProUnderVelocityGivenS_" + "B" + BINSIZE + "C" + CC + "F" + FF + "NORMAL" + NORMAL + "S" + GIVENS
				+ "T" + TT);
		outputNormalPro("ProUnderVelocityGivenS_" + "B" + BINSIZE + "C" + CC + "F" + FF + "NORMAL" + NORMAL + "S"
				+ GIVENS + "T" + TT);
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

			int v = p1.mid - mid[(index + TT - gg[i]) % TT];
			if (v >= GIVENS - BINSIZE && v <= GIVENS + BINSIZE
					&& p1.time / 1000 - time[(index + TT - gg[i]) % TT] / 1000 <= gg[i] * (1 + 0.05)) {
				for (int j = 0; j < pt.length; j += CC) {
					tltk.get(i).get(j / CC).t1++;
					if (ld3.get(i).get(j / CC).size() < LL && sum[i][j] != 0) {
						ld3.get(i).get(j / CC).add((double) (sum[i][j] / 1000.0));
						tltk.get(i).get(j / CC).t2++;
					}
				}
			}
		}
		for (int i = 0; i < pt.length; i += CC)
			a[index][i] = pt[i];
		mid[index] = p1.mid;
		time[index] = p1.time;
	}

	void outputTime(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + "Time.txt");
		output.openFile();
		for (int i = 0; i < tltk.size(); i++) {
			for (int j = 0; j < tltk.get(i).size(); j++)
				output.write(tltk.get(i).get(j).t2 / (tltk.get(i).get(j).t1 + 1e-8) + " ");
			output.write("\n");
		}
		output.closeFile();
	}

	void outputSigma(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + "Sigma.txt");
		output.openFile();
		for (int i = 0; i < ld3.size(); i++) {
			for (int j = 0; j < ld3.get(i).size(); j++) {
				output.write(Uti.calSigma(ld3.get(i).get(j)) + " ");
			}
			output.write("\n");
		}
		output.closeFile();
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

	void outputNormalPro(String file) {
		PdfOutput pdf1 = new PdfOutput();
		pdf1.setParam(100, -NORMAL, NORMAL);
		pdf1.openFile(file + "NormalPro.txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(100, -3, 2);
		pdf2.openFile(file + "AutoPro.txt");

		for (int i = 0; i < ld3.size(); i++) {
			for (int j = 0; j < ld3.get(i).size(); j++) {
				pdf1.calNormalPdf(ld3.get(i).get(j));
				pdf2.calAutoPdf(ld3.get(i).get(j));
			}
		}
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
