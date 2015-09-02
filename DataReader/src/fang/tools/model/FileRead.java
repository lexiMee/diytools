package fang.tools.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class FileRead extends GPSNmeaModel.GPSNmeaReader
{
	private String mPath = null;
	private DataBuffer mBuffer = null;
	
	public FileRead(GPSNmeaModel model, String str, DataBuffer buffer)
	{
		model.super();
		mPath = str;
		mBuffer = buffer;
	}
	
	public void read()
	{
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(mPath);
			br = new BufferedReader(fr);
			String line;
			int idx, idx1;
			while ((line = br.readLine()) != null) {
				idx = line.indexOf("$GP");
				idx1 = line.indexOf("$BD");
				if (idx >= 0)
				{
					mBuffer.PutMsg(line.substring(idx) + "\r\n");
					//Thread.sleep(10);
				} else if (idx1 >= 0) {
					mBuffer.PutMsg(line.substring(idx1) + "\r\n");
				}
			}
			
			mBuffer.PutMsg(END);
			br.close();
			fr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		/*} catch (InterruptedException e) {
			e.printStackTrace();*/
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {}
			}
		}
		System.out.println("Reader Done!");
	}
	
}