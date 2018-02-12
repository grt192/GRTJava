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
	//private QElapsedTimer timer; 

	public ImageThread() {
		visionMode = Mode.CUBE;
		//timer = new QElapsedTimer();
	}
	
	public void setVisionMode(Mode mode) {
		visionMode = mode;
	}
	
	public void run() {
		
		VideoCapture cap = new VideoCapture(0);
		//cap.set(38, 1); //38 = CV_CAP_PROP_BUFFERSIZE
		//cap.set(5, 50); //5 = CV_CAP_PROP_FPS
		//cap.set(10, 100); //10 = CV_CAP_PROP_BRIGHTNESS
		
		Mat image = new Mat();
        CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
        
		cap.open(0);
        
        while(!Thread.interrupted()) {
        		try {
        			cap.grab(); 
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
