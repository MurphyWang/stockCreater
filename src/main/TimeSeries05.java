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
 * ʱ������ͼ:�ƶ�ƽ����
 * 
 * �������ܵ㣺 �� ����һ�����ݺ���ͼ������ʾ���������ݵ��ƶ�ƽ����
 * 
 * @author ��ΰ 2012-10-25
 * 
 *         ¥������������˽����ṩ����ҹۿ�����ϧ¥�����࣬ת��ʱ��ע��������http://lw2078.iteye.com/
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
		TimeSeries05 timeSeries = new TimeSeries05("�ƶ�ƽ����ʾ��ͼ");
		timeSeries.pack();
		RefineryUtilities.centerFrameOnScreen(timeSeries);
		timeSeries.setVisible(true);
	}

	/**
	 * ��ʾ��Demoͼ�������
	 * 
	 * ChartBasePanel����Swing������������ӹ�ͬʹ�õģ����ﲻ�ظ�����
	 * �����Դ��λ�ã�http://lw2078.iteye.com/blog/1705637
	 */
	private class TimeSeriesPanel extends ChartBasePanel {
		private TimeSeries series[] = new TimeSeries[2]; // �������ʱ��(���ꡢ�¡��ա�ʱ���֡����)����������
		private ChartPanel chartPanel;
		private JFreeChart chart = createChart(); // ����һ��JFreeChartʱ������ͼ��

		public TimeSeriesPanel() {
			super();

			addChart(this.chart); // ����JFreeChart����JFreeChart�б���

			// ��JFreeChart����ר�õ�ͼ������ChartPanel��
			this.chartPanel = new ChartPanel(this.chart);
			this.chartPanel.setPreferredSize(new Dimension(600, 250));

			// ����chartPanel�����߿�
			CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createEtchedBorder());
			this.chartPanel.setBorder(compoundBorder);

			// ��chartPanel���뵽��������
			add(this.chartPanel);
		}

		/**
		 * ����jfreechartͼ��
		 */
		private JFreeChart createChart() {
			// ����ͼ�����ݼ���
			XYDataset xyDataset = createDataset();

			// ���Ӻ���֧��
			StandardChartTheme standardChartTheme = new StandardChartTheme("CN"); // ����������ʽ
			standardChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 20)); // ���ñ�������
			standardChartTheme.setRegularFont(new Font("SimSun", Font.PLAIN, 15)); // ����ͼ��������
			standardChartTheme.setLargeFont(new Font("����", Font.PLAIN, 15)); // �������������
			ChartFactory.setChartTheme(standardChartTheme); // Ӧ��������ʽ

			// ����һ��ʱ������ͼ���JFreeChart
			JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart("�ƶ�ƽ����ʾ��ͼ", // ͼ����
					"ʱ��", // �����ǩ����
					"��ֵ", // �����ǩ����
					xyDataset, // ͼ������ݼ���
					true, // �Ƿ���ʾͼ����ÿ���������е�˵��
					false, // �Ƿ���ʾ������ʾ
					false); // �Ƿ���ʾͼ�������õ�url��������

			// XYPlotͼ����������ö���,��������ͼ���һЩ��ʾ����
			XYPlot xyPlot = (XYPlot) jFreeChart.getPlot();

			// ����Xʱ���ᰴ����ʾ��ʱ����Ϊ1����
			DateAxis dateAxis = (DateAxis) xyPlot.getDomainAxis(); // DateAxis��Xʱ�����ߵ���ʾ��ʽ���ö���
			SimpleDateFormat frm = new SimpleDateFormat("MM��"); // ����ʱ����ʾ��ʽ
			dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH, 1, frm)); // ������ʾʱ����Ϊ1��

			return jFreeChart;
		}

		/**
		 * ����jfreechartͼ�����õ����ݼ���
		 * 
		 * @return
		 */
		private XYDataset createDataset() {

			// ������������
			this.series[0] = new TimeSeries("��Ʊ�۸�");
			setSeriesData(series[0], 100, new Day(1, 1, 2011), 365); // ����Ϊʱ�䵥λ����2011��1��1�տ�ʼ���������365���ÿ���ģ������

			////////////////////////////// �������ܵ�
			////////////////////////////// ////////////////////////////////////
			/*
			 * MovingAverage�ж�������ƶ�ƽ���߶���ķ�����
			 * �и���һ���������д�������һ���ƶ�ƽ�������С��и���һ�����ݼ��ϴ������ش��ƶ�ƽ���ߵ����ݼ��ϵĵȶ��ַ���
			 * �����õ����ǵ�һ�ִ�����ʽ
			 */
			// �����������������ƶ�ƽ��ֵ����
			this.series[1] = MovingAverage.createMovingAverage(series[0], // Դ��������
					"�ƶ�ƽ����", // Ҫ�������ƶ�ƽ������������
					30, // �����ƶ�ƽ���ߵ����ݿ��
					0); // ��ʼ���������ݵ�ĸ���
			//////////////////////////////////////////////////////////////////////

			// �������������ж�����һ�����ݼ�����
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(this.series[0]);
			dataset.addSeries(this.series[1]);

			return dataset;
		}

		/**
		 * �����������,�Զ���λ��ʱ�������ϵ���һ��ʱ��㣬�������ݵ���뵽����������
		 * 
		 * @param series
		 *            �������ж���
		 * @param baseData
		 *            ���ɵ�������ݵĻ�׼ֵ
		 * @param regularTime
		 *            ������ʱ����(�ꡢ�¡��ա�ʱ���֡����)
		 * @param sampleNum
		 *            ���ɵ����ݵ����
		 */
		private void setSeriesData(TimeSeries series, double baseData, RegularTimePeriod regularTime, int sampleNum) {

			// �������ģ������
			double value = baseData;
			for (int i = 0; i < sampleNum; i++) {
				series.add(regularTime, value);
				regularTime = regularTime.next(); // �Զ���λ����һ��ʱ���
				value *= (1.0D + (Math.random() - 0.495D) / 4.0D);
			}
		}
	}

}
