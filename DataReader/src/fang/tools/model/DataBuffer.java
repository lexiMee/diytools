package fang.tools.model;

public class DataBuffer {

	private String Content = "";
	private String CurrentMsg, TempContent;
	private boolean available = false;
	private int LengthNeeded = 1;

	
	/**
	 * 
	 * This function returns a string with all data in buffer
	 * 
	 */
	public synchronized String GetMsg() {
		int length = Content.length();
		return GetMsg(length);
	}
	
	
	public synchronized String GetOneLine()
	{
		int length = Content.indexOf("\r\n", 0);
		if (length == -1) return null;
		//System.out.println("content:" + Content + "length: " + length + "contentlength:" + Content.length() + "\r\n");
		return GetMsg(length + 2);
	}

	/**
	 * 
	 * This function returns a string with a certain length from the incomin
	 * messages.
	 * 
	 * @param Length
	 *            The length of the string to be returned.
	 * 
	 */
	public synchronized String GetMsg(int Length) {
		LengthNeeded = Length;
		notifyAll();
		if (LengthNeeded > Content.length()) {
			available = false;
			while (available == false) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
		CurrentMsg = Content.substring(0, LengthNeeded);
		TempContent = Content.substring(LengthNeeded);
		Content = TempContent;
		LengthNeeded = 1;
		notifyAll();
		return CurrentMsg;
	}

	/**
	 * 
	 * This function stores a character captured from the serial port to the
	 * buffer area.
	 * 
	 * @param t
	 *            The char value of the character to be stored.
	 * 
	 */
	public synchronized void PutChar(int c) {
		Character d = new Character((char) c);
		Content = Content.concat(d.toString());
		if (LengthNeeded < Content.length()) {
			available = true;
		}
		notifyAll();
	}

	/**
	 * 
	 * This function stores a string to the buffer area.
	 * 
	 * @param str:  The string to be stored.
	 * 
	 */
	public synchronized void PutMsg(String str) {
		System.out.println("PutMsg str:" + str + "strlength:" + str.length());
		Content = Content.concat(str);
		if (LengthNeeded < Content.length()) {
			available = true;
		}
		notifyAll();
	}
}
