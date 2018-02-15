package capstone.kookmin.interpreter.main;

import java.io.File;
import java.util.Arrays;

import capstone.kookmin.interpreter.io.Loader;
import capstone.kookmin.interpreter.rule.Rule;

public class Main {
	public static void main(String[] args) throws Exception {
//		if(args.length !=2) {
//			System.err.println("Usage: <Input> <Output>");
//			System.err.println("ex) pseudo_dir/pseudo.txt output_dir/converted.java");
//			System.exit(2);
//		}
		
		args = new String[] { "Pseudo/pseudo.txt", "Converted/converted.java" };

		String rawPath = args[0];
		String rawCodes[] = Loader.load(rawPath).split("\n");

		String converted = args[1];
		int pos = Math.max(converted.lastIndexOf("/"), converted.lastIndexOf("\\"));
		if(pos >= 0) {
			new File(converted.substring(0, pos)).mkdirs();
			new File(converted).delete();
		}
		
		Rule rule = new Rule(converted);
		
		System.out.println("[ Pseudo 코드] ");
		Arrays.stream(rawCodes).forEach(System.out::println);
		
		System.out.print("\n변환 시작 ... ");
		for(String pseudoLine : rawCodes) {
			boolean matched = false;
			for(int i=0;i<pseudoLine.length();i++) {
				matched = rule.match(pseudoLine.substring(0, i), pseudoLine);
				if(matched) break;
			}
			if(matched == false) rule.forceMatch(pseudoLine+"\n");
		}
		System.out.println("완료\n");
		
		System.out.println("[ Java 코드 ]");
		String converteds[] = Loader.load(converted).split("\n");
		Arrays.stream(converteds).forEach(System.out::println);
	}
}
