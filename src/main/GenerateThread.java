package main;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import dao.mapper.IStockInfoDao;
import model.StockInfo;
import service.IPriceGenerater;
import util.StockUtil;

public class GenerateThread extends Thread {
		private int[] x;
		private BigDecimal[][] kB;
		
		@Autowired
		private IStockInfoDao stockInfoDao;
		
		@Autowired 
		private IPriceGenerater priceGenerater;
		
		public void setProperties(int[] x, BigDecimal[][] kB) {
			this.x = x;
			this.kB = kB;
		}

		public void run() {
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
				//generate price
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
				// stockInfoDao.insert(stockInfo);
				cache.put(i, stockInfo);
				lastClosedPrice = stockInfo.getClose();
			}
		}

	}
