package capstone.kookmin.interpreter.common;

public class Pair <T, U> {
	public T format;
	public U preserved;
	
	public Pair(T format, U preserved) {
		this.format = format;
		this.preserved = preserved;
	}
}