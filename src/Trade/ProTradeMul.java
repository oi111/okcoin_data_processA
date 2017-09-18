package Trade;

import java.util.ArrayList;
import java.util.List;

import Data.Trade;
import Uti.InputFile;
import Uti.PdfOutput;

public class ProTradeMul {

	Trade current = null, pre = null;
	List<Trade> lt = new ArrayList<Trade>();
	List<Double> ld = new ArrayList<Double>();
	int type = 2;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProTradeMul pt = new ProTradeMul();
		pt.read(args[0], Integer.valueOf(args[1]).intValue());
	}

	void read(String file, int type) {
		this.type = type;
		pre = new Trade(0, 0, 0, 0, 0, 0);
		InputFile input = new InputFile();
		input.setFileName(file);
		input.openFile();
		String line = null;
		int n = 0;
		while ((line = input.read()) != null) {
			processData(line);
			pre = current;
			if ((++n) % 100000 == 0)
				System.out.println(n);
		}
		input.closeFile();
		output("ProTradeMul_TP" + this.type);
	}

	void processData(String str) {
		String pt[] = str.split(" ");
		current = new Trade(Long.valueOf(pt[0]), Long.valueOf(pt[1]), Long.valueOf(pt[2]), Integer.valueOf(pt[3]),
				Double.valueOf(pt[4]), Double.valueOf(pt[5]));
		if (current.date != pre.date && pre.date > 0) {
			process();
			lt.clear();
		}
		lt.add(current);
	}

	void process() {
		double t1 = 0, t0 = 0;
		for (int i = 0; i < lt.size(); i++) {
			if (lt.get(i).type == 1)
				t1 += lt.get(i).amount;
			if (lt.get(i).type == 0)
				t0 += lt.get(i).amount;
		}

		ld.add(t0 - t1);
	}

	void output(String file) {
		PdfOutput pdf1 = new PdfOutput();
		pdf1.setParam(100, -1000, 1000);
		pdf1.openFile(file + ".txt");
		PdfOutput pdf2 = new PdfOutput();
		pdf2.setParam(100, -3, 3);
		pdf2.openFile(file + "Log.txt");

		pdf1.calPdf(ld);
		pdf2.calLogPdf(ld);

		pdf1.closeFile();
		pdf2.closeFile();
	}

}
