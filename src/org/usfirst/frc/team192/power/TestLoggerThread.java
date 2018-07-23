package org.usfirst.frc.team192.power;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.logging.SimpleLogger;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.hal.PowerJNI;

public class TestLoggerThread extends Thread {

	private SimpleLogger batteryLogger;
	private SimpleLogger everythingLogger;
	private SimpleLogger pickupMotorLogger;
	private SimpleLogger driveMotorLogger;
	private BufferedLinearRegressor batteryRegression;
	private PowerDistributionPanel pdp;
	private Compressor compressor;

	private TalonSRX pickupMotor;
	private TalonSRX driveMotor;

	private volatile boolean write = false;

	public TestLoggerThread() {
		batteryLogger = new SimpleLogger("Voc", "Rint");
		everythingLogger = new SimpleLogger("PDP Voltage", "PDP Total Current", "PDP Sum Current", "PowerJNI Voltage",
				"PowerJNI Current", "Compressor Current");
		pickupMotor = new TalonSRX(Config.getInt("upper_flywheel"));
		driveMotor = new TalonSRX(Config.getInt("fl_drive_port"));
		pickupMotorLogger = new SimpleLogger("Input Voltage", "Output Voltage", "Percent Out", "Output Current");
		driveMotorLogger = new SimpleLogger("Input Voltage", "Output Voltage", "Percent Out", "Output Current",
				"Speed");
		pdp = new PowerDistributionPanel();
		compressor = new Compressor();
		batteryRegression = new BufferedLinearRegressor(50);
		batteryRegression.add(pdp.getVoltage(), pdp.getTotalCurrent());
	}

	@Override
	public void run() {
		while (true) {
			if (write)
				writeLoggers();
			batteryRegression.add(pdp.getVoltage(), pdp.getTotalCurrent());
			batteryLogger.add(batteryRegression.getB(), -batteryRegression.getM());
			pickupMotorLogger.add(pickupMotor.getBusVoltage(), pickupMotor.getMotorOutputVoltage(),
					pickupMotor.getMotorOutputPercent(), pickupMotor.getOutputCurrent());
			driveMotorLogger.add(driveMotor.getBusVoltage(), driveMotor.getMotorOutputVoltage(),
					driveMotor.getMotorOutputPercent(), driveMotor.getOutputCurrent(),
					driveMotor.getSensorCollection().getQuadratureVelocity());
			double pdpSum = 0;
			for (int i = 0; i < 16; i++)
				pdpSum += pdp.getCurrent(i);
			everythingLogger.add(pdp.getVoltage(), pdp.getTotalCurrent(), pdpSum, PowerJNI.getVinVoltage(),
					PowerJNI.getVinCurrent(), compressor.getCompressorCurrent());
			Timer.delay(0.05);
		}
	}

	public void write() {
		write = true;
	}

	private void writeLoggers() {
		batteryLogger.write("Battery Output", true, true, true);
		everythingLogger.write("Other Power Stats", true, true, true);
		pickupMotorLogger.write("Pickup Motor Output", true, true, true);
		driveMotorLogger.write("Drive Motor Output", true, true, true);
		write = false;
	}
}
