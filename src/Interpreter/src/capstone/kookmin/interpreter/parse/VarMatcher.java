package capstone.kookmin.interpreter.parse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Variable Matcher
 * 변수 자동 타입 추론 전담 static way 클래스
 *
 * @author 이성준
 */
public class VarMatcher {
    /** static way */
    private VarMatcher() {}
    /** <key: varName, value: varType> */
    private static final HashMap<String, String> VAR_TABLE = new HashMap<>();
    /** <key: 변수 자료형, value: 자료형 이름> */
    private static final HashMap<Integer, String> DATA_TYPE = new HashMap<Integer, String>() {{
        put(INT, "int");
        put(LONG, "long");
        put(FLOAT, "float");
        put(DOUBLE, "double");
        put(OBJECT, "object");
        put(METHOD, "method");
        put(CHAR, "char");
        put(STRING, "String");
        put(ERROR, "");
    }};

    private static Class VAR_MATCHER_CLASS = VarMatcher.class;
    private static List<Method> CHECK_METHOD = Arrays.stream(VAR_MATCHER_CLASS.getDeclaredMethods())
            .filter(method -> method.getName().contains("chk"))
            .collect(Collectors.toList());

    private static final int INT = 0x01; // a = 1 (INTEGER.MAX_VALUE 이하)
    private static final int LONG = 0x02; // b = 10 (INTEGER.MAX_VALUE 초과)
    private static final int FLOAT = 0x03; // 안쓸듯
    private static final int DOUBLE = 0x04; // d = 3.14
    private static final int OBJECT = 0x05; // e = new MyClass()
    private static final int METHOD = 0x06; // f = func()
    private static final int CHAR = 0x07; // g = 'c'
    private static final int STRING = 0x08; // h = "str"
    private static final int ERROR = -1; // 변환 불가능한 상태
    private static final int INIT = 0; // 아무것도 하지 않은 초기 상태

    private static final String varRegex = "^[_a-zA-Z_$][a-zA-Z_$0-9]*\\s*=\\s*.*"; // var = .* 형태
    private static final String strRegex = "\".*\"";
    private static final String fltRegex = ""; // 안쓸듯
    private static final String dblRegex = "\\d*\\.\\d*"; //"(\\d+(\\.\\d+)?)|(\\.\\d+)";
    private static final String objRegex = "new [_a-zA-Z_$][a-zA-Z_$0-9]*\\(\\)";
    private static final String mtdRegex = ""; // method call
    private static final String chrRegex = "'.'";

    /**
     * 무조건 (var = ?) 형식이어야 한다.
     *
     * @param line 변수 선언 라인인지 검증할 라인
     * @return 변수 선언부이면 true, 아니면 false
     */
    private static boolean isVarDeclare(String line) {
        return line.matches(varRegex);
    }

    /**
     * 변수 선언 라인 추론 & 변환 메서드
     * @param line 변수 추론을 할 라인
     * @return 변수 추론 결과가 포함된 라인
     * @throws Exception 변수 선언부가 아니면 발생
     */
    public static String convert(String line) throws Exception {
        // 변수 형태가 아니면 에러
        if (isVarDeclare(line) == false) {
            throw new Exception("Not variable declaration format");
        }

        String lhs = line.split("=")[0].trim(); // 변수명
        String rhs = line.split("=")[1].trim(); // 변수값
        int type = ERROR;

        try {
            for(Method checker : CHECK_METHOD) {
                type = (int) checker.invoke(VAR_MATCHER_CLASS, rhs);
                if(type != ERROR) break;
            }

            /* 새로운 변수라면 기록하고 자료형 붙임 */
            if (type != ERROR && inputVarTable(lhs, type) == true) {
                if(type == OBJECT || type == METHOD){
                    String objName = rhs.split(" ")[1].replaceAll("\\(\\)", "");
                    line = objName + " " + line;
                } else {
                    line = DATA_TYPE.get(type) + " " + line;
                }
            }
            line += ";";
        } catch (Exception e) {
            // 변수의 중복 선언인 경우
            e.printStackTrace();
        }

        return line;
    }

    /**
     * 정수 자료형인지 검사하는 메서드
     *
     * @param rhs 정수형 선언인지 검사할 변수 선언부 라인의 우변
     * @return int면 {@code INT}, long이면 {@code LONG}, 정수형이 아니면 {@code ERROR}를 리턴
     */
    private static int chkNumeric(String rhs) {
        try {
            return Long.parseLong(rhs) <= 0x7fffffff ? INT : LONG;
        } catch (Exception e) {
            return ERROR; // 형변환 실패 -> 숫자가 아니다
        }
    }

    /**
     * char 자료형인지 검사하는 메서드
     *
     * @param rhs char 형 선언인지 검사할 변수 선언부 라인의 우변
     * @return char면 {@code CHAR}, 아니면 {@code ERROR}를 리턴
     */
    private static int chkChar(String rhs) {
        return rhs.trim().matches(chrRegex) ? CHAR : ERROR;
    }

    /**
     * String 자료형인지 검사하는 메서드
     *
     * @param rhs String 형 선언인지 검사할 변수 선언부 라인의 우변
     * @return String이면 {@code STRING}, 아니면 {@code ERROR}를 리턴
     */
    private static int chkString(String rhs) {
        return rhs.trim().matches(strRegex) ? STRING : ERROR;
    }

    /**
     * double 자료형인지 검사하는 메서드
     *
     * @param rhs double 형 선언인지 검사할 변수 선언부 라인의 우변
     * @return double이면 {@code DOUBLE}, 아니면 {@code ERROR}를 리턴
     */
    private static int chkDouble(String rhs) {
        return rhs.trim().matches(dblRegex) ? DOUBLE : ERROR;
    }

    /**
     * Object 객체(class 등)인지 검사하는 메서드
     *
     * @param rhs Object 형 선언인지 검사할 변수 선언부 라인의 우변
     * @return Object면 {@code OBJECT}, 아니면 {@code ERROR}를 리턴
     */
    private static int chkObject(String rhs) {
        return rhs.trim().matches(objRegex) ? OBJECT : ERROR;
    }

    /**
     * 처음 선언하는 변수인지 여부를 기록 및 판단하는 메서드.
     * 첫 선언이면 {@code VAR_TABLE}에 기록한다.
     * 중복 선언인 경우 Exception 발생
     *
     * @param lhs  검증할 변수명
     * @param TYPE 현재 변수명의 추론된 자료형
     * @return 처음 변수 선언이면 true, 재 정의이면 false
     * @throws Exception 같은 변수명의 다른 자료형을 선언한 적이 있는 중복선언의 경우
     */
    private static boolean inputVarTable(String lhs, final int TYPE) throws Exception {
        boolean isNew;

        /* 한번도 선언하지 않은 변수라면 변수 테이블(VAR_TABLE)에 입력 */
        if (VAR_TABLE.containsKey(lhs) == false) {
            isNew = true;
            VAR_TABLE.put(lhs, DATA_TYPE.get(TYPE));
        } else { // 이미 선언했던 변수라면
            isNew = false;

            String befTypeName = VAR_TABLE.get(lhs); // 이전에 같은 변수명으로 저장되어 있던 자료형
            String curTypeName = DATA_TYPE.get(TYPE); // 현재 변수의 자료형

            /* 서로 다른 자료형의 중복된 변수명 선언. ex) int var, long var */
            if (befTypeName.equals(curTypeName) == false) {
                throw new Exception(String.format(
                        "Duplicated variables [%s, %s]",
                        befTypeName + " " + lhs, curTypeName + " " + lhs));
                /**
                 * <b>에러 테스트케이스</b>
                 * 1. 0x7fffffff보다 큰 값을 가진 변수를 변환한다.
                 *    ex) var = 11111111111 -> long var = 11111111111;
                 * 2. 이 변수에 0x7fffffff보다 큰 값을 재 정의 하면 ok
                 * 3. 이 변수에 0x7fffffff보다 작거나 같은 값을 대입하면 Exception
                 *   ex) var = 1 -> int var = 1;
                 *   => long <-> ing 간 변환 처리가 불완전함
                 * @date 2018.03.28
                 * @author 이성준
                 */
            }
        }

        return isNew;
    }

    public static void main(String[] args) throws Exception {
        String vars[] = {"a = 10", "b = '!'", "c = \"Hello World!\"", "d = 3.14", "e = new MyClass()"};
        System.out.println("\n변환 전");
        Arrays.stream(vars).forEach(System.out::println);
        System.out.println("\n변환 후");
        Arrays.stream(vars)
                .map(var -> { try { return VarMatcher.convert(var); } catch (Exception e) { return var; } })
                .forEach(System.out::println);
    }
}