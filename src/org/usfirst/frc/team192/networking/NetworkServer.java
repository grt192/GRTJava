package org.usfirst.frc.team192.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class NetworkServer extends Thread {

	private DatagramSocket socket;
	private boolean connected;
	private InetAddress clientAddress;
	private int clientPort;

	public NetworkServer(int port, String name) throws IOException {
		socket = new DatagramSocket(port, InetAddress.getByName("roborio-192-frc.local"));
		System.out.println(name + " server joinable at " + socket.getLocalAddress().getHostAddress() + ":"
				+ socket.getLocalPort());
		connected = false;
		start();
	}

	@Override
	public void run() {
		byte[] buffer = new byte[4];
		while (!connected) {
			DatagramPacket receive = new DatagramPacket(buffer, 4);
			try {
				socket.receive(receive);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			String message = new String(buffer);
			if (message.equals("join")) {
				clientAddress = receive.getAddress();
				clientPort = receive.getPort();
				connected = true;
				System.out.println("Connected to " + clientAddress.getHostAddress() + ":" + clientPort);
			}
		}
	}

	public void waitUntilConnected() throws InterruptedException {
		join();
	}

	public double[] receiveDoubles(int n) throws IOException {
		byte[] buffer = receiveBytes(n * Double.BYTES);
		double[] data = new double[n];
		ByteBuffer parser = ByteBuffer.wrap(buffer);
		for (int i = 0; i < n; i++) {
			data[i] = parser.getDouble();
		}
		return data;
	}

	public float[] receiveFloats(int n) throws IOException {
		byte[] buffer = receiveBytes(n * Float.BYTES);
		float[] data = new float[n];
		ByteBuffer parser = ByteBuffer.wrap(buffer);
		for (int i = 0; i < n; i++) {
			data[i] = parser.getFloat();
		}
		return data;
	}

	public int[] receiveInts(int n) throws IOException {
		byte[] buffer = receiveBytes(n * Integer.BYTES);
		int[] data = new int[n];
		ByteBuffer parser = ByteBuffer.wrap(buffer);
		for (int i = 0; i < n; i++) {
			data[i] = parser.getInt();
		}
		return data;
	}

	public String receiveString(int n) throws IOException {
		byte[] buf = receiveBytes(n);
		return new String(buf);
	}

	public byte[] receiveBytes(int n) throws IOException {
		checkConnection();
		byte[] buffer = new byte[n];
		DatagramPacket receive = new DatagramPacket(buffer, n);
		socket.receive(receive);
		return buffer;
	}

	public void sendDoubles(double[] message) throws IOException {
		checkConnection();
		byte[] buffer = new byte[message.length * Double.BYTES];
		ByteBuffer parser = ByteBuffer.wrap(buffer);
		for (int i = 0; i < message.length; i++) {
			parser.putDouble(message[i]);
		}
		sendBytes(buffer);
	}

	public void sendFloats(float[] message) throws IOException {
		checkConnection();
		byte[] buffer = new byte[message.length * Float.BYTES];
		ByteBuffer parser = ByteBuffer.wrap(buffer);
		for (int i = 0; i < message.length; i++) {
			parser.putFloat(message[i]);
		}
		sendBytes(buffer);
	}

	public void sendInts(int[] message) throws IOException {
		checkConnection();
		byte[] buffer = new byte[message.length * Integer.BYTES];
		ByteBuffer parser = ByteBuffer.wrap(buffer);
		for (int i = 0; i < message.length; i++) {
			parser.putInt(message[i]);
		}
		sendBytes(buffer);
	}

	public void sendString(String s) throws IOException {
		sendBytes(s.getBytes());
	}

	public void sendBytes(byte[] message) throws IOException {
		checkConnection();
		DatagramPacket send = new DatagramPacket(message, message.length);
		send.setAddress(clientAddress);
		send.setPort(clientPort);
		socket.send(send);
	}

	private void checkConnection() throws IOException {
		if (!connected)
			throw new IOException("There is no client connected");
	}

	public void close() {
		socket.close();
	}

	public boolean isConnected() {
		return connected;
	}

}
