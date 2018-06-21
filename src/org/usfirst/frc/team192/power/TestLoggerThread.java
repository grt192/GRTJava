package org.usfirst.frc.team192.power;

import org.usfirst.frc.team192.logging.SimpleLogger;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.hal.PowerJNI;

public class TestLoggerThread extends Thread {

	private SimpleLogger batteryLogger;
	private SimpleLogger everythingLogger;
	private BufferedLinearRegressor batteryRegression;
	private PowerDistributionPanel pdp;
	private Compressor compressor;

	private volatile boolean write = false;

	public TestLoggerThread() {
		batteryLogger = new SimpleLogger("Voc", "Rint");
		everythingLogger = new SimpleLogger("PDP Voltage", "PDP Total Current", "PDP Sum Current", "PowerJNI Voltage",
				"PowerJNI Current", "Compressor Current");
		pdp = new PowerDistributionPanel();
		compressor = new Compressor();
		batteryRegression = new BufferedLinearRegressor(5);
		batteryRegression.add(pdp.getVoltage(), pdp.getTotalCurrent());
	}

	@Override
	public void run() {
		while (true) {
			if (write)
				writeLoggers();
			batteryRegression.add(pdp.getVoltage(), pdp.getTotalCurrent());
			batteryLogger.add(batteryRegression.getB(), -batteryRegression.getM());
			double pdpSum = 0;
			for (int i = 0; i < 16; i++)
				pdpSum += pdp.getCurrent(i);
			everythingLogger.add(pdp.getVoltage(), pdp.getTotalCurrent(), pdpSum, PowerJNI.getVinVoltage(),
					PowerJNI.getVinCurrent(), compressor.getCompressorCurrent());
			Timer.delay(0.5);
		}
	}

	public void write() {
		write = true;
	}

	private void writeLoggers() {
		batteryLogger.write("Battery Output", true, true);
		everythingLogger.write("Other Power Stats", true, true);
		write = false;
	}
}
