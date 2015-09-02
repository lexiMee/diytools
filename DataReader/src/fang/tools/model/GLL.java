package fang.tools.model;

import java.util.Date;

import fang.tools.util.NMEAException;



/**
	GLL Geographic Position ¨C Latitude/Longitude
	1 2 3 4 5 6 7
	| | | | | | |
	$--GLL,llll.ll,a,yyyyy.yy,a,hhmmss.ss,A*hh
	1) Latitude
	2) N or S (North or South)
	3) Longitude
	4) E or W (East or West)
	5) Time (UTC)
	6) Status A - Data Valid, V - Data Invalid
	7) Checksum

 * 
 * @author Jeff
 *
 */
public class GLL implements NMEASentence {
	public static final String SENTENCE_TYPE = "GLL";
	private Date UTCTime;
	private GeoPos pos;
	private boolean dataValid = false;

	private ParseSentence parsedSentence;

	public GLL(ParseSentence paramSentence) throws NMEAException {
		
		this.parsedSentence = paramSentence;

		checkSentence();

		checkSum();
		parsePosition();

		parseTime();

		parseStatus();
	}

	private void checkSentence() throws NMEAException {
		if (!(parsedSentence.getSentenceType().equals("GLL"))) {
			throw new NMEAException("NMEA0183 - GLL: Expected GLL mnemonic", 0);
		}
	}

	private void checkSum() throws NMEAException {
		if (parsedSentence.isChecksumBad()) {
			throw new NMEAException("NMEA0183 - GLL: Bad checksum", 15);
		}
	}

	private void parsePosition() throws NMEAException {
		double d1 = parsedSentence.getDouble(1);
		if (!(parsedSentence.isNorth(2))) {
			d1 = -d1;
		}
		double d2 = parsedSentence.getDouble(3);
		if (!(parsedSentence.isEast(4))) {
			d2 = -d2;
		}
		this.pos = new GeoPos(Degree.parseDDMM_MM(d1), Degree.parseDDMM_MM(d2));
	}

	private void parseTime() throws NMEAException {
		this.UTCTime = parsedSentence.getUTCTime(5);
	}

	private void parseStatus() throws NMEAException {
		this.dataValid = parsedSentence.isTrue(6);
	}

	public Date getDateofFix() {
		return this.UTCTime;
	}

	public boolean isDataValid() {
		return this.dataValid;
	}

	public GeoPos getLocationOfFix() {
		return this.pos;
	}

	public String inPlainEnglish() {
		String str = "At ";
		str = str + this.UTCTime.toString();
		str = str + " UTC, you were at ";
		str = str + " !!!TODO - Add Position information!!! ";

		str = str + ", ";
		if (this.dataValid) {
			str = str + " Data is valid";
		} else {
			str = str + " Data is valid";
		}
		return str;
	}

	public String getSentenceType() {
		return SENTENCE_TYPE;
	}

	public int getSentenceID() {
		return 1007;
	}


	public void doWork() {
		// TODO Auto-generated method stub
		System.out.println("GLL doWork");
	}


	public boolean hasValue() {
		// TODO Auto-generated method stub
		return false;
	}
}
