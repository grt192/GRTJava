package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.mechs.Climber;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.nn.RemoteVisionThread;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Teleop {
	private XboxController xbox;
	private Elevator elevator;
	private Climber climber;
	private Intake intake;
	private RemoteVisionThread vision;

	DigitalInput innerLimitSwitch;
	DigitalInput outerLimitSwitch;

	private FullSwervePID swerve;

	public Teleop(FullSwervePID swerve, Intake intake, Elevator elevator, Gyro gyro) {
		xbox = new XboxController(1);
		// climber = new Climber());
		// 1 is the gear, 0 is main, 2 is the right
		this.swerve = swerve;
		// CAMCENTER.x = 320;
		// CAMCENTER.y = 240;
		this.intake = intake;
		this.elevator = elevator;
		init();

		// this.zeroing = false;
	}

	public void init() {
		// 0 and 1 are digital input ports
		// eencoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	}

	public void periodic() {
		if (xbox.getXButtonPressed()) {
			intake.moveCenterPickup(xbox);
		}
		if (xbox.getBumperPressed(Hand.kRight)) {
			intake.movePickup();
		}
		if (xbox.getBumperPressed(Hand.kLeft)) {
			elevator.breakElevator();
		}

		// right trigger
		intake.moveWheels(xbox);

		// elevator code
		// right xbox axis
		elevator.manualControl(xbox);
		if (xbox.getY() < -.1) {
			intake.movePickupOut();
		}
	}
}
