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

	public FullSwervePID(GyroBase gyro) {
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
		pid.setSetpoint(getGyroAngle());
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

	@Override
	public void update(JoystickInput input) {
		XboxController xbox = input.getXboxController();
		if (xbox.getAButtonPressed() && xbox.getYButtonPressed())
			zero();
		double y = xbox.getX(Hand.kRight);
		double x = -xbox.getY(Hand.kRight);
		if (Math.sqrt(x * x + y * y) > 0.7) {
			usePID = true;
			pid.setSetpoint((Math.toDegrees(Math.atan2(y, x)) + 360.0) % 360.0);
		}
		updatePID();
		SmartDashboard.putNumber("PID Setpoint", pid.getSetpoint());
		SmartDashboard.putNumber("PID Output", pid.get());
		double rotate = 0.0;
		double lTrigger = xbox.getTriggerAxis(Hand.kLeft);
		double rTrigger = xbox.getTriggerAxis(Hand.kRight);
		if (lTrigger + rTrigger > 0.05)
			usePID = false;
		if (usePID) {
			rotate = rotateInput;
		} else {
			rotate += Math.pow(rTrigger, 2);
			rotate -= Math.pow(lTrigger, 2);
		}

		// double[] array = new double[] { -1.0, rotate, 1.0 };
		// Arrays.sort(array);
		// rotate = array[1];
		rotate = Math.min(Math.max(-1.0, rotate), 1.0);
		changeMotors(rotate, -input.getClippedY(Hand.kLeft), input.getClippedX(Hand.kLeft));
	}

	@Override
	public void pidWrite(double output) {
		rotateInput = output;
		SmartDashboard.putNumber("PID Error", pid.getError());
	}

}
