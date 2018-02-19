package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.ImageThread;
import org.usfirst.frc.team192.vision.Imshow;
import org.usfirst.frc.team192.vision.VisionThread;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends IterativeRobot {

//	private Gyro gyro;
	//private FullSwervePID swerve;
	private JoystickInput input;
//	private VisionThread vision;
//	private Autonomous auto;
	private Teleop teleop;
//	private Imshow imshow;

	@Override
	public void robotInit() {
		Config.start();
//		gyro = new ADXRS450_Gyro();
//		swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);
//		vision = new ImageThread();
//		vision.start();
//		teleop = new Teleop(vision, swerve, input, gyro);
		teleop = new Teleop(input);
		//auto = new Autonomous(swerve);
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		teleop.init();
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
