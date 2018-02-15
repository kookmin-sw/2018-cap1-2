package capstone.kookmin.interpreter.db;

public class Dao {
	public Type getType(String typeName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String name = "db."+toClassName(typeName);
		Type type = (Type) Class.forName(name).newInstance();
		
		return type;
	}
	
	private String toClassName(String typeName) {
		char capital = (char)(typeName.charAt(0) - 32);
		return capital+typeName.substring(1, typeName.length());
	}
}