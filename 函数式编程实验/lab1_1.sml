(*实验内容一*)
3+4;
3+2;(*or 3.0+2.0*)
it+6;
val it="hello";
it^" world";(*连接字符串用^*)
(*it+5因为it为string类型*)
val a=5;
(*a=6;更改a时因为val a=6;*)
a+8;
val twice=(fn x=>2*x);
twice a;
let val x=1 in x end;
(*foo;未绑定变量或构造函数*)
(*[1,"foo"]元祖前后类型不一致*)