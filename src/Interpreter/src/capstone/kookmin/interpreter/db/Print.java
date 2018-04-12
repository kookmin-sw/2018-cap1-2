package capstone.kookmin.interpreter.db;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import capstone.kookmin.interpreter.common.Pair;

/**
 * print 예약어 매칭
 * @author occidere
 */
public class Print extends Type {
	
	public Print() {
		originalFormat = "System.out.print(?);";
		addMatchedFormat(".*(print\\().*\\).*", "(print\\()|\\)");	// print(i)
	}

	@Override
	public String convert(String pseudoLine) {
		String lines = pseudoLine.replaceAll(" |\\t", "");
		StringBuilder converted = new StringBuilder(originalFormat.length());

		System.out.printf("   => 예약어(%s) 정규식 매칭 시작\n", "print");
		
		for(Pair<String, String> each : matchedFormat) {
			if(lines.matches(each.format) == false) continue;

			lines = lines.replaceAll(each.preserved, " ");
			List<String> varList = Arrays.stream(lines.split(" "))
					.filter(x-> x.length() > 0)
					.collect(Collectors.toList());
			String vars[] = varList.toArray(new String[varList.size()]);
			System.out.printf("   => 라인에서 변수들만 추출: %s\n", Arrays.toString(vars));
			
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

			System.out.printf("   => 변수들을 자바코드 포맷에 채워서 완성: %s\n", converted.toString());

			break;
		}
		
		return converted.toString();
	}
	
}
