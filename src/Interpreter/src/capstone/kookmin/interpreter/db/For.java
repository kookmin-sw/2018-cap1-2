package capstone.kookmin.interpreter.db;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import capstone.kookmin.interpreter.common.Pair;

/**
 * For 예약어 매칭
 * @author occidere
 */
public class For extends Type{
	
	public For() {
		originalFormat = "for(int ?=?;?<=?;?++)";
		addMatchedFormat(".*(for)\\([a-zA-Z]+=\\w+~\\w+\\).*", "(for\\()|=|~|\\)");	// for(i = 0 ~ N)
		addMatchedFormat(".*(for)\\([a-zA-Z]+=\\w+->\\w+\\).*", "(for\\()|=|->|\\)");	// for(i = 0 -> N)
		addMatchedFormat(".*(for)\\([a-zA-Z]+->\\w+:\\w+\\).*", "(for\\()|->|:|\\)");// for(i -> 0 : N)
	}
	
	@Override
	public String convert(String pseudoLine) {
		String lines = pseudoLine.replaceAll(" |\\t", "");
		StringBuilder converted = new StringBuilder(originalFormat.length());

		System.out.printf("   => 예약어(%s) 정규식 매칭 시작\n", "for");
		
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
			 * vars[1] = 0
			 * vars[2] = N
			 */
			int endOfVarIdx = 2;
			int i, j, len = originalFormat.length(), seq[] = {0, 1, 0, 2, 0};
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
