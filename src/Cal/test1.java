package Cal;

import Data.CalK;

public class test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CalK ck = new CalK();
		for (int i = 0; i < 20; i++)
			ck.add(i, i);
		ck.cal();
		System.out.println(ck.k + " " + ck.b);
	}

}
