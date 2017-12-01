package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class WheelReadThread extends Thread
{
	private Talon turnMotor;
	private DigitalInput limitSwitch;
	private Encoder enc;
	
	private double theta;
	private int lastUpdated;
	private boolean limitActivated;
	
	private double TO_RADIANS;
	
	public WheelReadThread(Talon turnMotor, DigitalInput limitSwitch, Encoder enc)
	{
		this.turnMotor = turnMotor;
		this.limitSwitch = limitSwitch;
		this.enc = enc;
		theta = 0;
		lastUpdated = 0;
		limitActivated = false;
		
		TO_RADIANS = 0.000785;
	}
	
	public void run()
	{
		while (true)
		{
			if (limitSwitch.get() && !limitActivated)
			{
				theta = 0;
				lastUpdated = enc.get();
				limitActivated = true;
			}
			else if (!limitSwitch.get())
			{
				int moddedEncoderValue = enc.get() - lastUpdated;
				double toBeTheta = moddedEncoderValue * TO_RADIANS;
				if (toBeTheta < 0)
				{
					toBeTheta += 2 * Math.PI;
				}
				theta = toBeTheta;
				limitActivated = false;
			}
		}
	}
	
	public void zero()
	{
		theta = 0;
	}
	
	public double getTheta()
	{
		return theta;
	}
}
























