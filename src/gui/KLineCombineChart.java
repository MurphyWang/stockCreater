package gui;



import java.awt.Color;
import java.awt.Paint;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import model.StockInfo;
import util.StockUtil;

public class KLineCombineChart {
	private static KLineCombineChart instance;

	public static KLineCombineChart getInstance() {
		if (instance == null) {
			instance = new KLineCombineChart();
		}
		return instance;
	}

	private KLineCombineChart() {
	}

	public JFreeChart getChart(Collection<StockInfo> stockInfos) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		double highValue = Double.MIN_VALUE;
		double minValue = Double.MAX_VALUE;
		double high2Value = Double.MIN_VALUE;
		double min2Value = Double.MAX_VALUE;
		OHLCSeries series = new OHLCSeries("");
		TimeSeries series2 = new TimeSeries("");
		RegularTimePeriod period = new Day(1, 1, 2001);
		for (StockInfo stockInfo : stockInfos) {
			period = period.next();
			series.add(period, stockInfo.getOpen().doubleValue(), stockInfo.getHigh().doubleValue(),
					stockInfo.getLow().doubleValue(), stockInfo.getClose().doubleValue());
			series2.add(period, StockUtil.getRandomDouble(10000.1, 1000000.1));
		}
		final OHLCSeriesCollection seriesCollection = new OHLCSeriesCollection();
		seriesCollection.addSeries(series);
		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
		timeSeriesCollection.addSeries(series2);

		int seriesCount = seriesCollection.getSeriesCount();
		for (int i = 0; i < seriesCount; i++) {
			int itemCount = seriesCollection.getItemCount(i);
			for (int j = 0; j < itemCount; j++) {
				if (highValue < seriesCollection.getHighValue(i, j)) {
					highValue = seriesCollection.getHighValue(i, j);
				}
				if (minValue > seriesCollection.getLowValue(i, j)) {
					minValue = seriesCollection.getLowValue(i, j);
				}
			}

		}

		int seriesCount2 = timeSeriesCollection.getSeriesCount();
		for (int i = 0; i < seriesCount2; i++) {
			int itemCount = timeSeriesCollection.getItemCount(i);
			for (int j = 0; j < itemCount; j++) {
				if (high2Value < timeSeriesCollection.getYValue(i, j)) {
					high2Value = timeSeriesCollection.getYValue(i, j);
				}
				if (min2Value > timeSeriesCollection.getYValue(i, j)) {
					min2Value = timeSeriesCollection.getYValue(i, j);
				}
			}

		}
		final CandlestickRenderer candlestickRender = new CandlestickRenderer();
		candlestickRender.setUseOutlinePaint(true);
//		candlestickRender.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
		candlestickRender.setCandleWidth(3);
		candlestickRender.setAutoWidthGap(0.1);
		candlestickRender.setUpPaint(Color.red);
		candlestickRender.setDownPaint(Color.GREEN);
		DateAxis x1Axis = new DateAxis();
//		x1Axis.setAutoRange(false);
//		try {
//			x1Axis.setRange(dateFormat.parse("2007-08-20"), dateFormat.parse("2007-09-29"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		x1Axis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
		x1Axis.setAutoTickUnitSelection(false);
		x1Axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
		x1Axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
//		x1Axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 7));
		x1Axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
		NumberAxis y1Axis = new NumberAxis();
		y1Axis.setAutoRange(false);
		y1Axis.setRange(minValue * 0.9, highValue * 1.1);
		y1Axis.setTickUnit(new NumberTickUnit((highValue * 1.1 - minValue * 0.9) / 10));
		XYPlot plot1 = new XYPlot(seriesCollection, x1Axis, y1Axis, candlestickRender);
		
		XYBarRenderer xyBarRender = new XYBarRenderer() {
			private final long serialVersionUID = 1L;

			public Paint getItemPaint(int i, int j) {
				if (seriesCollection.getCloseValue(i, j) > seriesCollection.getOpenValue(i, j)) {
					return candlestickRender.getUpPaint();
				} else {
					return candlestickRender.getDownPaint();
				}
			}
		};
		xyBarRender.setMargin(0.2);
		xyBarRender.setShadowVisible(true);
		xyBarRender.setDrawBarOutline(false);
		NumberAxis y2Axis = new NumberAxis();
		y2Axis.setAutoRange(false);
		y2Axis.setRange(min2Value * 0.9, high2Value * 1.1);
		y2Axis.setTickUnit(new NumberTickUnit((high2Value * 1.1 - min2Value * 0.9) / 4));
		XYPlot plot2 = new XYPlot(timeSeriesCollection, null, y2Axis, xyBarRender);
		CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(x1Axis);
		combineddomainxyplot.add(plot1, 2);
		combineddomainxyplot.add(plot2, 1);
		combineddomainxyplot.setGap(10);
		JFreeChart chart = new JFreeChart("֤ȯb 150172.SZ", JFreeChart.DEFAULT_TITLE_FONT, combineddomainxyplot, false);
		ChartPanel panel = new ChartPanel(chart);
		panel.setFillZoomRectangle(true);
		panel.setMouseWheelEnabled(true);
		return chart;
	}

}
