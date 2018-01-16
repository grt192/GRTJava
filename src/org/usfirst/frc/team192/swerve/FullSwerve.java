package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;

public class FullSwerve extends SwerveBase {
	private ADXRS450_Gyro gyro;
	private long lastUpdated;
	
	// for PID loop
	private double integral;
	private double p;
	private double i;
	private double d;
	private double f;
	private double lastError;
	private double lastTargetAngle;
	private final double TOLERANCE = 0.05;

	public FullSwerve(double robotWidth, double robotHeight, ADXRS450_Gyro gyro) {
		super(robotWidth, robotHeight, true);
		this.gyro = gyro;
		this.lastUpdated = System.currentTimeMillis();
		this.integral = 0;
		this.p = 1.0;
		this.i = 0.01;
		this.d = 0.1;
		this.f = 1.0;
		SmartDashboard.putNumber("p", p);
		SmartDashboard.putNumber("i", i);
		SmartDashboard.putNumber("d", d);
		SmartDashboard.putNumber("f", f);
	}
	
	@Override
	public void zero() {
		gyro.calibrate();
		gyro.reset();
		super.zero();
	}
	
	@Override
	public void enable() {
		super.enable();
		p = SmartDashboard.getNumber("p", 3.0);
		i = SmartDashboard.getNumber("i", 0.001);
		d = SmartDashboard.getNumber("d", 0.01);
		f = SmartDashboard.getNumber("f", 1.0);
	}

	private void changeMotors(double rv, double vx, double vy) {
		double currentAngle = Math.toRadians(gyro.getAngle());
		SmartDashboard.putNumber("rv", rv);
		SmartDashboard.putNumber("vx", 0);
		SmartDashboard.putNumber("vy", 0);
		double r = Math.sqrt(robotWidth * robotWidth + robotHeight * robotHeight) / 2;
		double[] driveSpeeds = new double[4];
		double[] targetPositions = new double[4];
		double maxDriveSpeed = 0;
		for (int i = 0; i < 4; i++) {
			double wheelAngle = Math.atan2(robotWidth, robotHeight);
			if (i == 0 || i == 3) {
				wheelAngle *= -1;
			}
			if (i == 2 || i == 3) {
				wheelAngle += Math.PI;
			}
			wheelAngle += currentAngle;
			double dx = r * Math.cos(wheelAngle);
			double dy = r * Math.sin(wheelAngle);
			double actualvx = vx + rv * dy;
			double actualvy = vy - rv * dx;
			double wheelTheta = Math.atan2(actualvy, actualvx);
			double speed = Math.sqrt(actualvx * actualvx + actualvy * actualvy);
			driveSpeeds[i] = speed;
			maxDriveSpeed = Math.max(maxDriveSpeed, Math.abs(speed));
			targetPositions[i] = wheelTheta - currentAngle;
			SmartDashboard.putNumber("target position " + i, wheelTheta - currentAngle);
		}
		if (maxDriveSpeed > 0.1) {
			double driveRatio = Math.min(1, 1 / maxDriveSpeed) / 3; // should only scale down if the wheels should go really fast
			for (int i = 0; i < 4; i++) {
				wheels[i].setDriveSpeed(driveSpeeds[i] * driveRatio);
				wheels[i].setTargetPosition(targetPositions[i]);
				SmartDashboard.putNumber("drive speed " + i, driveSpeeds[i] * driveRatio);
			}
		} else {
			for (int i = 0; i < 4; i++) {
				wheels[i].setDriveSpeed(0);
				SmartDashboard.putNumber("drive speed " + i, 0);
			}
		}
		System.out.println(currentAngle);
	}

	@Override
	public void update(JoystickInput input) {
		XboxController xbox = input.getXboxController();
		if (xbox.getAButton() && xbox.getYButton())
			zero();
		double targetAngle = lastTargetAngle;
		if (Math.sqrt(Math.pow(input.getClippedX(Hand.kRight), 2) + Math.pow(input.getClippedY(Hand.kRight), 2)) > 0.3) {
			targetAngle = (Math.atan2(input.getClippedX(Hand.kRight), -input.getClippedY(Hand.kRight)) + 2 * Math.PI) % (2 * Math.PI);
		}
		double currentAngle = ((Math.toRadians(gyro.getAngle()) % 2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
		double error = targetAngle - currentAngle;
		if (Math.abs(error) > Math.PI) {
			error -= Math.PI * 2 * Math.signum(error);
		}
		SmartDashboard.putNumber("error", error);
		SmartDashboard.putNumber("targetAngle", targetAngle);
		double rv;
		long newTime = System.currentTimeMillis();
		double dt = (newTime - lastUpdated) / 1000.0;
		lastUpdated = newTime;
		if (Math.abs(error) > TOLERANCE) {
			integral += dt * (lastError + error) / 2;
			lastError = error;
			double delta = targetAngle - lastTargetAngle;
			if (Math.abs(delta) > Math.PI) {
				delta -= Math.PI * 2 * Math.signum(error);
			}
			double dTargetAngle = delta / dt;
			rv = (error * p /* - Math.toRadians(gyro.getRate()) * d + integral * i + dTargetAngle * f */) / Math.PI;
		} else {
			rv = 0;
		}
		changeMotors(-rv, -input.getClippedY(Hand.kLeft), input.getClippedX(Hand.kLeft));
	}

}
