package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import dao.impl.StockInfoDaoImpl;
import dao.mapper.IStockInfoDao;
import model.StockInfo;
import service.IPhaseGenerater;
import service.IPriceGenerater;
import service.impl.LinearPriceGeneraterImpl;
import service.impl.PhaseGeneraterImpl;
import util.StockUtil;

public class StartUp {

	private static int days;
	private static String windCode;
	private static double leastPhasePercent;
	private static double amplitude;
	private static BigDecimal startPrice;

	private static Map<Integer, StockInfo> cache = new HashMap<>();
	private static Map<String, String> properties = new HashMap<>();

	private static IStockInfoDao stockInfoDao;

	private static IPriceGenerater priceGenerater;

	private static IPhaseGenerater phaseGenerater;

	public static void main(String[] args) {

		final FileSystemXmlApplicationContext classpathContext = new FileSystemXmlApplicationContext(
				"config/applicationContext.xml");
		stockInfoDao = classpathContext.getBean(StockInfoDaoImpl.class);
		priceGenerater = classpathContext.getBean(LinearPriceGeneraterImpl.class);
		phaseGenerater = classpathContext.getBean(PhaseGeneraterImpl.class);
		Properties pro = getProperties("config/config.properties");

		days = Integer.parseInt(pro.getProperty("days"));
		windCode = pro.getProperty("windCode");
		leastPhasePercent = Double.parseDouble(pro.getProperty("leastPhasePercent"));
		amplitude = Double.parseDouble(pro.getProperty("amplitude"));
		startPrice = new BigDecimal(Double.parseDouble(pro.getProperty("startPrice")));

		System.out.println("**********Processing***********");
		
		// random point
		int[] x = phaseGenerater.divideX(days, 1, leastPhasePercent);
		BigDecimal[] y = phaseGenerater.generateY(startPrice, 1, amplitude);
		for (int i = 0; i < y.length; i++) {
			System.out.println("**********six pionts: {x = " + x[i] + ", y = " + y[i]);
		}

		// generate model
		BigDecimal[][] kB = priceGenerater.calcLineParamAndB(0, y, x);

		run(x, kB);
		
		System.out.println("**********Processed***********");
		// for (int i = 1; i < phase[a] - phase[a - 1]; i++) {
		// System.out.println("processing line: y = " + kB[0][i - 1] + "x + " +
		// kB[1][i - 1]);
		// }
		// GenerateThread generateThread = new GenerateThread();
		// generateThread.setProperties(x, kB);
		// Thread thread1 = new Thread(generateThread);
		// thread1.start();
	}

	private static void run(int[] x, BigDecimal[][] kB) {
		BigDecimal lastClosedPrice = startPrice;
		stockInfoDao.deleteAll();
		for (int i = 1; i <= days; i++) {
			StockInfo stockInfo = new StockInfo();
			stockInfo.setWindCode(windCode);
			stockInfo.setSnid(i);
			BigDecimal priceOnLine = priceGenerater.getPriceOnline(i, kB, x);
			if (priceOnLine == null) {
				System.err.println("error when getPriceOnline");
				break;
			}
			
			// generate price
			stockInfo = priceGenerater.generateFourPrices(stockInfo, priceOnLine, lastClosedPrice);
			for (int j = 1; j < x.length; j++) {
				if (i == x[j]) {
					stockInfo.setClose(priceOnLine);
					if (stockInfo.getLow().doubleValue() < priceOnLine.doubleValue()) {
						stockInfo.setLow(
								StockUtil.getRandomBigDecimal(StockUtil.limitDown(lastClosedPrice), priceOnLine));
					}
				}
			}
			System.out.println("***********SNID = " + i + "****" + stockInfo.getSnid() + "**" + stockInfo.getClose());
			stockInfoDao.insert(stockInfo);
			cache.put(i, stockInfo);
			lastClosedPrice = stockInfo.getClose();
		}
	}

	private static Properties getProperties(String filePath) {
		Properties pro = new Properties();
		try {
			FileInputStream in = new FileInputStream(filePath);
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
		return pro;
	}

}
