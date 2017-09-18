package DeltaN;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Uti.InputFile;
import Uti.PdfOutput;

public class ProDeltaNGivenNAndS {
	int NN = 500;
	int TT = 40;
	int KK = 100;
	int LL = 10000;
	int CC = 10;
	int BINSIZE = 100;
	int FLG = 1;
	int VBIN = 5;
	int VLEN = 100;

	int index = -1;
	int a[][] = new int[TT][NN];
	int sum[] = new int[NN];
	int time[] = new int[TT];
	int mid[];
	StrOrder pre = null, current = null;
	List<List<List<List<Double>>>> ld3 = new ArrayList<List<List<List<Double>>>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProDeltaNGivenNAndS pu = new ProDeltaNGivenNAndS();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]), Integer.valueOf(args[7]),
				Integer.valueOf(args[8]));
	}

	void init() {
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
		index = -1;
		for (int i = 0; i < NN / CC; i++) {
			List<List<List<Double>>> t2 = new ArrayList<List<List<Double>>>();
			t2.clear();
			for (int k = 0; k < VLEN; k++) {
				List<List<Double>> tmp = new ArrayList<List<Double>>();
				tmp.clear();
				for (int j = 0; j < KK; j++)
					tmp.add(new ArrayList<Double>());
				t2.add(tmp);
			}
			ld3.add(t2);
		}
	}

	void process(String inputfile, int limit, int binsize, int cc, int kk, int nn, int vbin, int vlen, int flg) {
		this.BINSIZE = binsize;
		this.CC = cc;
		this.KK = kk;
		this.FLG = flg;
		this.VBIN = vbin;
		this.VLEN = vlen;
		this.NN = nn;
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
		outputExpect("ProDeltaNGivenNAndS_B" + this.BINSIZE + "C" + this.CC + "K" + this.KK + "N" + NN + "VB" + VBIN
				+ "VL" + VLEN + "FLG" + FLG);

		// WZ: without zero;
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
		index = (index + 1) % TT;

		time[index] = (int) (p1.time / 1000);
		for (int i = 0; i < pt.length; i++) {
			a[index][i] = pt[i].t1;
		}
		int v = (int) ((Math.log(p1.mid) - Math.log(p2.mid)) * 1e6 / VBIN) + VLEN / 2;
		if (p1.time / 1000 - p2.time / 1000 == 1 && v >= 0 && v < VLEN) {
			for (int i = 0; i < pt.length; i += CC)
				if (pt[i].t2 / BINSIZE < KK) {
					if (FLG == 0 && a[index][i] == 0)
						continue;

					ld3.get(i / CC).get(v).get(pt[i].t2 / BINSIZE).add(a[index][i] / 1000.0);
					// ld3.get(i / CC).get(v).get(pt[i].t2 / BINSIZE).t2++;
				}
		}
	}

	void outputExpect(String outputfile) {
		PdfOutput pdf1 = new PdfOutput();
		pdf1.setParam(100, -25, 25);
		pdf1.openFile(outputfile + "Pro.txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(50, -2, 2);
		pdf2.openFile(outputfile + "LogPro.txt");

		for (int i = 0; i < ld3.size(); i++) {
			for (int j = 0; j < ld3.get(i).size(); j++) {
				for (int k = 0; k < ld3.get(i).get(j).size(); k++) {
					pdf1.calAutoPdf(ld3.get(i).get(j).get(k));
					pdf2.calLogPdf(ld3.get(i).get(j).get(k));
					// output.write(ld3.get(i).get(j).get(k).t1 /
					// (ld3.get(i).get(j).get(k).t2 + 1e-8) + " ");
				}
				// output.write("\n");
			}
		}
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
