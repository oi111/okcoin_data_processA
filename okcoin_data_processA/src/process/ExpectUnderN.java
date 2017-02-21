package process;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;

public class ExpectUnderN {
	int NN = 600;
	int TT = 40;
	int KK = 100;
	int LL = 10000;
	int CC = 10;
	int BINSIZE = 100;

	int index = -1;
	int a[][] = new int[TT][NN];
	int sum[] = new int[NN];
	int time[] = new int[TT];
	StrOrder pre = null, current = null;
	List<List<ITK>> ld3 = new ArrayList<List<ITK>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExpectUnderN pu = new ExpectUnderN();
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
			List<ITK> tmp = new ArrayList<ITK>();
			tmp.clear();
			for (int j = 0; j < KK; j++)
				tmp.add(new ITK(0, 0));
			ld3.add(tmp);
		}
	}

	void process(String inputfile, int limit, int binsize) {
		this.BINSIZE = binsize;
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
		outputExpect("ExpectUnderN_" + BINSIZE);
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
				if (pt[i].t2 / BINSIZE < KK)
				// if (a[index][i] != 0)
				{
					ld3.get(i / CC).get(pt[i].t2 / BINSIZE).t1 += a[index][i];
					ld3.get(i / CC).get(pt[i].t2 / BINSIZE).t2++;
				}
		}
	}

	void outputExpect(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + ".txt");
		output.openFile();
		for (int i = 0; i < ld3.size(); i++) {
			for (int j = 0; j < ld3.get(i).size(); j++)
				output.write(ld3.get(i).get(j).t1 / (ld3.get(i).get(j).t2 + 1e-8) + " ");
			output.write("\n");
		}
		output.closeFile();
	}

}
