package capstone.kookmin.commons.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 파일을 읽어오는 클래스
 * @author occidere
 */
public class Loader {
	
	private Loader() {}
	
	/**
	 * path의 파일을 읽어들이는 메서드
	 * @param path 읽어들일 파일의 경로
	 * @return 라인별로 쪼개져서 배열에 담긴 읽은 파일 전문 문자열
	 * @throws IOException
	 */
	public static String[] load(final String path) throws IOException {
		LinkedList<String> lines = new LinkedList<>();
		
		String line;
		BufferedReader br = new BufferedReader(new FileReader(path));
		while((line = br.readLine()) != null)
			lines.add(line);
		br.close();
		
		return lines.toArray(new String[lines.size()]);
	}
}