package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.swerve.FullSwervePID;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends IterativeRobot {

	private Gyro gyro;
	private FullSwervePID swerve;
	private JoystickInput input;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new ADXRS450_Gyro();
		swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);

	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		swerve.enable();

	}

	@Override
	public void teleopPeriodic() {
		swerve.updateWithJoystick(input);
	}

	@Override
	public void disabledInit() {
		swerve.disable();
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {
	}
}
