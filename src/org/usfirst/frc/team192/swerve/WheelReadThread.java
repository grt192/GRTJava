package org.usfirst.frc.team192.swerve;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;

public class WheelReadThread extends Thread
{
	private CANTalon turnMotor;
	private DigitalInput limitSwitch;
	
	private double theta;
	// private int lastUpdated;
	private boolean limitSwitchIsInTheMidstOfBeingActivated;
	private boolean zeroing;
	public int deltaEnc;
	private int startPos;
	
	private double TO_RADIANS;
	
	public WheelReadThread(CANTalon turnMotor, DigitalInput limitSwitch)
	{
		this.turnMotor = turnMotor;
		this.limitSwitch = limitSwitch;
		theta = 0;
		// lastUpdated = 0;
		limitSwitchIsInTheMidstOfBeingActivated = false;
		
		TO_RADIANS = 0.0008359831298535551;
		
		zeroing = false;
		deltaEnc = 0;
	}
	
	public void run()
	{
		while (true)
		{
			if (zeroing && limitSwitch.get())
			{
				System.out.println("stopping it from zeroing");
				turnMotor.set(0);
				zeroing = false;
				// lastUpdated = turnMotor.getEncPosition();
			}
			else if (limitSwitch.get() && !limitSwitchIsInTheMidstOfBeingActivated)
			{
				deltaEnc = turnMotor.getEncPosition() - startPos;
				System.out.println("hit limit at " + turnMotor.getEncPosition());
				limitSwitchIsInTheMidstOfBeingActivated = true;
			}
			else if (!limitSwitch.get())
			{
				int moddedEncoderValue = turnMotor.getEncPosition() - deltaEnc;
				double toBeTheta = moddedEncoderValue * TO_RADIANS;
				theta = ((toBeTheta % (2 * Math.PI)) + 2 * Math.PI) % (2 * Math.PI);
				limitSwitchIsInTheMidstOfBeingActivated = false;
			}
		}
	}
	
	public void zero()
	{
		// turnMotor.set(0.2);
		// zeroing = true;
		theta = 0;
		startPos = turnMotor.getEncPosition();
		limitSwitchIsInTheMidstOfBeingActivated = true;
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
























