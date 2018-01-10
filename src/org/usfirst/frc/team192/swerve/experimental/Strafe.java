package org.usfirst.frc.team192.swerve.experimental;

import org.usfirst.frc.team192.robot.JoystickInput;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class Strafe {

	private Wheel[] wheels;

	private Mode currentMode;
	
	private double robotWidth;
	private double robotHeight;
	
	private enum Mode {
		STRAFE, ROTATE
	}
	
	public Strafe(double robotWidth, double robotHeight) {
		wheels = new Wheel[4];
		wheels[2] = new Wheel(new TalonSRX(1), new TalonSRX(2), null);// new DigitalInput(2));
		wheels[3] = new Wheel(new TalonSRX(8), new TalonSRX(7), new DigitalInput(3));
		wheels[1] = new Wheel(new TalonSRX(9), new TalonSRX(10), new DigitalInput(0));
		wheels[0] = new Wheel(new TalonSRX(14), new TalonSRX(16), null);// new
		// DigitalInput(1));
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.initialize();
		
		currentMode = Mode.STRAFE;
		this.robotWidth = robotWidth;
		this.robotHeight = robotHeight;
	}

	public void enable() {
		for (Wheel wheel : wheels)
			if (wheel != null)
				wheel.enable();
	}

	public void disable() {
		for (int i = 0; i < wheels.length; i++) {
			if (wheels[i] == null)
				continue;
			wheels[i].disable();
			wheels[i] = wheels[i].copy();
		}
	}
	
	private void changeMode(JoystickInput input) {
		Joystick joystick = input.getJoystick();
		Mode lastMode = currentMode;
		
		if (joystick.getRawButton(2)) {
			currentMode = Mode.STRAFE;
		} else if (joystick.getRawButton(3)) {
			currentMode = Mode.ROTATE;
		}
		
		if (lastMode != currentMode && currentMode == Mode.ROTATE) {
			double robotAngle = Math.atan2(robotHeight, robotWidth);
			wheels[0].setTargetPosition(robotAngle);
			wheels[1].setTargetPosition(Math.PI - robotAngle);
			wheels[2].setTargetPosition(-robotAngle);
			wheels[3].setTargetPosition(robotAngle + Math.PI);
		}
	}

	public void update(JoystickInput input) {
		changeMode(input);
		if (currentMode == Mode.STRAFE) {
			double speed = input.getPolarRadius();
			double angle = input.getPolarAngle();
			for (Wheel wheel : wheels) {
				if (wheel == null)
					continue;
				if (speed > 0.2)
					wheel.setTargetPosition(angle);
				if (speed < 0.2)
					wheel.setDriveSpeed(0.0);
				else
					wheel.setDriveSpeed(speed / 3);
			}
		} else if (currentMode == Mode.ROTATE) {
			double speed = input.getJoystick().getX();
			/*
			wheels[0].setDriveSpeed(speed);
			wheels[1].setDriveSpeed(-speed);
			wheels[2].setDriveSpeed(speed);
			wheels[3].setDriveSpeed(-speed);
			*/
			for (Wheel wheel : wheels) {
				wheel.setDriveSpeed(speed);
			}
		} else {
			System.out.println("something has gone horribly wrong");
		}
	}

	
	
}
