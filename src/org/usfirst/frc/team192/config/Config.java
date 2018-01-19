package org.usfirst.frc.team192.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	
	public Config(String fileName) {
		map = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			while (line != null) {
				String[] splitted = line.split("=");
				map.put(splitted[0], splitted[1]);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}



















