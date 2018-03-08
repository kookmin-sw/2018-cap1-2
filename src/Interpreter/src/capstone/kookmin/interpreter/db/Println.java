package capstone.kookmin.interpreter.db;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import capstone.kookmin.interpreter.common.Pair;

/**
 * println 예약어 매칭
 * @author occidere
 */
public class Println extends Type {
	
	public Println() {
		originalFormat = "System.out.println(?);";
		addMatchedFormat(".*(println\\().*\\).*", "(println\\()|\\)"); //println(i)
	}

	@Override
	public String convert(String pseudoLine) {
		String lines = psuedoLine.replaceAll(" |\\t", ""); //공백 제거
		StringBuilder converted = new StringBuilder();
		
		for(Pair<String, String> each : matchedFormat) {
			if(lines.matches(each.format) == false) continue; //정규식 매칭 실패시 건너뜀
			
			lines = lines.replaceAll(each.preserved, " "); //문법 부분 제거(변수만 남음)
			List<String> varList = Arrays.stream(lines.split(" "))
					.filter(x-> x.length() > 0)
					.collect(Collectors.toList()); //변수들만 걸러냄
			String vars[] = varList.toArray(new String[varList.size()]);
			
			/*
			 * vars[0] = i
			 */
			char ch;
			int endOfVarIdx = 0;
			int i, j, len = originalFormat.length(), seq[] = {0};
			for(i=j=0;i<len;i++) {
				ch = originalFormat.charAt(i);
				converted.append(ch == '?' ? vars[seq[j++]] : ch);
			}
			
			// 나머지 부분들 이어붙여주기
			for(i=endOfVarIdx+1; i<vars.length; i++) converted.append(vars[i]);
			
			break; //매칭 & 변환 성공 시 종료
		}
		
		return converted.toString();
	}
}

/**
println 매칭 전 print에 매칭되는 문제 해결 필요
*/
