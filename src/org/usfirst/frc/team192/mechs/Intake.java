package org.usfirst.frc.team192.mechs;

import org.usfirst.frc.team192.config.Config;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;

public class Intake {

	private TalonSRX left, right, upper;
	private Solenoid mainSol, leftSol, rightSol;
	public boolean armsExtended;
	public boolean centerExtended;

	public Intake() {
		left = new TalonSRX(Config.getInt("lower_left_flywheel"));
		right = new TalonSRX(Config.getInt("lower_right_flywheel"));
		right.setInverted(true);
		upper = new TalonSRX(Config.getInt("upper_flywheel"));
		mainSol = new Solenoid(Config.getInt("centersol"));
		leftSol = new Solenoid(Config.getInt("leftsol"));
		rightSol = new Solenoid(Config.getInt("rightsol"));

		armsExtended = false;
		centerExtended = false;
	}

	public void spitOut() {
		upper.set(ControlMode.PercentOutput, -1);
		left.set(ControlMode.PercentOutput, -1);
		right.set(ControlMode.PercentOutput, -1);
	}

	public void moveWheels(double speed) {
		upper.set(ControlMode.PercentOutput, speed);
		right.set(ControlMode.PercentOutput, speed);
		left.set(ControlMode.PercentOutput, speed);
	}

	public void movePickupOut() {
		mainSol.set(true);
		centerExtended = true;
		rightSol.set(true);
		leftSol.set(true);
		armsExtended = true;
	}

	public void movePickup() {
		armsExtended = !armsExtended;
		if (armsExtended) {
			mainSol.set(true);
			centerExtended = true;
		}
		rightSol.set(armsExtended);
		leftSol.set(armsExtended);
		System.out.println(armsExtended);
	}

	public void moveCenterPickup() {
		if (!centerExtended) {
			mainSol.set(true);
			centerExtended = true;
		} else {
			leftSol.set(false);
			rightSol.set(false);
			mainSol.set(false);
			centerExtended = false;
			armsExtended = false;
		}
	}

	public void autonPickup() {
		if (!centerExtended) {
			mainSol.set(true);
			centerExtended = true;
		}
		if (!armsExtended) {
			rightSol.set(true);
			leftSol.set(true);
			armsExtended = true;
		}
		upper.set(ControlMode.PercentOutput, 1);
		left.set(ControlMode.PercentOutput, 1);
		right.set(ControlMode.PercentOutput, 1);

	}

	public void autonClamp() {
		if (armsExtended) {
			rightSol.set(false);
			leftSol.set(false);
			armsExtended = false;
		}
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
