package org.usfirst.frc.team192.power;

public class BufferedLinearRegressor {

	private double[] x;
	private double[] y;

	private int index = 0;
	private boolean changed = true;
	private boolean full = false;

	private double m, b;

	public BufferedLinearRegressor(int size) {
		x = new double[size];
		y = new double[size];
	}

	public void add(double x, double y) {
		this.x[index] = x;
		this.y[index] = y;
		if (!full)
			full = index == (this.x.length - 1);
		index = (++index) % this.x.length;
		changed = true;
	}

	public double getM() {
		if (changed) {
			calculate();
			changed = false;
		}
		return m;
	}

	public double getB() {
		if (changed) {
			calculate();
			changed = false;
		}
		return b;
	}

	private void calculate() {
		double mX = 0;
		double mY = 0;
		int length = full ? x.length : index + 1;
		for (int i = 0; i < length; i++) {
			mX += x[i];
			mY += y[i];
		}
		mX /= length;
		mY /= length;
		double cov = 0;
		double var = 0;
		for (int i = 0; i < length; i++) {
			double temp = (x[i] - mX);
			cov += (y[i] - mY) * temp;
			var += temp * temp;
		}
		m = cov / var;
		b = mY - m * mX;
	}

}
