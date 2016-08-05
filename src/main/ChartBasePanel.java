package main;


import java.awt.LayoutManager;  
import java.util.ArrayList;  
import javax.swing.JPanel;  
import org.jfree.chart.JFreeChart;  
  
/** 
 * 描绘JFreeChart所用到的基础panel容器 
 *  
 * @author 刘伟  2012-10-24 
 */  
@SuppressWarnings("serial")  
public class ChartBasePanel extends JPanel {  
      
    ArrayList<JFreeChart> charts = new ArrayList<JFreeChart>();  
  
    public ChartBasePanel() {  
        super();  
    }  
      
    public ChartBasePanel(LayoutManager paramLayoutManager) {  
        super(paramLayoutManager);  
    }  
  
    /** 
     * 向panel容器中添加一个JFreeChart图表对象 
     * */  
    public void addChart(JFreeChart paramJFreeChart) {  
        this.charts.add(paramJFreeChart);  
    }  
  
    /** 
     * 得到panel容器中所有的JFreeChart图表对象 
     * */  
    public JFreeChart[] getCharts() {  
        int chartNum = this.charts.size();  
        JFreeChart[] arrayOfJFreeChart = new JFreeChart[chartNum];  
          
        for (int i = 0; i < chartNum; i++){  
            arrayOfJFreeChart[i] =this.charts.get(i);  
        }  
          
        return arrayOfJFreeChart;  
    }  
}  

