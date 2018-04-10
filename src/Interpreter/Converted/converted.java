for(int i=0;i<=N;i++){//First_example
System.out.print(i);
}

for(int j=0;j<=M;j++){//Second_example
System.out.println(j);
}

for(int k=0;k<=N;k++){//Third_example
System.out.print(k);
}

//변수_매칭_테스트
int a = 10;
char b = '!';
String c = "Hello World!";
double d = 3.14;
TestClass e = new TestClass();

//재_정의_테스트
a = 0;
b = '?';
c = "Hell World!";
d = 3.141592;
e = new TestClass();

//변수_매칭_테스트
int id = 20123087;
char operator = '+';
String tt = "ᕙ(•̀‸•́‶)ᕗ";
double kill = .1;
Twice twice = new Twice();

/**************************************************************/

main(String[] args) {
hm = new HashMap<String, String>();
BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));

line[] = in.readLine().split(" ")
n = Integer.parseInt(line[0])
k = Integer.parseInt(line[1])
int arr[] = new int[n];
int p = 2;

StringTokenizer st = new StringTokenizer(in.readLine(), " ");
for(int i=0;i<=N;i++){
   	    arr[i] = Integer.parseInt(st.nextToken())
    }

    out.write(quickSelect(arr, 0, n-1, k-1)+"")
    out.close()
    in.close()
}
