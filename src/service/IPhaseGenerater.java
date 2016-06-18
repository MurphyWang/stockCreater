package service;

import java.math.BigDecimal;

public interface IPhaseGenerater {

	int[] divideX(int days, int type, double leastPhasePercent);

	BigDecimal[] generateY(int numberOfModel, BigDecimal startPrice, BigDecimal amplitude, int[] x);
}
