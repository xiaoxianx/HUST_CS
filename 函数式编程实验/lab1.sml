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

(*实验内容二*)
(*mult:int list->int*)
(*REQUIRES:true*)
(*ENSURES:mult(L) evaluates to the product of the integers in L.*)
fun mult []=1
  | mult(x::L)=x*(mult L);
val intlist=[2,3,4];
val test2=mult intlist;
(*实验内容三*)
(*Mult:int list list->int*)
(*REQUIRES:true*)
(*ENSURES:Mult(R) evaluates to the product of the integers in R.*)
fun Mult[]=1(*不能为0*)
  | Mult(r::R)=mult(r)*Mult(R);
val intlistlist=[[1,2],[2,3]];
val test3=Mult intlistlist;
(*实验内容四*)
(*mult':int list*int->int*)
(*REQUIRES:TRUE*)
(*ENSURES:mult'(L,a)computes the product of each element in integers then multiplies a*)
 fun mult'([],a)=a
   | mult'(x::L,a)=mult'(L,x*a);
 val test4_1=mult'(intlist,2);
 
 fun Mult'([],a)=a
   | Mult'(r::R,a)=Mult'(R,mult'(r,1)*a);
 val test4_2=Mult'(intlistlist,2);
(*实验内容五 *)
(*double:int->int *)
(*REQUIRES:n>=0*)
(*ENSURES:double n evaluates to 2*n.*)
fun double(0:int):int=0
  | double n=2+double(n-1);
(*square:int->int*)
(*REQUIRES:n>=0*)
(*ENSURES:square n evaluates to n*n.*)
fun square(0:int):int=0
  | square n=square(n-1)+double(n-1)+1;(* n^2=(n-1)^2+2(n-1)+1*)
val test4=square(6);
(*实验内容六 *)
(*divisibleByThree:int->bool*)
(*REQUIRES:true*)
(*ENSURES:divisibleByThree n evaluates to true if n is a multiplie
of 3 and to false otherwise*)
fun divisibleByThree(0:int):bool = true
	|divisibleByThree 1 = false
	|divisibleByThree 2 = false
	|divisibleByThree n = if(n>0) then  divisibleByThree (n-3) else divisibleByThree (n+3);
val test6a=divisibleByThree(7);
val test6b=divisibleByThree(6);
val test6c=divisibleByThree(~3);
(*实验内容七 *)
(*oddP: int -> bool*)
(*REQUIRES: n>=0*)
(*ENSURES:oddP n evaluates to true iff n is odd*)
fun oddP(0:int):bool = false
	|oddP(1) = true
	|oddP(n) = oddP(n-2);
val test7 = oddP(7);







