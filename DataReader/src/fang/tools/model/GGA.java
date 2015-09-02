package fang.tools.model;

import java.util.Date;

import fang.tools.util.NMEAException;


/*
 * GGA Global Positioning System Fix Data. Time, Position and fix related data
 * for a GPS receiver
 * 
 * 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 | | | | | | | | | | | | | | |
 * $--GGA,hhmmss.ss,llll.ll,a,yyyyy.yy,a,x,xx,x.x,x.x,M,x.x,M,x.x,xxxx*hh 
 * 1) Time (UTC), UTC时间，格式为hhmmss.sss； 
 * 2) Latitude, 纬度，格式为ddmm.mmmm(第一位是零也将传送)； 
 * 3) N or S (North or South), 纬度半球，N或S(北纬或南纬) 
 * 4) Longitude, 经度，格式为dddmm.mmmm(第一位零也将传送) 
 * 5) E or W (East or West), 经度半球，E或W(东经或西经) 
 * 6) GPS Quality Indicator, 0 - fix not available, 1 - GPS fix, 2 - Differential GPS fix 
 * 7) Number of satellites in view, 00 - 12, 使用卫星数量，从00到12(第一个零也将传送) 
 * 8) Horizontal Dilution of precision, 水平精确度，0.5到99.9 
 * 9) Antenna Altitude above/below mean-sea-level (geoid), 天线离海平面的高度，-9999.9到9999.9米 
 * 10) Units of antenna altitude, meters 
 * 11) Geoidal separation, the difference between the WGS-84 earth, 
 *     ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level
 *     below ellipsoid 大地水准面高度，-9999.9到9999.9米 
 * 12) Units of geoidal separation,meters 
 * 13) Age of differential GPS data, time in seconds since last SC104
 *     type 1 or 9 update, null field when DGPS is not used, 差分GPS数据期限(RTCM
 *     SC-104)，最后设立RTCM传送的秒数量 
 * 14) Differential reference station ID, 0000-1023,
 *     差分参考基站标号，从0000到1023(首位0也将传送)
 * 15) Checksum $GPGGA
 * 
 * 语句包括17个字段：语句标识头，世界时间，纬度，纬度半球，经度，经度半球，
 * 定位质量指示，使用卫星数量，水平精确度，海拔高度 ，高度单位，大地水准面高度，高度单位
 * ，差分GPS数据期限，差分参考基站标号，校验和结束标记(用回车符<CR>和换行符<LF>)，
 * 分别用14个逗号进行分隔。
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
