package org.usfirst.frc.team192.swerve;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;

public class WheelRotateThread extends Thread
{
	private CANTalon motor;
	private WheelReadThread wheelRead;
	private double targetTheta;
	
	public double TOLERANCE; // radians
	
	public WheelRotateThread(CANTalon motor, WheelReadThread wheelRead)
	{
		this.motor = motor;
		this.wheelRead = wheelRead;
		this.targetTheta = 0;
		TOLERANCE = 0.02;
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
			double current = wheelRead.getTheta();
			double forwardChange = (targetTheta - current + 2 * Math.PI) % (2 * Math.PI);
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
			System.out.println("right now: " + current);
		}
	}
}



















