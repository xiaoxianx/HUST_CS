(*
lab2.1 reverse: int list -> int list
requires: int list
ensures: remainingverse of the input list
*)
fun reverse []=[]
    |reverse(a::L)=(reverse L)@[a];
val  list1 = [1,2,3,4,5,6,7,8,9];
val reverse_list1 = reverse list1;
(*
lab2.1.2 reverse' int list -> int list    //类似尾递归？
require: int list
ensures:  reverse of the input list in O(n)
*)
fun reverse'(L:int list):int list=
    let
        fun reversehelp(L:int list,A:int list):int list=
            case L of
                []=>A
				| x::R=>reversehelp(R,x::A)
    in 
        reversehelp(L,[])
    end
val reverse'_list1 = reverse' list1;	
(*
lab2.2 interleave:int list*int list -> int list
requires: 
ensures: ements in the list in the results of alternating until the end of one of the Int list data, 
while remaining in the second int list data are directly attached to the tail of the resulting data
*)
fun interleave(A:int list,B:int list):int list=
    case(A,B) of
    ([],_)=>B
    |(_,[])=>A
    |(x::X,y::Y)=>x::y::interleave(X,Y)
val list2=[1,3,5,7,9];
val list3=[2,4,6];
val interleave_list=interleave(list2,list3);
(*
lab2.3 listToTree:int list ->tree
requires:true
ensures:evaluate a balance tree
*)	
datatype 'a tree = Lf
                  |Br of 'a*'a tree*'a tree;(* empty|tree*int*tree *)
fun listToTree [] = Lf 
    |listToTree (L) = let 
                               val index = List.length L div 2   (*floor*)
                               val llist =  List.take(L,index)   (*前index个*)
                               val x::rlist = List.drop(L,index)	(*index后所有*)
                          in 
                               Br(x,listToTree(llist),listToTree(rlist))
                          end;
					  
val listtotree1 = listToTree(list1);    
(*
lab2.4 revTree: tree -> tree height log n span log n  work n/2
requires:
ensures:
*)
fun revT(T) = case T of 
              Lf => Lf
              |Br(x,ltree,rtree) => Br(x,revT(rtree),revT(ltree));
val revT_test = revT(listtotree1);

(*
lab2.5 binarySearch:tree*int -> bool
requries:
ensures:argument 1 is sorted, contain a node argument 2？ 
*)

fun binarySearch(T) = case T of 
                      Lf => false
                      |Br(x,ltree,rtree) => case Int.compare(2,x) of 
                                          LESS => binarySearch(ltree)
                                          |EQUAL => true
                                          |GREATER => binarySearch(rtree);
val list4 = [5,4,3,2,8,7,4];(*sorted	*)
val tree = listToTree(list4);
val binarySearch_test = binarySearch(tree);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	