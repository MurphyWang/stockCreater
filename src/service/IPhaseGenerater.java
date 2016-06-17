package service;

import java.math.BigDecimal;

public interface IPhaseGenerater {

	int[] divideX(int days, int type, double leastPhasePercent);

	BigDecimal[] generateY(BigDecimal startPrice, int numberOfModel, double amplitude);
}
