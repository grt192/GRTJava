package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FullSwervePID extends FullSwerve implements PIDOutput {

	private PIDController pid;
	private double rotateInput;
	
	// for autonomous
	private double angle;
	private double vx;
	private double vy;

	private static final double BUMPER = 0.4;

	public FullSwervePID(ADXRS450_Gyro gyro) {
		super(gyro);
		// double p = Config.getDouble("swervepid_p");
		// p = 0.02;
		// double i = Config.getDouble("swervepid_i");
		// i = 0.0001;
		// double d = Config.getDouble("swervepid_d");
		// d = 0.5;
		// double f = Config.getDouble("swervepid_f");
		// f = 0.0;
		double p = SmartDashboard.getNumber("p", 0.015);
		double i = SmartDashboard.getNumber("i", 0.00);
		double d = SmartDashboard.getNumber("d", 0.01);
		double f = SmartDashboard.getNumber("f", 0.01);
		SmartDashboard.putNumber("p", p);
		SmartDashboard.putNumber("i", i);
		SmartDashboard.putNumber("d", d);
		SmartDashboard.putNumber("f", f);
		pid = new PIDController(p, i, d, f, gyro, this, 0.01);
		pid.setContinuous();
		pid.setInputRange(0.0, 360.0);
		pid.setAbsoluteTolerance(3.0);
		pid.setOutputRange(-1.0, 1.0);
		pid.reset();
		pid.setSetpoint(0.0);
	}

	@Override
	public void enable() {
		super.enable();
		pid.reset();
		pid.setSetpoint(gyro.getAngle());
		pid.enable();
	}

	private void updatePID() {
		double p = SmartDashboard.getNumber("p", 1.0);
		double i = SmartDashboard.getNumber("i", 0.01);
		double d = SmartDashboard.getNumber("d", 0.1);
		double f = SmartDashboard.getNumber("f", 1.0);
		pid.setPID(p, i, d, f);
	}

	@Override
	public void disable() {
		super.disable();
		pid.disable();
		rotateInput = 0.0;
	}
	
	private void updateMovement(double vx, double vy, double radians, boolean changePID) {
		if (changePID)
			pid.setSetpoint((Math.toDegrees(radians) + 360.0) % 360.0);
		updatePID();
		SmartDashboard.putNumber("PID Setpoint", pid.getSetpoint());
		SmartDashboard.putNumber("PID Error", pid.getError());
		SmartDashboard.putNumber("PID Output", pid.get());
		changeMotors(rotateInput, vx, vy);
	}
	
	private void updateMovement(double vx, double vy, double radians) {
		updateMovement(vx, vy, radians, true);
	}

	@Override
	public void updateTeleop(JoystickInput input) {
		XboxController xbox = input.getXboxController();
		if (xbox.getAButtonPressed() && xbox.getYButtonPressed())
			zero();
		double y = xbox.getX(Hand.kRight);
		double x = -xbox.getY(Hand.kRight);
		double angle = Math.atan2(y, x);
		updateMovement(-input.getClippedY(Hand.kLeft), input.getClippedX(Hand.kLeft), angle, Math.sqrt(x * x + y * y) > 0.7);
	}
	
	// for autonomous
	public void setVelocity(double vx, double vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	public void rotateTo(double radians) {
		angle = radians;
	}
	
	public void rotateBy(double radians) {
		angle = Math.toRadians(gyro.getAngle()) + radians;
	}
	
	public void autonomousInit() {
		vx = 0;
		vy = 0;
		angle = 0;
	}
	
	public void updateAutonomous() {
		updateMovement(vx, vy, angle);
	}
	
	// for pid
	@Override
	public void pidWrite(double output) {
		rotateInput = output;
		SmartDashboard.putNumber("PID Error", pid.getError());
	}

}
