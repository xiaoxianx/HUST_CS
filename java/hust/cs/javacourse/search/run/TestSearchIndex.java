package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Posting;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.Hit;
import hust.cs.javacourse.search.query.impl.IndexSeacher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args){

        String queryWord=null; //问询单词
        String queryWord1,queryWord2;
        String pattern1="[a-zA-z]+&[a-zA-z]+";
        String pattern2="[a-zA-z]+\\|[a-zA-z]+";
        String[] tempStrings;
        AbstractHit[] hitlist=null;
        AbstractHit[] hitlist2=null;//存短语查询
        AbstractHit temhit=null;
        Sort sort1=new SimpleSorter();


        IndexSeacher seacher=new IndexSeacher();
        seacher.open("savaIndex.bin");
        System.out.println("load index saveIndex.bin  succesfully");
        System.out.println("intput format: xxxx(single worad searc)  XXX|XXX(double word logical or)   xxx&xxx(double word logical and)  XXX#XXX(phrase search) quit(quit)");
        System.out.println("no output hints  a blank resault");
        Scanner scan=new Scanner(System.in);
        queryWord=scan.nextLine();
        while (!queryWord.equals("quit")) {  //queryWord!="quit"  error
            if (queryWord.matches(pattern1)) {
                tempStrings=queryWord.split("&");
                queryWord1=tempStrings[0];
                queryWord2=tempStrings[1];
                hitlist=seacher.search(new Term(queryWord1),new Term(queryWord2),sort1, AbstractIndexSearcher.LogicalCombination.ADN);
                if (hitlist!= null) {
                    for (AbstractHit hi : hitlist)
                        System.out.print(hi.toString());
                }
            } else if (queryWord.matches(pattern2)) {
                tempStrings = queryWord.split("\\|");
                queryWord1 = tempStrings[0];
                queryWord2 = tempStrings[1];
                hitlist = seacher.search(new Term(queryWord1), new Term(queryWord2), sort1, AbstractIndexSearcher.LogicalCombination.OR);
                if (hitlist!= null) {
                    for (AbstractHit hi : hitlist)
                        System.out.print(hi.toString());
                }
            } else if(queryWord.matches("[a-zA-z]+")){
                hitlist = seacher.search(new Term(queryWord),sort1);
                if (hitlist!= null) {
                    for (AbstractHit hi : hitlist)
                        System.out.print(hi.toString());
                }

            } else if (queryWord.matches("[a-zA-z]+#[a-zA-z]+")) {
                tempStrings=queryWord.split("#");
                queryWord1=tempStrings[0];
                queryWord2=tempStrings[1];
                hitlist=seacher.search(new Term(queryWord1),new Term(queryWord2),sort1, AbstractIndexSearcher.LogicalCombination.ADN);
                if (hitlist==null){ queryWord=scan.nextLine();continue; }
                int hitNm=0; //存构造新hit数组长度
                for (AbstractHit hi : hitlist) {
                    int filenum= hi.getTermPostingMapping().size();
                    Map<AbstractTerm, AbstractPosting> mm=hi.getTermPostingMapping();
                    List<Integer> intlist1=mm.get(new Term(queryWord1)).getPositions();
                    List<Integer> intlist2=mm.get(new Term(queryWord2)).getPositions();
                    List<Integer> intlist3=new ArrayList<>();
                    int addnum=0; //短语数量 存freq
                    for (int i = 0; i < intlist1.size(); i++) {
                        if (intlist2.contains(intlist1.get(i)+1)) {
                            intlist3.add(intlist1.get(i));
                            addnum++;
                        }
                    }
                    if (intlist3.size() != 0) {   //说明这个文件有符合的，构造新的hit
                        Map<AbstractTerm, AbstractPosting> mm2=new HashMap<>();;
                        AbstractPosting pos=new Posting(mm.get(new Term(queryWord1)).getDocId(),addnum,intlist3);
                        mm2.put(new Term(queryWord1+" "+queryWord2), pos);
                        temhit=new Hit(hi.getDocId(),hi.getDocPath(),mm2);
                        hitlist[hitNm++]=temhit;  //hitlist 没用了 原址改
                    }
                }
                //hitNm--;
                hitlist2=new AbstractHit[hitNm];
                for (int n = 0; n < hitNm; n++) {
                    hitlist2[n]=hitlist[n];
                }
                for(AbstractHit hi:hitlist2)
                    System.out.println(hi.toString());

            } else
                ;
            queryWord=scan.nextLine();
        }
    }
}

