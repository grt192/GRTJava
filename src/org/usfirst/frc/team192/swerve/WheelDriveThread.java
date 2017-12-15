package org.usfirst.frc.team192.swerve;

import com.ctre.CANTalon;

public class WheelDriveThread extends Thread
{
	private CANTalon motor;
	private double speed;
	public boolean shouldStillRun;
	
	public WheelDriveThread(CANTalon motor)
	{
		this.motor = motor;
		speed = 0;
		shouldStillRun = true;
	}
	
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}
	
	public void run()
	{
		while (true)
		{
			if (shouldStillRun)
				motor.set(speed);
		}
	}
}
