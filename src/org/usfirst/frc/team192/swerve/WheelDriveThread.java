package org.usfirst.frc.team192.swerve;

import com.ctre.CANTalon;

public class WheelDriveThread extends Thread
{
	private CANTalon motor;
	private double speed;
	
	public WheelDriveThread(CANTalon motor)
	{
		this.motor = motor;
		this.speed = 0;
	}
	
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}
	
	public void run()
	{
		while (true)
		{
			motor.set(speed);
		}
	}
}
