package org.usfirst.frc.team192.vision;

import org.opencv.core.Point;

public abstract class VisionThread extends Thread {

	public abstract Point getCenter();

}
