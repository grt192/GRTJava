package org.usfirst.frc.team192.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

public class NetworkServer extends Thread {

	private DatagramSocket socket;
	private boolean running;

	private ArrayList<Client> clients;

	public NetworkServer(int port) throws IOException {
		socket = new DatagramSocket(port, InetAddress.getLocalHost());
		System.out.println(
				"Robot recieving at " + socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort());
		clients = new ArrayList<>();
		start();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			DatagramPacket recieve = new DatagramPacket(new byte[4], 4);
			try {
				socket.receive(recieve);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			String message = new String(recieve.getData());
			if (message.equals("join"))
				clients.add(new Client(recieve));
		}
		socket.close();
	}

	public void sendMessage(byte[] message) {
		Iterator<Client> itr = clients.iterator();
		while (itr.hasNext()) {
			Client c = itr.next();
			try {
				socket.send(c.addressPacket(message));
			} catch (IOException e) {
				e.printStackTrace();
				itr.remove();
			}
		}
	}

	public void close() {
		running = false;
	}

}
