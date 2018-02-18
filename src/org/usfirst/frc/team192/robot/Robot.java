package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.ImageThread;
import org.usfirst.frc.team192.vision.Imshow;
import org.usfirst.frc.team192.vision.VisionThread;
import org.usfirst.frc.team192.swerve.NavXGyro;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends IterativeRobot {

	private Gyro gyro;
	private FullSwervePID swerve;
	private JoystickInput input;
	private VisionThread vision;
	private Autonomous auto;
	private Teleop teleop;
	private Imshow imshow;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new NavXGyro();
		swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);
		vision = new ImageThread();
		vision.start();

		auto = new Autonomous(swerve);
		teleop = new Teleop(vision, swerve, input, gyro);
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
	}

	@Override
	public void teleopPeriodic() {
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
