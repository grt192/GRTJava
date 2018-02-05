package org.usfirst.frc.team192.robot;

import org.opencv.core.Point;
import org.usfirst.frc.team192.swerve.FullSwervePID;
import org.usfirst.frc.team192.vision.VisionTracking;

import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class VisionPID implements PIDOutput, PIDSource{
	private PIDController angle_pid;
	//private PIDController distance_pid;
	private FullSwervePID swerve;
	private VisionTracking vision;
	boolean changeoutput;
	boolean runforward;
	
	private org.opencv.core.Point CAMCENTER;
	
	public VisionPID(VisionTracking vision, FullSwervePID swerve){
		runforward = false;
		CAMCENTER = new org.opencv.core.Point();
		CAMCENTER.x = 320;
		CAMCENTER.y = 240;
		this.swerve = swerve;
		this.vision = vision;
		
		double p = 0.3;
		double i = 0.00;
		double d = 0.0001;
		double f = 0.1;
		angle_pid = new PIDController(p, i, d, f, this, this, 0.01);
		angle_pid.setInputRange(0, 640);
		//angle_pid.setContinuous();
		angle_pid.setAbsoluteTolerance(3.0);
		angle_pid.setOutputRange(-1.0, 1.0);
		angle_pid.reset();
		angle_pid.setSetpoint(CAMCENTER.x);

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
		Point center = vision.getCenter();
		//System.out.println(center.x);
		return center.x;
	}
	

	@Override
	public void pidWrite(double output) {
		double camerror = CAMCENTER.x - vision.getCenter().x;
		System.out.println(output);
		
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
	}
	
	public void setSetpoint(double point) {
		angle_pid.setSetpoint(point);
	}

}
