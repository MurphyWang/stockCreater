# stockCreater
//Stockinfo类有windCode, snid, open, close, high, low\<br>
Class StockInfo{\<br>
}
  
//根据文件路径config/config.property读取参数\<br>
Map getProperties(String filepath);\<br>
  
//numberOfModel:震荡上升=1； 震荡下降=2\<br>
//获得不同的处理器来进行下面的处理操作\<br>
Handler getModelHandler(int numberOfModel);\<br>
  
  
/*参数：\<br>
days:k线数\<br>
leastPhasePercent:震荡每一段的最小区间占总区间比例。\<br>
方法：\<br>
生成关键点的x坐标*/\<br>
int[] divideX(int days, double leastPhasePercent);\<br>
  
  
/*参数：\<br>
startPrice:初始价格\<br>
amplitude:振幅\<br>
x:生成好的x坐标的集合\<br>
方法：\<br>
生成关键点的y坐标*/\<br>
BigDecimal[] generateY(BigDecimal startPrice, BigDecimal amplitude, int[] x);\<br>
  
/*参数:\<br>
生成的点的坐标x,y\<br>
方法：计算出模型的参数A和B，（根据两个点算两个参数）*/\<br>
BigDecimal[][] calcLineParamAndB(BigDecimal[] y, int[] x)\<br>


//算出每一段的模型后\<br>
Class GenerateThread() extend Thread{\<br>
  //将参数传给子线程：handler, 模型的参数A和B, 已生成的X[]，初始值startprice \<br>
  public void setProperties(SelfProperty property)\<br>
    
  //根据模型计算出每个点的Stockinfo对应的四个值，并插入数据库\<br>
  run(){\<br>
    for(int i = 0; i<x[a]-x[a-1]; i++){\<br>
      Stockinfo stockInfo = handler.generateFourPrices();\<br>
      //数据库配置在config/jdbc.properties\<br>
      dao.insert(stockInfo)\<br>
    }\<br>
  }\<br>
}\<br>

/*参数：priceOnLine 模型上的价格\<br>
lastClosedPrice 前一日的收盘价\<br>
方法，根据前一天的收盘价随机生成四个±10%的数据，排序，中间两个任意选择一个为开盘价，另一个为收盘价了，同时要满足收盘价落在模型价格的某一个区间（直线模型就是±10%）范围内，否则重新随机。*/\<br>
StockInfo generateFourPrices(StockInfo stockInfo, BigDecimal priceOnLine, BigDecimal lastClosedPrice);\<br>
