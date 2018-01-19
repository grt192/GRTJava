package org.usfirst.frc.team192.filter;

public class KalmanFilter {
	private double vision_cent_x;
	private double vision_cent_y;
	private double[] filtered_vision_pos;
	private double gyro_x;
	private double gyro_y;
	private double error_constant;
	
	public KalmanFilter(double vision_cent_x, double vision_cent_y, double gyro_x, double gyro_y) {
		this.vision_cent_x = vision_cent_x;
		this.vision_cent_y = vision_cent_y;
		this.gyro_x = gyro_x;
		this.gyro_y = gyro_y;
		filtered_vision_pos = new double[]{0, 0};
		error_constant = 1;
	}
	
	public double[] PositionEstimate(double vision_cent_x, double vision_cent_y) {
		filtered_vision_pos[0] = vision_cent_x;
		filtered_vision_pos[1] = vision_cent_y;
		return filtered_vision_pos;
	}
	
	public Boolean CheckPositionWithGyro(double[] filtered_vision_pos) {
		Boolean is_in_range = true;
		return is_in_range;
	}

}
