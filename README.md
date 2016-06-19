# stockCreater
//Stockinfo类有windCode, snid, open, close, high, low
Class StockInfo{
}
  
//根据文件路径config/config.property读取参数
Map getProperties(String filepath);
  
//numberOfModel:震荡上升=1； 震荡下降=2
//获得不同的处理器来进行下面的处理操作
Handler getModelHandler(int numberOfModel);
  
  
/*参数：
days:k线数
leastPhasePercent:震荡每一段的最小区间占总区间比例。
  
方法：
生成关键点的x坐标*/
int[] divideX(int days, double leastPhasePercent);
  
  
/*参数：
startPrice:初始价格
amplitude:振幅
x:生成好的x坐标的集合
方法：
生成关键点的y坐标*/
BigDecimal[] generateY(BigDecimal startPrice, BigDecimal amplitude, int[] x);
  
/*参数:
生成的点的坐标x,y
方法：计算出模型的参数A和B，（根据两个点算两个参数）*/
BigDecimal[][] calcLineParamAndB(BigDecimal[] y, int[] x)


//算出每一段的模型后
Class GenerateThread() extend Thread{
  //将参数传给子线程：handler, 模型的参数A和B, 已生成的X[]，初始值startprice 
  public void setProperties(SelfProperty property)
    
  //根据模型计算出每个点的Stockinfo对应的四个值，并插入数据库
  run(){
    for(int i = 0; i<x[a]-x[a-1]; i++){
      Stockinfo stockInfo = handler.generateFourPrices();
      //数据库配置在config/jdbc.properties
      dao.insert(stockInfo)
    }
  }
}

/*参数：priceOnLine 模型上的价格
lastClosedPrice 前一日的收盘价
方法，根据前一天的收盘价随机生成四个±10%的数据，排序，中间两个任意选择一个为开盘价，另一个为收盘价了，同时要满足收盘价落在模型价格的某一个区间（直线模型就是±10%）范围内，否则重新随机。*/
StockInfo generateFourPrices(StockInfo stockInfo, BigDecimal priceOnLine, BigDecimal lastClosedPrice);
