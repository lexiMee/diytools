package fang.tools.model;

import java.util.ArrayList;
import java.util.HashMap;

import fang.tools.util.NMEAException;

/*
 *
 GSV Satellites in view
 1 2 3 4 5 6 7 n
 | | | | | | | |
 $--GSV,x,x,x,x,x,x,x,...*hh
 1) total number of messages, 总的GSV语句电文数
 2) message number, 当前GSV语句号
 3) satellites in view, 可视卫星总数
 4) satellite number, 卫星号
 5) elevation in degrees, 仰角(00～90度)
 6) azimuth in degrees to true, 方位角(000～359度)
 7) SNR in dB, 信噪比(00～99dB)
 more satellite infos like 4)-7)
 n) Checksum

 注：每条语句最多包括四颗卫星的信息，每颗卫星的信息有四个数据项，即：　
 (4)－卫星号，(5)－仰角，(6)－方位角，(7)－信噪比。

 */

/**
 * GSV Satellites in view
 * 
 */
public class GSV implements NMEASentence {

	public static final String SENTENCE_TYPE = "GSV";

	private ParseSentence parsedSentence;
	private boolean bValid = true;
	private boolean hasValue = true;

	// one message contains 4 fields
	public static final int FIELDS_NUM = 4;
	public static final int SATELLITES_NUM = 4;

	private int totalMsgNum = 0;
	private int curMsgNum = 0;
	private int satNumInView = 0;

	private ArrayList<Satellite> satInfoArray = new ArrayList();
	private HashMap<Integer,Satellite> staHashMap = new HashMap();

	public GSV(ParseSentence paramSentence) throws NMEAException {
		this.parsedSentence = paramSentence;

		checkSentenceType();
		if (checkSum()) {
			// total number of messages
			parseTotalMsgNum();
			parseCurMsgNum();
			parseSatNumInView();

			if (this.satNumInView > 0) {
				parseOneSatInfo();
			}
		} else {
			bValid = false;
		}
	}

	private void checkSentenceType() throws NMEAException {
		if (!(parsedSentence.getSentenceType().equals(SENTENCE_TYPE))) {
			throw new NMEAException("NMEA0183 - GSV: Expected GSV mnemonic");
		}
	}

	private boolean checkSum() throws NMEAException {
		if (parsedSentence.isChecksumBad()) {
			return false;
			//throw new NMEAException("NMEA0183 - GSV: Bad checksum");
		}
		else {
			return true;
		}
	}

	private void parseTotalMsgNum() throws NMEAException {
		if (parsedSentence.isSet(1)) {
			this.totalMsgNum = parsedSentence.getInteger(1);
		} else {
			// hasValue = false;
		}
	}

	private void parseCurMsgNum() throws NMEAException {
		if (parsedSentence.isSet(2)) {
			this.curMsgNum = parsedSentence.getInteger(2);
		} else {
			// hasValue = false;
		}
	}

	private void parseSatNumInView() throws NMEAException {
		if (parsedSentence.isSet(3)) {
			this.satNumInView = parsedSentence.getInteger(3);
		} else {
			// hasValue = false;
		}
	}

	private void parseOneSatInfo() throws NMEAException {

		int satNum, elevationDegree = 0, azimuthDegree = 0;
		Double snr;

		for (int i = 0; i < SATELLITES_NUM; i++) {

			if (parsedSentence.isSet(4 + i * FIELDS_NUM)) {
				satNum = parsedSentence.getInteger(4 + i * FIELDS_NUM);
			} else {
				satNum = 0;
			}

			if (parsedSentence.isSet(5 + i * FIELDS_NUM))
				elevationDegree = parsedSentence.getInteger(5 + i * FIELDS_NUM);

			if (parsedSentence.isSet(6 + i * FIELDS_NUM))
				azimuthDegree = parsedSentence.getInteger(6 + i * FIELDS_NUM);

			if (parsedSentence.isSet(7 + i * FIELDS_NUM)) {
				snr = parsedSentence.getDouble(7 + i * FIELDS_NUM);
			} else {
				snr = 0.0;
			}

			this.satInfoArray.add(new Satellite(satNum, elevationDegree,
					azimuthDegree, snr));
			staHashMap.put(new Integer(satNum), new Satellite(satNum, elevationDegree,
					azimuthDegree, snr));
		}
	}


	public String getSentenceType() {
		// TODO Auto-generated method stub
		return null;
	}


	public void doWork() {
		//print();
	}


	public boolean hasValue() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isValideSentence() {
		return bValid;
	}
	
	public int getTotalMsgNum() {
		return this.totalMsgNum;
	}

	public int getCurMsgNum() {
		return this.curMsgNum;
	}

	public int getSatInView() {
		return this.satNumInView;
	}

	public String getPlainMessage() {
		
		String str = SENTENCE_TYPE + ":";
		
		// check the sentence is valid or not first
		if(isValideSentence()) {
			str += "\tTotal number of messages: " + this.totalMsgNum;
			str += "\tCurrent message number: " + this.curMsgNum;
			str += "\tsatellites in view: " + this.satNumInView;
			
			for (int i = 0; i < SATELLITES_NUM; i++) {
				Satellite satellite = satInfoArray.get(i);
				if (satellite.getSatNum() > 0) {
					str += "\tSatellite number: " + satellite.getSatNum() + "\t";
					str += "Elevation degrees: " + satellite.getElevationDegree()
							+ "\t";
					str += "Azimuth degress: " + satellite.getAzimuthDegree()
							+ "\t";
					str += "SNR: " + satellite.getSNR() + "dB";
				}
			}
		}
		else {
			str = "Invalide sentence";
		}
		
		return str;
	}
	
	public HashMap<Integer, Satellite> getSateHashMap()
	{
		return staHashMap;
	}
	
	public void print() {
		System.out.println(getPlainMessage());
	}
}
