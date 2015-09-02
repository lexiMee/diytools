package fang.tools.model;

import java.util.ArrayList;
import java.util.HashMap;

public interface GPSModelInterface
{
	void start(String str);
	void stop();
	
	void registerObserver(GPSObserver o);
	void removeObserver(GPSObserver o);
	
	public ArrayList<GGA> getGGA();
	
	public ArrayList<GLL> getGLL();

	public ArrayList<GSA> getGSA();
	
	public ArrayList<RMC> getRMC();
	
	public ArrayList<VTG> getVTG();
	
	public ArrayList<GSV> getGSV();
	
	public HashMap<Integer, ArrayList<Double>> getStaHashMap();
	
	public void printGGA();
}