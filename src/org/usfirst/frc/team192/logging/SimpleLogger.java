package org.usfirst.frc.team192.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

public class SimpleLogger {

	private static String dir = "/home/lvuser/";

	private String[] titles;
	private LinkedList<double[]> data;

	public SimpleLogger(String... titles) {
		this.titles = titles;
		data = new LinkedList<>();
	}

	public void add(double... data) {
		this.data.add(data);
	}

	public void write(String fileName) {
		try {
			PrintWriter pw = new PrintWriter(new File(dir + fileName + ".csv"));
			pw.println(titlesToString());
			Iterator<double[]> iter = data.iterator();
			while (iter.hasNext())
				pw.println(dataToString(iter.next()));
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void write(String fileName, boolean addDate, boolean addLength) {
		if (addDate) {
			fileName += " " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());
		}
		if (addLength) {
			fileName += " " + data.size();
		}
		write(fileName);
	}

	private String titlesToString() {
		String s = "";
		for (int i = 0; i < titles.length - 1; i++) {
			s += titles[i] + ",";
		}
		s += titles[titles.length - 1];
		return s;
	}

	private String dataToString(double[] data) {
		String s = "";
		for (int i = 0; i < data.length - 1; i++) {
			s += data[i] + ",";
		}
		s += data[data.length - 1];
		return s;
	}

}
