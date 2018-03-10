package org.usfirst.frc.team192.vision;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public abstract class VisionThread extends Thread {

	private int width;
	private int height;
	protected VideoCapture cap;
	protected Mat image;
	
	public VisionThread(int width, int height) {
		this.width = width;
		this.height = height;
		cap = new VideoCapture(0);
		cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);
		cap.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
		image = new Mat(height, width, CvType.CV_8UC3);
	}
	
	public abstract Point getCenter();
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}

}
