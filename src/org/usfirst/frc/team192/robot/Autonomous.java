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
		
		SmartDashboard.putNumber("autonomous_delay_ms", 0.0);
		SmartDashboard.putNumber("autonomous_distance_from_left_edge", 0.0);
		
		modeChooser = new SendableChooser<Mode>();
		Mode[] modes = Mode.values();
		Mode defaultMode = modes[0];
//		modeChooser.addDefault(defaultMode.getDescription(), defaultMode);
		for (int i=0; i<modes.length; i++) {
			modeChooser.addObject(modes[i].getDescription(), modes[i]);
			System.out.println(modes[i].toString());
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
		if (timeElapsed() < delay) {
			return;
		}
		switch (selectedMode) {
		case ONLY_FORWARD:
			if (timeElapsed() < 7000) {
				swerve.setVelocity(0.3, 0.0);
			} else {
				swerve.setVelocity(0.0, 0.0);
			}
			break;
		default:
			break;
		}
		
		swerve.updateAutonomous();
	}
	
	public long timeElapsed() {
		return (System.currentTimeMillis() - startTime);
	}
	
	public long timeLeft() {
		return (15 * 1000) - timeElapsed();
	}
	
}
