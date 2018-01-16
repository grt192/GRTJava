package org.usfirst.frc.team192.swerve;

import org.usfirst.frc.team192.robot.JoystickInput;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;

public class FullSwerveAbsolute extends FullSwerve {
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

	public FullSwerveAbsolute(double robotWidth, double robotHeight, ADXRS450_Gyro gyro) {
		super(robotWidth, robotHeight, gyro);
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
	public void enable() {
		super.enable();
		p = SmartDashboard.getNumber("p", 3.0);
		i = SmartDashboard.getNumber("i", 0.001);
		d = SmartDashboard.getNumber("d", 0.01);
		f = SmartDashboard.getNumber("f", 1.0);
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
