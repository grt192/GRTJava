package org.usfirst.frc.team192.vision.nn;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.usfirst.frc.team192.vision.VisionThread;

import edu.wpi.first.wpilibj.Timer;

public class RemoteVisionThread extends VisionThread {

	private RemoteVision vision;

	private boolean running;

	public RemoteVisionThread(int width, int height) {
		super(width, height);
		vision = new RemoteVision(1920);
	}

	@Override
	public void run() {
		running = true;
		while (true) {
			if (running) {
				cap.grab();
				cap.read(image);
				vision.sendImage(image);
			}
		}
	}

	public boolean hasTarget() {
		return vision.getCenter() != null;
	}

	@Override
	public Point getCenter() {
		return vision.getCenter();
	}

	public void pause() {
		running = false;
	}

	public void restart() {
		running = true;
	}
}
