package capstone.kookmin.interpreter.db;

import java.util.LinkedList;
import java.util.List;

import capstone.kookmin.interpreter.common.Pair;

/**
 * 모든 예약어 매칭 클래스들이 상속해야 할 Type 클래스/
 * @author 이성준_로컬
 *
 */
public abstract class Type {
	/** 수도 코드의 원본 형식 */
	protected String originalFormat;
	/** 다양한 수도코드 허용 리스트 */
	protected List< Pair<String, String> > matchedFormat = new LinkedList<>();
	
	/**
	 * 매칭 포맷 추가
	 * @param pseudo 수도 코드
	 * @param preserved 예약어
	 */
	protected void addMatchedFormat(String pseudo, String preserved) {
		matchedFormat.add(new Pair<String, String>(pseudo, preserved));
	}
	
	/**
	 * 각 예약어 타입마다 구현해야 할 변환 메서드
	 * @param psuedoLine 수도 코드 라인
	 * @return 수도코드가 변환된 정식 문법 문장
	 */
	public abstract String convert(String psuedoLine);
}