package service.impl;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import service.IPhaseGenerater;
import util.StockUtil;

@Component
public class PhaseGeneraterImpl implements IPhaseGenerater {

	@Override
	public int[] divideX(int days, int numberOfModel, double leastPhasePercent) {
		//TODO: divide by type
		return randomX(days, leastPhasePercent);
	}

	@Override
	public BigDecimal[] generateY(int numberOfModel, BigDecimal startPrice, BigDecimal amplitude, int[] x){
		//TODO: by numberOfModel
		switch (numberOfModel) {
		case 1:
			return randomUpY(startPrice, amplitude, x);

		case 2:
			return randomDownY(startPrice, amplitude, x);
		
		default:
			return randomUpY(startPrice, amplitude, x);
		}
	}
	private static int[] randomX(int days, double leastPhasePercent) {
		int range[] = new int[6];
		range[0] = 1;
		range[5] = days;
		int phaseLimit = (int) (days * leastPhasePercent);

		for (int i = 1; i < 5; i++) {
			boolean outOfLimit = true;
			while (outOfLimit) {
				range[i] = StockUtil.getRandomInt(phaseLimit, days - phaseLimit);
				outOfLimit = isOutOfLimit(i, range, phaseLimit);
			}

		}
		Arrays.sort(range);
		for (int i = 1; i < 5; i++) {
			System.out.println("**********phase[" + i + "]" + range[i - 1] + " - " + range[i]);
		}
		return range;
	}

	private static boolean isOutOfLimit(int i, int[] range, int phaseLimit) {
		for (int j = 0; j < i; j++) {

		}
		int gap[] = new int[3];
		switch (i) {
		case 2:
			gap[0] = Math.abs(range[2] - range[1]);
			if (gap[0] > phaseLimit) {
				return false;
			}
			break;
		case 3:
			gap[0] = Math.abs(range[3] - range[1]);
			gap[1] = Math.abs(range[3] - range[2]);
			if (gap[0] > phaseLimit && gap[1] > phaseLimit) {
				return false;
			}
			break;
		case 4:
			gap[0] = Math.abs(range[4] - range[1]);
			gap[1] = Math.abs(range[4] - range[2]);
			gap[2] = Math.abs(range[4] - range[3]);
			if (gap[0] > phaseLimit && gap[1] > phaseLimit && gap[2] > phaseLimit) {
				return false;
			}
			break;
		default:
			return false;
		}
		return true;
	}
	
	private static BigDecimal[] randomUpY(BigDecimal startPrice, BigDecimal amplitude, int[] x) {
		BigDecimal[] location = new BigDecimal[6];
		location[0] = startPrice;
		BigDecimal amplitudePrice = startPrice.multiply(amplitude);
		
		BigDecimal top = highLimitRandom(startPrice, startPrice.add(amplitudePrice), x[5]);
		BigDecimal bottom = top.subtract(amplitudePrice);
		
		while(isTooHigh(bottom, top, x[1])){
			top = highLimitRandom(startPrice, startPrice.add(amplitudePrice), x[5]);
			bottom = top.subtract(amplitudePrice);
		}
		

		location[2] = StockUtil.formatPrice(bottom);
		int topLocation = StockUtil.getRandomInt(1, 3);

		switch (topLocation) {
		case 1:
			location[1] = top;
			location[3] = StockUtil.getRandomBigDecimal(startPrice, top);
			location[4] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			location[5] = StockUtil.getRandomBigDecimal(startPrice, top);
			return location;

		case 2:
			location[1] = StockUtil.getRandomBigDecimal(startPrice, top);
			location[3] = top;
			location[4] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			location[5] = StockUtil.getRandomBigDecimal(startPrice, top);
			return location;

		case 3:
			location[1] = StockUtil.getRandomBigDecimal(startPrice, top);
			location[3] = StockUtil.getRandomBigDecimal(startPrice, top);
			location[4] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			location[5] = top;
			return location;
		default:
			return location;
		}

	}

	private static BigDecimal highLimitRandom(BigDecimal startPrice, BigDecimal endPrice, int x) {
		BigDecimal high = StockUtil.getRandomBigDecimal(startPrice, endPrice);
		while(isTooHigh(startPrice, high, x)){
			high = StockUtil.getRandomBigDecimal(startPrice, endPrice);
		}
		return high;
	}
	
	private static boolean isTooHigh(BigDecimal startPrice, BigDecimal top, int phase) {
		return top.doubleValue() > startPrice.doubleValue()*Math.pow(1.1, phase - 1);
	}

	private static BigDecimal[] randomDownY(BigDecimal startPrice, BigDecimal amplitude, int[] x) {
		BigDecimal[] location = new BigDecimal[6];
		location[0] = startPrice;
		BigDecimal amplitudePrice = startPrice.multiply(amplitude);
		BigDecimal bottom = lowLimitRandom(startPrice.subtract(amplitudePrice), startPrice, x[5]);
		BigDecimal top = bottom.add(amplitudePrice);
		
		while(isTooLow(bottom, top, x[1])){
			bottom = lowLimitRandom(startPrice.subtract(amplitudePrice), startPrice, x[5]);
			top = bottom.add(amplitudePrice);
		}
		
		location[2] = StockUtil.formatPrice(top);
		int bottomLocation = StockUtil.getRandomInt(1, 3);

		switch (bottomLocation) {
		case 1:
			location[1] = bottom;
			location[3] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			location[4] = StockUtil.getRandomBigDecimal(startPrice, top);
			location[5] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			return location;

		case 2:
			location[1] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			location[3] = bottom;
			location[4] = StockUtil.getRandomBigDecimal(startPrice, top);
			location[5] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			return location;

		case 3:
			location[1] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			location[3] = StockUtil.getRandomBigDecimal(bottom, startPrice);
			location[4] = StockUtil.getRandomBigDecimal(startPrice, top);
			location[5] = bottom;
			return location;
		default:
			return location;
		}

	}

	private static BigDecimal lowLimitRandom(BigDecimal startPrice, BigDecimal endPrice, int x) {
		BigDecimal low = StockUtil.getRandomBigDecimal(endPrice, startPrice);
		while(isTooLow(startPrice, low, x)){
			low = StockUtil.getRandomBigDecimal(endPrice, startPrice);
		}
		return low;
	}
	private static boolean isTooLow(BigDecimal bottom, BigDecimal startPrice, int phase) {
		return bottom.doubleValue() < startPrice.doubleValue()*Math.pow(0.9, phase - 1);
	}
}
