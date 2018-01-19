package org.usfirst.frc.team192.vision;

import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import java.util.*;
import java.awt.Point;

public class TapeVisionTracking implements Runnable{

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
    public static void main(String[] args) {
    			//start thread here
    			//VideoCapture cap = new VideoCapture(int device);
    	
            Mat image = Imgcodecs.imread("/Users/karlyh66/eclipse-workspace/Vision Testing on Mac/src/org/usfirst/frc/team192/vision/tapepic.png");

            Imshow im = new Imshow("ControlsPicture");
            im.showImage(image);
            TapeVisionTracking code = new TapeVisionTracking();
            //code.maskImageForTape(copy);
            Imshow secondWindow = new Imshow("Mask");
            Mat maskedImage = code.maskImageForTape(image);
            Mat displayImage = new Mat();
            
            Imgproc.cvtColor(maskedImage, displayImage, Imgproc.COLOR_GRAY2BGR);
            //Imgproc.cvtColor(maskedImage, displayImage, Imgproc.COLOR_BGR2HSV);
            secondWindow.showImage(displayImage);
            
            Imshow contoursWindow = new Imshow("Contours");
            Mat contours = code.findContoursOfTape(displayImage);
            Mat contoursCopy = code.findContoursOfTape(displayImage);
            contoursWindow.showImage(contours);
            
            //Below: draw centroid for verification
            org.opencv.core.Point midpoint = code.findCentroids(contours);
            System.out.printf("%s%.2f%s%.2f", "x: ", midpoint.x, "y: ", midpoint.y);
            
            Imshow midpointWindow = newImshow("Midpoint");
            Scalar white = new Scalar(255, 255, 255);
            Imgproc.circle(contoursCopy, midpoint, 1, white);
            midpointWindow.showImage(contoursCopy);
	}
    
    public Mat maskImageForTape(Mat img) 
    {
    		//VISION TAPE
        //Scalar lower = new Scalar(65, 105, 195);
        //Scalar upper = new Scalar(105, 145, 255);
    		//Without HSV:
    		//
        
    		//POWER CUBE
        //Scalar lower = new Scalar(0, 130, 130);
        //Scalar upper = new Scalar(60, 255, 255);
    		//Without HSV:
    		//
    	
    		//PORTAL
        //Scalar lower = new Scalar(100, 100, 100);
        //Scalar upper = new Scalar(255, 255, 255);
    		//Without HSV:
    		//
    	
    		//EXCHANGE
        //Scalar lower = new Scalar(0, 150, 170);
        //Scalar upper = new Scalar(180, 205, 255);
    		//Without HSV:
    		//0 0 170, 60 60 255
        
        Mat hsv = new Mat();
        //Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV); 
        Mat convertedColorspace = new Mat();
        
        Core.inRange(hsv, lower, upper, convertedColorspace);         
        return convertedColorspace;
    }
    
    public Mat findContoursOfTape(Mat img) 
    {
    		//find the contours of the two pieces of tape
    		//List<MatOfPoint> contours = new ArrayList();
    		//Imgproc.findContours(img, contours, hierarchy, mode, method); //hier = Mat, mode/method = int
    		Mat imgIn32SC1 = new Mat();
    		Mat binary = new Mat();
    		Mat edges = new Mat();
    		Mat blurred = new Mat();
    		//img.convertTo(imgIn32SC1, CvType.CV_8uC1);
    		Imgproc.cvtColor(img, imgIn32SC1, Imgproc.COLOR_BGR2GRAY);
    		Imgproc.threshold(imgIn32SC1,  binary,  100,  255,  Imgproc.THRESH_BINARY);
    		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    		Imgproc.blur(binary,  blurred,  new Size(3,3));
    		Imgproc.Canny(blurred,  edges,  300,  600, 3, true);
    		Imgproc.findContours(edges,  contours,  new Mat(),  Imgproc.RETR_LIST,  Imgproc.CHAIN_APPROX_SIMPLE);
    		
    		//problem: finds bounding rectangle for all contours
    		//need overall rectangle
    		for (int i=0; i<contours.size(); i++) {
    			MatOfPoint contour2f = new MatOfPoint2f(contours.get(i).toArray());
    			double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
    			Imgproc.approxPolyDP(contour2f, contour2f, approxDistance, true);
    			MatOfPoint points = new MatOfPoint(contour2f.toArray());
    			Rect rect = Imgproc.boundingRect(points);
    			Scalar x = new Scalar(255, 0, 0, 255);
    			org.opencv.core.Point start = new org.opencv.core.Point(rect.x, rect.y);
    			org.opencv.core.Point end = new org.opencv.core.Point(rect.x+rect.width, rect.y+rect.height);
    			Imgproc.rectangle(edges, start, end, x, 3);
    		}
    		
    		return edges;   		
    }
    
    public Point findCentroids(Mat img) 
    {
    		//find centroids of two pieces of tape and find midpoint of centroids
    		Moments moments = Imgproc.moments(img); //moments are used to find center of mass of id'd pixels
    		Point centroid = new Point();
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

