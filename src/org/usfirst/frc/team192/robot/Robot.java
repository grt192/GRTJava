package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.fieldMapping.FieldMapperAccelerometer;
import org.usfirst.frc.team192.fieldMapping.FieldMapperEncoder;
import org.usfirst.frc.team192.fieldMapping.FieldMapperNavXAccel;
import org.usfirst.frc.team192.fieldMapping.FieldMapperNavXDisp;
import org.usfirst.frc.team192.fieldMapping.FieldMapperNavXVel;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.NavXGyro;
import org.usfirst.frc.team192.vision.Imshow;
import org.usfirst.frc.team192.vision.nn.RemoteVisionThread;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	private NavXGyro gyro;
	private FullSwervePID swerve;
	private FieldMapperNavXAccel fieldMapperNavXAccel;
	private FieldMapperNavXVel fieldMapperNavXVel;
	private FieldMapperAccelerometer fieldMapperRoborio;
	private FieldMapperNavXDisp fieldMapperNavXDisp;
	private FieldMapperEncoder fieldMapperEncoder;
	
	private double NAVX_X;
	private double NAVX_Y;
	private double ROBORIO_X;
	private double ROBORIO_Y;

	private XboxController input;
	private RemoteVisionThread vision;
	private Autonomous auto;
	private Teleop teleop;
	private Imshow imshow;

	@Override
	public void robotInit() {
		Config.start();
		gyro = new NavXGyro();
		swerve = new FullSwervePID(gyro);
		NAVX_X = Config.getDouble("navx_x");
		NAVX_Y = Config.getDouble("navx_y");
		ROBORIO_X = Config.getDouble("roborio_x");
		ROBORIO_Y = Config.getDouble("roborio_y");
		fieldMapperNavXAccel = new FieldMapperNavXAccel(gyro, NAVX_X, NAVX_Y);
		fieldMapperNavXVel = new FieldMapperNavXVel(gyro, NAVX_X, NAVX_Y);
		fieldMapperRoborio = new FieldMapperAccelerometer(gyro, new BuiltInAccelerometer(), ROBORIO_X, ROBORIO_Y);
		fieldMapperNavXDisp = new FieldMapperNavXDisp(gyro, NAVX_X, NAVX_Y);
		fieldMapperEncoder = new FieldMapperEncoder(gyro, swerve);
		input = new XboxController(0);
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
		fieldMapperNavXAccel.reset();
		fieldMapperNavXVel.reset();
		fieldMapperRoborio.reset();
		fieldMapperNavXDisp.reset();
		fieldMapperEncoder.reset();
		gyro.resetDisplacement();
		teleop.init();
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
