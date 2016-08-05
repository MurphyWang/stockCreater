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
		Font font = new Font("����", Font.BOLD, 12);

		// ����ͼ�ζ���
		JFreeChart jfreeChart = ChartFactory.createBarChart("", // ͼ�����
				"", // Ŀ¼�����ʾ��ǩ
				"", // ��ֵ�����ʾ��ǩ
				dataSetColumn, // ���ݼ�
				PlotOrientation.VERTICAL, // ͼ����ˮƽ����ֱ
				false, // �Ƿ���ʾͼ��(���ڼ򵥵���״ͼ������false)
				true, // �Ƿ����ɹ���
				false);// �Ƿ�����URL����
		jfreeChart.getTitle().setFont(font);
		// ͼ��ı���ɫ(Ĭ��Ϊ��ɫ)
		jfreeChart.setBackgroundPaint(Color.white);
		// ����ͼƬ����ɫ
		GradientPaint gradientPaint = new GradientPaint(0, 1000, Color.WHITE, 0, 0, Color.WHITE, false);
		jfreeChart.setBackgroundPaint(gradientPaint);

		CategoryPlot categoryPlot = (CategoryPlot) jfreeChart.getPlot();

		// ����ͼ�εı���ɫ
		categoryPlot.setBackgroundPaint(Color.WHITE);
		// ����ͼ���������Ƿ���ʾ
		categoryPlot.setDomainGridlinesVisible(false);
		// ����ͼ�������ߵ���ɫ
		categoryPlot.setDomainGridlinePaint(Color.GRAY);
		// ����ͼ���Ϻ��ߵ���ɫ
		categoryPlot.setRangeGridlinePaint(Color.GRAY);

		// ������״ͼ��Y����ʾ��ʽ
		setNumberAxisToColumn(categoryPlot);
		CategoryAxis categoryaxis = categoryPlot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);// ����б45��
		// ��������ͼ��Y����ʾ��ʽ
		setNumberAxisLine(categoryPlot);

		categoryPlot.setDataset(1, dataSetLine);// �������ݼ�����
		categoryPlot.mapDatasetToRangeAxis(1, 1);// ��������ӳ�䵽axis
		// ��һ������ָ���ݼ�������,�ڶ�������Ϊ�����������
		LineAndShapeRenderer lineAndShapeRenderer = new LineAndShapeRenderer();
		// ���ݵ㱻��伴���ǿ��ĵ�
		lineAndShapeRenderer.setShapesFilled(true);
		// ���ݵ�����߿ɼ�
		lineAndShapeRenderer.setLinesVisible(true);
		// �������߹յ����״��Բ��
		lineAndShapeRenderer.setSeriesShape(0, new Ellipse2D.Double(-2D, -2D, 4D, 4D));

		// ����ĳ���������������ݼ�����ʾ��ʽ
		categoryPlot.setRenderer(1, lineAndShapeRenderer);
		// ��������ͼ��ǰ��˳��
		// ��DatasetRenderingOrder.FORWARD��ʾ�����ͼ��ǰ�����棬DatasetRenderingOrder.REVERSE��ʾ
		// ��ʾ�����ͼ��ǰ�ߺ���
		categoryPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		createPicture(picName, jfreeChart);
	}

	/**
	 * ��������ͼ��Y����ʾ��ʽ
	 * 
	 * @param categoryplot
	 * @return
	 */
	private CategoryPlot setNumberAxisLine(CategoryPlot categoryplot) {
		ValueAxis numberaxis = new NumberAxis("");
		numberaxis.setUpperBound(100.00D); // ��������
		numberaxis.setLowerBound(0.00D); // ��������
		categoryplot.setRangeAxis(1, numberaxis);
		return categoryplot;
	}

	/**
	 * ������״ͼ��Y����ʾ��ʽ,NumberAxisΪ������ʽ
	 * 
	 * @param categoryplot
	 * @return
	 */
	private CategoryPlot setNumberAxisToColumn(CategoryPlot categoryplot) {
		// ��ȡ����
		NumberAxis numberAxis = (NumberAxis) categoryplot.getRangeAxis();
		// ��������Ŀ̶���
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// ����������ݱ�ǩ�Ƿ��Զ�ȷ����Ĭ��Ϊtrue��
		numberAxis.setAutoTickUnitSelection(true);
		// ����������ݱ�ǩ
		numberAxis.setStandardTickUnits(numberAxis.getStandardTickUnits());
		numberAxis.setLowerBound(0); // �������ϵ���ʾ��Сֵ;
		numberAxis.setAutoRangeMinimumSize(1);// 1Ϊһ�������λ
		categoryplot.setRangeAxis(numberAxis);
		LayeredBarRenderer layeredBarRenderer = new LayeredBarRenderer();
		// �������ӵı߿��Ƿ���ʾ
		layeredBarRenderer.setDrawBarOutline(false);
		// ����������
		layeredBarRenderer.setMaximumBarWidth(0.08);
		// ����������ɫ
		layeredBarRenderer.setSeriesPaint(0, new Color(198, 219, 248));
		categoryplot.setRenderer(layeredBarRenderer);

		return categoryplot;
	}

	/**
	 * ����ͼ��������ʽ
	 */
	private void setChartTheme() {
		// ����������ʽ
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// ���ñ�������
		standardChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 12));
		// ����ͼ��������
		standardChartTheme.setRegularFont(new Font("����", Font.PLAIN, 12));
		// �������������
		standardChartTheme.setLargeFont(new Font("����", Font.PLAIN, 12));
		// Ӧ��������ʽ
		ChartFactory.setChartTheme(standardChartTheme);
	}

	/**
	 * ����ͼƬ
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
	 * ����ͼ�����ݼ�
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
	 * ��ȡ���ݼ�
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