package capstone.kookmin.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import capstone.kookmin.commons.io.Loader;
import capstone.kookmin.commons.io.Saver;
import capstone.kookmin.commons.protocol.Packet;
import capstone.kookmin.interpreter.parse.Parser;

@SuppressWarnings("unused")
public class Worker extends Thread {
	protected Socket socket;
	private InetAddress inetAddress;

	private static final String ROOT_DIR = System.getProperty("user.dir") + "/data";
	private static final String IMG_DIR = ROOT_DIR + "/img";
	private static final String PSEUDO_DIR = ROOT_DIR + "/pseudo"; //이미지에서 추출된 text 형식의 수도코드가 담길 경로
	private static final String CONVERTED_DIR = ROOT_DIR + "/converted"; //변환된 .java 파일이 담길 경로

	private BufferedInputStream bis = null;
	private BufferedOutputStream bos = null;

	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public Worker(Socket clientSocket) {
		this.socket = clientSocket;
		this.inetAddress = clientSocket.getInetAddress();

		new File(ROOT_DIR).mkdirs();
		new File(IMG_DIR).mkdirs();
		new File(PSEUDO_DIR).mkdirs();
		new File(CONVERTED_DIR).mkdirs();

		System.out.println("===== Client Info =====");
		System.out.println("Date: " + new SimpleDateFormat("YYYY-MM-dd_HH:mm:ss").format(new Date()));
		System.out.println("ip: " + inetAddress);
		System.out.println();
	}

	@Override
	public void run() {
		String receivedImageFilePath = null;
		String pseudoFilePath = null;
		String convertedFilePath = null;

		try {
			receivedImageFilePath = fileReceive(); // 소켓으로 사진을 받아옴
			System.out.println("receivedImageFilePath: " + receivedImageFilePath);

			pseudoFilePath = pythonCall(receivedImageFilePath); // 받은 사진으로 파이썬 영상처리
			System.out.println("pseudoFilePath: " + pseudoFilePath);
			
		} catch(Exception e) {
			e.printStackTrace();
			/* 소켓 통신 과정 or 영상처리 과정에서 에러 -> System error(statusCode: 300) */
			try { send(Packet.SYSTEM_ERROR); }
			catch(IOException ioe) { ioe.printStackTrace(); }
			return; // 종료
		}

		try {
			convertedFilePath = interpreterCall(pseudoFilePath); // 영상처리로 추출된 텍스트 수도코드
			System.out.println("convertedFilePath: " + convertedFilePath);

			/* 모든 과정 성공 -> Success(statusCode: 100) */
			send(Packet.SUCCESS, pseudoFilePath, convertedFilePath);
		} catch(Exception e) {
			e.printStackTrace();
			/* 인터프리팅 과정에서 에러 -> Logical Error(statusCode: 200) */
			try { send(Packet.LOGICAL_ERROR, new int[] {1}, pseudoFilePath); }
			catch(IOException ioe) { ioe.printStackTrace(); }
			return; // 종료
		}
	}

	/**
	 * Client로 부터 Image가 담긴 패킷(statusCode: 500)을 받아 저장하는 메서드
	 * 
	 * @return Packet 수신 및 이미지 저장 성공 시 true, 실패 시 false
	 * @throws Exception 
	 */
	private String fileReceive() throws Exception {
		String path = IMG_DIR + "/img_" + generateName();

		ois = new ObjectInputStream(socket.getInputStream());
		bos = new BufferedOutputStream(new FileOutputStream(path)); // def buf

		Object recv = ois.readObject();
		if (recv instanceof Packet == false)
			throw new Exception("Not Packet Type!");

		Packet packet = (Packet) recv;
		if (packet.getStatusCode() != Packet.IMAGE_SEND)
			throw new Exception("Not Image Packet!");

		bos.write(packet.getImages());
		bos.close();
		
		return path;
	}

	private String pythonCall(String receivedImageFilePath) throws Exception {
		String pseudoPath = PSEUDO_DIR + "/pseudo_" + generateName();
		
		String cmd[] = {"bash", "-c", "sh bin/tesseract.sh " + receivedImageFilePath + " " + pseudoPath};
		
		Process proc = Runtime.getRuntime().exec(cmd);
		proc.waitFor();

		return pseudoPath;
	}

	private String interpreterCall(String pseudoFilePath) throws Exception {
		final String convertedPath = CONVERTED_DIR + "/converted_" + generateName() + ".java";

		/* Interpreter 호출 */
		Parser parser = Parser.getInstance();
		String converted = parser.parse(pseudoFilePath);

		/* 변환 결과 저장 */
		Saver.save(convertedPath, converted);

		return convertedPath; // 저장된 파일의 경로 반환
	}
	
	/**
	 * System Error시 전송
	 * @param statusCode 300
	 */
	private void send(int statusCode) throws IOException {
		Packet packet = new Packet(statusCode);
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(packet);
	}
	
	/**
	 * Logical Error 시 전송
	 * @param statusCode 200
	 * @param errorLines 빨간 줄을 칠 에러가 발생한 라인이 담긴 배열
	 * @param pseudoPath 수도코드가 저장된 경로
	 * @throws IOException
	 */
	private void send(int statusCode, int[] errorLines, String pseudoPath) throws IOException {
		String[] pseudoLines = Loader.load(pseudoPath);
		Packet packet = new Packet(statusCode, errorLines, pseudoLines);
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(packet);
	}

	/**
	 * 모든 변환 과정이 성공했을 시 전송
	 * @param statusCode 100
	 * @param pseudoPath 수도코드가 저장된 경로
	 * @param convertedPath 변환된 자바코드가 저장된 경로
	 * @throws IOException
	 */
	private void send(int statusCode, String pseudoPath, String convertedPath) throws IOException {
		String[] javaLines = Loader.load(convertedPath);
		String[] pseudoLines = Loader.load(pseudoPath);
		Packet packet = new Packet(statusCode, pseudoLines, javaLines);
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(packet);
	}

	private String generateName() {
		return "" + System.currentTimeMillis();
	}
}
