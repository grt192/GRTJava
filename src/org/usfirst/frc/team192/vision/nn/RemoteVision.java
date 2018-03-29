package org.usfirst.frc.team192.vision.nn;

import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.usfirst.frc.team192.networking.NetworkServer;

class RemoteVision extends Thread {

	private final int QUALITY = 90;
	private final int BUFFER_SIZE = 60000;
	private MatOfInt params;

	private int port;
	private NetworkServer server;
	private RemoteVisionThread rvt;

	private byte[] buffer;

	public RemoteVision(int port, RemoteVisionThread rvt) {
		this.rvt = rvt;
		params = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 0);
		buffer = new byte[BUFFER_SIZE];
		this.port = port;
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
		server = new NetworkServer(port, "Vision");
		server.waitUntilConnected();
	}

	private void safeRun() {
		double[] data;
		while (true) {
			try {
				data = server.receiveDoubles(3);
				rvt.recieve(data);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
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
		buf.release();
		try {
			server.sendBytes(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
