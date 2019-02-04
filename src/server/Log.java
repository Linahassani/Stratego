package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A log class that adds text to the log and returns log as string
 * 
 * @author Anders Qvist
 */
public class Log {
	private static Log instance;
	private String pattern = "yyyy:MM:dd:HH:mm:ss";
	private SimpleDateFormat format = new SimpleDateFormat(pattern);
	private static final String FILE_NAME = "files/serverlog.txt";
	private String date = format.format(new Date());

	
	private Log() {
	}

	/**
	 * returns the only instance of the class
	 */
	public static Log getInstance() {
		if (instance == null) {
			instance = new Log();
		}
		return instance;
	}

	/**
	 * adds text to the log
	 * 
	 * @param textToLog
	 *            the text to add to the log
	 */
	public void addToLog(String textToLog) {
		try {
			FileOutputStream fis = new FileOutputStream(FILE_NAME, true);
			OutputStreamWriter osr = new OutputStreamWriter(fis);
			BufferedWriter writer = new BufferedWriter(osr);
			
			writer.append(date.toString() + " " + textToLog);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			addToLog(" Log : addToLog: " + e.toString() + System.lineSeparator());
		}
	}

	/**
	 * Reads the log from filename and returns it in string format
	 * 
	 * @param filter
	 *            The filter to use, network traffic or Exceptions
	 * @return whole log file, all network traffic or all exceptions
	 */
	public String getLog(String filter) {
		String all = "";
		String network = "";
		String exceptions = "";
		String emptyLine = "";

		try {
			FileInputStream fis = new FileInputStream(FILE_NAME);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String strLine;
			while ((strLine = reader.readLine()) != null) {
				all += strLine + System.lineSeparator();
				emptyLine = reader.readLine() + System.lineSeparator();
				all += emptyLine;
				
				if (strLine.indexOf("ip :") >= 0) {
					network += strLine + System.lineSeparator();
					network += emptyLine;
					
				} else if (strLine.indexOf("Exception") >= 0)
					exceptions += strLine + System.lineSeparator();
				exceptions += emptyLine;
			}
			fis.close();
		} catch (Exception e) {
			addToLog(" Log : getLog: " + e.toString() + System.lineSeparator());
		}
		if (filter == "ip :") {
			return network;
			
		} else if (filter == "Exception") {
			return exceptions;
			
		} else {
			return all;
		}
	}

	/**
	 * Reads the log start time to to end time
	 * 
	 * @param filter
	 *            The filter to use, network traffic or Exceptions
	 * @param from
	 *            The start time
	 * @param to
	 *            The end time
	 * @return the log, filtered or unfiltered depending on options, as a string
	 */
	public String getLog(String filter, Date from, Date to) {
		String log = "";
		String network = "";
		String exceptions = "";
		String emptyLine = "";

		try {
			FileInputStream fis = new FileInputStream(FILE_NAME);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			String strLine;
			while ((strLine = reader.readLine()) != null) {
				System.out.println(strLine.length() + " testing");
				String subStr = strLine.substring(0, 19);
				Date parseDate = format.parse(subStr);
				System.out.println(parseDate + "parsedate");

				if (parseDate.after(from) && parseDate.before(to)) {

					log += strLine + System.lineSeparator();
					
					if ((emptyLine = reader.readLine()) != null) {
						log += emptyLine + System.lineSeparator();
					}
					emptyLine = reader.readLine() + System.lineSeparator();
					log += emptyLine + System.lineSeparator();
					
					if (strLine.indexOf("ip :") >= 0) {
						network += strLine + System.lineSeparator();
						
						if ((emptyLine = reader.readLine()) != null) {
							network += emptyLine + System.lineSeparator();
							
						} else if (strLine.indexOf("Exception") >= 0) {
							exceptions += strLine + System.lineSeparator();
							
							if ((emptyLine = reader.readLine()) != null) {
								exceptions += emptyLine + System.lineSeparator();
							}
						}
					}
				}
			}
			fis.close();
		} catch (Exception e) {
			addToLog(" Log : getLog: " + e.toString() + System.lineSeparator());
		}
		if (filter == "ip :") {
			return network;
			
		} else if (filter == "Exception") {
			return exceptions;
			
		} else {
			return log;
		}
	}
}