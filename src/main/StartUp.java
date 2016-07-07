package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import dao.mapper.IStockInfoDao;
import model.StockInfo;
import service.IPhaseGenerater;
import service.IPriceGenerater;
import util.StockUtil;

public class StartUp {

	private static StartUp instance;

	public static StartUp getInstance() {
		if (instance == null) {
			instance = new StartUp();
		}
		return instance;
	}

	private StartUp() {
	}

	private int days;
	private String windCode;
	private double leastPhasePercent;
	private double amplitude;
	private BigDecimal startPrice;

	final Map<Integer, StockInfo> cache = new HashMap<>();
	private Map<String, String> properties = new HashMap<>();

	@Autowired
	private IStockInfoDao stockInfoDaoImpl;

	@Autowired
	private IPhaseGenerater phaseGenerater;

	@Autowired
	private IPriceGenerater linearPriceGeneraterImpl;

	public Collection<StockInfo> generate() {

		// stockInfoDao = classpathContext.getBean(StockInfoDaoImpl.class);
		// priceGenerater =
		// classpathContext.getBean(LinearPriceGeneraterImpl.class);
		// phaseGenerater = classpathContext.getBean(PhaseGeneraterImpl.class);
		Properties pro = getProperties("config/config.properties");

		days = Integer.parseInt(pro.getProperty("days"));
		windCode = pro.getProperty("windCode");
		leastPhasePercent = Double.parseDouble(pro.getProperty("leastPhasePercent"));
		amplitude = Double.parseDouble(pro.getProperty("amplitude"));
		startPrice = new BigDecimal(Double.parseDouble(pro.getProperty("startPrice")));

		// random point
		int[] x = phaseGenerater.divideX(days, 1, leastPhasePercent);
		BigDecimal[] y = phaseGenerater.generateY(startPrice, 1, amplitude);
		for (int i = 0; i < y.length; i++) {
			System.out.println("**********six pionts: {x = " + x[i] + ", y = " + y[i]);
		}

		// generate model
		BigDecimal[][] kB = linearPriceGeneraterImpl.calcLineParamAndB(0, y, x);

		// for (int i = 1; i < phase[a] - phase[a - 1]; i++) {
		// System.out.println("processing line: y = " + kB[0][i - 1] + "x + " +
		// kB[1][i - 1]);
		// }
		// GenerateThread generateThread = new GenerateThread();
		// generateThread.setProperties(x, kB);
		// Thread thread1 = new Thread(generateThread);
		// thread1.start();

		Collection<StockInfo> stockInfos = run(x, kB);
		return stockInfos;
	}

	private Collection<StockInfo> run(int[] x, BigDecimal[][] kB) {
		BigDecimal lastClosedPrice = startPrice;
		stockInfoDaoImpl.deleteAll();
		Collection<StockInfo> stockInfos = new LinkedList<>();
		for (int i = 1; i <= days; i++) {
			StockInfo stockInfo = new StockInfo();
			stockInfo.setWindCode(windCode);
			stockInfo.setSnid(i);
			BigDecimal priceOnLine = linearPriceGeneraterImpl.getPriceOnline(i, kB, x);
			if (priceOnLine == null) {
				System.err.println("error when getPriceOnline");
				break;
			}
			// generate price
			stockInfo = linearPriceGeneraterImpl.generateFourPrices(stockInfo, priceOnLine, lastClosedPrice);
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
			// stockInfoDaoImpl.insert(stockInfo);
			stockInfos.add(stockInfo);
			cache.put(i, stockInfo);
			lastClosedPrice = stockInfo.getClose();
		}
		return stockInfos;
	}

	private Properties getProperties(String filePath) {
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
