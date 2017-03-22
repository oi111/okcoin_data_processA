package Cal;

import Data.CalK;
import Uti.InputFile;
import Uti.OutputFile;

public class CalDeltaNNK {

	int LENX = 600;
	int LENY = 95;
	double a[][];
	OutputFile output = new OutputFile();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CalDeltaNNK cs = new CalDeltaNNK();
		cs.work("111.txt", 50, 95, 20);
	}

	void init() {
		a = new double[LENX + 1][LENY + 1];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				a[i][j] = 0;

		output.setFileName("222.txt");
		output.openFile();

	}

	void work(String inputfile, int first, int last, int num) {
		init();
		read(inputfile);
		process(first, last, num);
		output.closeFile();
	}

	void read(String inputfile) {
		InputFile input = new InputFile();
		input.setFileName(inputfile);
		input.openFile();
		String line;
		for (int i = 1; i <= LENX; i++) {
			line = input.read();
			String pt[] = line.split("\t");
			for (int j = 1; j <= LENY; j++) {
				a[i][j] = Double.valueOf(pt[j - 1]).doubleValue();
			}
		}
		input.closeFile();
	}

	void process(int first, int last, int num) {
		double b[] = new double[LENY + 1];

		for (int i = 0; i < LENY; i++)
			b[i + 1] = i * 0.1;
		for (int i = 0; i < 50; i++)
			b[i + 1] = i * 0.02;
		for (int i = 50; i < 95; i++)
			b[i + 1] = b[i] + 0.2;
		for (int i = 1; i < a.length; i++) {
			// calKLog(a[i], first, last, num);
			calKLog(b, a[i], first, last, num);
		}
	}

	void calKLog(double p[], double q[], int first, int last, int num) {
		CalK ck = new CalK();
		double len = (Math.log10(p[last]) - Math.log10(p[first])) / num;
		int pre = first;
		ck.add(Math.log10(p[first]), Math.log10(q[first]));
		for (int i = first + 1; i <= last; i++) {
			if (Math.log10(p[i]) - Math.log10(p[pre]) < len)
				continue;
			if (p[i] > 0 && q[i] > 0)
				ck.add(Math.log10(p[i]), Math.log10(q[i]));
			pre = i;
		}
		ck.cal();
		System.out.println(ck.k + " " + ck.b);
		output.write(ck.k + " " + ck.b + "\n");
	}

	void calK(double p[], int first, int last, int num) {
		CalK ck = new CalK();
		for (int i = first; i <= last; i++)
			ck.add(i, p[i]);
		ck.cal();
		System.out.println(ck.k + " " + ck.b);
	}

}
