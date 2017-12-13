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
	
	public int updateWheelsUsingJoystickInputs(JoystickValue joyLeft, JoystickValue joyRight)
	{
		// TODO: put stuff in here
		return 0;
	}
	
	public double calcvx(JoystickValue joyLeft, JoystickValue joyRight)
	{
		// TODO: put stuff in here
		return 0.0;
	}
	
	public double calcvy(JoystickValue joyLeft, JoystickValue joyRight)
	{
		// TODO: put stuff in here
		return 0.0;
	}
	
	public double calcRotationalVelocity(JoystickValue joyLeft, JoystickValue joyRight)
	{
		// TODO: put stuff in here
		return 0.0;
	}
	
	public int zeroWheels()
	{
		// TODO: return 1 on success and 0 on error
		return 1;
	}

}
