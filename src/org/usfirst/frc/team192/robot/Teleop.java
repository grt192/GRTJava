package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.mechs.Chalupa;
import org.usfirst.frc.team192.mechs.Collection;
import org.usfirst.frc.team192.mechs.Lever;
import org.usfirst.frc.team192.mechs.Shooter;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Solenoid;

public class Teleop {

	private JoystickInput g_joysticks;
	private JoystickInput f_joysticks;

	private Chalupa g_chalupa;
	private Chalupa f_chalupa;

	private Collection g_collection;
	private Collection f_collection;

	private Shooter g_shooter;
	private Lever g_lever;

	private Solenoid f_release;
	private boolean f_open;

	private boolean prevShoot, prevChalupa, prevCollection;

	public Teleop() {
		g_joysticks = new JoystickInput(0, 1);
		f_joysticks = new JoystickInput(0, 2);

		CANTalon[] talons = new CANTalon[16];
		for (int i = 0; i < talons.length; i++)
			talons[i] = new CANTalon(i + 1);

		// TODO: Get actual talon/solenoid assignments
		g_chalupa = new Chalupa(talons[0]);
		g_collection = new Collection(talons[1], talons[2]);
		g_shooter = new Shooter(talons[3], talons[4]);
		g_lever = new Lever(new Solenoid(0));

		f_chalupa = new Chalupa(talons[0]);
		f_collection = new Collection(talons[1], talons[2]);
		f_release = new Solenoid(0);

		prevShoot = false;
		prevChalupa = false;
		prevCollection = false;

	}

	public void init() {

	}

	public void periodic() {
		doSwerve();
		executeGPeriodControl();
		executeFPeriodControl();
	}

	private void doSwerve() {
		// TODO: Do whatever swerve stuff here when Jonny pushes his code
	}

	private void executeGPeriodControl() {
		g_shooter.setFlywheelSpeed(g_joysticks.getClippedY(Hand.kLeft));
		g_shooter.setTurnTableSpeed(g_joysticks.getClippedX(Hand.kRight));

		if (g_joysticks.getLeverButton())
			g_lever.downAndUp();

		if (g_joysticks.getChalupaButton() && !prevChalupa)
			g_chalupa.toggle();

		if (g_joysticks.getCollectionButton() && !prevCollection)
			g_collection.toggle();

		System.out.println("Flywheel speed:" + g_shooter.getFlywheelSpeed());

		prevChalupa = g_joysticks.getChalupaButton();
		prevCollection = g_joysticks.getCollectionButton();
	}

	private void executeFPeriodControl() {
		if (f_joysticks.getShooterButton() && !prevShoot) {
			if (f_open)
				f_release.set(false);
			else
				f_release.set(true);
			f_open = !f_open;
		}

		if (f_joysticks.getChalupaButton() && !prevChalupa)
			f_chalupa.toggle();

		if (f_joysticks.getCollectionButton() && !prevCollection)
			f_collection.toggle();

		prevShoot = f_joysticks.getShooterButton();
		prevChalupa = f_joysticks.getChalupaButton();
		prevCollection = f_joysticks.getCollectionButton();
	}
}
