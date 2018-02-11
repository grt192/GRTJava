package org.usfirst.frc.team192.swerve;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class NavXGyro extends AHRS implements Gyro {

	public NavXGyro(Port serial_port_id) {
		super(serial_port_id);
	}

	@Override
	public void calibrate() {
		reset();
	}

}
