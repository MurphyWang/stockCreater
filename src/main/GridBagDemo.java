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
		openButton = new JButton("打开");
		openButton.addActionListener(new ReGenerateButtonListener());
		saveButton = new JButton("保存");
		saveOtherButton = new JButton("另存为");
		j4 = new JPanel();
		String[] str = { "java笔记", "C#笔记", "HTML5笔记" };
		combo = new JComboBox(str);
		j6 = new JTextField();
		clearButton = new JButton("清空");
		list = new JList(str);
		Collection<StockInfo> stockInfos = StartUp.getInstance().generate();
		JFreeChart chart = KLineCombineChart.getInstance().getChart(stockInfos);
		panel = new ChartPanel(chart);
		panel.setFillZoomRectangle(true);
		panel.setMouseWheelEnabled(true);
//		frame.setBackground(Color.PINK);// 为了看出效果，设置了颜色
		
		
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.add(panel);
		this.add(openButton);// 把组件添加进jframe
		this.add(saveButton);
//		this.add(saveOtherButton);
//		this.add(j4);
//		this.add(combo);
//		this.add(j6);
//		this.add(clearButton);
//		this.add(list);
		GridBagConstraints s = new GridBagConstraints();// 定义一个GridBagConstraints，
		// 是用来控制添加进的组件的显示位置
		s.fill = GridBagConstraints.BOTH;
		// 该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
		// NONE：不调整组件大小。
		// HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
		// VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
		// BOTH：使组件完全填满其显示区域。
		s.gridwidth = 8;
		s.gridheight = 8;
		s.weightx = 1;
		s.weighty = 1;
		layout.setConstraints(panel, s);
		s.gridwidth = 1;// 该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
		s.gridheight = 1;
		s.weightx = 0;// 该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		s.weighty = 0;// 该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		layout.setConstraints(openButton, s);// 设置组件
		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(saveButton, s);
		s.gridwidth = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(saveOtherButton, s);
		s.gridwidth = 0;// 该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
		s.weightx = 0;// 不能为1，j4是占了4个格，并且可以横向拉伸，
		// 但是如果为1，后面行的列的格也会跟着拉伸,导致j7所在的列也可以拉伸
		// 所以应该是跟着j6进行拉伸
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
