package model;

import java.math.BigDecimal;

public class StockInfo {

	private String windCode;
	
	private int snid;
	
	private BigDecimal open;
	
	private BigDecimal close;
	
	private BigDecimal high;
	
	private BigDecimal low;

	public String getWindCode() {
		return windCode;
	}

	public void setWindCode(String windCode) {
		this.windCode = windCode;
	}

	public int getSnid() {
		return snid;
	}

	public void setSnid(int snid) {
		this.snid = snid;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	@Override
	public String toString() {
		return "windCode" + windCode + "SNID" + snid + "close" + close;
		
	}
	
	
}
