package org.usfirst.frc.team192.vision;

import org.usfirst.frc.team192.vision.VisionTracking;
import org.usfirst.frc.team192.vision.VisionTracking.Mode;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
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
        		try {
        			cap.read(image);
        			VisionTracking.maskImageForTape(Mode.CUBE, image);
        			Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2BGR);
        			VisionTracking.findContoursOfTape(image);
        			Point midpoint = VisionTracking.findCentroid(image);
        			System.out.println("x: " + midpoint.x +  " y: " + midpoint.y);
        			Scalar white = new Scalar(255, 255, 255);
        			Imgproc.circle(image, midpoint, 1, white);
        			outputStream.putFrame(image);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        }
	}

}
