package org.usfirst.frc.team192.robot;

import org.opencv.core.Point;
import org.usfirst.frc.team192.fieldMapping.FieldMapper;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.nn.RemoteVisionThread;

public class VisionSwerve {

	private RemoteVisionThread vision;
	private FullSwervePID swerve;
	private Intake intake;
	private boolean blockFound;
	private double targetAngle;

	private Point box;
	private FieldMapper mapper;
	private double xStart, yStart;
	private long startTime;

	public VisionSwerve(FullSwervePID swerve, FieldMapper mapper, Intake intake) {
		this.swerve = swerve;
		this.intake = intake;
		this.mapper = mapper;
		vision = new RemoteVisionThread(320, 240);
		blockFound = false;
	}

	public void start() {
		System.out.println("Starting vision");
		startTime = System.currentTimeMillis();
		vision.start();
		swerve.setVelocity(0, 0);
		swerve.holdAngle();
	}

	public void kill() {
		blockFound = false;
		vision.kill();
		System.out.println("Stopping vision");
	}

	public boolean update() {
		if (!blockFound) {
			if (vision.hasData()) {
				if (!vision.hasTarget()) {
					System.out.println("No block detected");
					return false;
				}
				blockFound = true;
				beginNavigation();
			} else if (System.currentTimeMillis() - startTime > 2000) {
				System.out.println("Vision timed out");
				kill();
				return false;
			}
		} else {
			Point pos = getDisplacement();
			double dx = box.x - pos.x;
			double dy = box.y - pos.y;
			double vx = 0.0;
			double vy = Math.max(Math.min(dy * 0.4, 0.4), -0.4);
			if (Math.abs(dy) < 0.03) {
				// intake.movePickupOut();
				// intake.moveWheels(1.0);
				vx = 0.2;
			}
			if (dx < 0.165) {
				vx = 0.0;
				vy = 0.0;
				System.out.println("Got block!");
				// intake.autonClamp();
				// intake.moveWheels(1.0);
			}
			System.out.println(dx + ", " + dy + "; " + vx + ", " + vy);
			double angle = targetAngle;
			double trueVX = vx * Math.cos(angle) - vy * Math.sin(angle);
			double trueVY = vx * Math.sin(angle) + vy * Math.cos(angle);
			swerve.setVelocity(trueVX, trueVY);
		}
		swerve.updateAutonomous();
		return true;
	}

	private void beginNavigation() {
		double initialAngle = Math.toRadians(swerve.getGyroAngle());
		xStart = mapper.getX();
		yStart = mapper.getY();
		double angle = vision.getAngle();
		double posAngle, negAngle;
		box = vision.getPoint();
		if (angle > 0) {
			posAngle = angle;
			negAngle = angle - Math.PI / 2;
		} else {
			negAngle = angle;
			posAngle = angle + Math.PI / 2;
		}
		if (box.y > 0)
			angle = posAngle;
		else
			angle = negAngle;
		targetAngle = initialAngle + angle;
		swerve.setTargetPosition(targetAngle);

		angle *= -1;
		double x = box.x;
		double y = box.y;
		box.x = x * Math.cos(angle) - y * Math.sin(angle) + 0.165;
		box.y = x * Math.sin(angle) + y * Math.cos(angle) + 0.165 * Math.signum(angle);
		System.out.println("Box found at " + box);
	}

	private Point getDisplacement() {
		double dx = mapper.getX() - xStart;
		double dy = mapper.getY() - yStart;
		Point p = new Point();
		double angle = -targetAngle;
		p.x = dx * Math.cos(angle) - dy * Math.sin(angle);
		p.y = dx * Math.sin(angle) + dy * Math.cos(angle);
		return p;
	}

}
