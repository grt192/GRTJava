package org.usfirst.frc.team192.fieldMapping;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.video.KalmanFilter;
import org.usfirst.frc.team192.swerve.FullSwerve;

public class PositionTracker extends Thread {

	private static final double timestep = 0.01;

	private FullSwerve swerve;
	private KalmanFilter kalmanFilter;
	private Mat state;

	public PositionTracker(FullSwerve swerve) {
		this.swerve = swerve;
		kalmanFilter = new KalmanFilter(6, 4);
		float[] transitionMatrix = new float[] { // data for the state transition matrix
				1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, // x position
				0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, // y position
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, // x velocity
				0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // y velocity
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, // x acceleration
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };// y acceleration
		Mat transition = new Mat(4, 4, CvType.CV_32F);
		transition.put(0, 0, transitionMatrix);
		kalmanFilter.set_transitionMatrix(transition);
		state = Mat.zeros(6, 1, CvType.CV_32F);
		kalmanFilter.set_statePost(state);
		Mat initialCovariance = Mat.eye(6, 6, CvType.CV_32F);
		Core.multiply(initialCovariance, new Scalar(10e-4f), initialCovariance);
		kalmanFilter.set_errorCovPost(initialCovariance);
		Mat processNoise = new Mat(6, 1, CvType.CV_32F);
		processNoise.put(0, 0, new float[] { 10e-2f, 10e-2f, 10e-1f, 10e-1f, 1.0f, 1.0f });
		processNoise = Mat.diag(processNoise);
		kalmanFilter.set_processNoiseCov(processNoise);

	}

	@Override
	public void run() {

	}

}
