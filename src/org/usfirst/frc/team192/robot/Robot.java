package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import edu.wpi.first.wpilibj.DigitalInput;
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

	private Autonomous auto;
	private Teleop teleop;
	
	DigitalInput photosensor;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new ADXRS450_Gyro();
		//swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);
		//swerve.zero();
    
		auto = new Autonomous(swerve);
		teleop = new Teleop(input);
		photosensor = new DigitalInput(1);
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
		teleop.periodic();
		System.out.println(photosensor.get());
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
