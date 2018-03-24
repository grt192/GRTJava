package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.fieldMapping.FieldMapperEncoder;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.NavXGyro;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends IterativeRobot {

	private Gyro gyro;
	private FullSwervePID swerve;

	// private RemoteVisionThread vision;
	private Autonomous auto;
	private Teleop teleop;
	private Elevator elevator;
	private Intake intake;
	private XboxController input;

	private FieldMapperEncoder fieldMapperEncoder;

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
		fieldMapperEncoder = new FieldMapperEncoder(gyro, swerve);
		VisionSwerve vision = new VisionSwerve(swerve, fieldMapperEncoder, intake);
		auto = new Autonomous(swerve, intake, elevator, fieldMapperEncoder);
		teleop = new Teleop(swerve, intake, elevator, vision);
	}

	@Override
	public void autonomousInit() {
		swerve.enable();
		swerve.zeroGyro();
		fieldMapperEncoder.reset();
		auto.init();
	}

	@Override
	public void autonomousPeriodic() {
		checkForBrownout();
		fieldMapperEncoder.update();
		auto.periodic();
	}

	@Override
	public void teleopInit() {
		swerve.enable();
		teleop.init();
	}

	@Override
	public void teleopPeriodic() {
		checkForBrownout();
		fieldMapperEncoder.update();
		teleop.periodic();
	}

	@Override
	public void disabledInit() {
		swerve.disable();
	}

	// private int index;

	@Override
	public void testInit() {
		// index = 0;
		// System.out.println(index + 1);
	}

	private static long lastBrownout;

	private static void checkForBrownout() {
		if (RobotController.isBrownedOut())
			lastBrownout = System.currentTimeMillis();
	}

	public static long timeSinceLastBrownout() {
		return System.currentTimeMillis() - lastBrownout;
	}

	@Override
	public void testPeriodic() {
		checkForBrownout();
		swerve.controllerZero(input);
		// if (input.getAButtonPressed()) {
		// index++;
		// index %= 16;
		// System.out.println(index + 1);
		// }
		// new TalonSRX(index + 1).set(ControlMode.PercentOutput,
		// input.getX(Hand.kLeft));
	}
}
