package rule;

import java.util.HashSet;

import db.Dao;
import db.Type;
import io.Saver;

public class Rule {
	private volatile HashSet<String> typeTable = new HashSet<>();
	
	public Rule() {
		typeTable.add("for");
		typeTable.add("print");
	}
	
	public boolean match(String code, String codeLine) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(typeTable.contains(code) == false) return false;
		
		try {
			Type type = new Dao().getType(code);
			String converted = type.convert(codeLine);
			
			Saver.save("./Converted/converted.java", converted);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void forceMatch(String codeLine) throws Exception {
		Saver.save("./Converted/converted.java", codeLine);
	}
}