package capstone.kookmin.interpreter.rule;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import capstone.kookmin.interpreter.db.Dao;
import capstone.kookmin.interpreter.db.Type;
import capstone.kookmin.interpreter.io.Saver;

/**
 * 예약어 매칭을 검사하고, 변환하여 저장하는 클래스
 * @author occidere
 */
public class Rule {
	private volatile HashSet<String> typeTable = new HashSet<>();
	private String savePath;
	
	/**
	 * 예약어 테이블 업데이트 및 변환된 파일이 저장될 경로 설정
	 * @param savePath 변환된 파일이 저장될 경로
	 */
	public Rule(String savePath) {
		updateTable();
		this.savePath = savePath;
	}
	
	/**
	 * {@code code}와 맞는 예약어가 테이블에 있으면 {@code codeLine}을 룰에 맞춰 인터프리팅 후 true 리턴, 없으면 false 리턴
	 * @param code 검사할 예약어
	 * @param codeLine 예약어를 제외한 나머지 코드들
	 * @return 예약어 룰 매칭 성공시 변환 & 저장 후 true 리턴, 실패 시 false 리턴
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public boolean match(String code, String codeLine) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(typeTable.contains(code) == false) return false; //예약어가 없을 시 false 리턴
		
		try {
			Type type = new Dao().getType(code); //예약어를 바탕으로 알맞은 타입 추출
			String converted = type.convert(codeLine); //룰에 맞춰 코드 인터프리팅
			Saver.save(savePath, converted); //저장
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 룰 매칭 없이 라인을 그대로 저장
	 * @param codeLine 코드 라인
	 * @throws Exception
	 */
	public void forceMatch(String codeLine) throws Exception {
		Saver.save(savePath, codeLine);
	}
	
	/**
	 * capstone.kookmin.interpreter.db 내부의 
	 * 클래스 파일들의 이름을 직접 읽어서 테이블에 저장
	 */
	private void updateTable() {
		Arrays.stream(new File("src/capstone/kookmin/interpreter/db").listFiles())
			.map(f-> f.getName())
			.map(name-> name.substring(0, name.indexOf(".java")))
			.filter(name-> !name.contains("Type") && !name.contains("Dao")) //Type, Dao는 제외
			.forEach(name-> typeTable.add(name));
	}
}