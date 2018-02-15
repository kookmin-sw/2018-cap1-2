package capstone.kookmin.interpreter.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Loader {
	
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