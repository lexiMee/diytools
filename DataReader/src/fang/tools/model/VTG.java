package fang.tools.model;

import fang.tools.util.NMEAException;

/*
 * VTG Track Made Good and Ground Speed 
 * 地面速度信息
 * 1 2 3 4 5 6 7 8 9 | | | | | | | | |
 * $--VTG,x.x,T,x.x,M,x.x,N,x.x,K*hh 
 * 1) Track Degrees  : 以真北为参考基准的地面航向（000~359度，前面的0也将被传输）
 * 2) T = True 
 * 3) Track Degrees  : 以磁北为参考基准的地面航向（000~359度，前面的0也将被传输）
 * 4) M = Magnetic 
 * 5) Speed Knots    : 地面速率（000.0~999.9节，前面的0也将被传输）
 * 6) N = Knots 
 * 7) Speed Kilometers Per Hour  : 地面速率（0000.0~1851.8公里/小时，前面的0也将被传输）
 * 8) K = Kilometres Per Hour 
 * 9) 模式指示（仅NMEA0183 3.00版本输出，A=自主定位，D=差分，E=估算，N=数据无效） 
 * 10)Checksum
 * 
 */

/**
 * VTG Track Made Good and Ground Speed
 * 
 */
public class VTG implements NMEASentence {
	public static final String SENTENCE_TYPE = "VTG";

	private ParseSentence parsedSentence;

	private double trackDegreeN;
	private double trackDegreeM;
	private double speedKnots;
	private double speedKPH;
	private char mode;

	public VTG(ParseSentence paramSentence) throws NMEAException {
		this.parsedSentence = paramSentence;

		checkSentenceType();
		checkSum();
		parseTrackDegreeN();
		parseTrackDegreeM();
		parseSpeedKnots();
		parseSpeedKPH();
		parseMode();
	}

	private void checkSentenceType() throws NMEAException {
		if (!(parsedSentence.getSentenceType().equals(SENTENCE_TYPE))) {
			throw new NMEAException("NMEA0183 - VTG: Expected VTG mnemonic");
		}
	}

	private void checkSum() throws NMEAException {
		if (parsedSentence.isChecksumBad()) {
			throw new NMEAException("NMEA0183 - VTG: Bad checksum");
		}
	}

	private void parseTrackDegreeN() throws NMEAException {
		if (parsedSentence.isSet(1)) {
			this.trackDegreeN = parsedSentence.getDouble(1);
		} else {
			// hasValue = false;
		}
	}

	private void parseTrackDegreeM() throws NMEAException {
		if (parsedSentence.isSet(3)) {
			this.trackDegreeM = parsedSentence.getDouble(3);
		} else {
			// hasValue = false;
		}
	}

	private void parseSpeedKnots() throws NMEAException {
		if (parsedSentence.isSet(5)) {
			this.speedKnots = parsedSentence.getDouble(5);
		} else {
			// hasValue = false;
		}
	}


	private void parseSpeedKPH() throws NMEAException {
		if (parsedSentence.isSet(7)) {
			this.speedKPH = parsedSentence.getDouble(7);
		} else {
			// hasValue = false;
		}
	}

	private void parseMode() throws NMEAException {
		if (parsedSentence.isSet(9)) {
			this.mode = parsedSentence.getChar(9);
		} else {
			// hasValue = false;
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

	public void print() {
		System.out.println(SENTENCE_TYPE + ":");
		System.out.println("\tTrack Degrees(N): " + this.trackDegreeN );
		System.out.println("\tTrack Degrees(M): " + this.trackDegreeM );
		System.out.println("\tSpeed Knots: " + this.speedKnots );
		System.out.println("\tSpeed KmPH: " + this.speedKPH );

		String str = "";
		switch (this.mode) {
		case 'A':
			str = "Based upon a GPS fix ";
			break;
		case 'D':
			str = "Based upon a differential GPS fix ";
			break;
		case 'E':
			str = "Based upon an estimation";
			break;
		case 'N':
			str = "Invalide";
			break;
		default:
			str = "N/A";
		}
		
		System.out.println("\tMode: " + str);
		
	}
}
