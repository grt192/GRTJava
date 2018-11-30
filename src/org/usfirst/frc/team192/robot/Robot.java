package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.fieldMapping.FieldMapper;
import org.usfirst.frc.team192.swerve.BetterTankSwerve;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends IterativeRobot {

	private BetterTankSwerve swerve;
	private XboxController input;

	private FieldMapper fieldMapper;

	@Override
	public void robotInit() {
		Config.start();
		input = new XboxController(0);
		swerve = new BetterTankSwerve();
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
		swerve.updateWithJoystick(input);
	}

	@Override
	public void disabledInit() {
	}

	// private int index;

	@Override
	public void testInit() {
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
	}
}
