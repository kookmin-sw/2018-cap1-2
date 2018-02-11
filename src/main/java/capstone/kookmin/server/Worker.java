package capstone.kookmin.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class Worker extends Thread {
	protected Socket socket;
	private InetAddress inetAddress;
	
	private static final String ROOT_DIR = System.getProperty("user.dir") + "/test";
	private static final String IMG_DIR = ROOT_DIR + "/img";
	private static final String TXT_DIR = ROOT_DIR + "/txt";
	
	private String imgName = null;
	private String txtName = null;
	
	private BufferedInputStream bis = null;
	private BufferedOutputStream bos = null;
	
	public Worker(Socket clientSocket) {
		this.socket = clientSocket;
		this.inetAddress = clientSocket.getInetAddress();
		
		this.imgName = "img_"+generateName();
		this.txtName = "txt_"+generateName();
		
		new File(ROOT_DIR).mkdirs();
		new File(IMG_DIR).mkdirs();
		new File(TXT_DIR).mkdirs();
		
		System.out.println("===== Client Info =====");
		System.out.println("Date: "+new SimpleDateFormat("YYYY-MM-DD_HH:mm:ss").format(new Date()));
		System.out.println("ip: "+inetAddress);
	}
	
	@Override
	public void run() {
		boolean fileRecieveResult = fileRecieve();
		if(fileRecieveResult == false) {
			System.err.println("Failed to reciving image file from Android Client.");
			return;
		}
		
		boolean pythonCallResult = pythonCall();
		if(pythonCallResult == false) {
			System.err.println("Faild to image processing.");
			return;
		}
		
		boolean interpreterResult = interpreterCall();
		if(interpreterResult == false) {
			System.err.println("Failed to code interpreting.");
			return;
		}
	}
	
	private boolean fileRecieve() {
		boolean isSuccess = false;
		int len = 0;
		String path = IMG_DIR+"/"+imgName;
		
		try {
			bis = new BufferedInputStream(socket.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(path)); // def buf
			
			while((len = bis.read()) != -1) {
				bos.write(len);
			}
			bos.close();
			bis.close();
			
			isSuccess = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	private boolean pythonCall() {
		
		
		return false;
	}
	
	private boolean interpreterCall() {
		
		
		
		return false;
	}
	
	private String generateName() {
		return ""+System.currentTimeMillis();
	}
}
