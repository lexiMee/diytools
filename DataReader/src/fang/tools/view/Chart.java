package fang.tools.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;

import fang.tools.model.Satellite;

class Chart {


	private int mMaxStaLength;
	private double mMaxStaSnr;
	JFreeChart mJFreeChart;
	XYPlot mXYPlot;

	public Chart(String title) {
		mMaxStaLength = 0;
		mMaxStaSnr = 0;
	}

	private JFreeChart createChart(XYDataset paramXYDataset) {
		mJFreeChart = ChartFactory.createXYLineChart("GPS NMEA GSV", "TIME",
				"DB", paramXYDataset, PlotOrientation.VERTICAL, true, true,
				false);
		mJFreeChart.setBackgroundPaint(Color.white);
		mXYPlot = (XYPlot) mJFreeChart.getPlot();

		mXYPlot.setBackgroundPaint(Color.lightGray);
		mXYPlot.setAxisOffset(new RectangleInsets(5.0D, 5.0D, 5.0D, 5.0D));
		mXYPlot.setDomainGridlinePaint(Color.white);
		mXYPlot.setRangeGridlinePaint(Color.white);

		NumberAxis axis1 = new NumberAxis("TIME/s");
		NumberAxis axis2 = new NumberAxis("SNR/db");

		double unit = 10;// 刻度的长度
		NumberTickUnit ntu = new NumberTickUnit(unit);
		axis1.setTickUnit(ntu);
		axis1.setTickLabelPaint(Color.blue);
		axis1.setRange(1, 100);

		unit = 20;// 刻度的长度
		ntu = new NumberTickUnit(unit);
		axis2.setTickUnit(ntu);
		axis2.setTickLabelPaint(Color.red);
		axis2.setRange(1, 100);

		mXYPlot.setDomainAxis(0, axis1);
		mXYPlot.setRangeAxis(0, axis2);

		return mJFreeChart;
	}

	public void repaintPanel(HashMap<Integer, ArrayList<Double>> map) {

		XYDataset xy = createCustomerDataset(map.size(), map);

		NumberAxis axis1 = new NumberAxis("TIME/s");
		NumberAxis axis2 = new NumberAxis("SNR/db");

		double unit = mMaxStaLength / 10;// 刻度的长度
		NumberTickUnit ntu = new NumberTickUnit(unit);
		axis1.setTickUnit(ntu);
		axis1.setTickLabelPaint(Color.blue);
		axis1.setRange(0, mMaxStaLength);

		unit = mMaxStaSnr / 10;// 刻度的长度
		ntu = new NumberTickUnit(unit);
		axis2.setTickUnit(ntu);
		axis2.setTickLabelPaint(Color.red);
		axis2.setRange(0, mMaxStaSnr + 10);

		mXYPlot.setDomainAxis(0, axis1);
		mXYPlot.setRangeAxis(0, axis2);
		
		// 获取折线对象
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) mXYPlot.getRenderer();

		// 定义折线绘制风格
		BasicStroke realLine = new BasicStroke(3.0f);
		
		for (int i = 0; i < map.size(); i++) {
				renderer.setSeriesStroke(i, realLine);
		}

		mXYPlot.setDataset(xy);

	}

	public JPanel createDemoPanel() {
		JFreeChart localJFreeChart = createChart(createDataset());
		return new ChartPanel(localJFreeChart);
	}


	private XYDataset createCustomerDataset(int size,
			HashMap<Integer, ArrayList<Double>> map) {
		XYSeriesCollection localXYSeriesCollection = new XYSeriesCollection();
		int i = 0;

		Iterator<Entry<Integer, ArrayList<Double>>> mapit = map.entrySet()
				.iterator();
		while (mapit.hasNext()) {
			i = 0;
			Entry<Integer, ArrayList<Double>> entry = mapit.next();
			XYSeries localXYSeries = new XYSeries("Sta"
					+ entry.getKey().toString());
			ArrayList<Double> temp = entry.getValue();
			Iterator<Double> it = temp.iterator();
			System.out.println("Sta" + entry.getKey() + " : size:" + temp.size());
			while (it.hasNext()) {
				Double snr = it.next();
				mMaxStaSnr = mMaxStaSnr <= snr ? snr : mMaxStaSnr;
				localXYSeries.add(i++, snr);
			}
			mMaxStaLength = mMaxStaLength <= i ? i : mMaxStaLength;
			localXYSeriesCollection.addSeries(localXYSeries);
		}
		return localXYSeriesCollection;
	}

	private static XYDataset createDataset() {
		return new XYSeriesCollection();
	}


}