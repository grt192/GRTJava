package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;
import org.usfirst.frc.team192.swerve.experimental.Wheel;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class FullSwerve
{
	private Wheel[] wheels;
	private double robotWidth;
	private double robotHeight;
	
	public FullSwerve(double robotWidth, double robotHeight)
	{
		wheels = new Wheel[4];
		wheels[2] = new Wheel(new TalonSRX(1), new TalonSRX(2), null);// new DigitalInput(2));
		wheels[3] = new Wheel(new TalonSRX(8), new TalonSRX(7), null); // new DigitalInput(3));
		wheels[1] = new Wheel(new TalonSRX(9), new TalonSRX(10), null); // new DigitalInput(0));
		wheels[0] = new Wheel(new TalonSRX(14), new TalonSRX(16), null);// new
		this.robotWidth = robotWidth;
		this.robotHeight = robotHeight;
	}
	
	private double calcrv(JoystickInput input)
	{
		return 0.0;
	}
	
	private double calcvx(JoystickInput input)
	{
		return 0.0;
	}
	
	private double calcvy(JoystickInput input)
	{
		return 0.0;
	}
	
	public double realAtan(double x, double y)
	{
		return (Math.atan2(y, x) + 2 * Math.PI) % (2 * Math.PI);
	}
	
	private void changeMotors(double rv, double vx, double vy)
	{
		for (int i = 0; i < 4; i++)
		{
			double dx = robotWidth / 2 * (((i % 2) * 2) - 1);
			double dy = robotHeight / 2 * (((i / 2) * 2) - 1);
			double actualvx = vx + rv * dy;
			double actualvy = vy - rv * dx;
			double wheelTheta = realAtan(actualvx, -actualvy); // haha
			double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
			wheels[i].setDriveSpeed(speed);
			wheels[i].setTargetPosition(wheelTheta);
		}
	}
	
	public void updateWithJoystickInput(JoystickInput input)
	{
		changeMotors(calcrv(input), calcvx(input), calcvy(input));
	}

}


















