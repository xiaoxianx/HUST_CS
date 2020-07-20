(*实验内容二*)
(*mult:int list->int*)
(*REQUIRES:true*)
(*ENSURES:mult(L) evaluates to the product of the integers in L.*)
fun mult []=1
  | mult(x::L)=x*(mult L);
val intlist=[1,2,3,4];
val test1=mult intlist;