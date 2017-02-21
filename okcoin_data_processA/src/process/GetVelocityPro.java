package process;

import java.util.ArrayList;
import java.util.List;

import Data.StrOrder;
import Uti.InputFile;
import Uti.PdfOutput;

public class GetVelocityPro {

	List<Double> ld = new ArrayList<Double>();
	StrOrder pre = null, current = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetVelocityPro gv = new GetVelocityPro();
		gv.read(args[0]);
	}

	void read(String inputfile) {
		InputFile input = new InputFile();
		input.setFileName(inputfile);
		input.openFile();

		String line = null;
		long time = 0;
		int n = 0;
		while ((line = input.read()) != null) {
			addOne(line);
			if (pre != null && current.time / 1000 - pre.time / 1000 == 1) {
				ld.add((current.mid - pre.mid) / 100.0);
			}
			if ((++n) % 10000 == 0)
				System.out.println(n);
			if (n > 1000000)
				break;
		}
		System.out.println(ld.size());
		input.closeFile();
		PdfOutput pdf = new PdfOutput();
		pdf.setParam(200, -1, 1);
		pdf.openFile("GetVelocityPro.txt");
		pdf.calPdf(ld);
		pdf.closeFile();
	}

	void addOne(String line) {
		String pt[] = line.split(" ");
		StrOrder tmp = new StrOrder(pt[0], pt[2], pt[1]);
		pre = current;
		current = tmp;
	}
}
