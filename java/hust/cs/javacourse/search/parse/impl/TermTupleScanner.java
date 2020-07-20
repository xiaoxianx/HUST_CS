package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TermTupleScanner extends AbstractTermTupleScanner {

    private  String inputString=null;
    private  TermTuple termtuple = new TermTuple();
    private  List<String> termlist=null;
    private  int termindex=0;
    private  StringBuffer buf = new StringBuffer();
    private Iterator<String> it= null;

    /**
     * 缺省构造函数
     * 将buffreader 读出字符串 ，
     * 字符串 》单词list
     * list》split
     */
    public TermTupleScanner() {
    }
    /**
     * 构造函数
     *
     * @param input ：指定输入流对象，应该关联到一个文本文件
     */
    public TermTupleScanner(BufferedReader input) {
        super(input);
        it= termlist.iterator();
    }
    {
        try{
            inputString = input.readLine();
            while( inputString!= null){
                buf.append(inputString).append("\n"); //reader.readLine())返回的字符串会去掉换行符，因此这里要加上
                inputString = input.readLine();
            }
            inputString = buf.toString().trim(); //去掉最后一个多的换行符
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        StringSplitter spliter= new StringSplitter();
        spliter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
        termlist=spliter.splitByRegex(inputString);
    }


    /**
     * 获得下一个三元组
     *
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        if (it.hasNext()) {
            termtuple.term=new Term(it.next());
            termtuple.curPos=++termindex;
            return termtuple;
        }
        return null;
    }
}
