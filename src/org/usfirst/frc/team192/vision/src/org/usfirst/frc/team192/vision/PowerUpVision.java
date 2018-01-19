package org.usfirst.frc.team192.vision;

import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import java.util.*;
import java.awt.Point;

public class PowerUpVision {
	
	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
	 public static void main(String[] args) {
		 
		 VideoCapture cap = new VideoCapture(int device);
		 
		 PowerUpVision replaceThisNameLater = new PowerUpVision();
		 
		 Mat image = Imgcodecs.imread("path-to-img-file");
		 
		 Imshow original = new Imshow("Power cube image");
		 im.showImage(image);
		 Imshow masked = new Imshow("Masked with color filter");
	 }
	 
	 public Mat maskImage() {
		 
	 }
	 
	 public Mat findContoursOfTarget() {
		 
	 }
	 
	 public Point getCentroid() {
		 
	 }
	
}