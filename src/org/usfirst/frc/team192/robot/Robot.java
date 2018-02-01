package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.vision.ImageThread;

import org.usfirst.frc.team192.vision.VisionTracking;
import org.usfirst.frc.team192.vision.Imshow;

import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.SwerveBase;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.usfirst.frc.team192.robot.Teleop;

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
		//swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);
			
		auto = new Autonomous(swerve);
		teleop = new Teleop(input, gyro);
		img = new ImageThread();
		
		img.start();

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
		//swerve.enable();
	}

	@Override
	public void teleopPeriodic() {
		// teleop.periodic();
		//swerve.updateWithJoystick(input);
	}

	@Override
	public void disabledInit() {
		//swerve.disable();
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {
	}
}
