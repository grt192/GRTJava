package org.usfirst.frc.team192.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.usfirst.frc.team192.vision.VisionTracking.Mode;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;

public class ImageThread extends VisionThread {
	private Point centroid;
	// private QElapsedTimer timer;

	public ImageThread(int width, int height) {
		super(width, height);
		// timer = new QElapsedTimer();
	}

	@Override
	public void run() {
		// cap.set(38, 1); //38 = CV_CAP_PROP_BUFFERSIZE
		// cap.set(5, 50); //5 = CV_CAP_PROP_FPS
		// cap.set(10, 100); //10 = CV_CAP_PROP_BRIGHTNESS

		CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);

		while (!Thread.interrupted()) {
			try {
				cap.grab();
				cap.read(image);
				VisionTracking.maskImageForTape(Mode.CUBE, image);
				Imgproc.cvtColor(image, image, Imgproc.COLOR_GRAY2BGR);
				VisionTracking.findContoursOfTape(image);
				centroid = VisionTracking.findCentroid(image);
				System.out.println("x: " + centroid.x + " y: " + centroid.y);
				Scalar white = new Scalar(255, 255, 255);
				Imgproc.circle(image, centroid, 5, white);
				outputStream.putFrame(image);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	@Override
	public Point getCenter() {
		// TODO Auto-generated method stub
		return centroid;
	}

}
