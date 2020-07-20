(*

(* 1 *)

fun all(your,base) =

  case your of

       0 => base

     | _ => "are belong to us"::all(your-1,base);

(* 类型：int* string list -> string list      *)



fun funny(f,[]) = 0

  | funny(f,x::xs) = f(x,funny(f,xs));

(*   类型：('a*int->int)*'a list -> int   *)



(fn x => (fn y => x)) "Hello,World!";

(* 类型：'a -> string *)

*)







(* 2.1 *)

fun toInt_inner (b,k) = 

  let fun baseToInt [] = 0

        | baseToInt (l::ls) = l*k + toInt_inner(b,k*b) ls

  in

    baseToInt

  end;





fun toInt b = toInt_inner(b,1);







(* 2.2 *)

fun toBase b 0 = []

  | toBase b n = (n mod b)::toBase b (n div b);



(* 2.3 *)

fun convert (b1,b2) = 

  fn L:int list => toBase b2  (toInt b1 L);