package capstone.kookmin.interpreter.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 파일을 읽어오는 클래스
 * @author occidere
 */
public class Loader {
	/**
	 * path의 파일을 읽어들이는 메서드
	 * @param path 읽어들일 파일의 경로
	 * @return 읽은 파일 전문 문자열
	 * @throws IOException
	 */
	public static String load(final String path) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		String line;
		BufferedReader br = new BufferedReader(new FileReader(path));
		while((line = br.readLine()) != null)
			sb.append(line+"\n");
		br.close();
		
		return sb.toString();
	}
}