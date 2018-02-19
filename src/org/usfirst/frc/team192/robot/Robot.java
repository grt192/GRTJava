package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.NavXGyro;
import org.usfirst.frc.team192.vision.Imshow;
import org.usfirst.frc.team192.vision.VisionThread;
import org.usfirst.frc.team192.vision.nn.RemoteVisionThread;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends IterativeRobot {
	private Gyro gyro;
	private FullSwervePID swerve;
	private XboxController input;
	private VisionThread vision;
	private Autonomous auto;
	private Teleop teleop;
	private Imshow imshow;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new NavXGyro();
		swerve = new FullSwervePID(gyro);
		input = new XboxController(1);
		vision = new RemoteVisionThread();
		vision.start();
		teleop = new Teleop(vision, swerve, gyro);
		auto = new Autonomous(swerve);
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
		teleop.init();
	}

	@Override
	public void teleopPeriodic() {
		swerve.updateWithJoystick(input);
		teleop.periodic();	
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
