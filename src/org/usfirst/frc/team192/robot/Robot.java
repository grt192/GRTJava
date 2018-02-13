package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.fieldMapping.FieldMapperAccelerometer;
import org.usfirst.frc.team192.fieldMapping.FieldMapperNavXAccel;
import org.usfirst.frc.team192.fieldMapping.FieldMapperNavXVel;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.NavXGyro;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	private NavXGyro gyro;
	private FullSwervePID swerve;
	private JoystickInput input;
	private FieldMapperNavXAccel fieldMapperNavXAccel;
	private FieldMapperNavXVel fieldMapperNavXVel;
	private FieldMapperAccelerometer fieldMapperRoborio;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new NavXGyro(SPI.Port.kMXP);
		gyro.resetDisplacement();
		swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);
		fieldMapperNavXAccel = new FieldMapperNavXAccel(gyro);
		fieldMapperNavXVel = new FieldMapperNavXVel(gyro);
		fieldMapperRoborio = new FieldMapperAccelerometer(gyro, new BuiltInAccelerometer());
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
		fieldMapperNavXAccel.reset();
		fieldMapperNavXVel.reset();
		fieldMapperRoborio.reset();
		gyro.resetDisplacement();
	}

	@Override
	public void teleopPeriodic() {
		swerve.updateWithJoystick(input);
		SmartDashboard.putNumber("X Displacement (gyro displacement)", gyro.getDisplacementX());
		SmartDashboard.putNumber("Y Displacement (gyro displacement)", gyro.getDisplacementY());
		SmartDashboard.putNumber("X Displacement (gyro velocity)", fieldMapperNavXVel.getX());
		SmartDashboard.putNumber("Y Displacement (gyro velocity)", fieldMapperNavXVel.getY());
		SmartDashboard.putNumber("X Displacement (gyro acceleration)", fieldMapperNavXAccel.getX());
		SmartDashboard.putNumber("Y Displacement (gyro acceleration)", fieldMapperNavXAccel.getY());
		SmartDashboard.putNumber("X Displacement (roborio)", fieldMapperRoborio.getX());
		SmartDashboard.putNumber("Y Displacement (roborio)", fieldMapperRoborio.getY());
		fieldMapperNavXVel.update();
		fieldMapperNavXAccel.update();
		fieldMapperRoborio.update();
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
