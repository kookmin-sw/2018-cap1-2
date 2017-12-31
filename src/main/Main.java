package main;

import java.io.File;
import java.util.Arrays;

import io.Loader;
import rule.Rule;

public class Main {
	public static void main(String[] args) throws Exception {

		String rawPath = "./Pseudo/pseudo.txt";
		String rawCodes[] = Loader.load(rawPath).split("\n");
		
		String convertedPath = "./Converted/converted.java";
		new File(convertedPath).delete();
		
		Rule rule = new Rule();
		
		System.out.println("Pseudo 코드");
		Arrays.stream(rawCodes).forEach(System.out::println);
		
		System.out.println("변환 시작");
		for(String pseudoLine : rawCodes) {
			boolean matched = false;
			for(int i=0;i<pseudoLine.length();i++) {
				matched = rule.match(pseudoLine.substring(0, i), pseudoLine);
				if(matched) break;
			}
			if(matched == false) rule.forceMatch(pseudoLine+"\n");
		}
		System.out.println("변환 완료\n");
		
		String converteds[] = Loader.load(convertedPath).split("\n");
		Arrays.stream(converteds).forEach(System.out::println);
	}
}
