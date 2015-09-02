package fang.tools.model;

import java.util.Date;


import fang.tools.util.NMEAException;

/*
 * 
	RMC Recommended Minimum Navigation Information
	12
	1 2 3 4 5 6 7 8 9 10 11|
	| | | | | | | | | | | |
	$--RMC,hhmmss.ss,A,llll.ll,a,yyyyy.yy,a,x.x,x.x,xxxx,x.x,a*hh
	1) Time (UTC), UTCʱ�䣬hhmmss(ʱ����)��ʽ
	2) Status, V = Navigation receiver warning, ��λ״̬��A=��Ч��λ��V=��Ч��λ
	3) Latitude, γ��ddmm.mmmm(�ȷ�)��ʽ
	4) N or S
	5) Longitude, ����dddmm.mmmm(�ȷ�)��ʽ
	6) E or W
	7) Speed over ground, knots, ��������(000.0~999.9��
	8) Track made good, degrees true, ���溽��(000.0~359.9�ȣ����汱Ϊ�ο���׼
	9) Date, ddmmyy, UTC���ڣ�ddmmyy(������)��ʽ
	10) Magnetic Variation, degrees, ��ƫ��(000.0~180.0��
	11) E or W, ��ƫ�Ƿ���E(��)��W(��)
	12) Checksum
	
 * GPRMC�����Ƽ���λ��Ϣ(GPRMC)
����<xx> ģʽָʾ(��NMEA0183 3.00�汾�����A=������λ��D=��֣�E=���㣬N=������Ч
 */

/**
 *  RMC: Recommended Minimum Navigation Information
 *  
 */
public class RMC implements NMEASentence {

	public static final String SENTENCE_TYPE = "RMC";
	private boolean hasValue = true;

	private ParseSentence parsedSentence;
	
	private Date UTCTime;
	private String status;
	private GeoPos pos;
	private double speed;
	private double track;
	private double magneticVar;
	
	public RMC(ParseSentence paramSentence) throws NMEAException {

		this.parsedSentence = paramSentence;

		checkSentenceType();
		checkSum();
		parseTime();
		parseStatus();
		parsePosition();
		parseSpeed();
		parseTrack();
		parseDate();
		parseMagneticVariation();
		
	}
	
	private void checkSentenceType() throws NMEAException {
		if (!(parsedSentence.getSentenceType().equals(SENTENCE_TYPE))) {
			throw new NMEAException("NMEA0183 - RMC: Expected RMC mnemonic", 0);
		}
	}

	private void checkSum() throws NMEAException {
		if (parsedSentence.isChecksumBad()) {
			throw new NMEAException("NMEA0183 - RMC: Bad checksum: ", 0);
		}
	}

	private void parseTime() throws NMEAException {
		this.UTCTime = parsedSentence.getUTCTime(1);
	}

	private void parseStatus() throws NMEAException {
		if (parsedSentence.isSet(2) && parsedSentence.isSet(4)) {
			this.status = parsedSentence.getString(2);
		}
	}
		
	private void parsePosition() throws NMEAException {
		// 3) Latitude  4) N or S (North or South)
		// 5) Longitude 6) E or W (East or West)
		if (parsedSentence.isSet(3) && parsedSentence.isSet(5)) {
			double d1 = parsedSentence.getDouble(3);
			if (!(parsedSentence.isNorth(4))) {
				d1 = -d1;
			}

			double d2 = parsedSentence.getDouble(5);
			if (!(parsedSentence.isEast(6))) {
				d2 = -d2;
			}
			this.pos = new GeoPos(Degree.parseDDMM_MM(d1), Degree
					.parseDDMM_MM(d2));
		} else {
			hasValue = false;
		}
	}

	private void parseSpeed() throws NMEAException {
		if (parsedSentence.isSet(7)) {
			this.speed = parsedSentence.getDouble(7);
		}
	}
	
	private void parseTrack() throws NMEAException {
		if (parsedSentence.isSet(8)) {
			this.speed = parsedSentence.getDouble(8);
		}
	}
	
	private void parseDate() throws NMEAException {
		//this.UTCTime = parsedSentence.getUTCTime(9);
	}

	
	private void parseMagneticVariation() throws NMEAException {
		if (parsedSentence.isSet(10)) {
			this.magneticVar = parsedSentence.getDouble(10);
			if (!(parsedSentence.isEast(6))) {
				this.magneticVar = - this.magneticVar;
			}
		}
	}


	public void doWork() {
		//System.out.println("RMC::doWork");
		print();
		
	}


	public String getSentenceType() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean hasValue() {
		return hasValue;
	}

	public Date getUTCTime() {
		return UTCTime;
	}

	public String getStatus() {
		return status;
	}

	public GeoPos getPos() {
		return pos;
	}

	public double getSpeed() {
		return speed;
	}

	public double getTrack() {
		return track;
	}

	public double getMagneticVar() {
		return magneticVar;
	}

	public void print() {
		System.out.println(SENTENCE_TYPE + ":");
		System.out.println("\tTime:" + getUTCTime().toString());
		System.out.println("\tPosition: " + pos.toString());
		System.out.println("\tSpeed:" + getSpeed());
		System.out.println("\tTrack:" + getTrack());
		System.out.println("\tMagnetic Variation:" + getMagneticVar());
	}
}
