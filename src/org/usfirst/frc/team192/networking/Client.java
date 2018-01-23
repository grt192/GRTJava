package org.usfirst.frc.team192.networking;

import java.net.DatagramPacket;
import java.net.InetAddress;

class Client {

	private final InetAddress address;
	private final int port;

	public Client(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	public Client(DatagramPacket packet) {
		this(packet.getAddress(), packet.getPort());
	}

	public DatagramPacket addressPacket(byte[] data) {
		return new DatagramPacket(data, data.length, address, port);
	}

}
