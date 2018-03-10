package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.config.Config;
import org.usfirst.frc.team192.fieldMapping.FieldMapperThreadEncoder;
import org.usfirst.frc.team192.mechs.Elevator;
import org.usfirst.frc.team192.mechs.Intake;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.swerve.NavXGyro;
import org.usfirst.frc.team192.vision.VisionThread;
import org.usfirst.frc.team192.vision.nn.RemoteVisionThread;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	private NavXGyro gyro;
	private FullSwervePID swerve;
	private FieldMapperThreadEncoder fieldMapperEncoder;

	// private RemoteVisionThread vision;
	private Autonomous auto;
	private Teleop teleop;
	private Elevator elevator;
	private Intake intake;
	private XboxController input;

	@Override
	public void robotInit() {
		Config.start();
		input = new XboxController(0);
		gyro = new NavXGyro();
		swerve = new FullSwervePID(gyro);
		fieldMapperEncoder = new FieldMapperThreadEncoder(gyro, swerve);
		elevator = new Elevator();
		intake = new Intake();
		VisionThread vision = new RemoteVisionThread(640, 480);
		vision.start();
		VisionSwerve visionSwerve = new VisionSwerve(vision, swerve);
		teleop = new Teleop(swerve, intake, elevator, visionSwerve);
		auto = new Autonomous(swerve, intake, elevator);
	}

	@Override
	public void autonomousInit() {
		auto.init();
	}

	@Override
	public void autonomousPeriodic() {
		auto.periodic();
	}

	@Override
	public void teleopInit() {
		swerve.enable();
		fieldMapperEncoder.reset();
		new Thread(fieldMapperEncoder).start();
		gyro.resetDisplacement();
		teleop.init();
	}

	private long start = System.currentTimeMillis();

	@Override
	public void teleopPeriodic() {
		SmartDashboard.putNumber("X Displacement (encoders)", fieldMapperEncoder.getX());
		SmartDashboard.putNumber("Y Displacement (encoders)", fieldMapperEncoder.getY());
		teleop.periodic();
		SmartDashboard.putNumber("Cycle time", (System.currentTimeMillis() - start) / 1000.0);
		start = System.currentTimeMillis();
	}

	@Override
	public void disabledInit() {
		swerve.disable();
	}

	private int index;

	@Override
	public void testInit() {
		index = 0;
		System.out.println(index + 1);
	}

	@Override
	public void testPeriodic() {
		// swerve.controllerZero(input);
		if (input.getAButtonPressed()) {
			index++;
			index %= 16;
			System.out.println(index + 1);
		}
		new TalonSRX(index + 1).set(ControlMode.PercentOutput, input.getX(Hand.kLeft));
	}
}
