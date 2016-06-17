package service.impl;

import java.math.BigDecimal;
import java.util.Arrays;

import service.IPhaseGenerater;
import util.StockUtil;

public class PhaseGeneraterImpl implements IPhaseGenerater {

	private static PhaseGeneraterImpl instance;

	private PhaseGeneraterImpl() {
        
    }

	public synchronized static PhaseGeneraterImpl getInstance() {
		if (instance == null) {
			instance = new PhaseGeneraterImpl();
		}
		return instance;
	}
	
	@Override
	public int[] divideX(int days, int numberOfModel, double leastPhasePercent) {
		//TODO: divide by type
		return randomX(days, leastPhasePercent);
	}

	@Override
	public BigDecimal[] generateY(BigDecimal startPrice, int numberOfModel, double amplitude){
		//TODO: by numberOfModel
		return randomY(startPrice, amplitude);
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
	
	private static BigDecimal[] randomY(BigDecimal startPrice, double amplitude) {
		BigDecimal[] location = new BigDecimal[6];
		location[0] = startPrice;
		BigDecimal amplitudePrice = startPrice.multiply(new BigDecimal(amplitude));
		BigDecimal top = StockUtil.getRandomBigDecimal(startPrice, startPrice.add(amplitudePrice));
		BigDecimal bottom = top.subtract(amplitudePrice);

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
}
