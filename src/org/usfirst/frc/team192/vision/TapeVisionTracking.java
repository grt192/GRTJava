package org.usfirst.frc.team192.vision;

import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import java.util.*;
import java.awt.Point;

public class TapeVisionTracking {

	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
    public static void main(String[] args) {
    	
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
            contoursWindow.showImage(contours);
            
            Point midpoint = code.findCentroids(contours);
            System.out.printf("%s%d%s%d", "x: ", midpoint.x, "y: ", midpoint.y);
	}
    
    public Mat maskImageForTape(Mat img) 
    {
        Scalar lower = new Scalar(65, 105, 195);
        Scalar upper = new Scalar(105, 145, 260);
        
        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV); 
        Mat convertedColorspace = new Mat();
        Core.inRange(hsv, lower, upper, convertedColorspace); //img
        
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
