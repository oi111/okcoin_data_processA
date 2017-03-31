package DeltaNFlg;

import java.util.ArrayList;
import java.util.List;

import Data.ITHR;
import Data.ITK;
import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;

public class ProDeltaNFlgWithGamma {
	int NN = 500;
	int TT = 40;
	int KK = 100;
	int LL = 10000;
	int CC = 1;
	int LOGLEN = 20;
	// int FF = 100;
	int BINSIZE = 1;

	int index = -1;
	int a[][] = new int[TT][NN];
	int sum[] = new int[NN];
	int time[] = new int[TT];
	int mid[];
	StrOrder pre = null, current = null;
	List<ITHR> ld3 = new ArrayList<ITHR>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProDeltaNFlgWithGamma pu = new ProDeltaNFlgWithGamma();
		pu.process(args[0], Integer.valueOf(args[1]));
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
		index = -1;
		for (int i = 0; i < NN / CC; i++) {
			List<ITHR> tmp = new ArrayList<ITHR>();
			tmp.clear();
			for (int j = 0; j < KK; j++)
				tmp.add(new ITHR(0, 0, 0));
			ld3.add(new ITHR(0, 0, 0));
		}

	}

	void process(String inputfile, int limit) {
		// this.FF = ff;

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
		outputData("ProDeltaNFlgWithGamma");
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
			for (int i = 0; i < pt.length; i += CC) {
				if (a[index][i] < 0)
					ld3.get(i / CC).t1++;
				if (a[index][i] == 0)
					ld3.get(i / CC).t2++;
				if (a[index][i] > 0)
					ld3.get(i / CC).t3++;
			}
		}
		mid[index] = p1.mid;
	}

	void outputData(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + ".txt");
		output.openFile();
		for (int i = 0; i < ld3.size(); i++) {
			output.write(ld3.get(i).t1 + " " + ld3.get(i).t2 + " " + ld3.get(i).t3);
			output.write("\n");
		}
		output.closeFile();
	}

}
