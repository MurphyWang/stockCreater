package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import Util.StockUtil;
import dao.IStockInfoDao;
import model.StockInfo;

public class creater {

	private static int days;
	private static String windCode;
	private static double leastPhasePercent;
	private static double amplitude;
	private static BigDecimal startPrice;

	private static Map<Integer, StockInfo> cache = new HashMap<>();
	private static Map<String, String> properties = new HashMap<>();

	// @Autowired
	// private IStockInfoDao stockInfoDao;

	public static void main(String[] args) {

		final FileSystemXmlApplicationContext classpathContext = new FileSystemXmlApplicationContext(
				"config/applicationContext.xml");

		Properties pro = new Properties();
		try {
			FileInputStream in = new FileInputStream("config/config.properties");
			pro.load(in);
			in.close();
		} catch (IOException e) {
			System.err.println("IO Exception, reading config!" + e.getMessage());
		}

		Iterator<String> it = pro.stringPropertyNames().iterator();
		while (it.hasNext()) {
			String key = it.next();
			properties.put(key, pro.getProperty(key));
			System.out.println(key + ":" + pro.getProperty(key));
		}

		days = Integer.parseInt(pro.getProperty("days"));
		windCode = pro.getProperty("windCode");
		leastPhasePercent = Double.parseDouble(pro.getProperty("leastPhasePercent"));
		amplitude = Double.parseDouble(pro.getProperty("amplitude"));
		startPrice = new BigDecimal(Double.parseDouble(pro.getProperty("startPrice")));

		int[] phase = randomX(days, leastPhasePercent);

		// thread 传参
		int a = 1;

		BigDecimal[] y = randomY(startPrice, amplitude);

		for (int i = 0; i < y.length; i++) {
			System.out.println("**********six pionts: " + y[i]);
		}

		BigDecimal[][] kB = calcLineParamKAndB(0, y, phase);

		BigDecimal lastClosedPrice = startPrice;

		for (int i = 1; i < phase[a] - phase[a - 1]; i++) {
			System.out.println("processing line: y = " + kB[0][i - 1] + "x + " + kB[1][i - 1]);
		}
		// for (int i = 1; i <= days; i++) {
		// StockInfo stockInfo = new StockInfo();
		// stockInfo.setWindCode(windCode);
		// stockInfo.setSnid(i);
		// BigDecimal priceOnLine = getPriceOnline(i, kB, phase);
		// if (priceOnLine == null) {
		// System.err.println("error when getPriceOnline");
		// break;
		// }
		// stockInfo = StockUtil.randomFourPrice(stockInfo, priceOnLine,
		// lastClosedPrice);
		// for (int j = 1; j < phase.length; j++) {
		// if (i == phase[j]) {
		// stockInfo.setClose(priceOnLine);
		// if (stockInfo.getLow().doubleValue() < priceOnLine.doubleValue()) {
		// stockInfo.setLow(
		// StockUtil.getRandomBigDecimal(StockUtil.limitDown(lastClosedPrice),
		// priceOnLine));
		// }
		// }
		// }
		// // stockInfoDao.insert(stockInfo);
		// cache.put(i, stockInfo);
		// System.out.println("***********SNID = " + i + "****" +
		// stockInfo.getSnid() + "**" + stockInfo.getClose());
		// lastClosedPrice = stockInfo.getClose();
		// }

	}

	public class MyThread2 implements Runnable {
		private String name;

		public void setName(String name) {
			this.name = name;
		}

		public void run() {
			System.out.println("hello " + name);
		}

	}

	// up trend = 0， down trend = 1
	private static BigDecimal[][] calcLineParamKAndB(int trend, BigDecimal[] y, int[] x) {
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

	private static BigDecimal getPriceOnline(int x, BigDecimal[][] kB, int[] phase) {

		BigDecimal result = null;
		for (int i = 0; i < 5; i++) {
			if (x >= phase[i] && x < phase[i + 1]) {
				result = (kB[0][i].multiply(new BigDecimal(x))).add(kB[1][i]);
			}
		}
		return result;
	}

	/*
	 * 参数：初始值和振幅 方法： 随机出三个高点和三个低点
	 */
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

	/*
	 * 参数：初始值和振幅 方法： 随机出三个高点和三个低点
	 */
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
			System.out.println("**********phase[" + i + "]" + range[i]);
		}
		return range;
	}

	/* 随机出的值与其他值比较 */
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

}
