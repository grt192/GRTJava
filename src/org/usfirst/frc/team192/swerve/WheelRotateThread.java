package org.usfirst.frc.team192.swerve;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;

public class WheelRotateThread extends Thread
{
	private CANTalon motor;
	private WheelReadThread wheelRead;
	private double targetTheta;
	public boolean shouldStillRun;
	private double readingWhenZeroed;
	
	public double TOLERANCE; // radians
	
	public WheelRotateThread(CANTalon motor, WheelReadThread wheelRead, double readingWhenZeroed)
	{
		this.motor = motor;
		this.wheelRead = wheelRead;
		this.targetTheta = 0;
		TOLERANCE = 0.02;
		shouldStillRun = true;
		this.readingWhenZeroed = readingWhenZeroed;
	}
	
	public void setReadingWhenZeroed(double readingWhenZeroed)
	{
		this.readingWhenZeroed = readingWhenZeroed;
	}
	
	public void setTargetTheta(double targetTheta)
	{
		// System.out.println("setting target theta to " + targetTheta);
		this.targetTheta = targetTheta;
	}
	
	public void run()
	{
		while (true)
		{
			if (shouldStillRun)
			{
				System.out.println("reading when zeroed: " + readingWhenZeroed);
				double current = wheelRead.getTheta() - readingWhenZeroed;
				double forwardChange = ((targetTheta - current) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
				double backwardChange = (-forwardChange + 2 * Math.PI) % (2 * Math.PI);
				// System.out.println(targetTheta);
				if (Math.min(forwardChange, backwardChange) < TOLERANCE) // kind of a waste to keep changing if it's about right already
				{
					// System.out.println("not moving motor");
					motor.set(0);
				}
				else if (forwardChange < backwardChange) // if it would be shorter to move forward than backward, move forward
				{
					motor.set(forwardChange / Math.PI);
				}
				else
				{
					motor.set(-backwardChange / Math.PI);
				}
				
				// System.out.println("target: " + targetTheta);
				// System.out.println(motor.getDeviceID() + " encoder value: " + current);
			}
			else
			{
				motor.set(0);
			}
		}
	}
}



















