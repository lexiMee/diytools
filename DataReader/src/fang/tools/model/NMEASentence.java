package fang.tools.model;

public abstract interface NMEASentence {

	/**
	 * 
	 * @return
	 */
	public abstract String getSentenceType();
	
	public abstract void doWork();

	public abstract boolean hasValue();

}
