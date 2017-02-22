package process;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Data.TCor;
import Uti.InputFile;
import Uti.OutputFile;

public class CorUnderN {
	int NN = 600;
	int TT = 40;
	int KK = 100;
	int LL = 10000;
	int CC = 10;

	int index = -1;
	int a[][] = new int[TT][NN];
	int sum[] = new int[NN];
	int time[] = new int[TT];
	StrOrder pre = null, current = null;
	List<TCor> lt = new ArrayList<TCor>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CorUnderN pu = new CorUnderN();
		pu.process(args[0], Integer.valueOf(args[1]));
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
		for (int i = 0; i < NN; i++)
			lt.add(new TCor());
	}

	void process(String inputfile, int limit) {
		init();
		InputFile input = new InputFile();
		input.setFileName(inputfile);
		input.openFile();
		String line = null;
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
		outputCorrelation("CorUnderN");
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
			for (int i = 0; i < pt.length; i++)
				// if (a[index][i] != 0)
				lt.get(i).add(pt[i].t1, pt[i].t2);
		}
	}

	void outputCorrelation(String outputfile) {
		OutputFile output = new OutputFile();
		output.setFileName(outputfile + ".txt");
		output.openFile();
		for (int i = 0; i < lt.size(); i++) {
			output.write(lt.get(i).calCorrelation() + " ");
		}
		output.write("\n");
		output.closeFile();
	}

}
