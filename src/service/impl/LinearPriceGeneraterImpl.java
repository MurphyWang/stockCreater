package service.impl;

import java.math.BigDecimal;

import service.IPriceGenerater;
import util.StockUtil;

public class LinearPriceGeneraterImpl extends AbstractPriceGeneraterImpl implements IPriceGenerater {

	private static LinearPriceGeneraterImpl instance;

	private LinearPriceGeneraterImpl() {

	}

	public synchronized static LinearPriceGeneraterImpl getInstance() {
		if (instance == null) {
			instance = new LinearPriceGeneraterImpl();
		}
		return instance;
	}

	@Override
	public BigDecimal randomPrice(BigDecimal lastClosedPrice) {
		BigDecimal price;
		price = StockUtil.getRandomBigDecimal(StockUtil.limitUp(lastClosedPrice), StockUtil.limitDown(lastClosedPrice));
		return price;
	}

	@Override
	public boolean isLogical(BigDecimal priceOnLine, BigDecimal price) {
		return price.doubleValue() > StockUtil.limitUp(priceOnLine).doubleValue()
				|| price.doubleValue() < StockUtil.limitDown(priceOnLine).doubleValue();
	}

	@Override
	public BigDecimal getPriceOnline(int x, BigDecimal[][] kB, int[] phase) {

		BigDecimal result = null;
		for (int i = 0; i < 5; i++) {
			if (x >= phase[i] && x <= phase[i + 1]) {
				result = (kB[0][i].multiply(new BigDecimal(x))).add(kB[1][i]);
			}
		}
		return result;
	}

	// up trend = 0, down trend = 1
	@Override
	public BigDecimal[][] calcLineParamAndB(int trend, BigDecimal[] y, int[] x) {
		if (y.length != 6 || x.length != 6) {
			System.err.println("wrong columns when calclines");
			return null;
		}
		BigDecimal[][] kB = new BigDecimal[2][5];
		for (int i = 0; i < 5; i++) {
			kB[0][i] = new BigDecimal(1000);
			int phaseI = x[i + 1] - x[i];
			kB[0][i] = y[i + 1].subtract(y[i]).divide(new BigDecimal(phaseI), 4);
			kB[1][i] = y[i].subtract(kB[0][i].multiply(new BigDecimal(x[i])));
			System.out.println("********" + (i + 1) + "th line is " + "y = " + kB[0][i] + " x + " + kB[1][i]);
		}
		return kB;
	}
}
