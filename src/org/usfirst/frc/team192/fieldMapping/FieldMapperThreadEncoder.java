package org.usfirst.frc.team192.fieldMapping;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.video.KalmanFilter;
import org.usfirst.frc.team192.swerve.FullSwerve;
import org.usfirst.frc.team192.swerve.SwerveData;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class FieldMapperThreadEncoder extends FieldMapperGyro implements Runnable {
	
	private FullSwerve swerve;
	private int stateSize;
	private int sensorSize;
	private final int TYPE;
	
	KalmanFilter filter;
	double dt;
	
	public FieldMapperThreadEncoder(Gyro gyro, FullSwerve swerve) {
		super(gyro, 0, 0);
		this.swerve = swerve;
		stateSize = 4;
		sensorSize = 2;
		TYPE = CvType.CV_64FC1;
		filter = new KalmanFilter(stateSize, sensorSize);
		dt = 0.005;
		Mat P = makeP();
		Mat F = makeF();
		Mat H = makeH();
		Mat Q = makeQ();
		Mat R = makeR();
		filter.set_transitionMatrix(F);
		filter.set_errorCovPost(P);
		filter.set_measurementMatrix(H);
		filter.set_measurementNoiseCov(R);
		filter.set_processNoiseCov(Q);
		filter.set_statePost(Mat.zeros(stateSize, 1, TYPE));
		reset();
	}
	
	public void reset(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void reset() {
		reset(0, 0);
	}
	
	private Mat makeP() {
		Mat result = Mat.eye(stateSize, stateSize, TYPE);
		result = result.mul(result, 1e-5);
		/*
		for (int i = 0; i < stateSize; i++) {
			for (int j = 0; j < stateSize; j++) {
				if ((i + j) % 2 == 0) {
					result.put(i, j, 1);
				}
			}
		}
		*/
		return result;
	}
	
	private Mat makeF() {
		Mat result = Mat.eye(stateSize, stateSize, TYPE);
		result.put(0, 2, dt);
		result.put(1, 3, dt);
		return result;
	}
	
	private Mat makeH() {
		Mat result = Mat.zeros(stateSize, stateSize, TYPE);
		result.put(0, 2, 1);
		result.put(1, 3, 1);
		return result;
	}
	
	private Mat makeQ() {
		Mat result = Mat.eye(stateSize, stateSize, TYPE);
		result.put(0, 0, .000001);
		result.put(1, 1, .000001);
		result.put(2, 2, .001);
		result.put(3, 3, .001);
		return result;
	}
	
	private Mat makeR() {
		Mat result = Mat.eye(sensorSize, sensorSize, TYPE);
		result = result.mul(result, 1e-3);
		return result;
	}
	
	private Mat getMeasurement() {
		Mat result = new Mat(sensorSize, 1, TYPE);
		result.put(0, 0, getVx());
		result.put(1, 0, getVy());
		return result;
	}
	
	public void run() {
		while (true) {
			Mat pos = filter.predict();
			x = pos.get(0, 0)[0];
			y = pos.get(0, 1)[0];
			filter.correct(getMeasurement());
			Timer.delay(dt);
		}
	}
	
	protected double getVx() {
		SwerveData data = swerve.getSwerveData();
		return data.encoderVX;
	}
	
	protected double getVy() {
		SwerveData data = swerve.getSwerveData();
		return data.encoderVY;
	}
}
