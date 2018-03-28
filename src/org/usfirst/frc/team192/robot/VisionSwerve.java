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
	private long startTime, endTime;

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
		blockFound = false;
		endTime = -1;
		vision.start();
		swerve.setWithAngularVelocity(0.0, 0.0, 0.0);
		swerve.updateAutonomous();
	}

	public void kill() {
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
			} else if (System.currentTimeMillis() - startTime > 20000) {
				System.out.println("Vision timed out");
				kill();
				return false;
			}
		} else {
			Point pos = getDisplacement();
			double dx = box.x - pos.x;
			double dy = box.y - pos.y;
			double vy = Math.max(Math.min(dy * 5, 0.75), -0.75);
			double vx = Math.max(0.0, 1.0 - Math.abs(dy));
			vx *= vx * 2;
			if (dx < 1.0)
				vx *= Math.max(0, dx - 12 * dy);
			if (Math.abs(dy) < 0.4) {
				intake.movePickupOut();
			}
			if (Math.abs(dy) < 0.2 && dx < 0.5) {
				intake.moveWheels(1.0);
			}
			if (dx < 0.3) {
				vx = 0.0;
				vy = 0.0;
				if (endTime == -1) {
					System.out.println("Got block!");
					intake.autonClamp();
					endTime = System.currentTimeMillis() + 500;
				} else if (endTime < System.currentTimeMillis()) {
					intake.moveWheels(0.0);
					return false;
				}
				intake.moveWheels(1.0);
			}
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
		if (posAngle < Math.PI / 4)
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
		System.out.println("Rotate angle: " + Math.toDegrees((-angle)));
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
