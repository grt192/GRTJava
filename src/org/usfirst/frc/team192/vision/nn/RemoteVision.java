package org.usfirst.frc.team192.vision.nn;

import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.usfirst.frc.team192.networking.NetworkServer;

public class RemoteVision extends Thread {

	private final int QUALITY = 90;
	private final int BUFFER_SIZE = 55000;
	private MatOfInt params;

	private NetworkServer server;
	private Point point;

	private long timeOfLastData;
	private byte[] buffer;

	public RemoteVision() {
		point = new Point();
		params = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 0);
		buffer = new byte[BUFFER_SIZE];
		start();
	}

	@Override
	public void run() {
		try {
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		try {
			safeRun();
		} finally {
			server.close();
		}

	}

	private void initialize() throws IOException, InterruptedException {
		server = new NetworkServer(1920, "Vision");
		server.waitUntilConnected();
	}

	private void safeRun() {
		double[] data;
		while (true) {
			try {
				data = server.receiveDoubles(2);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			point.set(data);
			timeOfLastData = System.currentTimeMillis();
		}
	}

	public void sendImage(Mat img) {
		if (server == null || !server.isConnected())
			return;
		params.put(1, 0, QUALITY);
		MatOfByte buf = new MatOfByte();
		int temp = QUALITY;
		Imgcodecs.imencode(".jpeg", img, buf, params);
		while (buf.total() > BUFFER_SIZE) {
			temp -= 10;
			params.put(1, 0, temp);
			Imgcodecs.imencode(".jpeg", img, buf, params);
		}
		buf.get(0, 0, buffer);
		try {
			server.sendBytes(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public long getAgeOfData() {
		return System.currentTimeMillis() - timeOfLastData;
	}

	public Point getCenter() {
		if (getAgeOfData() < 1000)
			return point;
		else
			return null;
	}

}
