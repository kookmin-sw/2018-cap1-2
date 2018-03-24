# Interpreter
Regex based Pseudo Code Interpreter Module

# How to works ?
## 1. Read **pseudo code** as **String Array**
* raw pseudo code
````
for (i = 0 ~ N) {
  println("Hello World")
}
````
* String array format
````
String pseudo[] = new String[3];
pseudo[0] = "for (i = 0 ~ N) {"
pseodo[1] = "  println("Hello World")"
pseudo[2] = "}"
````
## 2. Try match
* Sequential matcing: first idx -> last idx
### 1. Remove white space
`"for (i = 0 ~ N) {"` -> `"for(i=0~N){"`
### 2. Split with <i>Special Characters</i>
````
original: "for(i=0~N){"
splitted: [ "for", "i", "0", "N" ]
````
### 3. Try match with HashMap (Named Type Table)
* Sequential searching: first idx -> last idx <br>
* If the chunk **not exist in Type Table, ignore it & export as original format**.
  * Stop matching current line and move to the next line.
* Else if **find `for` from Type Table successfully**, try matching using **regex**.
  * regex: `".*(for)\\([a-zA-Z]+=\\w+~\\w+\\).*"`
* At the same time, Find variable place & relocate it as structured java format. <br>
  ex) variables: `i`, `0`, `N`<br>
  -> for(int **i**=**0**; **i**<=**N**;**i**++){
* After successful interpreting, save the result as **java code** in arrays.
  ````
  converted[0] = "for(int i=0;i<=N;i++){"
  ````
## 3. After interpreting, Save the converted result as `.java` files
````
for(int i=0;i<=N;i++){
  System.out.println("i");
}
````
