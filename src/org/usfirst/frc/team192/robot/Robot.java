package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.swerve.FullSwervePID;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

	private GyroBase gyro;
	private FullSwervePID swerve;
	private JoystickInput input;

	private Autonomous auto;
	private Teleop teleop;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new ADXRS450_Gyro();
		swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);
		swerve.zero();

		auto = new Autonomous(swerve);
		teleop = new Teleop(input);

	}

	@Override
	public void autonomousInit() {
		auto.init();
	}

	@Override
	public void autonomousPeriodic() {
		auto.periodic();
	}

	@Override
	public void teleopInit() {
		swerve.enable();
	}

	@Override
	public void teleopPeriodic() {
		teleop.periodic();
	}

	@Override
	public void disabledInit() {
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {

	}
}
