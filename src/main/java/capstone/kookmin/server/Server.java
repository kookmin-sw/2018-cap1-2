package capstone.kookmin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	
	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("===== Server Started =====");
			System.out.println("ip: "+serverSocket.getInetAddress()+" ("+port+")\n");
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void run() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
		executor.scheduleAtFixedRate(()->{
			try {
				socket = serverSocket.accept();
				new Worker(socket).start();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public void close() {
		try {
			serverSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}