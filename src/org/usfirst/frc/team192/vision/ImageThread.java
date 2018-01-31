package org.usfirst.frc.team192.vision;

import org.usfirst.frc.team192.vision.VisionTracking;
import org.usfirst.frc.team192.vision.VisionTracking.Mode;
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

	public ImageThread() {
		visionMode = Mode.CUBE;
	}
	
	public void setVisionMode(Mode mode) {
		visionMode = mode;
	}
	
	public void run() {
		//Imshow im = new Imshow("ControlsPicture");
		VideoCapture cap = new VideoCapture(0);
		Mat image = new Mat();
		//UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        //camera.setResolution(640, 480);
        
        //CvSink cvSink = CameraServer.getInstance().getVideo();
        CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
        
        //Mat source = new Mat();
        //Mat output = new Mat();
		cap.open(0);
        
        while(!Thread.interrupted()) {
        		cap.read(image);
        		VisionTracking.maskImageForTape(Mode.CUBE, image);
        		Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2BGR);
        		VisionTracking.findContoursOfTape(image);
        		image.convertTo(image, CvType.CV_32SC1, 255, 0);
        		org.opencv.core.Point midpoint = VisionTracking.findCentroid(image);
        		Scalar red = new Scalar(0, 0, 255);
	            Imgproc.circle(image, midpoint, 1, red);
        		//cvSink.grabFrame(source);
        		outputStream.putFrame(image);
        }
	}

}
