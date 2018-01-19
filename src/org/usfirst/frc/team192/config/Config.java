package org.usfirst.frc.team192.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Config {
	private Map<String, String> map;
	
	public int getInt(String key) {
		try {
			return Integer.parseInt(map.get(key));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(map.get(key));
	}
	
	public String getString(String key) {
		return map.get(key);
	}
	
	public double getDouble(String key) {
		try {
			return Double.parseDouble(map.get(key));
		} catch (NumberFormatException e) {
			return -1.0;
		}
	}
	
	public Config() {
		map = new HashMap<String, String>();
		try {
			Scanner nameScanner = new Scanner(new File("/home/lvuser/name.192"));
			String name = nameScanner.nextLine();
			nameScanner.close();
			String fileName = "src/org/usfirst/frc/team192/config/" + name + ".192";
			Scanner scanner = new Scanner(new File(fileName));
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
	}
}



















