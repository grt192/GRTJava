package org.usfirst.frc.team192.swerve;

import edu.wpi.first.wpilibj.Talon;

public class Swerve
{
	/*
	Layout of robot:
	
	upperLeft --> 	+-----------------+  <-- upperRight
					|                 |
					|                 |
					|                 |
					|                 |
					|                 |
					|                 |
					|                 |
					|                 |
					|                 |
					|                 |
					|                 |
					|                 |
	lowerLeft -->	+-----------------+  <-- lowerRight
	
	 */
	
	private Wheel upperRight;
	private Wheel upperLeft;
	private Wheel lowerRight;
	private Wheel lowerLeft;
	private double width;
	private double height;
	
	public Swerve(Wheel upperRight, Wheel upperLeft, Wheel lowerRight, Wheel lowerLeft, double width, double height)
	{
		this.upperRight = upperRight;
		this.upperLeft = upperLeft;
		this.lowerRight = lowerRight;
		this.lowerLeft = lowerLeft;
		this.width = width;
		this.height = height;
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
