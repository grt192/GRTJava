package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.ImageThread;
import org.usfirst.frc.team192.vision.Imshow;
import org.usfirst.frc.team192.vision.VisionTracking;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

	private GyroBase gyro;
	private FullSwervePID swerve;
	private JoystickInput input;
	private VisionTracking vision;
	private Autonomous auto;
	private Teleop teleop;
	private Imshow imshow;
	private ImageThread img;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new ADXRS450_Gyro();
		swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);
		auto = new Autonomous(swerve);

		img.start();
		teleop = new Teleop(vision, swerve, input, gyro);
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
		teleop = new Teleop(vision, swerve, input, gyro);
	}

	@Override
	public void teleopPeriodic() {
		teleop.periodic();

		// swerve.updateWithJoystick(input);
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
