package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.fieldMapping.FieldMapperAccelerometer;
import org.usfirst.frc.team192.fieldMapping.FieldMapperEncoder;
import org.usfirst.frc.team192.fieldMapping.FieldMapperNavXAccel;
import org.usfirst.frc.team192.fieldMapping.FieldMapperNavXDisp;
import org.usfirst.frc.team192.fieldMapping.FieldMapperNavXVel;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.NavXGyro;
import org.usfirst.frc.team192.vision.VisionThread;
import org.usfirst.frc.team192.vision.nn.RemoteVisionThread;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Robot extends IterativeRobot {

	private NavXGyro gyro;
	private FullSwervePID swerve;
	private JoystickInput input;
	private FieldMapperNavXAccel fieldMapperNavXAccel;
	private FieldMapperNavXVel fieldMapperNavXVel;
	private FieldMapperAccelerometer fieldMapperRoborio;
	private FieldMapperNavXDisp fieldMapperNavXDisp;
	private FieldMapperEncoder fieldMapperEncoder;
	
	private double NAVX_X = 0;
	private double NAVX_Y = 0;

	private VisionThread vision;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new NavXGyro();
		swerve = new FullSwervePID(gyro);
		input = new JoystickInput(0, 1);
		fieldMapperNavXAccel = new FieldMapperNavXAccel(gyro, NAVX_X, NAVX_Y);
		fieldMapperNavXVel = new FieldMapperNavXVel(gyro, NAVX_X, NAVX_Y);
		fieldMapperRoborio = new FieldMapperAccelerometer(gyro, new BuiltInAccelerometer(), NAVX_X, NAVX_Y);
		fieldMapperNavXDisp = new FieldMapperNavXDisp(gyro, NAVX_X, NAVX_Y);
		fieldMapperEncoder = new FieldMapperEncoder(gyro, swerve);
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
		fieldMapperNavXDisp.reset();
		fieldMapperEncoder.reset();
		gyro.resetDisplacement();
	}

	@Override
	public void teleopPeriodic() {
		swerve.updateWithJoystick(input);
		SmartDashboard.putNumber("X Displacement (gyro displacement)", fieldMapperNavXDisp.getX());
		SmartDashboard.putNumber("Y Displacement (gyro displacement)", fieldMapperNavXDisp.getY());
		SmartDashboard.putNumber("X Displacement (gyro velocity)", fieldMapperNavXVel.getX());
		SmartDashboard.putNumber("Y Displacement (gyro velocity)", fieldMapperNavXVel.getY());
		SmartDashboard.putNumber("X Displacement (gyro acceleration)", fieldMapperNavXAccel.getX());
		SmartDashboard.putNumber("Y Displacement (gyro acceleration)", fieldMapperNavXAccel.getY());
		SmartDashboard.putNumber("X Displacement (roborio)", fieldMapperRoborio.getX());
		SmartDashboard.putNumber("Y Displacement (roborio)", fieldMapperRoborio.getY());
		SmartDashboard.putNumber("X Displacement (encoders)", fieldMapperEncoder.getX());
		SmartDashboard.putNumber("Y Displacement (encoders)", fieldMapperEncoder.getY());
		fieldMapperNavXVel.update();
		fieldMapperNavXAccel.update();
		fieldMapperRoborio.update();
		fieldMapperNavXDisp.update();
		fieldMapperEncoder.update();
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
