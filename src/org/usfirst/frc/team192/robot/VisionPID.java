package org.usfirst.frc.team192.robot;

import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.VisionTracking;

import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionPID implements PIDOutput, PIDSource{
	private PIDController angle_pid;
	//private PIDController distance_pid;
	private FullSwervePID swerve;
	private VisionTracking vision;
	boolean changeoutput;
	boolean runforward;
	private GyroBase gyro;
	
	private org.opencv.core.Point CAMCENTER;
	private final int CAMERA_WIDTH = 640;
	private final int CAMERA_HEIGHT = 480;
	
	private double initp;
	private double initi;
	private double initd;
	private double initf;
	
	public VisionPID(VisionTracking vision, FullSwervePID swerve, GyroBase gyro){
		runforward = false;
		CAMCENTER = new org.opencv.core.Point();
		CAMCENTER.x = CAMERA_WIDTH / 2;
		CAMCENTER.y = CAMERA_HEIGHT / 2;
		this.swerve = swerve;
		this.vision = vision;
		this.gyro = gyro;
		
		initp = 0.003;
		initi = 0.0;
		initd = 0.1;
		initf = 0.001;
		
		double p = initp;
		double i = initi;
		double d = initd;
		double f = initf;
		SmartDashboard.putNumber("p_vision", p);
		SmartDashboard.putNumber("i_vision", i);
		SmartDashboard.putNumber("d_vision", d);
		SmartDashboard.putNumber("f_vision", f);
		SmartDashboard.putNumber("blockLocation", 0);
		
		angle_pid = new PIDController(p, i, d, f, this, this, 0.01);
		angle_pid.setInputRange(0, 640);
		//angle_pid.setContinuous();
		angle_pid.setAbsoluteTolerance(3.0);
		angle_pid.setOutputRange(-1.0, 1.0);
		angle_pid.reset();
		angle_pid.setSetpoint(CAMCENTER.x);
		
		SmartDashboard.putData(angle_pid);

	}

	public void updatePID() {
		double p = SmartDashboard.getNumber("p_vision", initp);
		double i = SmartDashboard.getNumber("i_vision", initi);
		double d = SmartDashboard.getNumber("d_vision", initd);
		double f = SmartDashboard.getNumber("f_vision", initf);
		angle_pid.setPID(p, i, d, f);
	}
	
	public void PIDEnable() {
		angle_pid.reset();
		angle_pid.enable();
		changeoutput = true;
	}
	
	public double getCamCenter() {
		return CAMCENTER.x;
	}
	
	public void getValue() {
		System.out.println(angle_pid.get());
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return CAMCENTER.x;
	}
	

	@Override
	public void pidWrite(double output) {
		// double camerror = CAMCENTER.x - vision.getCenter().x;
		// System.out.println(output);
		System.out.println(output);
		swerve.setWithAngularVelocity(0, 0, output);
		
		/*
		if (changeoutput) {
			if (Math.abs(camerror) < 50) {
				runforward = true;
			}else {
				runforward = false;
			}
			
			if (camerror < 0) {
				if (runforward){
					swerve.setWithAngularVelocity(.7, .5, 0);
				}else {
					swerve.setWithAngularVelocity(.7, .5, 0);
				}
			} else if (camerror > 0) {
				if (runforward){
					swerve.setWithAngularVelocity(.7, -.5, 0);
				}else {
					swerve.setWithAngularVelocity(.7, -.5, 0);
				}
			}
		}
		*/
	}
	
	public void update() {
		// angle_pid.setSetpoint(vision.getCenter().x);
		// angle_pid.setSetpoint(SmartDashboard.getNumber("blockLocation", CAMCENTER.x));
		double currentAngle = Math.toRadians((gyro.getAngle() % 360 + 360) % 360);
		double targetAngle = SmartDashboard.getNumber("blockLocation", currentAngle);
		double deltaAngle = targetAngle - currentAngle;
		if (Math.abs(deltaAngle) > Math.PI) {
			deltaAngle = deltaAngle - 2 * Math.PI * Math.signum(deltaAngle);
		}
		SmartDashboard.putNumber("deltaAngle", deltaAngle);
		angle_pid.setSetpoint(CAMCENTER.x + CAMERA_WIDTH * deltaAngle / Math.PI);
	}

}
