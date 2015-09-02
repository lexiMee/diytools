package fang.tools.model;

import java.text.DecimalFormat;

public class GeoPos {
	private Degree latitude;
	private Degree longitude;

	public GeoPos(Degree paramDegree1, Degree paramDegree2) {
		this.latitude = paramDegree1;
		this.longitude = paramDegree2;
	}

	public Degree getLongitude() {
		return this.longitude;
	}

	private void setLongitude(Degree paramDegree) {
		this.longitude = paramDegree;
	}

	public Degree getLatitude() {
		return this.latitude;
	}

	private void setLatitude(Degree paramDegree) {
		this.latitude = paramDegree;
	}

	public String getLatInDegMinDec() {
		String sgn = (latitude.getDegrees() >= 0) ? "N" : "S";

		String str = "";
		DecimalFormat df = new DecimalFormat("00");
		str += sgn  + " " + df.format(latitude.getDegrees())  + "бу" + 
				df.format(latitude.getMinutes()) + "'";
				
		df = new DecimalFormat("00.00");
		str += df.format(latitude.getSeconds());
		
		return str;
	}
	
	public String getLngInDegMinDec() {
		String sgn = (longitude.getDegrees() >= 0) ? "E" : "W";

		String str = "";
		DecimalFormat df = new DecimalFormat("00");
		str += sgn  + " " + df.format(longitude.getDegrees())  + "бу" + 
				df.format(longitude.getMinutes()) + "'";
				
		df = new DecimalFormat("00.00");
		str += df.format(longitude.getSeconds());
		
		return str;
	}
	
	/*
	public String getLatInDegMinDec() {
		int degree = (int) this.lat;
		String sgn = (degree >= 0) ? "N" : "S";
		double minutes = Math.abs(this.lat - degree);
		double hexMin = 100.0D * minutes * 0.6D;
		DecimalFormat df = new DecimalFormat("00.00");
		String strMinutes = df.format(hexMin);
		df = new DecimalFormat("00");

		return sgn + "  " + df.format(Math.abs(degree)) + "бу" + strMinutes
				+ "'";

	}

	public String getLngInDegMinDec() {
		int degree = (int) this.lng;
		String sgn = (degree >= 0) ? "E" : "W";
		double minutes = Math.abs(this.lng - degree);
		double hexMin = 100.0D * minutes * 0.6D;
		DecimalFormat df = new DecimalFormat("00.00");
		String strMinutes = df.format(hexMin);
		df = new DecimalFormat("000");

		return sgn + " " + df.format(Math.abs(degree)) + "бу" + strMinutes + "'";
	}
	*/
	
	public String toString() {
		return getLatInDegMinDec() + ", " + getLngInDegMinDec();
	}
}
