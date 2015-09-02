package fang.tools.model;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import fang.tools.util.NMEAException;

public class ParseSentence {

	private String sentence;
	private String[] fields;
	private int maxFields;
	private byte checksum;
	private boolean hasChecksum;

	public ParseSentence(String str) {
		this.sentence = str;

		sentence.trim();
		chopSentence();

		printFields();

	}

	private void printFields() {
		for (int i = 0; i < this.fields.length; ++i) {
			System.out.print(" [" + i + "]= " + this.fields[i]);
		}
		System.out.println();
	}

	public String getTalkerID() throws NMEAException {
		try {
			return this.fields[0].substring(1, 3);
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			throw new NMEAException("NMEA0183: not a valid start of sentence: "
					+ this.fields[0], 0);
		}
	}

	public String getSentenceType() throws NMEAException {
		try {
			if (this.fields[0].startsWith("$P")) {
				return this.fields[0].substring(1, this.fields[0].length());
			}

			return this.fields[0].substring(3, this.fields[0].length());
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			throw new NMEAException("NMEA0183: not a valid start of sentence: "
					+ this.fields[0], 0);
		}
	}

	public boolean isTrue(int paramInt) throws NMEAException {
		try {
			if (this.fields[paramInt].equals("A"))
				return true;
			if (this.fields[paramInt].equals("V"))
				return false;
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
		}
		throw new NMEAException("NMEA0183: index is not a boolean field",
				paramInt);
	}

	public boolean isNorth(int paramInt) throws NMEAException {
		try {
			if (this.fields[paramInt].equals("N"))
				return true;
			if (this.fields[paramInt].equals("S"))
				return false;
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
		}
		throw new NMEAException("NMEA0183: index is not a North/South field:",
				paramInt);
	}

	public boolean isEast(int paramInt) throws NMEAException {
		try {
			if (this.fields[paramInt].equals("E"))
				return true;
			if (this.fields[paramInt].equals("W"))
				return false;
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
		}
		throw new NMEAException("NMEA0183: index is not a East/West field",
				paramInt);
	}

	public Date getUTCTime(int paramInt) throws NMEAException {
		try {
			int i = Integer.parseInt(this.fields[paramInt].substring(0, 2));
			int j = Integer.parseInt(this.fields[paramInt].substring(2, 4));
			int k = Integer.parseInt(this.fields[paramInt].substring(4, 6));
			int l = 0;
			if (this.fields[paramInt].length() > 6) {
				float f = Float.valueOf(
						this.fields[paramInt].substring(6,
								this.fields[paramInt].length())).floatValue();

				l = (int) f * 1000;
			}

			GregorianCalendar localGregorianCalendar = new GregorianCalendar(
					TimeZone.getTimeZone("UTC"));

			localGregorianCalendar.set(10, i);
			localGregorianCalendar.set(12, j);
			localGregorianCalendar.set(13, k);
			localGregorianCalendar.set(14, l);
			return localGregorianCalendar.getTime();
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
		} catch (NumberFormatException localNumberFormatException) {
		}
		throw new NMEAException("NMEA0183: index is not a UTC time field",
				paramInt);
	}

	public int getInteger(int paramInt) throws NMEAException {
		try {
			return Integer.parseInt(this.fields[paramInt]);
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
		} catch (NumberFormatException localNumberFormatException) {
		}
		throw new NMEAException("NMEA0183: index is not an interger field:",
				paramInt);
	}

	public byte getByte(int paramInt) throws NMEAException {
		try {
			return Byte.parseByte(this.fields[paramInt]);
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
		} catch (NumberFormatException localNumberFormatException) {
		}
		throw new NMEAException("NMEA0183: index is not an byte field ",
				paramInt);
	}

	public char getChar(int paramInt) throws NMEAException {
		try {
			return this.fields[paramInt].charAt(0);
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
		} catch (NumberFormatException localNumberFormatException) {
		}
		throw new NMEAException("NMEA0183: index is not an byte field ",
				paramInt);
	}

	public double getDouble(int paramInt) throws NMEAException {
		try {
			return Double.valueOf(this.fields[paramInt]).doubleValue();
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
		} catch (NumberFormatException localNumberFormatException) {
		}
		throw new NMEAException("NMEA0183: index is not an double field:",
				paramInt);
	}

	public String getString(int paramInt) throws NMEAException {
		try {
			return this.fields[paramInt];
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
			throw new NMEAException(
					"NMEA0183: getString(int) index too high!!", paramInt);
		}
	}

	public int getNumberOfDataFields() {
		return this.fields.length;
	}

	public boolean isSet(int paramInt) throws NMEAException {
		try {
			if(paramInt >= this.maxFields)
				return false;
			
			return (!(this.fields[paramInt].equals("")));
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
			throw new NMEAException("NMEA0183: isSet(int) index too high!!",
					paramInt);
		}
	}

	public boolean isChecksumBad() throws NMEAException {
		return ((this.hasChecksum) && (computeChecksum() != this.checksum));
	}

	private void chopSentence() {

		try {
			StringTokenizer localStringTokenizer = new StringTokenizer(
					this.sentence, ",", true);
			Vector<String> localVector = new Vector<String>();
			Object localObject = "";
			while (localStringTokenizer.hasMoreTokens()) {
				String str1 = localStringTokenizer.nextToken();
				if ((str1.equals(",")) && (((String) localObject).equals(","))) {
					localVector.addElement("");
				} else if (!(str1.equals(","))) {
					localVector.addElement(str1);
				}
				localObject = str1;
			}
			
			this.maxFields = localVector.size();
			this.fields = new String[this.maxFields];
			localVector.copyInto(this.fields);

			if (this.fields[(this.fields.length - 1)].indexOf(42) == -1) {
				this.hasChecksum = false;
			} else {
				String str2 = this.fields[(this.fields.length - 1)];
				this.fields[(this.fields.length - 1)] = str2.substring(0, str2
						.indexOf(42));
				this.checksum = Byte.parseByte(str2.substring(
						str2.indexOf(42) + 1, str2.length()), 16);
				this.hasChecksum = true;
			}
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
			// throw new NMEAException("NMEA0183: invalid data in checksum",
			// -1);
		} catch (Exception localException) {
			// throw new NMEAException(
			// "NMEA0183: Sentence is not a Valid NMEA0183 Sentence", 0);
		}

	}

	private byte computeChecksum() {
		int i = 0;

		for (int k = 1; k < this.sentence.length(); ++k) {
			int j = this.sentence.charAt(k);
			if (j == 42) {
				break;
			}
			i = (byte) (i ^ j);
		}
		return (byte) i;
	}

	public String toString() {
		return this.sentence;
	}

}
