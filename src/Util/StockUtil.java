package Util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

import model.StockInfo;

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
		if (price.intValue() >= 100000) {
			throw new UnsupportedOperationException("price is out of 99999.9999");
		}
		BigDecimal result = price.setScale(4, BigDecimal.ROUND_HALF_UP);
		return result;

	}
	
	
	public static BigDecimal formatDoublePrice(Double price) {
		BigDecimal result = formatPrice(new BigDecimal(price));
		return result;
	}

	//random four price{open, low, high, close}
	public static StockInfo randomFourPrice(StockInfo stockInfo, BigDecimal priceOnLine, BigDecimal lastClosedPrice) {
		if (stockInfo.getSnid() == 1) {
			stockInfo.setClose(priceOnLine);
			return stockInfo;
		}
		
		BigDecimal prices[] = new BigDecimal[4];
		for (int i = 0; i < 4; i++) {
			prices[i] = new BigDecimal(0);
			while (prices[i].doubleValue() > limitUp(lastClosedPrice).doubleValue()
					|| prices[i].doubleValue() < limitDown(lastClosedPrice).doubleValue()) {
				prices[i] = StockUtil.getRandomBigDecimal(priceOnLine.multiply(new BigDecimal(0.9)),
						priceOnLine.multiply(new BigDecimal(1.1)));
			}
			
		}
		Arrays.sort(prices);
		stockInfo.setHigh(StockUtil.formatPrice(prices[3]));
		stockInfo.setLow(StockUtil.formatPrice(prices[0]));
		Random random = new Random();
		if (random.nextBoolean()) {
			stockInfo.setOpen(StockUtil.formatPrice(prices[2]));
			stockInfo.setClose(StockUtil.formatPrice(prices[1]));
		} else {
			stockInfo.setOpen(StockUtil.formatPrice(prices[1]));
			stockInfo.setClose(StockUtil.formatPrice(prices[2]));
		}
		return stockInfo;
	}

	//calculate the limit-up price
	public static BigDecimal limitUp(BigDecimal price) {
		return formatPrice(price.multiply(new BigDecimal(1.1)));
	}

	//calculate the limit-down price
	public static BigDecimal limitDown(BigDecimal price) {
		return formatPrice(price.multiply(new BigDecimal(0.9)));
	}
}
