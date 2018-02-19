package org.usfirst.frc.team192.swerve;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class NavXGyro extends AHRS implements Gyro {

	public NavXGyro() {
		super(SPI.Port.kMXP);
	}

	@Override
	public void calibrate() {
		reset();
	}

}
