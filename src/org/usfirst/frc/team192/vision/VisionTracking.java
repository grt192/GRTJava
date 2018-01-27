package org.usfirst.frc.team192.vision;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import java.util.*;
import java.awt.Point;
import org.opencv.videoio.*;

public class VisionTracking /*implements Runnable*/{
	
	public org.opencv.core.Point centroid;
	public int width, height;
	protected Mode mode;
	
	protected enum Mode {
		CUBE, TAPE, EXCHANGE;
	}
	
	public org.opencv.core.Point getCenter() {
		return centroid;
	}
	
	public int getArea() {
		return width * height;
	}
	
	protected void setMode(int x) {
		if (x == 0)
			mode = Mode.CUBE;
		else if (x == 1)
			mode = Mode.TAPE;
		else if (x == 2)
			mode = Mode.EXCHANGE;		
	}
    
    public Mat maskImageForTape(Mat img) 
    {
    	
    	/*//VISION TAPE
    	Scalar lower = new Scalar(0, 0, 0);
    	Scalar upper = new Scalar(0, 0, 0);
    	//With HSV: 65 105 195, 105 145 255*/
        
    	//POWER CUBE
    	Scalar lower = new Scalar(100, 180, 170);
		Scalar upper = new Scalar(150, 255, 215);
		//With HSV: 0 130 130, 50 255 255
 
    	/*//EXCHANGE
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
    		Imgproc.rectangle(edges, start, end, x, 3); 		
    		return edges;   		
    }
    
    public org.opencv.core.Point findCentroid(Mat img) 
    {   
    		//find centroid point of vision target
    		Moments moments = Imgproc.moments(img); //moments used to find center of mass of id'd pixels
    		org.opencv.core.Point centroid2 = new org.opencv.core.Point();
    		centroid2.x = (int) (moments.get_m10()/moments.get_m00());
    		centroid2.y = (int) (moments.get_m01()/moments.get_m00());
    		centroid = centroid2;
    		return centroid2;
    }
}

