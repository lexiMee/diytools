package fang.tools.model;

import java.text.DecimalFormat;

public class Degree {
	
	private int degrees;
	private int minutes;
	private double seconds;

	public Degree(int paramInt1, int paramInt2, double paramDouble) {
		this.degrees = paramInt1;
		this.minutes = paramInt2;
		this.seconds = paramDouble;
		if (paramInt2 > 60) {
			throw new IllegalArgumentException(
					"'There can not be more than 60 minutes in a degree");
		}
		if (paramDouble > 60.0D)
			throw new IllegalArgumentException(
					"'There can not be more than 60 seconds in a minute");
	}

	public int getDegrees() {
		return this.degrees;
	}

	public int getMinutes() {
		return this.minutes;
	}

	public double getSeconds() {
		return this.seconds;
	}

	public static Degree parseDDMM_MM(double paramDouble) {
		int i = (int) (paramDouble / 100.0D);
		int j = (int) (paramDouble - (i * 100));
		double d = (paramDouble - (i * 100) - j) * 60.0D;
		return new Degree(i, j, d);
	}

	public static Degree parseDDMM_SS(double paramDouble) {
		int i = (int) (paramDouble / 100.0D);
		int j = (int) (paramDouble - (i * 100));
		double d = (paramDouble - (i * 100) - j) * 100.0D;
		return new Degree(i, j, d);
	}

	public double getAsDegree() {
		return (this.degrees + this.minutes / 60.0D + this.seconds / 3600.0D);
	}

	public double getAsMinutes() {
		return (this.degrees * 60 + this.minutes + this.seconds / 60.0D);
	}
	
	public String getDegMinDec() {
		String str = "";
		
		DecimalFormat df = new DecimalFormat("00");
		str += df.format(degrees);
		str += df.format(minutes);
		
		df = new DecimalFormat("00.00");
		str += df.format(seconds);
		
		return str;
	}
	
}
