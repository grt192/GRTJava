package org.usfirst.frc.team192.mechs.gperiod;

import edu.wpi.first.wpilibj.Solenoid;

public class Lever extends Thread {

	private Solenoid solenoid;
	private boolean busy;

	public Lever(Solenoid solenoid) {
		this.solenoid = solenoid;
		busy = false;
	}

	public void up() {
		solenoid.set(false);
	}

	public void down() {
		solenoid.set(true);
	}

	public void downAndUp() {
		if (!busy)
			run();
	}

	@Override
	public void run() {
		busy = true;
		down();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		up();
		busy = false;
	}
}
