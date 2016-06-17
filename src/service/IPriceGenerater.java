package service;

import java.math.BigDecimal;

import model.StockInfo;

public interface IPriceGenerater {

	StockInfo generateFourPrices(StockInfo stockInfo, BigDecimal priceOnLine, BigDecimal lastClosedPrice);

	BigDecimal randomPrice(BigDecimal lastClosedPrice);

	boolean isLogical(BigDecimal priceOnLine, BigDecimal price);
	
	BigDecimal getPriceOnline(int x, BigDecimal[][] kB, int[] phase);
	
	BigDecimal[][] calcLineParamAndB(int trend, BigDecimal[] y, int[] x);
}
