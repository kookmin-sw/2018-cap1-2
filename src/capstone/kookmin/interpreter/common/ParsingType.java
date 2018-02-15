package capstone.kookmin.interpreter.common;

/**
 * 파싱 종류를 명시한 static 클래스
 * @author occidere
 */
public class ParsingType {
	/** 수도 -> 수도 */
	public static final int PSEUDO_TO_PSEUDO = 0xA0;
	/** 수도 -> 자바 */
	public static final int PSEUDO_TO_JAVA = 0xA1;
}