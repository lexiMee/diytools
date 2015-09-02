package fang.tools.model;

import java.util.*;

import fang.tools.util.NMEAException;

/*
 * GSA : GPS DOP and active satellites
 *  1 2 3 14 15 16 17 18 | | | | | | | |
 * $--GSA,a,a,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x.x,x.x,x.x*hh 
 * 
 * 1) Selection mode M = 手动， A = 自动
 * 2) Mode , 3D fix - values include:  1 = no fix
                                       2 = 2D fix
                                       3 = 3D fix
                              定位型式 1 = 未定位， 2 = 二维定位， 3 = 三维定位
 * 3) ID of 1st satellite used for fix , 
 *    PRN 数字：01 至 32 表天空使用中的卫星编号，最多可接收12颗卫星信息
 * 4) ID of 2nd satellite used for fix
 * ... 
 * 14) ID of 12th satellite used for fix 
 * 15) PDOP in meters ,PDOP位置精度因子（0.5~99.9）
 * 16) HDOP in meters ,HDOP水平精度因子（0.5~99.9）
 * 17) VDOP in meters ,VDOP垂直精度因子（0.5~99.9）
 * 18) Checksum
 * 
 * 
 */

/**
 *  GSA GPS DOP and active satellites
 */
public class GSA implements NMEASentence {
	public static final String SENTENCE_TYPE = "GSA";

	public static final int FIX_SATELLITE_NUM = 12;

	private ParseSentence parsedSentence;

	private char selectionMode;
	private byte mode;
	private List<Integer> listSatellites = new ArrayList<Integer>();
	private double pdop;
	private double hdop;
	private double vdop;
	
	
	private boolean hasValue = true;

	public GSA(ParseSentence paramSentence) throws NMEAException {

		this.parsedSentence = paramSentence;

		checkSentenceType();
		checkSum();
		parseSelectionMode();
		parseMode();
		parseFixSatellites();
		parsePDOP();
		parseHDOP();
		parseVDOP();

	}

	private void checkSentenceType() throws NMEAException {
		if (!(parsedSentence.getSentenceType().equals(SENTENCE_TYPE))) {
			throw new NMEAException("NMEA0183 - GSA: Expected GGA mnemonic", 0);
		}
	}

	private void checkSum() throws NMEAException {
		if (parsedSentence.isChecksumBad()) {
			throw new NMEAException("NMEA0183 - GSV: Bad checksum");
		}
	}

	private void parseSelectionMode() throws NMEAException {
		if (parsedSentence.isSet(1)) {
			this.selectionMode = parsedSentence.getChar(1);
		} else {
			hasValue = false;
		}
	}

	private void parseMode() throws NMEAException {
		if (parsedSentence.isSet(2)) {
			this.mode = parsedSentence.getByte(2);
		} else {
			hasValue = false;
		}
	}

	private void parseFixSatellites() throws NMEAException {

		for (int i = 0; i < FIX_SATELLITE_NUM; i++) {
			if (parsedSentence.isSet(3 + i)) {
				int id = parsedSentence.getInteger(3 + i);
				listSatellites.add(id);
			} else {
				listSatellites.add(0);
				hasValue = false;
			}
		}
	}

	private void parsePDOP() throws NMEAException {
		if (parsedSentence.isSet(15)) {
			this.pdop = parsedSentence.getDouble(15);
		} else {
			hasValue = false;
		}
	}
	
	private void parseHDOP() throws NMEAException {
		if (parsedSentence.isSet(16)) {
			this.hdop = parsedSentence.getDouble(16);
		} else {
			hasValue = false;
		}
	}

	private void parseVDOP() throws NMEAException {
		if (parsedSentence.isSet(17)) {
			this.vdop = parsedSentence.getDouble(17);
		} else {
			hasValue = false;
		}
	}


	public void doWork() {
		print();

	}


	public String getSentenceType() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean hasValue() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getSelectionMode() {
		return selectionMode;
	}

	public String getSelectionModeString() {
		String str = "";
		switch(this.selectionMode) {
		case 'A':
			str = "Automatic";
			break;
		case 'M':
			str = "Manual";
			break;
		default:
			str = "Unknown";
		}
		return str;
	}

	public int getMode() {
		return mode;
	}

	public String getModeString() {
		String str = "";
		switch(this.mode) {
		case 1:
			str = "No fix";
			break;
		case 2:
			str = "2D fix";
			break;
		case 3:
			str = "3D fix";
			break;
		default:
			str = "Unknown";
		}
		
		return str;
	}

	public List getListSatellites() {
		return listSatellites;
	}

	public double getPdop() {
		return pdop;
	}

	public double getHdop() {
		return hdop;
	}

	public double getVdop() {
		return vdop;
	}

	public void print() {
		System.out.println(SENTENCE_TYPE + ":");
		System.out.println("\tSelection mode: " + getSelectionModeString());
		System.out.println("\tMode: " + getModeString());
		
		String str = "";
		for(int i=0; i<FIX_SATELLITE_NUM; i++){
			int n = listSatellites.get(i);
			if(n > 0)
				str += "S[" + (i+1) + "]=" + n + " ";  
		}
		System.out.println("\tRPN: " + str);
		System.out.println("\tPDOP: " + this.pdop + " m");
		System.out.println("\tHDOP: " + this.hdop + " m");
		System.out.println("\tVDOP: " + this.vdop + " m");
		
	}
}
