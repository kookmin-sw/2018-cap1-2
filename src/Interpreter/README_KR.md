# Interpreter
정규식 기반의 수도코드 인터프리터 모듈

# 동작 원리
## 1. 수도코드를 String 배열 형태로 읽어들인다.
* 수도코드의 원본 형태
````
for (i = 0 ~ N) {
  println("Hello World")
}
````
* String 배열 형태로 읽어들인 수도코드
````
String pseudo[] = new String[3];
pseudo[0] = "for (i = 0 ~ N) {"
pseodo[1] = "  println("Hello World")"
pseudo[2] = "}"
````
## 2. 매칭을 시도한다.
* 읽어들이는 순서: 앞 -> 뒤
### 1. 공백을 제거한다.
`"for (i = 0 ~ N) {"` -> `"for(i=0~N){"`
### 2. <i>특수문자</i>를 기준으로 쪼갠다.
````
원본 형태: "for(i=0~N){"
쪼개진 형태: [ "for", "i", "0", "N" ]
````
### 3. Type Table이란 이름의 HashMap으로 해당 chunk의 매칭을 시도한다.
* 읽어들이는 순서: 앞 -> 뒤 <br>
* 만약 Type Table에 해당 chunk가 없으면, 매칭을 중지하고 **원문 그대로**를 내보낸다.
  * 현재 라인에 대한 매칭을 중지하고 다음 라인으로 넘어간다.
* 만약 해당 chunk를 Type Table에서 찾으면, **정규식**을 이용해 java코드로의 변환을 시도한다.
  * 정규식: `".*(for)\\([a-zA-Z]+=\\w+~\\w+\\).*"`
* 변환하며 변수와 상수를 적절한 위치로 재배치 시킨다. <br>
  ex) 변수 및 상수: `i`, `0`, `N`<br>
  -> for(int **i**=**0**; **i**<=**N**;**i**++){
* 인터프리팅을 성공하면 **java 코드** 형태로 String 배열에 저장된다.
  ````
  converted[0] = "for(int i=0;i<=N;i++){"
  ````
## 3. 모든 인터프리팅이 종료되면 `.java` 파일로 저장한다.
````
for(int i=0;i<=N;i++){
  System.out.println("i");
}
````
