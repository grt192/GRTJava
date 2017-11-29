package org.usfirst.frc.team192.vision;

public class ShooterCalcTester {
	private static double distance;

	public ShooterCalcTester() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		ShooterCalc chopper = new ShooterCalc();
		distance = chopper.distanceToPoint();
		chopper.allignRotationX();
		
		

	}

}
