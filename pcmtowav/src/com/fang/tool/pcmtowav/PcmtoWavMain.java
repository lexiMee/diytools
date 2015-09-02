package com.fang.tool.pcmtowav;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

public class PcmtoWavMain extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JFileChooser mFileChooser;
	private JLabel resultLabel;
	private File mFile;
	private int mSamplingRate = 8000;
	private short mChannel = 1;
	private short mBitWidth = 8;
	private final int miSamplingRates[] = new int[6];
	{
		int i = 0;
		miSamplingRates[i++] = 8000;
		miSamplingRates[i++] = 16000;
		miSamplingRates[i++] = 44100;
		miSamplingRates[i++] = 48000;
		miSamplingRates[i++] = 96000;
		miSamplingRates[i++] = 192000;
	}

	private final String mSamplingRates[] = new String[6];
	{
		int i = 0;
		mSamplingRates[i++] = "8k";
		mSamplingRates[i++] = "16k";
		mSamplingRates[i++] = "44.1k";
		mSamplingRates[i++] = "48k";
		mSamplingRates[i++] = "96k";
		mSamplingRates[i++] = "192k";

	}

	private final String mChannels[] = new String[2];
	{
		int i = 0;
		mChannels[i++] = "1";
		mChannels[i++] = "2";
	}

	private final short miBitWidths[] = new short[4];
	{
		int i = 0;
		miBitWidths[i++] = 8;
		miBitWidths[i++] = 16;
		miBitWidths[i++] = 24;
		miBitWidths[i++] = 32;
	}

	private final String mBitWidths[] = new String[4];
	{
		int i = 0;
		mBitWidths[i++] = "8bit";
		mBitWidths[i++] = "16bit";
		mBitWidths[i++] = "24bit";
		mBitWidths[i++] = "32bit";
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PcmtoWavMain frame = new PcmtoWavMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void initData() {
		mFile = new File("D:\\");
		mFileChooser = new JFileChooser();
		mFileChooser.setCurrentDirectory(mFile.getParentFile());
		mFileChooser.setAcceptAllFileFilterUsed(false);
		FileFilterDemo filter = new FileFilterDemo(".pcm", "pcm文件(.pcm)");
		mFileChooser.setFileFilter(filter);
		/*
		 * //在添加了过滤器之后，假如还想添加其他的过滤器，可如下进行 FileFilterDemo fr=new
		 * FileFilterDemo(); fr.addExtension(new
		 * String[]{".doc",".docx"},"微软WORD文档(.doc；.docx)");
		 * //注意这里是addChoosableFileFilter()方法，而不是setF...开头
		 * chooser.addChoosableFileFilter(fr);
		 */
	}

	/**
	 * Create the frame.
	 */
	public PcmtoWavMain() {
		setTitle("pcm2wav");
		initData();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JComboBox comboBox = new JComboBox(mSamplingRates);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mSamplingRate = miSamplingRates[comboBox.getSelectedIndex()];
			}
		});
		comboBox.setBounds(112, 93, 65, 21);
		contentPane.add(comboBox);

		JLabel lblSamplerate = new JLabel("SampleRate");
		lblSamplerate.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
		lblSamplerate.setBounds(28, 95, 74, 15);
		contentPane.add(lblSamplerate);

		JLabel lblChannel = new JLabel("Channel");
		lblChannel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
		lblChannel.setBounds(28, 150, 54, 15);
		contentPane.add(lblChannel);

		final JComboBox comboBox_1 = new JComboBox(mChannels);
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mChannel = (short) (comboBox_1.getSelectedIndex() == 0 ? 1 : 2);
			}
		});
		comboBox_1.setBounds(112, 149, 65, 21);
		contentPane.add(comboBox_1);

		JLabel lblNewLabel = new JLabel("SamplerWidth");
		lblNewLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
		lblNewLabel.setBounds(28, 205, 80, 15);
		contentPane.add(lblNewLabel);

		final JComboBox comboBox_2 = new JComboBox(mBitWidths);
		comboBox_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mBitWidth = miBitWidths[comboBox_2.getSelectedIndex()];
			}
		});
		comboBox_2.setBounds(112, 205, 65, 21);
		contentPane.add(comboBox_2);

		JToggleButton tglbtnNewToggleButton = new JToggleButton("Convert");
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					convertAudioFiles(mFile.getAbsolutePath(),
							mFile.getAbsolutePath() + "_" + mSamplingRate + "_"
									+ mChannel + "_" + mBitWidth + ".wav");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		tglbtnNewToggleButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		tglbtnNewToggleButton.setBounds(228, 146, 135, 23);
		contentPane.add(tglbtnNewToggleButton);

		textField = new JTextField();
		textField.setBounds(68, 35, 253, 23);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("\u6253\u5F00\u6587\u4EF6");
		btnNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = 0;
				resultLabel.setText("");
				mFileChooser.setApproveButtonText("确定");
				mFileChooser.setDialogTitle("打开文件");
				mFileChooser
						.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				mFileChooser.setCurrentDirectory(mFile);
				result = mFileChooser.showOpenDialog(PcmtoWavMain.this);
				if (mFile != null)
					mFileChooser.setCurrentDirectory(mFile.getParentFile());
				if (result == JFileChooser.APPROVE_OPTION) {// 选择的是确定按钮
					mFile = mFileChooser.getSelectedFile();// 得到选择的文件
					textField.setText(mFile.getPath());
				} else if (result == JFileChooser.CANCEL_OPTION) {
					textField.setText("没有选择任何的文件!");
				} else {
					textField.setText("操作出现错误");
				}
			}
		});
		btnNewButton.setBounds(331, 35, 93, 23);
		contentPane.add(btnNewButton);

		JLabel lblFile = new JLabel("File:");
		lblFile.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
		lblFile.setBounds(28, 40, 54, 15);
		contentPane.add(lblFile);

		JLabel lblNewLabel_1 = new JLabel("State:");
		lblNewLabel_1.setBounds(228, 205, 54, 15);
		contentPane.add(lblNewLabel_1);

		resultLabel = new JLabel("");
		resultLabel.setBounds(292, 205, 132, 15);
		contentPane.add(resultLabel);
	}

	public class FileFilterDemo extends javax.swing.filechooser.FileFilter {
		private ArrayList<String> all_extension = null; // 存储加入的后缀
		private String remark = "对文件类型的描述";

		public FileFilterDemo() {
			all_extension = new ArrayList<String>();
		}

		public FileFilterDemo(String ext, String des) {
			this(); // 调用本类中的构造方法
			all_extension.add(ext);
			remark = des;
		}

		public FileFilterDemo(String[] ext, String des) {
			this();
			all_extension.addAll(Arrays.asList(ext)); // 把ext转换为Collection类型
			remark = des;
		}

		// 添加一个后缀名和一个描述
		public void addExtension(String ext) {
			all_extension.add(ext);
		}

		public void setDescription(String des) {
			remark = des;
		}

		// 添加一个数组后缀名
		public void addExtension(String[] ext) {
			all_extension.addAll(Arrays.asList(ext));
		}

		// 添加一个数组后缀名和一个描述
		public void addExtension(String[] ext, String des) {
			all_extension.addAll(Arrays.asList(ext));
			remark = des;
		}

		@Override
		public boolean accept(File f) {
			String file_name = f.getName().toLowerCase();
			if (f.isDirectory()) {
				return true;
			}
			// 以下方法显得有点繁琐，不用

			for (String ext : all_extension) {
				if (file_name.endsWith(ext)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String getDescription() {
			return remark;
		}
	}

	private void convertAudioFiles(String src, String target) throws Exception {
		// FIXME
		if (src == null || mFile.isDirectory()) {
			resultLabel.setText("Nothing");
			return;
		}

		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(target);

		// 计算长度
		byte[] buf = new byte[1024 * 4];
		int size = fis.read(buf);
		int PCMSize = 0;
		while (size != -1) {
			PCMSize += size;
			size = fis.read(buf);
		}
		fis.close();

		// 填入参数，比特率等等。这里用的是16位单声道 8000 hz
		WaveHeader header = new WaveHeader();
		// 长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
		header.FmtHdrLeth = 16;
		header.BitsPerSample = mBitWidth;
		header.Channels = mChannel;
		header.FormatTag = 0x0001;
		header.SamplesPerSec = mSamplingRate;
		header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
		header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
		
		if (header.BlockAlign == 6)
			PCMSize = (PCMSize * 3) >> 2;
		// 长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
		header.fileLength = PCMSize + (44 - 8);
		header.DataHdrLeth = PCMSize;

		byte[] h = header.getHeader();

		assert h.length == 44; // WAV标准，头部应该是44字节
		// write header
		fos.write(h, 0, h.length);
		// write data stream
		fis = new FileInputStream(src);
		size = fis.read(buf);
		
		if (header.BlockAlign == 6) {
			int j = 0;
			byte[] buf2 = new byte[1024 * 3];
			for (int i = 0; i < size; ++i) {
				if ((i % 4) == 3)
					continue;
				buf2[j++] = (byte)(buf[i] & 0xff);
			}
			while (size != -1) {
				fos.write(buf2, 0, j);
				size = fis.read(buf);
				j = 0;
				for (int i = 0; i < size; ++i) {
					if ((i % 4) == 3)
						continue;
					buf2[j++] = (byte)(buf[i] & 0xff);
				}
			}
		} else {
			while (size != -1) {
				fos.write(buf, 0, size);
				size = fis.read(buf);
			}
		}
		fis.close();
		fos.close();
		resultLabel.setText("convert ok!");
	}
}
