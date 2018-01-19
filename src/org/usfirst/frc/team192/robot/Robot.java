package org.usfirst.frc.team192.robot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.swerve.FullSwerve;
import org.usfirst.frc.team192.swerve.SwerveBase;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private JoystickInput input;
	private ADXRS450_Gyro gyro;
	private Config config;

	private double ROBOT_WIDTH = 0.8128;
	private double ROBOT_HEIGHT = 0.7112;

	private SwerveBase swerve;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		gyro = new ADXRS450_Gyro();
		config = new Config("/home/lvuser/robot.conf");
		swerve = new FullSwerve(config, gyro);
		input = new JoystickInput(0, 1);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		swerve.enable();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		swerve.update(input);

	}

	@Override
	public void disabledInit() {
		swerve.disable();
	}

	@Override
	public void testInit() {
		String fileName = "/home/lvuser/hello";
		// System.out.println("reading and writing from a file");
		// try {
		// File f = new File(fileName);
		// f.createNewFile();
		// PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
		// out.println("hello hello");
		// out.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			System.out.println("file: " + br.readLine());
			br.close();
		} catch (IOException e) {
			System.out.println("can't read");
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

	}
}
