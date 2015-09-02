package fang.tools.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import fang.tools.controller.ControllerInterface;
import fang.tools.model.GPSModelInterface;
import fang.tools.model.GPSObserver;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class GPSView extends ApplicationFrame implements GPSObserver, ActionListener{
	private ControllerInterface mControl = null;
	private GPSModelInterface mModel = null;
	private Chart mChart = null;
	private JButton mOpen;
	private JButton mStart;
	private JLabel mLabel;
	private JTextArea mText;
	private JFileChooser mFileChooser;
	private File mFile;

	public GPSView(ControllerInterface control, GPSModelInterface model) {
		super("Fang");
		
		mControl = control;
		mModel = model;
		model.registerObserver(this);
		mChart = new Chart("GPS");
		initUI();
		this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
	}

	private void initUI()
	{
		mFile = new File("D:\\Lenovo\\workspace\\DataReader\\data");
		JPanel chartPanel = mChart.createDemoPanel();
		JPanel myDiy = new JPanel();
		mFileChooser = new JFileChooser();
		mFileChooser.setCurrentDirectory(mFile.getParentFile()); 
		mOpen = new JButton("打开文件");
		mOpen.addActionListener(this);
		mStart = new JButton("Begin");
		mStart.addActionListener(this);
		mLabel = new JLabel("Location:");
		mText = new JTextArea(1, 25);
		mText.setPreferredSize(new Dimension(1, 25));
		mText.setEditable(false);

		myDiy.setLayout(new FlowLayout());
		myDiy.add(mLabel);
		myDiy.add(mText);
		myDiy.add(mOpen);
		myDiy.add(mStart);
		
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		this.setLayout(new BorderLayout(5, 5));
		this.add(myDiy, BorderLayout.NORTH);
		this.add(chartPanel, BorderLayout.CENTER);
	}
	
	public String getType1() {
		return "File";
	}

	public void onDone() {
//		System.out.println("view onDone!");
		mStart.setEnabled(true);
		onDraw();
	}

	private void onDraw() {
		mChart.repaintPanel(mModel.getStaHashMap());
		//mModel.printGGA();
	}
	
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		System.out.println("event:" + event.getSource());
		int result = 0;
		if (event.getSource() == mOpen) {
			mFileChooser.setApproveButtonText("确定");
			mFileChooser.setDialogTitle("打开文件");
			result = mFileChooser.showOpenDialog(this);
			if (mFile != null)
				mFileChooser.setCurrentDirectory(mFile.getParentFile()); 
			if (result == JFileChooser.APPROVE_OPTION) {// 选择的是确定按钮
				mFile = mFileChooser.getSelectedFile();// 得到选择的文件
				mText.setText(mFile.getPath());
			} else if (result == JFileChooser.CANCEL_OPTION) {
				mText.setText("没有选择任何的文件!");
			} else {
				mText.setText("操作出现错误");
			}
		} else if (event.getSource() == mStart)
		{
			mControl.start();
			mStart.setEnabled(false);
		}
	}
	
	public String getFileName()
	{
		return mFile.getPath();
	}

}