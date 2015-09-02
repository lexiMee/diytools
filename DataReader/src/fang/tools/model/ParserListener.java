package fang.tools.model;

public interface ParserListener
{
	
	void registerObserver(GPSObserver o);
	void removeObserver(GPSObserver o);
}