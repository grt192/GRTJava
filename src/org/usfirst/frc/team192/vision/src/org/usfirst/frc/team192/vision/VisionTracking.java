package org.usfirst.frc.team192.vision;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import java.util.*;
import java.awt.Point;
import org.opencv.videoio.*;

public class VisionTracking /*implements Runnable*/{

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
    public static void main(String[] args) {
    		Imshow im = new Imshow("ControlsPicture");
    		Imshow secondWindow = new Imshow("Mask");
    		VideoCapture cap = new VideoCapture(0);
    		Mat image = new Mat();
    		
    		while (true) {
	    		cap.read(image);
	            im.showImage(image);
	            VisionTracking code = new VisionTracking();
	            Mat maskedImage = code.maskImageForTape(image);
	            Mat displayImage = new Mat();
	            
	            Imgproc.cvtColor(maskedImage, displayImage, Imgproc.COLOR_GRAY2BGR);
	            //secondWindow.showImage(displayImage);
	            
	            //Imshow contoursWindow = new Imshow("Contours");
	            Mat contours = code.findContoursOfTape(displayImage);
	            Mat contoursCopy = code.findContoursOfTape(displayImage);
	            //contoursWindow.showImage(contours);
	            
	            //Below: draw centroid for verification
	            org.opencv.core.Point midpoint = code.findCentroids(contours);
	            System.out.printf("%s%.2f%s%.2f", "x: ", midpoint.x, "y: ", midpoint.y);
	            
	            //Imshow midpointWindow = new Imshow("Midpoint");
	            Scalar white = new Scalar(255, 255, 255);
	            Imgproc.circle(contoursCopy, midpoint, 1, white);
	            secondWindow.showImage(contoursCopy);
	           // midpointWindow.showImage(contoursCopy);
    		}
	}
    
    public Mat maskImageForTape(Mat img) 
    {
    	//mask images using RGB hi/lo bounds 
    	
    	/*//VISION TAPE
    	Scalar lower = new Scalar(0, 0, 0);
    	Scalar upper = new Scalar(0, 0, 0);
    	//With HSV: 65 105 195, 105 145 255*/
        
    	//POWER CUBE
    	Scalar lower = new Scalar(100, 170, 170);
		Scalar upper = new Scalar(150, 255, 215);
		//With HSV: 0 130 130, 50 255 255
    	
    	/*//PORTAL
		Scalar lower = new Scalar(200, 100, 30);
		Scalar upper = new Scalar(255, 160, 70);
		//With HSV: 100 100 100, 255 255 255
 
    	//EXCHANGE
		Scalar lower = new Scalar(0, 0, 170);
		Scalar upper = new Scalar(60, 60, 255);
		//With HSV: 0 150 170, 180 205 255*/
        
        Core.inRange(img, lower, upper, img);         
        return img;
    }
    
    public Mat findContoursOfTape(Mat img) 
    {	
    		//find the contours of the two pieces of tape
    		Mat imgIn32SC1 = new Mat();
    		Mat binary = new Mat();
    		Mat edges = new Mat();
    		Mat blurred = new Mat();
    		Imgproc.cvtColor(img, imgIn32SC1, Imgproc.COLOR_BGR2GRAY);
    		Imgproc.threshold(imgIn32SC1,  binary,  100,  255,  Imgproc.THRESH_BINARY);
    		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    		Imgproc.blur(binary,  blurred,  new Size(3,3));
    		Imgproc.Canny(blurred, edges, 300, 600, 3, true);
    		Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    	
    		/*List<Mat> allContours = new List<Mat>;
    		
    		for (int i=0; i<contours.size(); i++) {
    			//if contour too small, don't include
    			allContours.add(contours[i]);
    		}
    		
    		Mat mergedContour = new Mat();
    		Core.merge(allContours, new Mat mergedContour);*/
    		
    		Scalar x = new Scalar(255, 0, 0, 255);
    		org.opencv.core.Point start = new org.opencv.core.Point(0,0);
    		org.opencv.core.Point end = new org.opencv.core.Point(0,0);
    		
    		for (int i=0; i<contours.size(); i++) {
    			MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
        		double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
        		Imgproc.approxPolyDP(contour2f, contour2f, approxDistance, true);
        		MatOfPoint points = new MatOfPoint(contour2f.toArray());
        		Rect rect = Imgproc.boundingRect(points);
        		if (rect.width<10 || rect.height<10) {
        			continue;
        		}
        		else {
        			if (rect.x < start.x) start.x = rect.x;
        			if (rect.y < start.y) start.y = rect.y;
        			if (rect.x+rect.width > end.x) end.x = rect.x + rect.width;
        			if (rect.y + rect.height > end.y) end.y = rect.y + rect.height;
        		}
    		}
    		
    		Imgproc.rectangle(edges, start, end, x, 3); 		
    		return edges;   		
    }
    
    public org.opencv.core.Point findCentroids(Mat img) 
    {   
    		//find centroid point of vision target
    		Moments moments = Imgproc.moments(img); //moments used to find center of mass of id'd pixels
    		org.opencv.core.Point centroid = new org.opencv.core.Point();
    		centroid.x = (int) (moments.get_m10()/moments.get_m00());
    		centroid.y = (int) (moments.get_m01()/moments.get_m00());
    		return centroid;
    }
}

/* Easier/less precise way (boundingRect):
 * For each contour found, convert the contours from MatOfPoint to MatOfPoint2f
 * Processing on mMOP2f1, which is in type MatOfPoint2f
 * Convert back to MatOfPoint
 * Get bounding rect of contour
 * Draw enclosing rectangle 
 */


/* STRATEGY FOR ALL VISION - 2018
 * 
 * Tracking targets: bright yellow power cubes, red exchange (2 holes), blue portal
 * Picking up power cube: useful only in auton or also in teleop?
 * Different vision modes for power cube vs. exchange/portal bc yellow border around exchange & portal
 * 		may confuse vision otherwise
 * Make sure to account for lighting differences in filter tuning
 * Get boundingRect for shape, from which get a centroid using moments
 * Use centroid relative position to robot vision center to move left/right
 * 
 * Integrate with auton and teleop
 * Testing + tuning w/ mechs, relies on full bot first
 */

/* VISION-AUTON INTEGRATION
 * VISION-TELEOP INTEGRATION
 * MECHS INTEGRATION
 * VR INTEGRATION
 * FIELD-MAPPING? (not currently in org map)
 */

/*public enum RobotStatus {
	VALUE, VALUE2, VALUE3, ETC
}*/

