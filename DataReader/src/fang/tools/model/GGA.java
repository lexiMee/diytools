package fang.tools.model;

import java.util.Date;

import fang.tools.util.NMEAException;


/*
 * GGA Global Positioning System Fix Data. Time, Position and fix related data
 * for a GPS receiver
 * 
 * 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 | | | | | | | | | | | | | | |
 * $--GGA,hhmmss.ss,llll.ll,a,yyyyy.yy,a,x,xx,x.x,x.x,M,x.x,M,x.x,xxxx*hh 
 * 1) Time (UTC), UTCʱ�䣬��ʽΪhhmmss.sss�� 
 * 2) Latitude, γ�ȣ���ʽΪddmm.mmmm(��һλ����Ҳ������)�� 
 * 3) N or S (North or South), γ�Ȱ���N��S(��γ����γ) 
 * 4) Longitude, ���ȣ���ʽΪdddmm.mmmm(��һλ��Ҳ������) 
 * 5) E or W (East or West), ���Ȱ���E��W(����������) 
 * 6) GPS Quality Indicator, 0 - fix not available, 1 - GPS fix, 2 - Differential GPS fix 
 * 7) Number of satellites in view, 00 - 12, ʹ��������������00��12(��һ����Ҳ������) 
 * 8) Horizontal Dilution of precision, ˮƽ��ȷ�ȣ�0.5��99.9 
 * 9) Antenna Altitude above/below mean-sea-level (geoid), �����뺣ƽ��ĸ߶ȣ�-9999.9��9999.9�� 
 * 10) Units of antenna altitude, meters 
 * 11) Geoidal separation, the difference between the WGS-84 earth, 
 *     ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level
 *     below ellipsoid ���ˮ׼��߶ȣ�-9999.9��9999.9�� 
 * 12) Units of geoidal separation,meters 
 * 13) Age of differential GPS data, time in seconds since last SC104
 *     type 1 or 9 update, null field when DGPS is not used, ���GPS��������(RTCM
 *     SC-104)���������RTCM���͵������� 
 * 14) Differential reference station ID, 0000-1023,
 *     ��ֲο���վ��ţ���0000��1023(��λ0Ҳ������)
 * 15) Checksum $GPGGA
 * 
 * ������17���ֶΣ�����ʶͷ������ʱ�䣬γ�ȣ�γ�Ȱ��򣬾��ȣ����Ȱ���
 * ��λ����ָʾ��ʹ������������ˮƽ��ȷ�ȣ����θ߶� ���߶ȵ�λ�����ˮ׼��߶ȣ��߶ȵ�λ
 * �����GPS�������ޣ���ֲο���վ��ţ�У��ͽ������(�ûس���<CR>�ͻ��з�<LF>)��
 * �ֱ���14�����Ž��зָ���
 */

/**
 * GGA: Global Positioning System Fix Data. Time, Position and fix related data
 * for a GPS receiver
 */
public class GGA implements NMEASentence {

	public static final String SENTENCE_TYPE = "GGA";
	private boolean hasValue = true;
	private Date UTCTime;
	private GeoPos pos = null;

	private byte GPSQuality = 0;
	public static final byte NO_FIX = 0;
	public static final byte GPS_FIX = 1;
	public static final byte DGPS_FIX = 2;
	private byte numberOfSatellitesInUse = 0;

	private double horizontalDilutionOfPrecision = 0.0D;
	private double antennaAltitudeMeters = 0.0D;
	private double geoidalSeparationMeters = 0.0D;
	private double ageOfDifferentialGPSDataSeconds = 0.0D;
	private int differentialReferenceStationID = 0;

	private ParseSentence parsedSentence;

	public GGA(ParseSentence paramSentence) throws NMEAException {

		this.parsedSentence = paramSentence;

		checkSentenceType();
		checkSum();
		parseTime();
		parsePosition();
		parseGPSQuality();
		parseSatelliteNum();
		parseHPrecision();
		parseAntAltitude();
		parseEarthSeaDifference();
		parseGPSDataAge();
		parseDiffStationID();
	}

	private void checkSentenceType() throws NMEAException {
		if (!(parsedSentence.getSentenceType().equals(SENTENCE_TYPE))) {
			throw new NMEAException("NMEA0183 - GGA: Expected GGA mnemonic", 0);
		}
	}

	private void checkSum() throws NMEAException {
		if (parsedSentence.isChecksumBad()) {
			throw new NMEAException("NMEA0183 - GGA: Bad checksum: ", 0);
		}
	}

	private void parseTime() throws NMEAException {
		this.UTCTime = parsedSentence.getUTCTime(1);
	}

	private void parsePosition() throws NMEAException {
		// 2) Latitude 3) N or S (North or South)
		// 4) Longitude 5) E or W (East or West)
		if (parsedSentence.isSet(2) && parsedSentence.isSet(4)) {
			double d1 = parsedSentence.getDouble(2);
			if (!(parsedSentence.isNorth(3))) {
				d1 = -d1;
			}

			double d2 = parsedSentence.getDouble(4);
			if (!(parsedSentence.isEast(5))) {
				d2 = -d2;
			}
			this.pos = new GeoPos(Degree.parseDDMM_MM(d1), Degree
					.parseDDMM_MM(d2));
		} else {
			hasValue = false;
		}
	}

	private void parseGPSQuality() throws NMEAException {
		this.GPSQuality = parsedSentence.getByte(6);
	}

	private void parseSatelliteNum() throws NMEAException {
		this.numberOfSatellitesInUse = parsedSentence.getByte(7);
	}

	private void parseHPrecision() throws NMEAException {
		if (parsedSentence.isSet(8)) {
			this.horizontalDilutionOfPrecision = parsedSentence.getDouble(8);
		} else {
			hasValue = false;
		}
	}

	private void parseAntAltitude() throws NMEAException {
		if (parsedSentence.isSet(9)) {
			this.antennaAltitudeMeters = parsedSentence.getDouble(9);
		} else {
			hasValue = false;
		}

		if (!(parsedSentence.getString(10).equals("M"))) {
			throw new NMEAException("NMEA0183 - GGA: Expected Unit of meters.",
					10);
		}
	}

	private void parseEarthSeaDifference() throws NMEAException {
		if (parsedSentence.isSet(11)) {
			this.geoidalSeparationMeters = parsedSentence.getDouble(11);
			if (!(parsedSentence.getString(12).equals("M"))) {
				throw new NMEAException(
						"NMEA0183 - GGA: Expected Unit of meters.", 12);
			}
		} else {
			hasValue = false;
		}
	}

	private void parseGPSDataAge() throws NMEAException {
		if (parsedSentence.isSet(13)) {
			this.ageOfDifferentialGPSDataSeconds = parsedSentence.getDouble(13);
		} else {
			hasValue = false;
		}
	}

	private void parseDiffStationID() throws NMEAException {
		if (parsedSentence.isSet(14)) {
			this.differentialReferenceStationID = parsedSentence.getInteger(14);
		} else {
			hasValue = false;
		}
	}

	public Date getDateofFix() {
		return this.UTCTime;
	}

	public GeoPos getLocationOfFix() {
		return this.pos;
	}

	public byte getQualityOfFix() {
		return this.GPSQuality;
	}

	public byte getNumberOfSatellitesInUse() {
		return this.numberOfSatellitesInUse;
	}

	public double getHorizontalDilutionOfPrecision() {
		return this.horizontalDilutionOfPrecision;
	}

	public double getAntennaAltitudeMeters() {
		return this.antennaAltitudeMeters;
	}

	public double getGeoidalSeparationMeters() {
		return this.geoidalSeparationMeters;
	}

	public double getAgeOfDifferentialGPSDataSeconds() {
		return this.ageOfDifferentialGPSDataSeconds;
	}

	public int getDifferentialReferenceStationID() {
		return this.differentialReferenceStationID;
	}

	public String inPlainEnglish() {
		String str = "GGA: you were at:\n";
		str = str + "\t" + this.UTCTime.toString() + "\n";

		if (pos != null) {
			str = str + "\t" + this.pos.getLatInDegMinDec() + "\n" + "\t"
					+ this.pos.getLngInDegMinDec() + "\n";
		} else {
			str = str + "\tNo position info\n";
		}
		str = str + "\t";
		switch (this.GPSQuality) {
		case 1:
			str = str + "Based upon a GPS fix ";
			break;
		case 2:
			str = str + "Based upon a differential GPS fix ";
			break;
		default:
			str = str + "A GPS fix was not available ";
		}

		str = str + "\n\t" + this.numberOfSatellitesInUse
				+ " satellites are in use.";
		return str;
	}

	public String getSentenceType() {
		return SENTENCE_TYPE;
	}

	public int getSentenceID() {
		return 1006;
	}

	/**
	 * 
	 */
	public void doWork() {
		// TODO Auto-generated method stub
		print();
	}

	public boolean hasValue() {
		return hasValue;
	}

	public void print() {
		String str = inPlainEnglish();
		System.out.println(str);
	}
}
