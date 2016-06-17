package util;

import java.math.BigDecimal;
import java.util.Random;

public class StockUtil {

	//random from min to max including (min & max)
	public static int getRandomInt(int min, int max) {
		Random random = new Random();
		int result = random.nextInt(max) % (max - min + 1) + min;
		return result;
	}

	public static double getRandomDouble(double min, double max) {
		Random random = new Random();
		double result = random.nextDouble() * (max - min) + min;
		return result;
	}
	
	public static BigDecimal getRandomBigDecimal(BigDecimal min, BigDecimal max) {
		Random random = new Random();
		double result = random.nextDouble() * (max.doubleValue() - min.doubleValue()) + min.doubleValue();
		return formatDoublePrice(result);
	}
	
	//format to BigDecimal(#####.####)
	public static BigDecimal formatPrice(BigDecimal price) {
		if (price.doubleValue() >= 99999.99994) {
			throw new UnsupportedOperationException("price is out of 99999.99994");
		}
		BigDecimal result = price.setScale(4, BigDecimal.ROUND_HALF_UP);
		return result;

	}
	
	public static BigDecimal formatDoublePrice(Double price) {
		BigDecimal result = formatPrice(new BigDecimal(price));
		return result;
	}
	
	//calculate the limit-up price
	public static BigDecimal limitUp(BigDecimal price) {
		return formatPrice(price.multiply(new BigDecimal(1.1)));
	}

	//calculate the limit-down price
	public static BigDecimal limitDown(BigDecimal price) {
		return formatPrice(price.multiply(new BigDecimal(0.9)));
	}
	
	public static BigDecimal square(BigDecimal a, int x) {
		return formatDoublePrice(Math.pow(a.doubleValue(), 1.0/x));
	}
	
	public static BigDecimal pow(BigDecimal a, int x) {
		return formatPrice(a.pow(x));
	}
}
