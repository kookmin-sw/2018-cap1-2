package db;

public class Dao {
	public Type getType(String typeName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String name = "db."+typeName;
		Type type = (Type) Class.forName("db.For").newInstance();
		
		return type;
	}
}