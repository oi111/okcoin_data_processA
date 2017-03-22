package Cal;

import Data.CalK;
import Uti.InputFile;

public class CalSigmaDeltatK {

	int LENX = 200;
	int LENY = 250;
	double a[][];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CalSigmaDeltatK cs = new CalSigmaDeltatK();
		cs.work("111.txt", 5, 200, 40);
	}

	void init() {
		a = new double[LENY + 1][LENX + 1];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				a[i][j] = 0;
	}

	void work(String inputfile, int first, int last, int num) {
		init();
		read(inputfile);
		process(first, last, num);
	}

	void read(String inputfile) {
		InputFile input = new InputFile();
		input.setFileName(inputfile);
		input.openFile();
		String line;
		// for (int i = 1; i <= LENX; i++)
		// for (int j = 1; j <= LENY; j++) {
		// line = input.read();
		// String pt[] = line.split("\t");
		// a[j][i] = Double.valueOf(pt[2]).doubleValue();
		// }
		for (int i = 1; i <= LENX; i++) {
			line = input.read();
			String pt[] = line.split("\t");
			for (int j = 1; j <= LENY; j++)
				a[j][i] = Double.valueOf(pt[j - 1]).doubleValue();
		}
		input.closeFile();
	}

	void process(int first, int last, int num) {
		for (int i = 1; i < a.length; i++) {
			calKLog(a[i], first, last, num);
			// calK(a[i], first, last, num);
		}
	}

	void calKLog(double p[], int first, int last, int num) {
		CalK ck = new CalK();
		double t1 = Math.log10(first);
		double len = (Math.log10(last) - Math.log10(first)) / num;
		int pre = first;
		ck.add(Math.log10(first), Math.log10(p[first]));
		for (int i = 0; i < num; i++) {
			if ((int) Math.pow(10, t1 + len) <= pre)
				pre = (int) Math.pow(10, t1 + len) + 1;
			else
				pre = (int) Math.pow(10, t1 + len);
			if (pre > last)
				break;
			ck.add(Math.log10(pre), Math.log10(p[pre]));
			// System.out.println(Math.log10(pre) + " " + Math.log10(p[pre]));
			t1 += len;
		}
		ck.cal();
		System.out.println(ck.k + " " + ck.b);
	}

	void calK(double p[], int first, int last, int num) {
		CalK ck = new CalK();
		for (int i = first; i <= last; i++)
			ck.add(i, p[i]);
		ck.cal();
		System.out.println(ck.k + " " + ck.b);
	}

}
