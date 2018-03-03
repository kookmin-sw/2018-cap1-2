package capstone.kookmin.interpreter.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * 파일을 저장하는 클래스
 * @author occidere
 */
public class Saver {
	
	private Saver() {}
	
	/**
	 * 파일을 저장하는 메서드
	 * @param path 저장할 파일의 경로
	 * @param contents 저장할 문자열
	 * @throws Exception
	 */
	public static void save(String path, String contents) throws Exception {
		int idx = path.lastIndexOf("/");
		if(idx == -1) idx = path.lastIndexOf("\\");
		String folder = path.substring(0, idx), name = path.substring(idx+1);
		
		new File(folder).mkdirs();
		
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, true))); //append mode
		pw.write(contents);
		pw.close();
	}
}