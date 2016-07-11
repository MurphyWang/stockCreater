package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import gui.KLineCombineChart;
import listener.ReGenerateButtonListener;
import model.StockInfo;

public class GridBagDemo extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2052917652869460965L;
	/**
	 * 
	 */
	public static void main(String args[]) {
		try {
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			// TODO exception
		}
		final FileSystemXmlApplicationContext classpathContext = new FileSystemXmlApplicationContext(
				"config/applicationContext.xml");
		GridBagDemo demo = new GridBagDemo();
	}

	public GridBagDemo() {
		init();
		this.setSize(800, 600);
		this.setVisible(true);
	}

	public void init() {
		openButton = new JButton("open");
		openButton.addActionListener(new ReGenerateButtonListener());
		saveButton = new JButton("save");
		saveOtherButton = new JButton("saveOther");
		j4 = new JPanel();
		String[] str = { "java", "C#", "HTML5" };
		combo = new JComboBox(str);
		j6 = new JTextField();
		clearButton = new JButton("clear");
		list = new JList(str);
		Collection<StockInfo> stockInfos = StartUp.getInstance().generate();
		JFreeChart chart = KLineCombineChart.getInstance().getChart(stockInfos);
		panel = new ChartPanel(chart);
		panel.setFillZoomRectangle(true);
		panel.setMouseWheelEnabled(true);
//		frame.setBackground(Color.PINK);
		
		
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.add(panel);
		this.add(openButton);//
		this.add(saveButton);
//		this.add(saveOtherButton);
//		this.add(j4);
//		this.add(combo);
//		this.add(j6);
//		this.add(clearButton);
//		this.add(list);
		GridBagConstraints s = new GridBagConstraints();//
		//
		s.fill = GridBagConstraints.BOTH;
		
		s.gridwidth = 8;
		s.gridheight = 8;
		s.weightx = 1;
		s.weighty = 1;
		layout.setConstraints(panel, s);
		s.gridwidth = 1;
		s.gridheight = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(openButton, s);
		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(saveButton, s);
		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(saveOtherButton, s);
		s.gridwidth = 0;
		s.weightx = 0;
		
		s.weighty = 0;
		layout.setConstraints(j4, s);
		s.gridwidth = 2;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(combo, s);
		;
		s.gridwidth = 7;
		s.weightx = 1;
		s.weighty = 0;
		layout.setConstraints(j6, s);
		;
		s.gridwidth = 0;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(clearButton, s);
		;
		s.gridwidth = 2;
		s.weightx = 0;
		s.weighty = 1;
		layout.setConstraints(list, s);
		;
	}

	JButton openButton;
	JButton saveButton;
	JButton saveOtherButton;
	JPanel j4;
	JComboBox combo;
	JTextField j6;
	JButton clearButton;
	JList list;
	ChartPanel panel;
}
