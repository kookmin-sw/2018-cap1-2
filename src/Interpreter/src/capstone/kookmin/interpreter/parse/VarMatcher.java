package capstone.kookmin.interpreter.parse;

/**
 * Variable Matcher
 * 변수 자동 타입 추론 전담 static way 클래스
 * @author 이성준
 */
public class VarMatcher {
    /** static way */
    private VarMatcher() {}

    private static final int INT = 0x01; // a = 1 (INTEGER.MAX_VALUE 이하)
    private static final int LONG = 0x02; // b = 10 (INTEGER.MAX_VALUE 초과)
    private static final int FLOAT = 0x03; //안쓸듯
    private static final int DOUBLE = 0x04; // d = 3.14
    private static final int CLASS = 0x05; // e = new MyClass()
    private static final int METHOD = 0x06; // f = func()
    private static final int CHAR = 0x07; // g = 'c'
    private static final int STRING = 0x08; // h = "str"
    private static final int ERROR = -1; // 변환 불가능한 상태

    private static final String varRegex = "^[_a-zA-Z_$][a-zA-Z_$0-9]*\\s*=\\s*.*"; // var = .* 형태
    private static final String numRegex =""; //추가예정
    private static final String strRegex ="";
    private static final String fltRegex ="";
    private static final String dblRegex ="";
    private static final String clsRegex ="";
    private static final String chrRegex ="";

    /**
     * 무조건 (var = ?) 형식이어야 한다.
     * @param line
     * @return
     */
    private static boolean isVarDeclare(String line) {
        return line.matches(varRegex);
    }

    private static int isNumeric(String line) {
        if(isVarDeclare(line) == false) return ERROR;

        String rhs = line.split("=")[1].replaceAll(" ", "");
        int type = ERROR; // INT, LONG, ERROR 중 1

        try{
            long num = Long.parseLong(rhs);

            if(num <= 0x7fffffff) type = INT;
            else type = LONG;
        }
        catch(Exception e){
            /* 형변환 실패 -> 숫자가 아니다. */
            type = ERROR;
        }

        return type;
    }

    public static String convert(String line) throws Exception {
        // 변수 형태가 아니면 에러
        if(isVarDeclare(line) == false) {
            throw new Exception("Not variable declaration format");
        }

        int type = ERROR;
        type = isNumeric(line);
        if(type == INT) return "int " + line + ";";
        else if(type == LONG) return "long " + line + ";";

        // type = isDouble(line);
        // if(type == DOUBLE) return "double " + line + ";";

        return line;
    }


    public static void main(String[] args) throws Exception{
        VarMatcher varMatcher = new VarMatcher();

        String _int = "_var1 = 1324";
        String _long = "_var2 = 111111111111";

        System.out.println(varMatcher.convert(_int));
        System.out.println(varMatcher.convert(_long));
    }
}
