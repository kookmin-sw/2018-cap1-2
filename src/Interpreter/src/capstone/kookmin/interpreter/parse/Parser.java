package capstone.kookmin.interpreter.parse;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import capstone.kookmin.interpreter.db.Dao;
import capstone.kookmin.interpreter.db.Type;
import capstone.kookmin.commons.io.Loader;

public class Parser {
	private final Dao DAO = new Dao();
	private volatile HashSet<String> typeTable = new HashSet<>();

	/* DCL Singleton */
	private static Parser instance = null;
	public static Parser getInstance() {
		if(instance == null) {
			synchronized(Parser.class) {
				if(instance == null)
					instance = new Parser();
			}
		}
		return instance;
	}
	private Parser() {
		updateTable();
	}
	
	/**
	 * capstone.kookmin.interpreter.db 내부의 
	 * 클래스 파일들의 이름을 직접 읽어서 테이블에 저장
	 */
	private void updateTable() {
		Arrays.stream(new File("src/capstone/kookmin/interpreter/db").listFiles())
			.map(f-> f.getName().toLowerCase()) //소문자로
			.map(name-> name.substring(0, name.indexOf(".java")))
			.filter(name-> !name.contains("Type") && !name.contains("Dao")) //Type, Dao는 제외
			.forEach(name-> typeTable.add(name));
	}
	
	/**
	 * {@code filePath}의 텍스트 수도코드를 읽어서 java 코드로 변환하여 리턴
	 * @param filePath 읽어들일 수도코드 텍스트 파일
	 * @return java로 변환된 결과
	 * @throws IOException 수도코드 텍스트 파일 로드 과정에서 발생
	 */
	public String parse(String filePath) throws IOException {
		String rawCodes[] = null, cvt;
		StringBuilder converted = new StringBuilder();
		
		/* 파일 load 실패 시 IOException */
		rawCodes = Loader.load(filePath);
		
		for(String line : rawCodes) {
			try {
				cvt = match(line);
			}
			catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				//매칭과정에서 오류 -> 그냥 원본 수도코드 라인 사용
				cvt = line;
			}
			converted.append(cvt + "\n");
		}
		
		return converted.toString();
	}
	
	/**
	 * {@code line}을 받아서 실제 java 코드로 변환한 결과를 반환. 
	 * 변환 실패 또는 변환할 내용이 없을 시 원본 코드를 그대로 반환
	 * @param line 수도코드 한 줄
	 * @return 변환된 java코드
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private String match(String line) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String converted = line;
		line = line.replaceAll(" |\\t", ""); //공백, 탭문자 제거
		
		for(String each : line.split("\\W+")) {
			if(typeTable.contains(each)) {
				Type type = DAO.getType(each);
				converted = type.convert(line);
				break;
			}
		}
		
		return converted;
	}
}