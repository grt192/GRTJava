package org.usfirst.frc.team192.vision.nn;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class RemoteVisionThread implements Runnable {

	private RemoteVision vision;
	private VideoCapture cap;
	private Mat image;

	private volatile boolean running;
	private volatile double[] data;

	public RemoteVisionThread(int width, int height) {
		cap = new VideoCapture(0);
		cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);
		cap.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
		image = new Mat(height, width, CvType.CV_8UC3);
		vision = new RemoteVision(1920, this);
	}

	@Override
	public void run() {
		running = true;
		data = null;
		while (running) {
			cap.grab();
			cap.read(image);
			vision.sendImage(image);
		}
	}

	public void recieve(double[] data) {
		if (this.data == null)
			this.data = data;
		running = false;
	}

	public void kill() {
		running = false;
	}

	public boolean hasData() {
		return data != null;
	}

	public Point getPoint() {
		return new Point(data[0], data[1]);
	}

	public double getAngle() {
		return data[2];
	}

	public boolean hasTarget() {
		return !Double.isNaN(data[0]);
	}
}
