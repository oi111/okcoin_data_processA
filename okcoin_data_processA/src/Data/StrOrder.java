package Data;

public class StrOrder {
	public Long time;

	ITK ask[] = new ITK[200];
	public ITK bid[] = new ITK[200];
	public int bidN[];
	public int mid;
	public int t1;

	public int t2;

	public int min1;
	public String str1;

	public String str2;

	public StrOrder(String time, String ask, String bid) {
		this.time = Long.valueOf(time).longValue();
		init(ask, bid);
		str1 = bid;
		str2 = ask;

	}

	void init(String ask, String bid) {
		processOne(ask, this.ask);
		processOne(bid, this.bid);
		t1 = this.bid[0].t1;
		min1 = this.bid[this.bid.length - 1].t1;
		t2 = this.ask[this.ask.length - 1].t1;
		mid = (t1 + t2);
	}

	void processOne(String str, ITK lt[]) {
		String pt[] = str.replace("[", "").replace("]", "").split(",");
		for (int i = 0; i < pt.length / 2; i++) {
			int index = (int) ((Double.valueOf(pt[2 * i]).doubleValue() + 1e-6) * 100);
			int volume = (int) ((Double.valueOf(pt[2 * i + 1]).doubleValue() + 1e-6) * 1000);
			lt[i] = new ITK(index, volume);
		}
	}

	public static int[] getBidDiff(StrOrder a, StrOrder b, int n) {
		int best_bid = Math.max(a.t1, b.t1);
		int pt[] = new int[n];
		for (int i = 0; i < pt.length; i++)
			pt[i] = 0;
		for (int i = 0; i < a.bid.length; i++)
			if (best_bid - a.bid[i].t1 >= 0 && best_bid - a.bid[i].t1 < n)
				pt[best_bid - a.bid[i].t1] = a.bid[i].t2;
		for (int i = 0; i < b.bid.length; i++)
			if (best_bid - b.bid[i].t1 >= 0 && best_bid - b.bid[i].t1 < n)
				pt[best_bid - b.bid[i].t1] -= b.bid[i].t2;
		return pt;
	}

	public static ITK[] getBidDiffITK(StrOrder a, StrOrder b, int n) {
		int best_bid = Math.max(a.t1, b.t1);
		ITK pt[] = new ITK[n];
		for (int i = 0; i < pt.length; i++)
			pt[i] = new ITK(0, 0);
		for (int i = 0; i < a.bid.length; i++)
			if (best_bid - a.bid[i].t1 >= 0 && best_bid - a.bid[i].t1 < n)
				pt[best_bid - a.bid[i].t1].t1 = a.bid[i].t2;
		for (int i = 0; i < b.bid.length; i++)
			if (best_bid - b.bid[i].t1 >= 0 && best_bid - b.bid[i].t1 < n) {
				pt[best_bid - b.bid[i].t1].t1 -= b.bid[i].t2;
				pt[best_bid - b.bid[i].t1].t2 = b.bid[i].t2;
			}
		return pt;
	}

}
