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

	private Point corner;
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
		new Thread(vision).start();
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
			double dx = corner.x - pos.x;
			double dy = corner.y - pos.y;
			double vx = 0.0;
			if (Math.abs(dy) < 0.05) {
				// intake.movePickupOut();
				// intake.moveWheels(1.0);
				vx = 0.2;
			}
			double vy = Math.max(dy * 0.5, 0.4);
			double angle = -targetAngle;
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
		corner = vision.getPoint();
		System.out.println("Block found at " + corner);
		if (angle > Math.PI / 4)
			angle -= Math.PI / 2;
		if (angle < -Math.PI / 4)
			angle += Math.PI / 2;
		targetAngle = initialAngle + angle;
		swerve.setTargetPosition(targetAngle);

		angle *= -1;
		double x = corner.x;
		double y = corner.y;
		corner.x = x * Math.cos(angle) - y * Math.sin(angle);
		corner.y = x * Math.sin(angle) + y * Math.cos(angle);
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
