package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class WheelRotateThread extends Thread
{
	private Wheel wheel;
	private double targetTheta;
	
	public WheelRotateThread(Wheel wheel, double targetTheta)
	{
		this.wheel = wheel;
		this.targetTheta = targetTheta;
	}
	
	public void run()
	{
		// TODO: put stuff in here
	}
}
