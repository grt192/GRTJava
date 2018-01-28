package org.usfirst.frc.team192.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class Tester /*implements Runnable*/ {

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
    public static void main (String[] args) {
    		Imshow im = new Imshow("ControlsPicture");
    		Imshow secondWindow = new Imshow("Mask");
    		VideoCapture cap = new VideoCapture(1);
    		Mat image = new Mat();
    		
    		while (true) {
	    		cap.read(image);
	            im.showImage(image);
	            VisionTracking code = new VisionTracking();
	            Mat maskedImage = code.maskImageForTape(image);
	            Mat displayImage = new Mat();
	            
	            Imgproc.cvtColor(maskedImage, displayImage, Imgproc.COLOR_GRAY2BGR);
	            
	            Mat contours = code.findContoursOfTape(displayImage);
	            Mat contoursCopy = code.findContoursOfTape(displayImage);
	            
	            org.opencv.core.Point midpoint = code.findCentroid(contours);
	            System.out.printf("%s%.2f%s%.2f", "x: ", midpoint.x, "y: ", midpoint.y);
	            
	            Scalar white = new Scalar(255, 255, 255);
	            Imgproc.circle(contoursCopy, midpoint, 1, white);
	            secondWindow.showImage(contoursCopy);
    		}
	}

}
