package org.usfirst.frc.team192.swerve;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;

public class WheelReadThread extends Thread
{
	private CANTalon turnMotor;
	private DigitalInput limitSwitch;
	
	private double theta;
	private int lastUpdated;
	private boolean limitActivated;
	
	private double TO_RADIANS;
	
	public WheelReadThread(CANTalon turnMotor, DigitalInput limitSwitch)
	{
		this.turnMotor = turnMotor;
		this.limitSwitch = limitSwitch;
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
				lastUpdated = turnMotor.getEncPosition();
				limitActivated = true;
			}
			else if (!limitSwitch.get())
			{
				int moddedEncoderValue = turnMotor.getEncPosition() - lastUpdated;
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
























