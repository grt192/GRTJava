package org.usfirst.frc.team192.vision;

import org.usfirst.frc.team192.vision.VisionTracking;
import org.usfirst.frc.team192.vision.VisionTracking.Mode;

import java.sql.Types;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class ImageThread extends Thread{
	private Mode visionMode;
	Mat image;

	public ImageThread() {
		visionMode = Mode.CUBE;
	}
	
	public void setVisionMode(Mode mode) {
		visionMode = mode;
	}
	
	public void run() {
		VideoCapture cap = new VideoCapture(0);
		Mat image = new Mat();
		CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
		
		cap.open(0);
		
		while (!Thread.interrupted()) {
			cap.read(image);
			VisionTracking.maskImageForTape(Mode.CUBE, image);
			Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2BGR);
			outputStream.putFrame(image);
		}
	}
	
	public Mat getImage() {
		return image;
	}

}
