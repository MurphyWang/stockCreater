package chart.candlestickchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.geom.Ellipse2D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class ChartsMixed {
	private static final long serialVersionUID = 1L;

	public void createMixedCharts(double[][] data_sucRate, String[] rowKeys_sucRate, String[] columnKeys_sucRate,
			double[][] data_useCase, String[] rowKeys_useCase, String[] columnKeys_useCase, String picName) {
		if (data_sucRate == null || rowKeys_sucRate == null || columnKeys_sucRate == null || data_useCase == null
				|| rowKeys_useCase == null || columnKeys_useCase == null || picName == null
				|| "".equals(picName.trim())) {
			return;
		}
		CategoryDataset dataSetColumn = createDataset(data_useCase, rowKeys_useCase, columnKeys_useCase);
		CategoryDataset dataSetLine = createDataset(data_sucRate, rowKeys_sucRate, columnKeys_sucRate);

		createChart(dataSetColumn, dataSetLine, picName);
	}

	@SuppressWarnings("deprecation")
	private void createChart(CategoryDataset dataSetColumn, CategoryDataset dataSetLine, String picName) {
		setChartTheme();
		Font font = new Font("宋体", Font.BOLD, 12);

		// 创建图形对象
		JFreeChart jfreeChart = ChartFactory.createBarChart("", // 图表标题
				"", // 目录轴的显示标签
				"", // 数值轴的显示标签
				dataSetColumn, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				false, // 是否显示图例(对于简单的柱状图必须是false)
				true, // 是否生成工具
				false);// 是否生成URL链接
		jfreeChart.getTitle().setFont(font);
		// 图表的背景色(默认为白色)
		jfreeChart.setBackgroundPaint(Color.white);
		// 设置图片背景色
		GradientPaint gradientPaint = new GradientPaint(0, 1000, Color.WHITE, 0, 0, Color.WHITE, false);
		jfreeChart.setBackgroundPaint(gradientPaint);

		CategoryPlot categoryPlot = (CategoryPlot) jfreeChart.getPlot();

		// 设置图形的背景色
		categoryPlot.setBackgroundPaint(Color.WHITE);
		// 设置图形上竖线是否显示
		categoryPlot.setDomainGridlinesVisible(false);
		// 设置图形上竖线的颜色
		categoryPlot.setDomainGridlinePaint(Color.GRAY);
		// 设置图形上横线的颜色
		categoryPlot.setRangeGridlinePaint(Color.GRAY);

		// 设置柱状图的Y轴显示样式
		setNumberAxisToColumn(categoryPlot);
		CategoryAxis categoryaxis = categoryPlot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);// 横轴斜45度
		// 设置折线图的Y轴显示样式
		setNumberAxisLine(categoryPlot);

		categoryPlot.setDataset(1, dataSetLine);// 设置数据集索引
		categoryPlot.mapDatasetToRangeAxis(1, 1);// 将该索引映射到axis
		// 第一个参数指数据集的索引,第二个参数为坐标轴的索引
		LineAndShapeRenderer lineAndShapeRenderer = new LineAndShapeRenderer();
		// 数据点被填充即不是空心点
		lineAndShapeRenderer.setShapesFilled(true);
		// 数据点间连线可见
		lineAndShapeRenderer.setLinesVisible(true);
		// 设置折线拐点的形状，圆形
		lineAndShapeRenderer.setSeriesShape(0, new Ellipse2D.Double(-2D, -2D, 4D, 4D));

		// 设置某坐标轴索引上数据集的显示样式
		categoryPlot.setRenderer(1, lineAndShapeRenderer);
		// 设置两个图的前后顺序
		// ，DatasetRenderingOrder.FORWARD表示后面的图在前者上面，DatasetRenderingOrder.REVERSE表示
		// 表示后面的图在前者后面
		categoryPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		createPicture(picName, jfreeChart);
	}

	/**
	 * 设置折线图的Y轴显示样式
	 * 
	 * @param categoryplot
	 * @return
	 */
	private CategoryPlot setNumberAxisLine(CategoryPlot categoryplot) {
		ValueAxis numberaxis = new NumberAxis("");
		numberaxis.setUpperBound(100.00D); // 纵轴上限
		numberaxis.setLowerBound(0.00D); // 纵轴下限
		categoryplot.setRangeAxis(1, numberaxis);
		return categoryplot;
	}

	/**
	 * 设置柱状图的Y轴显示样式,NumberAxis为整数格式
	 * 
	 * @param categoryplot
	 * @return
	 */
	private CategoryPlot setNumberAxisToColumn(CategoryPlot categoryplot) {
		// 获取纵轴
		NumberAxis numberAxis = (NumberAxis) categoryplot.getRangeAxis();
		// 设置纵轴的刻度线
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// 数据轴的数据标签是否自动确定（默认为true）
		numberAxis.setAutoTickUnitSelection(true);
		// 数据轴的数据标签
		numberAxis.setStandardTickUnits(numberAxis.getStandardTickUnits());
		numberAxis.setLowerBound(0); // 数据轴上的显示最小值;
		numberAxis.setAutoRangeMinimumSize(1);// 1为一个间隔单位
		categoryplot.setRangeAxis(numberAxis);
		LayeredBarRenderer layeredBarRenderer = new LayeredBarRenderer();
		// 设置柱子的边框是否显示
		layeredBarRenderer.setDrawBarOutline(false);
		// 设置柱体宽度
		layeredBarRenderer.setMaximumBarWidth(0.08);
		// 设置柱体颜色
		layeredBarRenderer.setSeriesPaint(0, new Color(198, 219, 248));
		categoryplot.setRenderer(layeredBarRenderer);

		return categoryplot;
	}

	/**
	 * 设置图表主题样式
	 */
	private void setChartTheme() {
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("宋书", Font.BOLD, 12));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 12));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 12));
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
	}

	/**
	 * 生成图片
	 * 
	 * @param picName
	 * @param jfreechart
	 */
	private void createPicture(String picName, JFreeChart jfreechart) {
		if (picName == null || "".equals(picName.trim()) || jfreechart == null) {
			return;
		}
		FileOutputStream fos_jpg = null;

		try {
			try {
				fos_jpg = new FileOutputStream(picName);
				ChartUtilities.writeChartAsJPEG(fos_jpg, 1, jfreechart, 240, 155, null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			if (fos_jpg != null) {
				try {
					fos_jpg.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 创建图表数据集
	 * 
	 * @param data
	 * @param rowKeys
	 * @param columnKeys
	 * @return
	 */
	public CategoryDataset createDataset(double[][] data, String[] rowKeys, String[] columnKeys) {
		CategoryDataset dataset = getData(data, rowKeys, columnKeys);
		return dataset;
	}

	/**
	 * 获取数据集
	 * 
	 * @param data
	 * @param rowKeys
	 * @param columnKeys
	 * @return
	 */
	private CategoryDataset getData(double[][] data, String[] rowKeys, String[] columnKeys) {

		return DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);

	}
}