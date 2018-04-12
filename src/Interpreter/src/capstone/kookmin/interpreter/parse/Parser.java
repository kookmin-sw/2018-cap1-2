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
//		Arrays.stream(new File("src/capstone/kookmin/interpreter/db").listFiles())
//			.map(f-> f.getName().toLowerCase()) //소문자로
//			.map(name-> name.substring(0, name.indexOf(".java")))
//			.filter(name-> !name.contains("Type") && !name.contains("Dao")) //Type, Dao는 제외
//			.forEach(name-> typeTable.add(name));
		/* 우선은 직접 입력 -> 추후 해결책 찾아볼 것 */
		typeTable.add("for");
		typeTable.add("print");
		typeTable.add("println");
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

		System.out.println("매칭 대상 라인: " + line);

		/* 변수 타입추론 들어갈 부분 */
		try{
			return VarMatcher.convert(line);
		}
		catch(Exception e){
			// 변수 선언 형태가 아니라는 의미
		}

		/* 변수 타입추론할 라인이 아닐 시 일반 예약어 매칭 진행 */
		line = line.replaceAll(" |\\t", ""); //공백, 탭문자 제거
		String splitted[] = line.split("\\W+");

		System.out.println("[예약어매칭]");
		System.out.printf(" => 공백 제거: %s\n", line);
		System.out.printf(" => 특수문자 기준 분리: %s\n", Arrays.toString(splitted));

		/* 특수문자 기준으로 쪼개서 순서대로 typeTable에 있는지 검사 & 매칭 */
		for(String each : splitted) {
			System.out.printf("  => 검사할 단어: %s, 예약어 화이트리스트 = %s\n", each, typeTable);
			if(typeTable.contains(each)) {
				Type type = DAO.getType(each);
				converted = type.convert(line);
				break;
			}
		}
		System.out.println(" => 예약어 매칭 종료\n");
		
		return converted;
	}
}