package org.usfirst.frc.team192.vision;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import java.util.*;

public class VisionTracking {
	
	protected static org.opencv.core.Point centroid;
	protected static int width, height;
	
	public enum Mode {
		CUBE, TAPE, EXCHANGE;
	}
	
	public org.opencv.core.Point getCenter() {
		return centroid;
	}
	
	public int getArea() {
		return width * height;
	}
    
    public static Mat maskImageForTape(Mode visionMode, Mat img) 
    {
    		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2HSV);
    		Scalar lower = new Scalar(0, 0, 0);
    		Scalar upper = new Scalar(0, 0, 0);
    	
        if (visionMode == Mode.CUBE) {
        	lower = new Scalar(29, 30, 170);
    		upper = new Scalar(90, 160, 300);
        }
        else if (visionMode == Mode.TAPE) {
        	lower = new Scalar(0, 0, 0);
        	upper = new Scalar(0, 0, 0);
        }
        else if (visionMode == Mode.EXCHANGE) {
        	lower = new Scalar(0, 0, 170);
        	upper = new Scalar(60, 60, 255);
        }
        Core.inRange(img, lower, upper, img);         
        return img;
    }
    
    
    public static Mat findContoursOfTape(Mat img) 
    {	
    		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
    		Imgproc.threshold(img,  img,  100,  255,  Imgproc.THRESH_BINARY);
    		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    		Imgproc.blur(img,  img,  new Size(3,3));
    		Imgproc.Canny(img, img, 300, 600, 3, true);
    		Imgproc.findContours(img, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    		
    		Scalar x = new Scalar(255, 0, 0, 255);
    		org.opencv.core.Point start = new org.opencv.core.Point(640,480);
    		org.opencv.core.Point end = new org.opencv.core.Point(0,0);
    		
    		for (int i=0; i<contours.size(); i++) {
    			MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
        		double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
        		Imgproc.approxPolyDP(contour2f, contour2f, approxDistance, true);
        		MatOfPoint points = new MatOfPoint(contour2f.toArray());
        		Rect rect = Imgproc.boundingRect(points);
        		if (rect.width<35 || rect.height<35) {
        			continue;
        		}
        		else {
        			if (rect.x < start.x) start.x = rect.x;
        			if (rect.y < start.y) start.y = rect.y;
        			if (rect.x+rect.width > end.x) end.x = rect.x + rect.width;
        			if (rect.y + rect.height > end.y) end.y = rect.y + rect.height;
        		}
    		}
    	
    		width = (int) (end.x - start.x);
    		height = (int) (end.y - start.y);
    		Imgproc.rectangle(img, start, end, x, 3); 		
    		return img;   		
    }
    
    /*//gets contours and finds extreme points
    //using logic statements, determines whether to rotate left, right, or well-aligned for pickup
    //return -1 for rotate left, 0 for aligned, 1 for rotate right
    public static int processTopDownVision(Mat img) {
    		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(img,  img,  100,  255,  Imgproc.THRESH_BINARY);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.blur(img,  img,  new Size(3,3));
		Imgproc.Canny(img, img, 300, 600, 3, true);
		Imgproc.findContours(img, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		Scalar x = new Scalar(255, 0, 0, 255);
		org.opencv.core.Point start = new org.opencv.core.Point(500,500);
		org.opencv.core.Point end = new org.opencv.core.Point(0,0);
		
		int xMin = 640;
		int yMin = 480;
		int xMax = 0; 
		int yMax = 0;
		
		for (int i=0; i<contours.size(); i++) {
			MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
			double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
			Imgproc.approxPolyDP(contour2f, contour2f, approxDistance, true);
			MatOfPoint points = new MatOfPoint(contour2f.toArray());
			
			int xVal = contours.get(i).x;
			int yVal = contours.get(i).y;
			
			if (xVal < xMin) xMin = xVal;
			else if (xVal > xMax) xMax = xVal;
			if (yVal < yMin) yMin = yVal;
			else if (yVal > yMax) yMax = yVal;
			
			org.opencv.core.Point a = new org.opencv.core.Point();
			org.opencv.core.Point b = new org.opencv.core.Point();
			org.opencv.core.Point c = new org.opencv.core.Point();
			org.opencv.core.Point d = new org.opencv.core.Point();
			
			
			
		}
		
		
    		//find extreme points of contours and assign points
    		//A = most left of 2 upper ys
    		//B = most right of 2 upper ys
    		//C = most right of 2 lower ys
    		//D = most left of 2 lower ys
    		return 0;
    }*/
   
    public static org.opencv.core.Point findCentroid(Mat img) 
    {   
    		Moments moments = Imgproc.moments(img); 
    		org.opencv.core.Point centroid2 = new org.opencv.core.Point();
    		centroid2.x = (int) (moments.get_m10()/moments.get_m00());
    		centroid2.y = (int) (moments.get_m01()/moments.get_m00());
    		centroid = centroid2;
    		return centroid2;
    }
}