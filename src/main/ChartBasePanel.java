package main;


import java.awt.LayoutManager;  
import java.util.ArrayList;  
import javax.swing.JPanel;  
import org.jfree.chart.JFreeChart;  
  
/** 
 * ���JFreeChart���õ��Ļ���panel���� 
 *  
 * @author ��ΰ  2012-10-24 
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
     * ��panel���������һ��JFreeChartͼ����� 
     * */  
    public void addChart(JFreeChart paramJFreeChart) {  
        this.charts.add(paramJFreeChart);  
    }  
  
    /** 
     * �õ�panel���������е�JFreeChartͼ����� 
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

