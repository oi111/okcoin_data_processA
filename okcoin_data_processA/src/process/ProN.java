package process;

import java.util.ArrayList;
import java.util.List;

import Data.ITK;
import Data.StrOrder;
import Uti.InputFile;
import Uti.OutputFile;
import Uti.PdfOutput;

public class ProN {
	int NN = 500;
	int TT = 40;
	int KK = 100;
	int LL = 20000;
	int CC = 10;

	StrOrder current = null, pre = null;
	List<List<Double>> ld3 = new ArrayList<List<Double>>();
	List<ITK> li = new ArrayList<ITK>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProN pu = new ProN();
		pu.process(args[0], Integer.valueOf(args[1]));
	}

	void init() {
		for (int i = 0; i < NN; i++) {
			ld3.add(new ArrayList<Double>());
		}
		for (int i = 0; i < NN; i++)
			li.add(new ITK(0, 0));
	}

	void process(String inputfile, int limit) {
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
		outputExpect("ProN");
		outputPro("ProN");
	}

	void addOne(String line) {
		String pt[] = line.split(" ");
		StrOrder tmp = new StrOrder(pt[0], pt[2], pt[1]);
		pre = current;
		current = tmp;
	}

	void processX(StrOrder p, StrOrder q) {
		if (p == null || q == null)
			return;
		if (p.time / 1000 - q.time / 1000 != 1)
			return;
		int t = Math.max(p.t1, q.t1);
		for (int i = 0; i < p.bid.length; i++)
			if (p.bid[i].t2 != 0 && t - p.bid[i].t1 >= 0 && t - p.bid[i].t1 < ld3.size()) {
				if (ld3.get(t - p.bid[i].t1).size() < LL)
					ld3.get(t - p.bid[i].t1).add(p.bid[i].t2 / 1000.0);
				li.get(t - p.bid[i].t1).t1++;
				li.get(t - p.bid[i].t1).t2 += p.bid[i].t2;
			}

	}

	void outputExpect(String file) {
		OutputFile output = new OutputFile();
		output.setFileName(file + "Expect.txt");
		output.openFile();
		for (int i = 0; i < li.size(); i++)
			output.write(li.get(i).t2 / (li.get(i).t1 + 1e-8) / 1000.0 + " ");
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
			pdf1.calPdf(ld3.get(i));
			pdf2.calLogPdf(ld3.get(i));
		}
		pdf1.closeFile();
		pdf2.closeFile();
	}

}
