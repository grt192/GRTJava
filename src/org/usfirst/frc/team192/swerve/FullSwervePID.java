package org.usfirst.frc.team192.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FullSwervePID extends FullSwerve implements PIDOutput {

	private PIDController pid;
	private double rotateInput;
	private boolean usePID;

	// for autonomous
	private double angle;
	private double vx;
	private double vy;
	private double rv;

	public FullSwervePID(Gyro gyro) {
		super(gyro);
		double p = SmartDashboard.getNumber("p", 0.015);
		double i = SmartDashboard.getNumber("i", 0.00);
		double d = SmartDashboard.getNumber("d", 0.01);
		double f = SmartDashboard.getNumber("f", 0.01);
		SmartDashboard.putNumber("p", p);
		SmartDashboard.putNumber("i", i);
		SmartDashboard.putNumber("d", d);
		SmartDashboard.putNumber("f", f);

		pid = new PIDController(p, i, d, f, (PIDSource) gyro, this, 0.01);
		pid.setInputRange(0.0, 360.0);
		pid.setContinuous();
		pid.setAbsoluteTolerance(3.0);
		pid.setOutputRange(-1.0, 1.0);
		pid.reset();
	}

	private void updatePID() {
		double p = SmartDashboard.getNumber("p", 0.015);
		double i = SmartDashboard.getNumber("i", 0.0);
		double d = SmartDashboard.getNumber("d", 0.1);
		double f = SmartDashboard.getNumber("f", 0.1);
		pid.setPID(p, i, d, f);
	}

	@Override
	public void enable() {
		super.enable();
		pid.reset();
		pid.enable();
		rotateInput = 0.0;
		holdAngle();
	}

	@Override
	public void disable() {
		super.disable();
		pid.disable();
		rotateInput = 0.0;
	}

	private void updateMovement(double vx, double vy, double rv) {
		updatePID();
		logPID();
		double rotate = usePID ? rotateInput : rv;
		changeMotors(rotate, vx, vy);
	}

	@Override
	public void updateWithJoystick(XboxController input) {
		if (input.getAButtonPressed())
			zeroGyro();
		double y = input.getX(Hand.kRight);
		double x = -input.getY(Hand.kRight);
		if (Math.sqrt(x * x + y * y) > 0.7) {
			usePID = true;
			pid.setSetpoint((Math.toDegrees(Math.atan2(y, x)) + 360.0) % 360.0);
		}
		double rotate = 0.0;
		double lTrigger = input.getTriggerAxis(Hand.kLeft);
		double rTrigger = input.getTriggerAxis(Hand.kRight);
		if (lTrigger + rTrigger > 0.05) {
			rotate += Math.pow(rTrigger, 2);
			rotate -= Math.pow(lTrigger, 2);
			usePID = false;
		} else if (!usePID) {
			holdAngle();
		}
		updateMovement(-input.getY(Hand.kLeft), input.getX(Hand.kLeft), rotate);
	}

	// for autonomous
	public void setVelocity(double vx, double vy) {
		this.vx = vx;
		this.vy = vy;
	}

	public void setTargetPosition(double radians) {
		usePID = true;
		pid.setSetpoint(((Math.toDegrees(radians) % 360) + 360) % 360);
	}

	public void holdAngle() {
		usePID = true;
		pid.setSetpoint(getGyroAngle());
	}

	public void setWithAngularVelocity(double vx, double vy, double rv) {
		this.vx = vx;
		this.vy = vy;
		this.rv = rv;
		usePID = false;
	}

	public void autonomousInit() {
		vx = 0;
		vy = 0;
		rv = 0;
		holdAngle();
	}

	public void updateAutonomous() {
		updateMovement(vx, vy, rv);
	}

	// for pid
	@Override
	public void pidWrite(double output) {
		rotateInput = output;
	}

	private void logPID() {
		SmartDashboard.putNumber("PID Setpoint", pid.getSetpoint());
		SmartDashboard.putNumber("PID Error", pid.getError());
		SmartDashboard.putNumber("PID Output", pid.get());

	}

	// for zeroing
	public void zeroWithInputs(int talonNumber, XboxController xbox) {
		wheels[talonNumber].getRotateMotor().set(ControlMode.PercentOutput, xbox.getX(Hand.kLeft) / 3);
	}

}
