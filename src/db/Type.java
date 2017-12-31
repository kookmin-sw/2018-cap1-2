package db;

import java.util.LinkedList;
import java.util.List;

import common.Pair;

public abstract class Type {
	protected String typeName;
	protected String originalFormat;
	protected List< Pair<String, String> > matchedFormat = new LinkedList<>();
	
	protected void addMatchedFormat(String pseudo, String preserved) {
		matchedFormat.add(new Pair<String, String>(pseudo, preserved));
	}
	
	public abstract String convert(String psuedoLine);
}