package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Saver {
	
	@SuppressWarnings("unused")
	public static void save(String path, String contents) throws Exception {
		int idx = path.lastIndexOf("/");
		if(idx == -1) idx = path.lastIndexOf("\\");
		String folder = path.substring(0, idx), name = path.substring(idx+1);
		
		new File(folder).mkdirs();
		
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, true))); //append mode
		pw.write(contents);
		pw.close();
	}
}