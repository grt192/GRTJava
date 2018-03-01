package org.usfirst.frc.team192.robot;

import org.opencv.core.Point;

public class KalmanFilter {
	double[][] K;
	double[][] F = {{-1,1}, {0,1}};
	double[][] B;
	double[][] z;
	double[][] Q = {{1,0}, {0,1}};
	double[][] R = {{-1,0}, {0,-1}};
	double[][] I = {{1,0}, {0,1}};
	double[][] P;
	double[][] u;
	double[][] x;
	double[][] neg = {{-1,0}, {0,-1}};
	double startTime;
	double durtation;
	
	public KalmanFilter() {
		double[][] u = {{0}, {0}};
		double[][] x = {{0}, {0}};
		double[][] z = {{0}, {0}};
		double[][] K = {{0, 0}, {0, 0}};
		double[][] B = {{0, 0}, {0, 0}};
		double[][] P = {{0, 0}, {0, 0}};

		
	}
	
	public void calculateCent() {
		double startTime = System.nanoTime();
		
		//equations();
		
		double duration = System.nanoTime() - startTime;
		System.out.println(duration);
	}
	
	public void equations(Point center, double velo) {
		u[0][0] = velo;
		x[0][0] =  center.x;
		x[1][0] = 320;
		z[0][0] = velo;
		
		K = multiplicar(matrixAdd(multiplicar(multiplicar(F, P), transposeMatrix(F)), Q),
				(matrixAdd(matrixAdd(multiplicar(multiplicar(F, P), transposeMatrix(F)), Q), R)));
		x = matrixAdd(multiplicar(matrixAdd(I, multiplicar(neg, K)), 
				matrixAdd(multiplicar(F, x), multiplicar(B, u))), multiplicar(K, z));
		P = multiplicar(matrixAdd(I, multiplicar(neg, K)), (matrixAdd(multiplicar(multiplicar(F, P), 
				transposeMatrix(F)), Q)));
		
	}
	
	public static double[][] matrixAdd(double[][] A, double[][] B){
	    // Check if matrices have contents
		if ((A.length < 0) || (A[0].length < 0)) return B;
	    if ((B.length < 0) || (B[0].length < 0)) return A;

	    // create new matrix to store added values in
	    double[][] C = new double[A.length][A[0].length];

	    for (int i = 0; i < A.length; i++)
	    {
	        for (int j = 0; j < A[i].length; j++)
	        {
	            C[i][j] = A[i][j] + B[i][j];
	        }
	    }
	    return C;
	}
	
	 public double[][] multiplicar(double[][] A, double[][] B) {

		 int aRows = A.length;
		 int aColumns = A[0].length;
		 int bRows = B.length;
		 int bColumns = B[0].length;

		 if (aColumns != bRows) {
			 throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
		 }

		 double[][] C = new double[aRows][bColumns];
		 for (int i = 0; i < aRows; i++) {
			 for (int j = 0; j < bColumns; j++) {
				 C[i][j] = 0.00000;
			 }
		 }

		 for (int i = 0; i < aRows; i++) { // aRow
			 for (int j = 0; j < bColumns; j++) { // bColumn
				 for (int k = 0; k < aColumns; k++) { // aColumn
					 C[i][j] += A[i][k] * B[k][j];
				 }
			 }
		 }

		 return C;
	 }
	 
	public double[][] transposeMatrix(double [][] m){
		double[][] temp = new double[m[0].length][m.length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				temp[j][i] = m[i][j];
		return temp;
	}
	   
	public double[][] inverse(double m[][]) {
		double det, temp;
		
		det = (m[0][0] * m[1][1]) - (m[0][1] * m[1][0]);
		
		System.out.println("\ndeterminant = " + det);
		
		temp = m[0][0];
		m[0][0] = m[1][1];
		m[1][1] = temp;
		
		m[0][1] = - m[0][1];
		m[1][0] = - m[1][0];
		
		return m;
	}  
	   
	   
	
}
