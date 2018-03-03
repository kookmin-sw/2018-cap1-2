package capstone.kookmin.interpreter.common;

/**
 * 공통적으로 사용될 Pair 클래스
 * @author occidere
 * @param <T> 
 * @param <U>
 */
public class Pair <T, U> {
	public T format;
	public U preserved;
	
	public Pair(T format, U preserved) {
		this.format = format;
		this.preserved = preserved;
	}
}