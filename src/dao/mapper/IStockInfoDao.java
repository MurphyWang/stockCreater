package dao.mapper;

import java.util.Collection;

import model.StockInfo;

public interface StockInfoMapper {
	void insert(StockInfo stockInfo);
	
	void getById(int id);
	
	void update(StockInfo stockInfo);
	
	Collection<StockInfo> getAll();
	
	void delete(int id);

	void deleteAll();

}
