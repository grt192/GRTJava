package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.fieldMapping.FieldMapper;
import org.usfirst.frc.team192.fieldMapping.FieldMapperEncoder;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.power.TestLoggerThread;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.NavXGyro;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	private Gyro gyro;
	private FullSwervePID swerve;

	// private RemoteVisionThread vision;
	private Autonomous auto;
	private Teleop teleop;
	private Elevator elevator;
	private Intake intake;
	private XboxController input;

	private FieldMapper fieldMapper;

	private TestLoggerThread test;

	private Gyro getGyro() {
		switch (Config.getString("gyro")) {
		case "cruddy":
			return new ADXRS450_Gyro();
		case "navx":
		default:
			return new NavXGyro();
		}
	}

	@Override
	public void robotInit() {
		Config.start();
		input = new XboxController(0);
		gyro = getGyro();
		swerve = new FullSwervePID(gyro);
		elevator = new Elevator();
		intake = new Intake();
		fieldMapper = new FieldMapperEncoder(gyro, swerve);
		new Thread(fieldMapper).start();
		test = new TestLoggerThread();
		test.start();
		auto = new Autonomous(swerve, intake, elevator, fieldMapper);
		teleop = new Teleop(swerve, intake, elevator, null);
	}

	@Override
	public void autonomousInit() {
		swerve.enable();
		swerve.zeroGyro();
		fieldMapper.reset();
		auto.init();
	}

	@Override
	public void autonomousPeriodic() {
		checkForBrownout();
		auto.periodic();
		printDisplacement();
	}

	@Override
	public void teleopInit() {
		swerve.enable();
		teleop.init();
	}

	private void printDisplacement() {
		SmartDashboard.putNumber("x displacement", fieldMapper.getX() * 39.31);
		SmartDashboard.putNumber("y displacement", fieldMapper.getY() * 39.31);
	}

	@Override
	public void teleopPeriodic() {
		SmartDashboard.putNumber("elevator height", elevator.getHeight());
		checkForBrownout();
		teleop.periodic();
		printDisplacement();
	}

	@Override
	public void disabledInit() {
		swerve.disable();
		if (test != null)
			test.write();
	}

	// private int index;

	@Override
	public void testInit() {
		// index = 0;
		// System.out.println(index + 1);
		start = System.currentTimeMillis();
		speed = SmartDashboard.getNumber("elevator speed", 1);
		SmartDashboard.putNumber("elevator speed", speed);
	}

	private static long lastBrownout;

	private static void checkForBrownout() {
		if (RobotController.isBrownedOut())
			lastBrownout = System.currentTimeMillis();
	}

	public static long timeSinceLastBrownout() {
		return System.currentTimeMillis() - lastBrownout;
	}

	long start;
	double speed;

	@Override
	public void testPeriodic() {
		checkForBrownout();
		swerve.controllerZero(input);
		if (input.getYButtonPressed()) {
			fieldMapper.reset();
		}

		/*
		 * double speed = (System.currentTimeMillis() - start) / 30000.0; if (speed <
		 * 0.1) { elevator.setSpeed(speed); SmartDashboard.putNumber("power output",
		 * speed); SmartDashboard.putNumber("speed", elevator.getSpeed()); }
		 */

		elevator.setSpeed(speed / 10000);
		System.out.println(speed / 10000);

		// if (input.getAButtonPressed()) {
		// index++;
		// index %= 16;
		// System.out.println(index + 1);
		// }
		// new TalonSRX(index + 1).set(ControlMode.PercentOutput,
		// input.getX(Hand.kLeft));
	}
}
