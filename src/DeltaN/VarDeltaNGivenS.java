package DeltaN;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Data.Variance;
import Uti.InputFile;
import Uti.OutputFile;

public class VarDeltaNGivenS {
	int NN = 500;
	int TT = 40;
	// int KK = 100;
	int LL = 10000;
	int CC = 10;
	// int BINSIZE = 100;
	int FLG = 1;
	int VBIN = 5;
	int VLEN = 100;
	int GT = 20;

	int index = -1;
	int a[][] = new int[TT][NN];
	int sum[] = new int[NN];
	int time[] = new int[TT];
	int mid[];
	StrOrder pre = null, current = null;
	List<List<Variance>> ld3 = new ArrayList<List<Variance>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VarDeltaNGivenS pu = new VarDeltaNGivenS();
		pu.process(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]),
				Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]));
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
			List<Variance> t2 = new ArrayList<Variance>();
			t2.clear();
			for (int k = 0; k < VLEN; k++) {
				t2.add(new Variance());
			}
			ld3.add(t2);
		}
	}

	void process(String inputfile, int limit, int cc, int gt, int vbin, int vlen, int flg) {
		// this.BINSIZE = binsize;
		this.CC = cc;
		// this.KK = kk;
		this.FLG = flg;
		this.VBIN = vbin;
		this.VLEN = vlen;
		this.GT = gt;
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
		outputVar("VarDeltaNGivenS_" + "C" + this.CC + "GT" + this.GT + "VB" + VBIN + "VL" + VLEN + "FLG" + FLG);

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
		if (p1.time / 1000 - p2.time / 1000 <= GT * 1.05 && v >= 0 && v < VLEN) {
			for (int i = 0; i < pt.length; i += CC) {
				ld3.get(i / CC).get(v).add(a[index][i] / 1000.0);
			}
		}
	}

	void outputVar(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + "Var.txt");
		output.openFile();
		for (int i = 0; i < ld3.size(); i++) {
			for (int j = 0; j < ld3.get(i).size(); j++) {
				output.write(ld3.get(i).get(j).cal() + " ");
			}
			output.write("\n");
		}
		output.closeFile();
	}

}
