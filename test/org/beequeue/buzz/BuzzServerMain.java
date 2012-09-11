package org.beequeue.buzz;

import java.io.File;

public class BuzzServerMain {
	public static void main(String[] args) {
		BuzzServer bz = new BuzzServer(9753);
		bz.setRoot(new File("web"));
		bz.start();
	}
}
