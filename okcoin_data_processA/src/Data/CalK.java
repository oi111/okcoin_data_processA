package Data;

public class CalK {
	double x2, xy, sumx, sumy;
	int n = 0;
	double k = 0, b = 0;

	CalK() {
		x2 = xy = sumx = sumy = n = 0;
	}

	void add(double x, double y) {
		n++;
		xy += x * y;
		sumx += x;
		sumy += y;
		x2 += x * x;
	}

	void cal() {
		k = (n * xy - sumx * sumy) / (n * x2 - sumx * sumx + 1e-8);
		b = sumy / (n + 1e-8) - k * sumx / (n + 1e-8);
	}
}
