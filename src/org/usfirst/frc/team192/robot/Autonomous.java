package org.usfirst.frc.team192.robot;

import java.util.Map;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.fieldMapping.FieldMapperEncoder;
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

	private boolean switchLeft;
	private boolean scaleLeft;

	private long startTime;
	private Mode selectedMode;
	private double delay;

	private Double start = null; // for driving distance
	private int step = 0;
	private Double stepTime = null; // for keeping track of time since the end of previous step

	private final double robotWidth;
	private final double robotHeight;

	private Elevator elevator;
	private Intake intake;
	private FieldMapperEncoder fieldMapping;

	private static double FIELD_LENGTH = 324; // inches
	private static double METERS_TO_INCHES = 39.37;

	private enum Mode {
		ONLY_FORWARD_TIME("drive forward for 4 seconds"),
		ONLY_FORWARD_ENCODERS("drive forward based on drive encoders"),
		ANGLED_AND_PLACE_SWITCH_ENCODERS("move at an angle to the correct side of the switch with drive encoder data"),
		PLACE_SCALE("do the switch thing but for the scale");
		
		String description;
		Mode(String description) {
			this.description = description;
		}
		String getDescription() {
			return description;
		}
	}
	
	private Map<String, Mode> modes;

	public Autonomous(FullSwervePID swerve, Intake intake, Elevator elevator, FieldMapperEncoder fieldMapping) {
		this.ds = DriverStation.getInstance();
		this.swerve = swerve;
		this.elevator = elevator;
		this.intake = intake;
		this.fieldMapping = fieldMapping;
		this.robotWidth = Config.getDouble("robot_width") * METERS_TO_INCHES;
		this.robotHeight = Config.getDouble("robot_height") * METERS_TO_INCHES;
		
		modes.put("only_forward_time", Mode.ONLY_FORWARD_TIME);
		modes.put("only_forward_encoders", Mode.ONLY_FORWARD_ENCODERS);
		modes.put("angled_and_place_switch_encoders", Mode.ANGLED_AND_PLACE_SWITCH_ENCODERS);
		String modesString = getModesString();	
		
		/*
		modeChooser = new SendableChooser<>();
		Mode[] modes = Mode.values();
		Mode defaultMode = modes[0];
		modeChooser.addDefault(defaultMode.getDescription(), defaultMode);
		for (int i = 1; i < modes.length; i++) {
			modeChooser.addObject(modes[i].getDescription(), modes[i]);
			// System.out.println(modes[i].toString());
		}

		SmartDashboard.putData("autonomous_mode", modeChooser);
		*/
		
		SmartDashboard.putString("autonomous_mode", "only_forward_time");
		SmartDashboard.putString("available modes", modesString);
	}
	
	private String getModesString() {
		String result = "";
		for (Map.Entry<String, Mode> modeEntry : modes.entrySet()) {
			result += modeEntry.getKey();
			result += ", ";
		}
		return result;
	}

	public void init() {
		startTime = System.currentTimeMillis();

		// get game information
		String fieldPositions = ds.getGameSpecificMessage();
		// selectedMode = modeChooser.getSelected();
		selectedMode = modes.get(SmartDashboard.getString("autonomous_mode", "only_forward_time"));
//		selectedMode = Mode.ONLY_FORWARD_ENCODERS;
		System.out.println(selectedMode.toString());

		delay = SmartDashboard.getNumber("autonomous_delay_ms", 0.0);
		switchLeft = (fieldPositions.charAt(0) == 'L');
		scaleLeft = (fieldPositions.charAt(1) == 'L');
		swerve.autonomousInit();
		swerve.holdAngle();
	}

	public void periodic() { // ~5 times / second
		double timeElapsed = timeElapsed();

		if (timeElapsed > 20 * 1000) {
			return;
		}

		if (timeElapsed < delay) {
			// System.out.println("waiting " + timeElapsed);
			return;
		}
		double timeAfterDelay = timeAfterDelay();

		switch (selectedMode) {
		case ONLY_FORWARD_TIME:
			if (timeAfterDelay < 3000) {
				System.out.println("driving");
				swerve.setVelocity(0.5, 0.0);
			} else if (timeAfterDelay < 3050) {
				System.out.println("not driving");
				swerve.setVelocity(0, 0);
			}
			break;
		case ONLY_FORWARD_ENCODERS:
			if (start == null) {
				start = getRobotDisplacementX();
			}
			double traveled = getRobotDisplacementX() - start;
			System.out.println(traveled);
			if (traveled < 110) {
				swerve.setVelocity(0.5, 0.0);
			} else if (traveled < 120) { // start to auto line x axis
				swerve.setVelocity(0.15, 0.0);
			} else {
				swerve.setVelocity(0.0, 0.0);
			}
			break;
		case ANGLED_AND_PLACE_SWITCH_ENCODERS:
		    double xTarget = 140 - robotHeight;
			double yTarget = switchLeft ? -59 : 50;
			if (step < 1) {
				if (moveToTargetPosition(xTarget, yTarget, 0.5) < 30) {
					step = 1;
					stepTime = timeAfterDelay;
					swerve.setVelocity(0.0, 0.0);
				}
			} else {
				double timeSinceSwerve = timeAfterDelay - stepTime;
				System.out.println(timeSinceSwerve);
				if (timeSinceSwerve < 1000) {
					intake.moveWheels(-1.0);
					System.out.println("releasing");
				} else {
					intake.moveWheels(0.0);
					System.out.println("stopping");
				}
			}
			break;
		case PLACE_SCALE:
			double xTarget2 =  235.0 - robotHeight;
			if (step < 1) {
				if (moveToTargetPosition(xTarget2, 0, 0.5) < 30) {
					step = 1;
					stepTime = timeAfterDelay;
					swerve.setVelocity(0.0, 0.0);
				}
			} else if (step < 2) {
				swerve.setTargetPosition(scaleLeft ? Math.PI / 2 :  -Math.PI / 2);
				if (scaleLeft) {
					step++;
					break;
				}			
				if (moveToTargetPosition(xTarget2, 264, 0.5) < 30) {
					step++;
					swerve.setVelocity(0.0, 0.0);
				}				
			} else if (step < 3) {
				if (moveToTargetPosition(xTarget2 + 70, scaleLeft? 0 : 264, 0.5) < 30) {
					step++;
					stepTime = timeAfterDelay;
					swerve.setVelocity(0.0, 0.0);
					elevator.setElevatorPosition(0);
				}
			} else if (step < 4) {
				double targetElevatorPosition = 10.0;
				if (getElevatorPosition() - targetElevatorPosition > 10) {
					elevator.
				}
			}
			break;
			
			
		}
			
			
		swerve.updateAutonomous();
	}
	
	public double moveToTargetPosition(double xTargetDisplacement, double yTargetDisplacement, double speed) {
		double xDisplacement = getRobotDisplacementX();
		double yDisplacement = getRobotDisplacementY();
		
		double xDistance = xTargetDisplacement - xDisplacement;
		double yDistance = yTargetDisplacement - yDisplacement;
		
		System.out.println("pos:"+xDisplacement+","+yDisplacement);
		
		double magnitudeNormal = Math.sqrt(xDistance*xDistance + yDistance*yDistance);
		double magnitudeX = speed * xDistance / magnitudeNormal;
		double magnitudeY = speed * yDistance / magnitudeNormal;
		
		swerve.setVelocity(magnitudeX, magnitudeY);
		
		System.out.println("normal "+magnitudeNormal);
		return magnitudeNormal;
	}

	public double timeForDistance(double inches) {
		return Math.abs(inches * 20);
	}

	// time since beginning of autonomous
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

	public double getRobotDisplacementX() {
		return fieldMapping.getX() * METERS_TO_INCHES;
	}

	public double getRobotDisplacementY() {
		return fieldMapping.getY() * METERS_TO_INCHES;
	}
	
	public double getElevatorPosition() {
		return elevator.getElevatorPosition() * 1.0;
	}

}
