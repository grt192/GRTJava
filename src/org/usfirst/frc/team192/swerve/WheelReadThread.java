package org.usfirst.frc.team192.swerve;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;

public class WheelReadThread extends Thread
{
	private CANTalon turnMotor;
	private DigitalInput limitSwitch;
	
	private double theta;
	private int lastUpdated;
	private boolean limitSwitchIsInTheMidstOfBeingActivated;
	private int deltaEnc;
	public boolean zeroing;
	
	private double TO_RADIANS;
	
	public WheelReadThread(CANTalon turnMotor, DigitalInput limitSwitch)
	{
		this.turnMotor = turnMotor;
		this.limitSwitch = limitSwitch;
		theta = 0;
		lastUpdated = 0;
		limitSwitchIsInTheMidstOfBeingActivated = false;
		deltaEnc = turnMotor.getEncPosition();
		
		TO_RADIANS = 0.0008359831298535551;
		
		zeroing = false;
	}
	
	public void run()
	{
		while (true)
		{
			if (zeroing && limitSwitch.get())
			{
				turnMotor.set(0);
				zeroing = false;
			}
			else if (limitSwitch.get() && !limitSwitchIsInTheMidstOfBeingActivated)
			{
				theta = 0;
				lastUpdated = turnMotor.getEncPosition() - deltaEnc;
				limitSwitchIsInTheMidstOfBeingActivated = true;
			}
			else if (!limitSwitch.get())
			{
				int moddedEncoderValue = turnMotor.getEncPosition() - deltaEnc - lastUpdated;
				double toBeTheta = moddedEncoderValue * TO_RADIANS;
				if (toBeTheta < 0)
				{
					toBeTheta += 2 * Math.PI;
				}
				theta = toBeTheta;
				limitSwitchIsInTheMidstOfBeingActivated = false;
			}
		}
	}
	
	public void zero()
	{
		turnMotor.set(0.2);
		zeroing = true;
	}
	
	public boolean isZeroing()
	{
		return zeroing;
	}
	
	public double getTheta()
	{
		return theta;
	}
}
























