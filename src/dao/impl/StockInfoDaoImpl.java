package dao.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.IStockInfoDao;
import dao.mapper.StockInfoMapper;
import model.StockInfo;

@Repository
public class StockInfoDaoImpl implements IStockInfoDao{

	@Autowired
	private StockInfoMapper mapper;
	
	@Override
	public void insert(StockInfo stockInfo) {
		mapper.insert(stockInfo);
	}

	@Override
	public void getById(int id) {
		mapper.getById(id);
	}

	@Override
	public Collection<StockInfo> getAll() {
		Collection<StockInfo> stockInfos = mapper.getAll();
		return stockInfos;
	}

	@Override
	public void delete(int id) {
		mapper.delete(id);
	}

	@Override
	public void update(StockInfo stockInfo) {
		mapper.update(stockInfo);
		
	}

	@Override
	public void deleteAll() {
		mapper.deleteAll();
	}

}
