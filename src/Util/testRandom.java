package Util;

import java.math.BigDecimal;

public class testRandom {

	public static void main(String[] args) {
		for (int i = 0; i<100 ;i ++){
			double factor = Math.pow(-1, i);
			System.out.println(new BigDecimal(4).multiply((new BigDecimal(factor))));
		}
	}

}
