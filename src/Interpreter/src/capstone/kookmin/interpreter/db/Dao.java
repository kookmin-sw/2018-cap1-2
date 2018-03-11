package capstone.kookmin.interpreter.db;

import org.apache.commons.lang3.StringUtils;

/**
 * 예약어에 맞는 Type 클래스를 가져오기 위한 Dao
 * @author occidere
 */
public class Dao {
	
	/**
	 * typeName에 맞는 Type 클래스를 찾아서 리턴
	 * @param typeName 예약어 이름
	 * @return typeName에 맞는 Type 객체
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Type getType(String typeName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String name = "capstone.kookmin.interpreter.db." + toClassName(typeName);
		Type type = (Type) Class.forName(name).newInstance(); //객체 생성
		return type;
	}
	
	/**
	 * 예약어(typeName)을 Java Class Naming convention에 맞춰 변환. 
	 * <br>
	 * <i>Java Class Naming Convention: 첫 글자는 대문자, 나머지는 소문자</i>
	 * <br>
	 * ex) {@code typeName: for} -> {@code className: For}
	 * @param typeName 클래스 네이밍 포맷으로 변경할 예약어
	 * @return Java Class Naming Convention에 맞게 변경된 예약어
	 */
	private String toClassName(String typeName) {
		return StringUtils.capitalize(typeName);
	}
}