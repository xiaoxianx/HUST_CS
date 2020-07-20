package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class t233 {
    public static void main(String args[]) {
        List<String> a= Arrays.asList(new String[]{"aaa", "bb", "vvv"});
       //a.remove();
        String[] b={"aaa", "bb", "vvv"};
        String[] c={"aaa", "bb", "vvv"};
        String[] d=new String[]{"aaa", "bb", "vvv"};
        String[] e=b;
        List<String[]> f=new ArrayList<>();
       f.add(b);
       f.add(new String[]{"qqq", "www"});

        e[2]="sad";
        System.out.println(a.toString());
        System.out.println(b[2]);
        System.out.println(d[2]);
        System.out.println("end");
        String s1="a";
        String s2="a";
        System.out.println(s1==s2);
        s2="b";
        System.out.println(s2);
         List<AbstractTermTuple> tuples = new ArrayList<AbstractTermTuple>();
         //tuples.

    }
}
