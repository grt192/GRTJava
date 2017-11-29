package org.usfirst.frc.team192.vision;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Vision
{
	//int[][] matrix = new int[100][200]; 
	Mat img = Imgcodecs.imread("path here"); 
	
	public static void main(String[] args) {
		
	}
	
	public static void readImageAndConvert() {
		//read in image and store as matrix of pixel values
	}
	
	public static void getBoundaries(int[][] matrix) {
		//boundary detection for tape
	}
	
	public static Mat outputData() {
		return null;
		//return matrix of 0s for non-tape, 1s for tape
	}	
}































/*import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Hello
{
   public static void main( String[] args )
   {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
      System.out.println( "mat = " + mat.dump() );
   }
}*/