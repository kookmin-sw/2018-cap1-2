package capstone.kookmin.interpreter.db;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import capstone.kookmin.interpreter.common.Pair;

public class Print extends Type {
	
	public Print() {
		typeName = "print";
		originalFormat = "System.out.print(?);";
		addMatchedFormat(".*(print\\().*\\).*", "(print\\()|\\)");	// print(i)
	}

	@Override
	public String convert(String psuedoLine) {
		String lines = psuedoLine.replaceAll(" ", "");
		StringBuilder converted = new StringBuilder(originalFormat.length());
		
		for(Pair<String, String> each : matchedFormat) {
			if(lines.matches(each.format) == false) continue;

			lines = lines.replaceAll(each.preserved, " ");
			List<String> varList = Arrays.stream(lines.split(" "))
					.filter(x-> x.length() > 0)
					.collect(Collectors.toList());
			String vars[] = varList.toArray(new String[varList.size()]);
			
			/*
			 * vars[0] = i
			 */
			int endOfVarIdx = 0;
			int i, j, len = originalFormat.length(), seq[] = {0};
			char ch;
			for(i=j=0;i<len;i++) {
				ch = originalFormat.charAt(i);
				converted.append(ch == '?' ? vars[seq[j++]] : ch);
			}

			for(i=endOfVarIdx+1; i<vars.length;i++) converted.append(vars[i]);
			
			converted.append("\n");
			break;
		}
		
		return converted.toString();
	}
	
}
