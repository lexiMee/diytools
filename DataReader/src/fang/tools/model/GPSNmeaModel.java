package fang.tools.model;

import fang.tools.*;
import fang.tools.util.NMEAException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class GPSNmeaModel implements GPSModelInterface {
	private ArrayList<GPSObserver> observers = null;
	private GPSNmeaReader mReaderThread = null;
	private GPSNmeaParser mParserThread = null;
	private DataBuffer mBuffer = null;
	private HashMap<Integer, ArrayList<Double>> mStaInfo = null;

	public GPSNmeaModel() {
		mBuffer = new DataBuffer();
		observers = new ArrayList<GPSObserver>();
		mStaInfo = new HashMap<Integer, ArrayList<Double>>();
	}

	public void start(String str) {
		mReaderThread = new FileRead(this, str, mBuffer);
		mParserThread = new GPSNmeaParser(mBuffer);
		mReaderThread.start();
		mParserThread.start();
	}

	public void stop() {
	}

	private void notifyObserver() {
		System.out.println("Model notifyObserver!");
		dataIntegration();
		Iterator<GPSObserver> it = observers.iterator();
		while (it.hasNext()) {
			GPSObserver ob = it.next();
			ob.onDone();
		}
	}

	private void dataIntegration() {
		ArrayList<GSV> gsv = getGSV();
		Iterator<GSV> it = gsv.iterator();
		HashMap<Integer, Satellite> map = null;
		while (it.hasNext()) {
			GSV sv = it.next();
			map = sv.getSateHashMap();
			// iterator hashmap
			Iterator<Entry<Integer, Satellite>> mapit = map.entrySet()
					.iterator();
			while (mapit.hasNext()) {
				Entry<Integer, Satellite> entry = mapit.next();
				if (mStaInfo.get(entry.getKey()) == null) {
					ArrayList<Double> snr = new ArrayList<Double>();
					snr.add(new Double(entry.getValue().getSNR()));
					mStaInfo.put(entry.getKey(), snr);
				} else {
					mStaInfo.get(entry.getKey()).add(
							new Double(entry.getValue().getSNR()));
				}
			}
		}
		mStaInfo.remove(0);//
		print();
		printGGA();
		printRMC();
	}

	private void print() {
		System.out.println("Model print");
		Iterator<Entry<Integer, ArrayList<Double>>> it = mStaInfo.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Integer, ArrayList<Double>> entry = it.next();
			System.out.println("StatelliteNum: " + entry.getKey());
			ArrayList<Double> snr = entry.getValue();
			Iterator<Double> it1 = snr.iterator();
			while (it1.hasNext()) {
				System.out.print("\t" + it1.next());
			}
			System.out.println("");
		}
	}
	
	public void printGGA() {
		ArrayList<GGA> temp = getGGA();
		System.out.println("GGA length:" + temp.size());
		Iterator<GGA> it = temp.iterator();
		while (it.hasNext())
		{
			it.next().doWork();
		}
	}
	
	public void printRMC() {
		ArrayList<RMC> temp = getRMC();
		System.out.println("RMC length:" + temp.size());
		Iterator<RMC> it = temp.iterator();
		while (it.hasNext())
		{
			it.next().doWork();
		}
	}

	public void registerObserver(GPSObserver o) {
		if (!observers.contains(o))
			observers.add(o);
	}

	public void removeObserver(GPSObserver o) {
		if (observers.contains(o))
			observers.remove(o);
	}

	public ArrayList<GGA> getGGA() {
		return ((GPSNmeaParser) mParserThread).getGGA();
	}

	public ArrayList<GSV> getGSV() {
		return ((GPSNmeaParser) mParserThread).getGSV();
	}

	public ArrayList<GLL> getGLL() {
		return ((GPSNmeaParser) mParserThread).getGLL();
	}

	public ArrayList<GSA> getGSA() {
		return ((GPSNmeaParser) mParserThread).getGSA();
	}

	public ArrayList<RMC> getRMC() {
		return ((GPSNmeaParser) mParserThread).getRMC();
	}

	public ArrayList<VTG> getVTG() {
		return ((GPSNmeaParser) mParserThread).getVTG();
	}
	
	public HashMap<Integer, ArrayList<Double>> getStaHashMap()
	{
		return mStaInfo;
	}

	private class GPSNmeaParser extends Thread {

		private DataBuffer mBuffer = null;
		private ArrayList<GGA> mGGA = null;
		private ArrayList<GLL> mGLL = null;
		private ArrayList<GSA> mGSA = null;
		private ArrayList<GSV> mGSV = null;
		private ArrayList<RMC> mRMC = null;
		private ArrayList<VTG> mVTG = null;
		private ArrayList<GPSObserver> observers = null;
		private final String END = "Done\r\n";

		public GPSNmeaParser(DataBuffer buffer) {
			mBuffer = buffer;
			mGGA = new ArrayList<GGA>();
			mGLL = new ArrayList<GLL>();
			mGSA = new ArrayList<GSA>();
			mGSV = new ArrayList<GSV>();
			mRMC = new ArrayList<RMC>();
			mVTG = new ArrayList<VTG>();
			observers = new ArrayList<GPSObserver>();
		}

		public ArrayList<GGA> getGGA() {
			return mGGA;
		}

		public ArrayList<GSV> getGSV() {
			return mGSV;
		}

		public ArrayList<GLL> getGLL() {
			return mGLL;
		}

		public ArrayList<GSA> getGSA() {
			return mGSA;
		}

		public ArrayList<RMC> getRMC() {
			return mRMC;
		}

		public ArrayList<VTG> getVTG() {
			return mVTG;
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(10);
					String str = mBuffer.GetOneLine();
					if (str != null) {
						//System.out.println(str + "\r\n");
						if (str.length() > 0 && !str.equals(END)) {

							ParseSentence sentence = new ParseSentence(str);

							try {

								int i = NMEAMappings.getSentenceID(sentence
										.getSentenceType());
								switch (i) {
								case 1006:
									mGGA.add(new GGA(sentence));
									break;
								case 1007:
									mGLL.add(new GLL(sentence));
									break;
								case 1008:
									mGSA.add(new GSA(sentence));
									break;
								case 1009:
									mGSV.add(new GSV(sentence));
									break;
								case 1015:
									mRMC.add(new RMC(sentence));
									break;
								case 1019:
									mVTG.add(new VTG(sentence));
									break;
								default:
									System.err
											.println("CustomClient: Unknown Sentence: "
													+ sentence
															.getSentenceType());
								}
								// notifyListener((NMEASentence) localObject);
							} catch (NMEAException e) {
								e.printStackTrace();
							}

						} else if (str.equals(END)) {
							System.out.println("Parser Done!");
							GPSNmeaModel.this.notifyObserver();
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}//end-while
		}//end-function-run()

	}

	public abstract class GPSNmeaReader extends Thread {

		boolean goRead = true;
		protected final String END = "Done\r\n";

		public GPSNmeaReader() {
		}

		public boolean canRead() {
			return this.goRead;
		}

		public void enableReading() {
			this.goRead = true;
		}

		/**
		 * Customize, overwrite this class to get plugged on the right data
		 * source like a Serial Port for example.
		 */
		public abstract void read();

		public void run() {
			System.out.println("Reader Running");
			read();
		}
	}
}