package org.usfirst.frc.team192.vision;

public class ShooterCalc {
	
	private static double shooter_h;
	private static double base_d_straight;
	private static double base_d;
	private static double base_width;
	private static double theta_cam;
	private static double theta_base;
	private static double rotation_rate; //degrees camera should rotate in one call of alignRotationX
	private static double distance;
	private static int[][] img;
	private static double theta_board;
	private static double target_h;
	private static double shooter_phi;

	public ShooterCalc() {
		shooter_h = 0.0;
		base_d_straight = 5.0;
		base_width = 10.0;
		theta_cam = 0;
		theta_base = 0.0;
		rotation_rate = 1; 
		for (int i = 0; i < 250; i++) {
			for (int j = 0; j < 250; j++){
				img[i][j] = 0;
			}
		}
		img[175][175] = 1;
	}
	
	public ShooterCalc(int[][] img) {
		shooter_h = 0.0;
		base_d_straight = 5.0;
		base_width = 10.0;
		theta_cam = 0;
		theta_base = 0.0;
		rotation_rate = 1; 
		this.img = img;
	}
	
	public ShooterCalc(double shooter_h, double base_d_straight, double base_width, double rotation_rate, int[][] img) {
		this.shooter_h = shooter_h;
		this.base_d_straight = base_d_straight;
		this.base_width = base_width;
		this.rotation_rate = rotation_rate; 
		this.img = img;
	}

	public static double distanceToPoint() {
		return ((base_d_straight + base_width * Math.atan(theta_base)) * Math.acos(theta_cam));
	}
	
	public static double calcBaseD() {
		return (base_d_straight * 1/Math.cos(theta_cam));
	}
	
	//returns relative position [x, y] of mark
	public double[] posOfMark() {
		double[] mark = {0,0};
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				if (img[i][j] == 1) {
					mark[0] = (i / img.length);
					mark[1] = (j / img[0].length);
					System.out.println("mark: " + mark[0] + ", " + mark[1]);
				}
			}
		}
		return mark;
	}
	
	//bringing mark to center of x-axis of img
	public void allignRotationX() {
		//while mark is not centered
		while ((Math.abs(posOfMark()[0] - .5)) > .1) {
			//if mark is on left side of img
			if (posOfMark()[0] < .5) {
				rotate(rotation_rate * 1);
				theta_cam += (rotation_rate * 1);
			}
			//if mark is on right side of img
			else {
				rotate(rotation_rate);
				theta_cam += rotation_rate;
			}
		}
	}
	
	public void rotate (double theta) {
		//somehow rotate the camera theta degrees in the x direction
	}
	
	
	//calculates the velocity needed to hit the hole - math is still wrong
	public double velCalculated(double shooter_h, double distance, double theta_cam, double theta_board) {
		return 0.0; // return (Math.sqrt(-1 * (4.9*base_d_straight*base_d_straight + 9.8*base_d_straight*x + 4.9*x*x) / (y - shooter_h - base_d_straight*Math.tan(a))))
	}
	
	//simulating throw and landing
	public double getXBall (double vel, double time, double theta, double initX) {
		return ((vel * Math.cos(theta) * time) + initX);
	}

	public double getYBall (double vel, double time, double theta, double initY) {
		return ((vel * Math.sin(theta) * time) + initY);
	}
	
	public double getZBall (double vel, double time, double phi, double initZ) {
		return ((vel * Math.sin(phi) * time) - (4.9 * (vel * vel)) + initZ);
	}
	
	public double timeAtBoard (double vel, double phi, double initZ, double holeZ) {
		return ((vel * Math.sin(phi) + Math.sqrt((vel* vel) * (Math.pow(Math.sin(phi), 2)) - 19.6 * (initZ - holeZ))) / 9.8);
	}
	
	public double[] posAtBoard(double vel, double theta, double phi, double initX, double initY, double initZ, double holeZ) {
		double t = timeAtBoard(vel, theta, initZ, holeZ);
		double x = getXBall(vel,t,theta,initX);
		double y = getYBall(vel,t,theta,initY);
		double z = getZBall(vel,t,phi,initZ);
		
		double[] pos = {x, y, z, t};
		return pos;
	}

	//euclidean error between where ball lands and target hole
	public double euclideanError(int[] targetPos, int[] pos) {
		return (Math.sqrt((pos[0] - Math.pow(targetPos[0], 2)) + (pos[1] - Math.pow(targetPos[1], 2)) + (pos[2] - Math.pow(targetPos[2], 2))));
	}
	
	
	
}
