package org.usfirst.frc.team192.robot;

import org.opencv.core.Point;

import org.usfirst.frc.team192.vision.VisionTracking;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionPID implements PIDOutput, PIDSource{
	private PIDController angle_pid;
	private PIDController distance_pid;
	
	private org.opencv.core.Point CAMCENTER;
	
	public VisionPID(){
		CAMCENTER.x = 0;
		CAMCENTER.y = 0;
		
		double p = 0.05;
		double i = 0.05;
		double d = 0.05;
		double f = 0.05;
		angle_pid = new PIDController(p, i, d, f, this, this, 0.01);
		angle_pid.setContinuous();
		angle_pid.setInputRange(0, 360);
		angle_pid.setAbsoluteTolerance(3.0);
		angle_pid.setOutputRange(-1.0, 1.0);
		angle_pid.reset();
		angle_pid.setSetpoint(0.0);
		
		distance_pid = new PIDController(p, i, d, f, this, this, 0.01);
		distance_pid.setContinuous();
		distance_pid.setInputRange(0,100);
		distance_pid.setAbsoluteTolerance(3.0);
		distance_pid.setOutputRange(-1.0, 1.0);
		distance_pid.reset();
		distance_pid.setSetpoint(0.0);
	}
	
	public void PIDEnable() {
		angle_pid.reset();
		angle_pid.enable();
		distance_pid.reset();
		distance_pid.enable();
	}
	
	public void updateWithVision(VisionTracking vision) {
		Point center = vision.getCenter();
		int area = vision.getArea();
		int AREACONSTANT = 1;
		angle_pid.setSetpoint(center.x - CAMCENTER.x);
		distance_pid.setSetpoint(area/AREACONSTANT);
		
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		
	}

}
