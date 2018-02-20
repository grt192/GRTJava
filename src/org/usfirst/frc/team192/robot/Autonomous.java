package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {

	private DriverStation ds;
	// private Vision vision;
	private FullSwervePID swerve;

	private SendableChooser<Mode> modeChooser;

	private int dsLocation;

	private boolean switchLeft;
	private boolean scaleLeft;

	private long startTime;
	private Mode selectedMode;
	private double delay;
	private double distanceFromLeft;
	private boolean startLeft;
	
	private double switchCenterToLeft;
	
	private Double start = null; //for driving distance
	
	private final double robotWidth;
	private final double robotHeight;
	
	private Elevator elevator;
	private Intake intake;
	
	private static double FIELD_LENGTH = 324; //inches
	private static double WALL_TO_SWITCH = 103.3; //y distance from left side of leftmost driver station to center of left switch plate
	
	private static double METERS_TO_INCHES = 39.37;
	
	private enum Mode {
		ONLY_FORWARD_TIME ("drive forward for 4 seconds"),
		ONLY_FORWARD_ENCODERS ("drive forward based on drive encoders"),
		FORWARD_AND_PLACE_SWITCH ("drive forward only and place in switch (50% chance)"),  //50% chance of right side
		FORWARD_AND_PLACE_SWITCH_SIDE ("drive forward and then to the side and place in switch (50% chance)"),  //50% chance of right side
		FORWARD_AND_PLACE_SCALE_SIDE ("drive forward and then to the side to place in scale (50% chance)"),
		PLACE_SWITCH_BOTTOM ("move to correct part of bottom of switch to place"),
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

	public Autonomous(FullSwervePID swerve, Intake intake, Elevator elevator) {
		this.ds = DriverStation.getInstance();
		this.swerve = swerve;
		this.elevator = elevator;
		this.intake = intake;
		
		this.robotWidth = Config.getDouble("robot_width") * METERS_TO_INCHES;
		this.robotHeight = Config.getDouble("robot_height") * METERS_TO_INCHES;
		
//		SmartDashboard.putNumber("autonomous_delay_ms", 1.0);
//		SmartDashboard.putNumber("autonomous_distance_from_left_edge_inches", FIELD_LENGTH/2 - robotWidth/2);
		
		modeChooser = new SendableChooser<Mode>();
		Mode[] modes = Mode.values();
		Mode defaultMode = modes[0];
		modeChooser.addDefault(defaultMode.getDescription(), defaultMode);
		for (int i = 1; i < modes.length; i++) {
			modeChooser.addObject(modes[i].getDescription(), modes[i]);
			// System.out.println(modes[i].toString());
		}

		SmartDashboard.putData("autonomous_mode", modeChooser);
	}

	public void init() {
		startTime = System.currentTimeMillis();

		// get game information
		dsLocation = ds.getLocation();
		String fieldPositions = ds.getGameSpecificMessage();
		selectedMode = modeChooser.getSelected();
		System.out.println(selectedMode.toString());

		delay = SmartDashboard.getNumber("autonomous_delay_ms", 0.0);

		distanceFromLeft = 30 + SmartDashboard.getNumber("autonomous_distance_from_left_edge_inches", FIELD_LENGTH/2 - robotWidth/2);
		
		switchLeft = (fieldPositions.charAt(0) == 'L');
		scaleLeft = (fieldPositions.charAt(1) == 'L');
		
		startLeft = distanceFromLeft < FIELD_LENGTH/2;
		
		//
		swerve.enable();
		swerve.autonomousInit();
		swerve.setTargetPosition(0);
	}
  
	public void periodic() { //~5 times / second
		double timeElapsed = timeElapsed();
		double timeLeft = timeLeft();
		
		if (timeElapsed > 20 * 1000) {
			return;
		}
		
		if (timeElapsed < delay) {
//			System.out.println("waiting " + timeElapsed);
			return;
		}
		double timeAfterDelay = timeAfterDelay();
		
		switch (selectedMode) {
		case ONLY_FORWARD_TIME:
			if (timeAfterDelay < 4000) {
//				System.out.println("driving");
				swerve.setVelocity(0.5, 0.0);
			} else {
//				System.out.println("not driving");
				swerve.setVelocity(0.0, 0.0);
			}
			break;
		case ONLY_FORWARD_ENCODERS:
			if (start == null) {
				start = getEncoderValue();
			}
			double traveled = getEncoderValue() - start;
			if (traveled < 110) {
				swerve.setVelocity(0.5, 0.0);
			} else if (traveled < 120) { //start to auto line x axis
				swerve.setVelocity(0.15, 0.0);
			} else {
				swerve.setVelocity(0.0, 0.0);
			}
		case FORWARD_AND_PLACE_SWITCH:
			if (start == null) {
				start = getEncoderValue();
			}
			double traveled2 = getEncoderValue() - start;
			if (traveled2 < 160) {
				swerve.setVelocity(0.5, 0.0);
			} else if (traveled2 < 166 - robotHeight) { //start to middle of switch x axis
				swerve.setVelocity(0.15, 0.0);
			} else {
				double leftToSwitch = WALL_TO_SWITCH - robotWidth/2;
				if (!switchLeft) {
					leftToSwitch = FIELD_LENGTH - leftToSwitch;
				}
				swerve.setVelocity(0.0, 0.0);
				if (startLeft == switchLeft) {
					if (timeAfterDelay < 5000) {
						//place block
						//move elevator back
						System.out.println("place block");
					}
				}
			}
			break;
		case FORWARD_AND_PLACE_SWITCH_SIDE:
			if (timeAfterDelay < 4000) {
				swerve.setVelocity(0.5, 0.0);
			} else if (timeAfterDelay < 4050) {
				swerve.setVelocity(0.0, 0.0);
			} else if (startLeft == switchLeft) {
				if (timeAfterDelay < 5000) {
					double theta = switchLeft ? Math.PI/2 : -Math.PI/2;
					swerve.setTargetPosition(theta);
				} else if (timeAfterDelay < 6500) {
					swerve.setVelocity(0.0, switchLeft ? 1.0 : -1.0);
				} else if (timeAfterDelay < 8000) {
					swerve.setVelocity(0.0, 0.0);
					//place block
					//move elevator back
					System.out.println("place block");
				}
			}
			break;
		case FORWARD_AND_PLACE_SCALE_SIDE:
			if (timeAfterDelay < 7000) {
				swerve.setVelocity(0.5, 0.0);
			} else if (timeAfterDelay < 7050) {
				swerve.setVelocity(0.0, 0.0);
			} else if (startLeft == scaleLeft) {
				if (timeAfterDelay < 8000) {
					double theta = scaleLeft ? Math.PI/2 : -Math.PI/2;
					swerve.setTargetPosition(theta);
				} else if (timeAfterDelay < 9500) {
					swerve.setVelocity(0.0, switchLeft ? 1.0 : -1.0);
				} else if (timeAfterDelay < 11000) {
					swerve.setVelocity(0.0, 0.0);
					//place block
					//move elevator back
					System.out.println("place block");
				}
			}
			break;
		case PLACE_SWITCH_BOTTOM:
			int direction = switchLeft ? -1 : 1; //move to the left if switch is on the left
			double switchPosition = WALL_TO_SWITCH - robotWidth;
			if (!switchLeft) {
				switchPosition = FIELD_LENGTH - switchPosition;
			}
			double timeForY = timeForDistance(distanceFromLeft - switchPosition);
			if (timeAfterDelay < timeForY) {
				swerve.setVelocity(0.0, direction);
			}
			if (timeAfterDelay < timeForY + 500) {
				swerve.setVelocity(0.0, 0.0);
			}
			if (timeAfterDelay < timeForY + 3500) {
				swerve.setVelocity(0.5, 0.0);
			}
			if (timeAfterDelay < timeForY + 4000) {
				swerve.setVelocity(0.0, 0.0);
				//place block
				//move elevator back
				System.out.println("place block");
			}
//		case PLACE_SWITCH_SIDE:
//			int direction = switchLeft ? -1 : 1; //move to the left if switch is on the left
//			double timeForY = timeForDistance(WALL_TO_SWITCH - robotWidth);
//			if (timeAfterDelay < timeForY) {
//				swerve.setVelocity(0.0, direction);
//			}
//			if (timeAfterDelay < timeForY + 500) {
//				swerve.setVelocity(0.0, 0.0);
//			}
//			if (timeAfterDelay < timeForY + 3500) {
//				swerve.setVelocity(0.5, 0.0);
//			}
//			if (timeAfterDelay < timeForY + 4000) {
//				swerve.setVelocity(0.0, 0.0);
//				//place block
//				//move elevator back
//			}
		default:
			break;
		}

		swerve.updateAutonomous();
	}
	
	public double timeForDistance(double inches) {
		return Math.abs(inches*20);
	}
	
	//time since beginning of autonomous
	public double timeElapsed() {
		return System.currentTimeMillis() - startTime;
	}

	// time since start of movement/since end of delay (may not last for 15 seconds
	// if delayed)
	public double timeAfterDelay() {
		return timeElapsed() - delay;
	}

	// time until autonomous 15 seconds ends
	public double timeLeft() {
		return (15 * 1000) - timeElapsed();
	}
	
	public double getEncoderValue() {
		return swerve.getMaxDistanceTraveled() * METERS_TO_INCHES;
	}

}
