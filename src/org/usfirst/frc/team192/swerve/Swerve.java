package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.Talon;

public class Swerve
{
	/*
	Layout of robot:
	
	frontLeft --> 	+-----------------+  <-- frontRight
					|                 |
					|                 |
					|                 |___
					|                 |   |
					|                 |   | <-- climber
					|                 |   |
					|                 |   |
					|                 |___|
					|                 |
					|                 |
					|                 |
					|                 |
	backLeft -->	+-----------------+  <-- backRight
	
	 */
	
	private Wheel frontRight;
	private Wheel frontLeft;
	private Wheel backRight;
	private Wheel backLeft;
	
	public Swerve(Wheel frontRight, Wheel frontLeft, Wheel backRight, Wheel backLeft)
	{
		this.frontRight = frontRight;
		this.frontLeft = frontLeft;
		this.backRight = backRight;
		this.backLeft = backLeft;
	}
	
	public Swerve()
	{
		
	}
	
	public int updateWheelsUsingJoystickInputs(JoystickInput joyLeft, JoystickInput joyRight)
	{
		// TODO: put stuff in here
		return 0;
	}
	
	public static double calcvx(JoystickInput joyLeft, JoystickInput joyRight)
	{
		// TODO: put stuff in here
		return 0.0;
	}
	
	public static double calcvy(JoystickInput joyLeft, JoystickInput joyRight)
	{
		// TODO: put stuff in here
		return 0.0;
	}
	
	public static double calcRotationalVelocity(JoystickInput joyLeft, JoystickInput joyRight)
	{
		// TODO: put stuff in here
		return 0.0;
	}

}
