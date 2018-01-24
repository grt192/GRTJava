package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.networking.NetworkServer;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */

	private NetworkServer gyroServer;
	private GyroBase gyro;
	private XboxController xbox;
	private TalonSRX[] talons;

	@Override
	public void robotInit() {
		// gyro = new ADXRS450_Gyro();
		// try {
		// gyroServer = new NetworkServer(1920, "Gyro");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		xbox = new XboxController(1);
		talons = new TalonSRX[16];
		for (int i = 0; i < 16; i++)
			talons[i] = new TalonSRX(i + 1);

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
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

	}

	@Override
	public void disabledInit() {
	}

	@Override
	public void testInit() {

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		if (xbox.getAButtonPressed()) {
			for (int i = 0; i < 16; i++) {
				TalonSRX test = talons[i];
				System.out.println(
						"ID: " + test.getDeviceID() + "; QuadPos: " + test.getSensorCollection().getQuadraturePosition()
								+ "; AnalPos: " + test.getSensorCollection().getAnalogIn());
			}
		}
		// if (gyroServer != null) {
		// byte[] buf = new byte[Double.BYTES];
		// ByteBuffer.wrap(buf).putDouble(gyro.getAngle());
		// gyroServer.sendMessage(buf);
		// }
	}
}
