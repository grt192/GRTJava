package org.usfirst.frc.team192.filter;

import org.opencv.core.*;

public abstract class KalmanFilter {
	protected Mat x;
	protected Mat F;
	protected Mat FT;
	protected Mat B;
	protected Mat P;
	protected Mat Q;
	protected Mat H;
	protected Mat HT;
	
	public KalmanFilter(Mat x) {
		this.x = x;
	}
	
	public void predictEquations(Mat x, Mat P, Mat u) {
		Core.gemm(F, x, 1, null, 0, x);
		if(u != null) {
			Core.gemm(B, u, 1, null, 0, u);
			Core.add(x, u, x);
		}
		
		Core.gemm(F, P, 1, null, 0, P);
		Core.transpose(F, FT);
		Core.gemm(P, FT, 1, Q, 1, P);
	}
	
	public void updateEquations(Mat y, Mat z, Mat R, Mat S, Mat K) {
		Mat SI = null;
		Core.gemm(H, x, -1, z, 1, y);
		
		Core.gemm(H, P, 1, null, 0, P);
		Core.transpose(H, HT);
		Core.gemm(P, HT, 1, R, 1, S);
		
		Core.invert(S, SI);
		Core.gemm(P, HT, 1, null, 0, P);
		Core.gemm(P, SI, 1, null, 0, K);
		
		Core.gemm(K, y, 1, x, 1, x);
		
		Core.gemm(K, K, 1, null, 0, H);
		Core.gemm(K, P, -1, P, 1, P);
		
		Core.gemm(H, x, -1, z, 1, y);
	}
		
	

}
