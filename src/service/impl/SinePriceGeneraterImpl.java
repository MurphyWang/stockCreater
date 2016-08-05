package service.impl;

import java.math.BigDecimal;

import service.IPriceGenerater;

public class SinePriceGeneraterImpl extends AbstractPriceGeneraterImpl implements IPriceGenerater{

	@Override
	public BigDecimal randomPrice(BigDecimal lastClosedPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLogical(BigDecimal priceOnLine, BigDecimal price) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BigDecimal getPriceOnline(int x, BigDecimal[][] kB, int[] phase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal[][] calcLineParamAndB(int trend, BigDecimal[] y, int[] x) {
		// TODO Auto-generated method stub
		return null;
	}

}
