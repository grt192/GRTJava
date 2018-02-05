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
    	Scalar lower = new Scalar(0, 0, 0);
    	Scalar upper = new Scalar(0, 0, 0);
    	
        if (visionMode == Mode.CUBE) {
        	lower = new Scalar(50, 190, 180);
    		upper = new Scalar(150, 255, 245);
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
//    		Mat imgIn32SC1 = new Mat();
//    		Mat binary = new Mat();
//    		Mat edges = new Mat();
//    		Mat blurred = new Mat();
    		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
    		Imgproc.threshold(img,  img,  100,  255,  Imgproc.THRESH_BINARY);
    		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    		Imgproc.blur(img,  img,  new Size(3,3));
    		Imgproc.Canny(img, img, 300, 600, 3, true);
    		Imgproc.findContours(img, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    		
    		Scalar x = new Scalar(255, 0, 0, 255);
    		org.opencv.core.Point start = new org.opencv.core.Point(500,500);
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
    	
    		width = (int) (end.x - start.x);
    		height = (int) (end.y - start.y);
    		Imgproc.rectangle(img, start, end, x, 3); 		
    		return img;   		
    }
    
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