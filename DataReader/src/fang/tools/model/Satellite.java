package fang.tools.model;

/**
 * 
 * 	 1) satellite number, ���Ǻ�
	 2) elevation in degrees, ����(00��90��)
	 3) azimuth in degrees to true, ��λ��(000��359��)
	 4) SNR in dB, �����(00��99dB)

 * @author Jeff
 *
 */
public class Satellite {

	private int satNum;
	private int elevationDegree;
	private int azimuthDegree;
	private Double snr;
	

	public Satellite() {
		
	}
	
	public Satellite(int satNum, int elevationDegree, int azimuthDegree, Double snr) {
		this.satNum = satNum;
		this.elevationDegree = elevationDegree;
		this.azimuthDegree = azimuthDegree;
		this.snr = snr;
	}
	
	public void setSatNum(int num) {
		this.satNum = num;
	}
	public int getSatNum() {
		return this.satNum;
	}
	
	public void setElevationDegree(int degree) {
		this.elevationDegree = degree;
	}
	public int getElevationDegree() {
		return this.elevationDegree;
	}
	
	public void setAzimuthDegree(int degree) {
		this.azimuthDegree = degree;
	}
	public int getAzimuthDegree() {
		return this.azimuthDegree;
	}
	
	public void setSNR(Double snr) {
		this.snr = snr;
	}
	public Double  getSNR() {
		return this.snr;
	}
}
