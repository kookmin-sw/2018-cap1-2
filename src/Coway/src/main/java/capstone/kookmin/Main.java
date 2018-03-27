package capstone.kookmin;

import capstone.kookmin.server.Server;

public class Main {
	public static void main(String[] args) {
		Server server = new Server(13579);
		server.run();
	}
}