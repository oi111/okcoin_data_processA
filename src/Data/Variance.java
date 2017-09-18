package Data;

public class Variance {
	double n;
	double x, x2;

	public Variance() {
		x = 0;
		x2 = 0;
		n = 0;
	}

	public void add(double val) {
		x += val;
		x2 += val * val;
		n++;
	}

	public double cal() {
		double avg = x / (n + 1e-8);
		return x2 / (n + 1e-8) - avg * avg;
	}
}
