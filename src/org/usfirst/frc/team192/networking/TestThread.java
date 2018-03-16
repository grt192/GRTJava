package org.usfirst.frc.team192.networking;

public class TestThread extends Thread {

	public TestThread() {

	}

	@Override
	public void run() {
		try {
			NetworkServer ns = new NetworkServer(1922, "Test");
			ns.waitUntilConnected();
			while (true) {
				String s = ns.receiveString(5);
				ns.sendString(s.toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
