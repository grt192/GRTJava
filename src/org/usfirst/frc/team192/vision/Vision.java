package org.usfirst.frc.team192.vision;

import java.awt.Point;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
	
public class Vision {
	private Point centroid;
	private Point new_centroid;
	
	public Vision() {
//		last_centroid.x = 0;
//		last_centroid.y = 0;
//		centroid = last_centroid;
//		centroid.y = last_centroid.y;
//		new_centroid.x = 0;
//		new_centroid.y = 0;
		
	}
	
	 public void cameraThread() {
		 //System.out.println("bendnsnap");
         new Thread(() -> {
             UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
             camera.setResolution(640, 480);
             
             CvSink cvSink = CameraServer.getInstance().getVideo();
             CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
             
             Mat source = new Mat();
             Mat output = new Mat();
             
             while(!Thread.interrupted()) {
//            	 	new_centroid.x = centroid.x + 1;
//            	 	new_centroid.y = centroid.y + 1;
//            	 	centroid = new_centroid;
//                 cvSink.grabFrame(source);
//                 Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
//                 outputStream.putFrame(output);
//            	 	System.out.println("vision loop");
             }
         }).start();
         
	 }
	 
//	 public int getCentroid() {
//		 return int;
//	 }


}
