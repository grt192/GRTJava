package org.usfirst.frc.team192.mechs;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.hal.PowerJNI;

public class Intake {

	private TalonSRX left, right, upper;
	private Solenoid mainSol, leftSol, rightSol;

	public Intake() {
		left = new TalonSRX(Config.getInt("lower_left_flywheel"));
		right = new TalonSRX(Config.getInt("lower_right_flywheel"));
		right.setInverted(true);
		upper = new TalonSRX(Config.getInt("upper_flywheel"));
		mainSol = new Solenoid(Config.getInt("centersol"));
		leftSol = new Solenoid(Config.getInt("leftsol"));
		rightSol = new Solenoid(Config.getInt("rightsol"));
	}

	public void spitOut() {
		upper.set(ControlMode.PercentOutput, -1);
		left.set(ControlMode.PercentOutput, -1);
		right.set(ControlMode.PercentOutput, -1);
	}

	public void moveWheels(double speed) {
		if (PowerJNI.getVinVoltage() < 11 || Robot.timeSinceLastBrownout() < 500)
			speed = 0;
		upper.set(ControlMode.PercentOutput, speed);
		right.set(ControlMode.PercentOutput, speed);
		left.set(ControlMode.PercentOutput, speed);
	}

	public void movePickupOut() {
		mainSol.set(true);
		rightSol.set(true);
		leftSol.set(true);
	}

	public void movePickup() {
		boolean armsExtended = !rightSol.get();
		if (armsExtended) {
			mainSol.set(true);
		}
		rightSol.set(armsExtended);
		leftSol.set(armsExtended);
	}

	public void moveCenterPickup() {
		if (!mainSol.get()) {
			mainSol.set(true);
		} else {
			leftSol.set(false);
			rightSol.set(false);
			mainSol.set(false);
		}
	}

	public void autonPickup() {
		if (!mainSol.get()) {
			mainSol.set(true);
		}
		if (!rightSol.get()) {
			rightSol.set(true);
			leftSol.set(true);
		}
		upper.set(ControlMode.PercentOutput, 1);
		left.set(ControlMode.PercentOutput, 1);
		right.set(ControlMode.PercentOutput, 1);

	}

	public void autonClamp() {
		rightSol.set(false);
		leftSol.set(false);
	}

	public void stopAutonIntake() {
		upper.set(ControlMode.PercentOutput, 0);
		left.set(ControlMode.PercentOutput, 0);
		right.set(ControlMode.PercentOutput, 0);
	}

	public void autonrelease() {
		upper.set(ControlMode.PercentOutput, -.75);
		left.set(ControlMode.PercentOutput, -.75);
		right.set(ControlMode.PercentOutput, -.75);
	}

}
