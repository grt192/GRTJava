package org.usfirst.frc.team192.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {
	private static Map<String, String> map;
	
	public static int getInt(String key) {
		try {
			return Integer.parseInt(map.get(key));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(map.get(key));
	}
	
	public static String getString(String key) {
		String result = map.get(key);
		if (result == null) {
			return "";
		}
		return result;
	}
	
	public static double getDouble(String key) {
		try {
			return Double.parseDouble(map.get(key));
		} catch (NumberFormatException e) {
			return -1.0;
		}
	}
	
	public static void start() {
		map = new HashMap<String, String>();
		try {
			Scanner nameScanner = new Scanner(new File("/home/lvuser/name.192"));
			String name = nameScanner.nextLine();
			nameScanner.close();
			String fileName = "beta2017.192";
			System.out.println("reading from file " + fileName);
			InputStream s = Config.class.getResourceAsStream(fileName);
			Scanner scanner = new Scanner(s);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() > 0 && line.charAt(0) != '#') {
					String[] splitted = line.split("=");
					map.put(splitted[0], splitted[1]);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		/*
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ": " + getInt(entry.getKey()));
		}
		*/
		System.out.println(getString("name"));
	}
}



















