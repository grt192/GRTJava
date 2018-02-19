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
	private VideoCapture cap;
	private Mat image;
	private final Point CENTER;

	private boolean running;

	public RemoteVisionThread() {
		CENTER = new Point(320, 240);
		vision = new RemoteVision(1920);
		cap = new VideoCapture(0);
		cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, 240);
		cap.set(Videoio.CAP_PROP_FRAME_WIDTH, 320);
		image = new Mat(480, 640, CvType.CV_8UC3);
	}

	@Override
	public void run() {
		running = true;
		while (true) {
			if (running) {
				cap.grab();
				cap.read(image);
				vision.sendImage(image);
				Timer.delay(0.1);
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
