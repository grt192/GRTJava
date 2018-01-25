package org.usfirst.frc.team192.robot;

import javax.xml.ws.Service.Mode;

import org.usfirst.frc.team192.swerve.FullSwervePID;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	
	private DriverStation ds;
//	private Vision vision;
	private FullSwervePID swerve;
	
	private SendableChooser<Mode> modeChooser;
	
	private int dsLocation;
	
	private boolean switchLeft;
	private boolean scaleLeft;
	
	private long startTime;
	private Mode selectedMode;
	private double delay;
	private double distanceFromLeft;
	
	private static double FIELD_EDGE_LENGTH = 670.56; //cm
	
	private enum Mode {
		ONLY_FORWARD ("drive forward only"),
		FORWARD_AND_PLACE_SWITCH ("drive forward only and place in switch (50% chance)"),  //50% chance of right side
		FORWARD_AND_PLACE_SCALE_SIDE ("drive forward and then to the side to place in scale (50% chance)"),
		PLACE_SWITCH_BOTTOM ("move to correct part of bottom switch to place"),
		PLACE_SWITCH_SIDE ("move to correct side of switch to place"),
		PLACE_SCALE_SIDE ("move to correct side of scale to place");
		
		String description;
		
		Mode(String description) {
			this.description = description;
		}
		
		String getDescription() {
			return description;
		}
	}

	public Autonomous(FullSwervePID swerve) {
		this.ds = DriverStation.getInstance();
		this.swerve = swerve;
		
//		SmartDashboard.putNumber("autonomous_delay_ms", 0.0);
//		SmartDashboard.putNumber("autonomous_distance_from_left_edge", 0.0);
		
		modeChooser = new SendableChooser<Mode>();
		Mode[] modes = Mode.values();
		Mode defaultMode = modes[0];
		modeChooser.addDefault(defaultMode.getDescription(), defaultMode);
		for (int i=1; i<modes.length; i++) {
			modeChooser.addObject(modes[i].getDescription(), modes[i]);
//			System.out.println(modes[i].toString());
		}
		
		SmartDashboard.putData("autonomous_mode", modeChooser);
	}
	
	public void init() {
		startTime = System.currentTimeMillis();
		
		//get game information
		dsLocation = ds.getLocation();
		String fieldPositions = ds.getGameSpecificMessage();
		selectedMode = modeChooser.getSelected();
		System.out.println(selectedMode.toString());
		
		delay = SmartDashboard.getNumber("autonomous_delay_ms", 0.0);
		distanceFromLeft = SmartDashboard.getNumber("autonomous_distance_from_left_edge", 0.0);
		
		switchLeft = (fieldPositions.charAt(0) == 'L');
		scaleLeft = (fieldPositions.charAt(1) == 'L');
		
		//
		swerve.autonomousInit();
	}
	
	public void periodic() {
		double timeElapsed = timeElapsed();
		double timeLeft = timeLeft();
		if (timeElapsed < delay) {
			System.out.println("waiting " + timeElapsed);
			return;
		}
		switch (selectedMode) {
		case ONLY_FORWARD:
			if (timeAfterDelay() < 4000) {
				System.out.println("driving");
				swerve.setVelocity(1.0, 0.0);
			} else {
				System.out.println("not driving");
				swerve.setVelocity(0.0, 0.0);
			}
			break;
		default:
			break;
		}
		
		swerve.updateAutonomous();
	}
	
	//time since beginning of autonomous
	public double timeElapsed() {
		return System.currentTimeMillis() - startTime;
	}
	
	//time since start of movement/since end of delay (may not last for 15 seconds if delayed)
	public double timeAfterDelay() {
		return timeElapsed() - delay;
	}
	
	//time until autonomous 15 seconds ends
	public double timeLeft() {
		return (15 * 1000) - timeElapsed();
	}
	
}
