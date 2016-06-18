package service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

import model.StockInfo;
import service.IPriceGenerater;
import util.StockUtil;

public abstract class AbstractPriceGeneraterImpl implements IPriceGenerater {

	// random four price{open, low, high, close}
	public StockInfo generateFourPrices(StockInfo stockInfo, BigDecimal priceOnLine, BigDecimal lastClosedPrice) {
		if (stockInfo.getSnid() == 1) {
			BigDecimal close = priceOnLine;
			stockInfo.setClose(close);
			BigDecimal limitUp = StockUtil.limitUp(close);
			BigDecimal limitDown = StockUtil.limitDown(close);
			BigDecimal high = StockUtil.getRandomBigDecimal(close, limitUp);
			stockInfo.setHigh(high);
			stockInfo.setLow(StockUtil.getRandomBigDecimal(limitDown, close));
			BigDecimal open = StockUtil.getRandomBigDecimal(limitDown, limitUp);
			stockInfo.setOpen(open);
			return stockInfo;
		}

		BigDecimal[] prices = randomFourPrices(priceOnLine, lastClosedPrice);
		Arrays.sort(prices);
		return extractPrices(stockInfo, prices);
	}

	protected BigDecimal[] randomFourPrices(BigDecimal priceOnLine, BigDecimal lastClosedPrice) {
		BigDecimal prices[] = new BigDecimal[4];
		for (int i = 0; i < 4; i++) {
			prices[i] = generatePrice(priceOnLine, lastClosedPrice);
		}
		return prices;
	}

	protected BigDecimal generatePrice(BigDecimal priceOnLine, BigDecimal lastClosedPrice) {
		BigDecimal price = new BigDecimal(0);
		while (isLogical(priceOnLine, price)) {
			price = randomPrice(lastClosedPrice);
		}
		return price;
	}

	protected StockInfo extractPrices(StockInfo stockInfo, BigDecimal[] prices) {
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
}
