package org.usfirst.frc.team192.fieldMapping;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.video.KalmanFilter;
import org.usfirst.frc.team192.config.Config;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public abstract class FieldMapperThreadVel extends FieldMapperGyro implements Runnable {
	
	private int stateSize;
	private int sensorSize;
	private final int TYPE;
	
	KalmanFilter filter;
	double dt;
	double C;
	
	public FieldMapperThreadVel(Gyro gyro, double relX, double relY) {
		super(gyro, relX, relY);
		stateSize = 4;
		sensorSize = 2;
		TYPE = CvType.CV_64FC1;
		filter = new KalmanFilter(stateSize, sensorSize);
		dt = 0.005;
		C = 1 / Config.getDouble("drive_encoder_scale");
		Mat P = makeP();
		Mat F = makeF();
		Mat H = makeH();
		Mat Q = makeQ();
		Mat R = makeR();
		filter.set_transitionMatrix(F);
		filter.set_processNoiseCov(P);
		filter.set_measurementMatrix(H);
		filter.set_measurementNoiseCov(R);
		filter.set_processNoiseCov(Q);
		filter.set_statePre(Mat.zeros(stateSize, 1, TYPE));
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
		for (int i = 0; i < stateSize; i++) {
			for (int j = 0; j < stateSize; j++) {
				if ((i + j) % 2 == 0) {
					result.put(i, j, 1);
				}
			}
		}
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
		result.put(0, 2, C);
		result.put(1, 3, C);
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
		Mat result = Mat.eye(stateSize, stateSize, TYPE);
		result.put(0, 0, .000001);
		result.put(1, 1, .000001);
		result.put(2, 2, .001);
		result.put(3, 3, .001);
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
			filter.correct(getMeasurement());
			Mat pos = filter.predict();
			x = pos.get(0, 0)[0];
			y = pos.get(0, 1)[0];
		}
	}
	
	protected abstract double getVx();
	protected abstract double getVy();
	
}

















