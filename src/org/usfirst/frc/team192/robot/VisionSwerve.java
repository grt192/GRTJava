package org.usfirst.frc.team192.robot;

import org.opencv.core.Point;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.VisionThread;

public class VisionSwerve {
	private VisionThread vision;
	private FullSwervePID swerve;
	private boolean blockFound;
	
	public VisionSwerve(VisionThread vision, FullSwervePID swerve) {
		this.vision = vision;
		this.swerve = swerve;
		blockFound = false;
	}
	
	public void kill() {
		blockFound = false;
	}
	
	public boolean update() {
		if (!blockFound) {
			Point center = vision.getCenter();
			if (center == null) {
				blockFound = false;
				return false;
			}
			center.y = vision.getHeight() - center.y;
			center.x -= vision.getWidth() / 2;
			double angle = Math.atan(center.x / center.y);
			swerve.setVelocity(0, 0);
			swerve.setTargetPosition(angle + Math.toRadians(swerve.getGyroAngle()));
			blockFound = true;
		}
		swerve.updateAutonomous();
		return true;
	}
}












