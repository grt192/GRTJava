package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class WheelDriveThread extends Thread
{
	private Wheel wheel;
	private double speed;
	
	public WheelDriveThread(Wheel wheel, double speed)
	{
		this.wheel = wheel;
		this.speed = speed;
	}
	
	public void run()
	{
		// TODO: put stuff in here
	}
}
