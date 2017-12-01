package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class WheelDriveThread extends Thread
{
	private WheelReadThread wheelRead;
	
	public WheelDriveThread(Wheel wheel, double speed)
	{
		this.wheelRead = wheelRead;
	}
	
	public void run()
	{
		// TODO: put stuff in here
	}
}
