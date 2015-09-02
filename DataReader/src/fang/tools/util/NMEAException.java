package fang.tools.util;

public class NMEAException extends Exception {

	public NMEAException() {
		
	}
	
	public NMEAException(String s) {
		super(s);
	}

	public NMEAException(String s, int i) {
		super(s + Integer.valueOf(i).toString());
	}
}
