package process;

import Uti.InputFile;
import Uti.OutputFile;

public class GetData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetData gd = new GetData();
		gd.read(args[0], args[1]);
	}

	void read(String inputfile, String outputfile) {
		InputFile input = new InputFile();
		input.setFileName(inputfile);
		input.openFile();

		OutputFile output = new OutputFile();
		output.setFileName(outputfile);
		output.openFile();
		String line = null;
		long time = 0;
		while ((line = input.read()) != null) {
			String pt[] = line.split(" ");
			if (Long.valueOf(pt[0]).longValue() / 1000 == time)
				continue;
			time = Long.valueOf(pt[0]).longValue() / 1000;
			output.write(line + "\n");
		}
		input.closeFile();
		output.closeFile();
	}

}
