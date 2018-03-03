package capstone.kookmin.interpreter.main;

import java.io.File;
import java.util.Arrays;

import capstone.kookmin.interpreter.io.Loader;
import capstone.kookmin.interpreter.io.Saver;
import capstone.kookmin.interpreter.parse.Parser;

public class Main {
	public static void main(String[] args) throws Exception {
		args = new String[] { "Pseudo/pseudo.txt", "Converted/converted.java" };

		String rawPath = args[0];
		String rawCodes[] = Loader.load(rawPath).split("\n");

		String convertPath = args[1];
		int pos = Math.max(convertPath.lastIndexOf("/"), convertPath.lastIndexOf("\\"));
		if(pos >= 0) {
			new File(convertPath.substring(0, pos)).mkdirs();
			new File(convertPath).delete();
		}

		Parser parser = Parser.getInstance();
		
		System.out.println("[ Pseudo 코드] ");
		Arrays.stream(rawCodes).forEach(System.out::println);
		
		System.out.print("\n변환 시작 ... ");
		
		String converted = parser.parse(rawPath);
		Saver.save(convertPath, converted);
		
		System.out.println("완료\n");
		
		System.out.println("[ Java 코드 ]");
		String converteds[] = Loader.load(convertPath).split("\n");
		Arrays.stream(converteds).forEach(System.out::println);
	}
}
