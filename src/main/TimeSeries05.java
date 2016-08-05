package main;

import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * 时间序列图:移动平均线
 * 
 * 新增功能点： ① 定义一组数据后，在图表中显示出该组数据的移动平均线
 * 
 * @author 刘伟 2012-10-25
 * 
 *         楼主辛勤整理，无私免费提供给大家观看，体惜楼主辛苦，转载时请注明出处：http://lw2078.iteye.com/
 */
@SuppressWarnings("serial")
public class TimeSeries05 extends ApplicationFrame {

	public TimeSeries05(String title) {
		super(title);
		setContentPane(new TimeSeriesPanel());
	}
	
	public JPanel createDemoPanel() {
		return new TimeSeriesPanel();
	}

	public static void main(String[] arg) {
		TimeSeries05 timeSeries = new TimeSeries05("移动平均线示例图");
		timeSeries.pack();
		RefineryUtilities.centerFrameOnScreen(timeSeries);
		timeSeries.setVisible(true);
	}

	/**
	 * 显示该Demo图表的容器
	 * 
	 * ChartBasePanel类是Swing框架下所有例子共同使用的，这里不重复贴出
	 * 这个类源码位置：http://lw2078.iteye.com/blog/1705637
	 */
	private class TimeSeriesPanel extends ChartBasePanel {
		private TimeSeries series[] = new TimeSeries[2]; // 间隔定长时间(如年、月、日、时、分、秒等)的数据序列
		private ChartPanel chartPanel;
		private JFreeChart chart = createChart(); // 创建一个JFreeChart时间序列图表

		public TimeSeriesPanel() {
			super();

			addChart(this.chart); // 将此JFreeChart加入JFreeChart列表中

			// 将JFreeChart放在专用的图表容器ChartPanel中
			this.chartPanel = new ChartPanel(this.chart);
			this.chartPanel.setPreferredSize(new Dimension(600, 250));

			// 设置chartPanel容器边框
			CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createEtchedBorder());
			this.chartPanel.setBorder(compoundBorder);

			// 将chartPanel加入到本容器中
			add(this.chartPanel);
		}

		/**
		 * 创建jfreechart图表
		 */
		private JFreeChart createChart() {
			// 生成图表数据集合
			XYDataset xyDataset = createDataset();

			// 增加汉字支持
			StandardChartTheme standardChartTheme = new StandardChartTheme("CN"); // 创建主题样式
			standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20)); // 设置标题字体
			standardChartTheme.setRegularFont(new Font("SimSun", Font.PLAIN, 15)); // 设置图例的字体
			standardChartTheme.setLargeFont(new Font("宋体", Font.PLAIN, 15)); // 设置轴向的字体
			ChartFactory.setChartTheme(standardChartTheme); // 应用主题样式

			// 创建一个时间序列图表的JFreeChart
			JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart("移动平均线示例图", // 图表名
					"时间", // 横轴标签文字
					"数值", // 纵轴标签文字
					xyDataset, // 图表的数据集合
					true, // 是否显示图表中每条数据序列的说明
					false, // 是否显示工具提示
					false); // 是否显示图表中设置的url网络连接

			// XYPlot图表区域的设置对象,用来设置图表的一些显示属性
			XYPlot xyPlot = (XYPlot) jFreeChart.getPlot();

			// 设置X时间轴按月显示，时间间隔为1个月
			DateAxis dateAxis = (DateAxis) xyPlot.getDomainAxis(); // DateAxis是X时间轴线的显示样式设置对象
			SimpleDateFormat frm = new SimpleDateFormat("MM月"); // 设置时间显示样式
			dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH, 1, frm)); // 设置显示时间间隔为1年

			return jFreeChart;
		}

		/**
		 * 创建jfreechart图表所用的数据集合
		 * 
		 * @return
		 */
		private XYDataset createDataset() {

			// 生成数据序列
			this.series[0] = new TimeSeries("股票价格");
			setSeriesData(series[0], 100, new Day(1, 1, 2011), 365); // 以天为时间单位，从2011年1月1日开始，随机产生365天的每天的模拟数据

			////////////////////////////// 新增功能点
			////////////////////////////// ////////////////////////////////////
			/*
			 * MovingAverage有多个创建移动平均线对象的方法，
			 * 有根据一个数据序列创建返回一个移动平均线序列、有根据一个数据集合创建返回带移动平均线的数据集合的等多种方法
			 * 这里用到的是第一种创建方式
			 */
			// 根据数据序列生成移动平均值序列
			this.series[1] = MovingAverage.createMovingAverage(series[0], // 源数据序列
					"移动平均线", // 要创建的移动平均线序列名字
					30, // 计算移动平均线的数据跨度
					0); // 初始跳过的数据点的个数
			//////////////////////////////////////////////////////////////////////

			// 将两条数据序列都放在一个数据集合中
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(this.series[0]);
			dataset.addSeries(this.series[1]);

			return dataset;
		}

		/**
		 * 随机生成数据,自动定位到时间序列上的下一个时间点，将新数据点加入到数据序列中
		 * 
		 * @param series
		 *            数据序列对象
		 * @param baseData
		 *            生成的随机数据的基准值
		 * @param regularTime
		 *            定长的时间间隔(年、月、日、时、分、秒等)
		 * @param sampleNum
		 *            生成的数据点个数
		 */
		private void setSeriesData(TimeSeries series, double baseData, RegularTimePeriod regularTime, int sampleNum) {

			// 生成随机模拟数据
			double value = baseData;
			for (int i = 0; i < sampleNum; i++) {
				series.add(regularTime, value);
				regularTime = regularTime.next(); // 自动定位到下一个时间点
				value *= (1.0D + (Math.random() - 0.495D) / 4.0D);
			}
		}
	}

}
