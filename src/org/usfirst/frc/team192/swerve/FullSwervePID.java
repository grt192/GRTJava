package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.XboxController;
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

	public FullSwervePID(GyroBase gyro) {
		super(gyro);
		double p = SmartDashboard.getNumber("p", 0.015);
		double i = SmartDashboard.getNumber("i", 0.00);
		double d = SmartDashboard.getNumber("d", 0.01);
		double f = SmartDashboard.getNumber("f", 0.01);
		SmartDashboard.putNumber("p", p);
		SmartDashboard.putNumber("i", i);
		SmartDashboard.putNumber("d", d);
		SmartDashboard.putNumber("f", f);
		pid = new PIDController(p, i, d, f, gyro, this, 0.01);
		//pid.setContinuous();
		pid.setInputRange(0.0, 360.0);
		pid.setAbsoluteTolerance(3.0);
		pid.setOutputRange(-1.0, 1.0);
		pid.reset();
		pid.setSetpoint(0.0);
		usePID = false;
	}

	@Override
	public void enable() {
		super.enable();
		pid.reset();
		pid.enable();
		usePID = false;
	}

	private void updatePID() {
		double p = SmartDashboard.getNumber("p", 0.015);
		double i = SmartDashboard.getNumber("i", 0.0);
		double d = SmartDashboard.getNumber("d", 0.1);
		double f = SmartDashboard.getNumber("f", 0.1);
		pid.setPID(p, i, d, f);
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
	public void updateWithJoystick(JoystickInput input) {
		XboxController xbox = input.getXboxController();
		if (xbox.getAButtonPressed() && xbox.getYButtonPressed())
			zero();
		double y = xbox.getX(Hand.kRight);
		double x = -xbox.getY(Hand.kRight);
		if (Math.sqrt(x * x + y * y) > 0.7) {
			usePID = true;
			pid.setSetpoint((Math.toDegrees(Math.atan2(y, x)) + 360.0) % 360.0);
		}
		double rotate = 0.0;
		double lTrigger = xbox.getTriggerAxis(Hand.kLeft);
		double rTrigger = xbox.getTriggerAxis(Hand.kRight);
		if (lTrigger + rTrigger > 0.05)
			usePID = false;
		if (!usePID) {
			rotate += Math.pow(rTrigger, 2);
			rotate -= Math.pow(lTrigger, 2);
		}
		updateMovement(-input.getClippedY(Hand.kLeft), input.getClippedX(Hand.kLeft), rotate);
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
	}

	public void updateAutonomous() {
		updateMovement(vx, vy, rv);
	}

	// for pid
	@Override
	public void pidWrite(double output) {
		rotateInput = output;
		SmartDashboard.putNumber("PID Error", pid.getError());
	}

	private void logPID() {
		SmartDashboard.putNumber("PID Setpoint", pid.getSetpoint());
		SmartDashboard.putNumber("PID Error", pid.getError());
		SmartDashboard.putNumber("PID Output", pid.get());

	}

}
