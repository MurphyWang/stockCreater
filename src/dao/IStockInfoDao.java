package dao;

import java.util.Collection;

import model.StockInfo;

public interface IStockInfoDao {
	void insert(StockInfo stockInfo);
	
	void update(StockInfo stockInfo);
	
	void getById(int id);
	
	Collection<StockInfo> getAll();
	
	void delete(int id);
}
