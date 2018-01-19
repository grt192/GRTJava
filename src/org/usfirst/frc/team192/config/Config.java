package org.usfirst.frc.team192.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Config {
	public int getInt(String key) {
		return 0;
	}
	
	public boolean getBoolean(String key) {
		return false;
	}
	
	public String getString(String key) {
		return "";
	}
	
	public double getDouble(String key) {
		return 0.0;
	}
	
	public Config(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}



















