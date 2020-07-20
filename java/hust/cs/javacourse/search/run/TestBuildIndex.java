package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args){
        /*
        //docbuider 里面构建
              List<String> filepaths= FileUtil.list(Config.DOC_DIR,".txt");
        Iterator<String> it=filepaths.iterator();
        int docIdindex=0;
        DocumentBuilder docnewbuider=new DocumentBuilder();
        IndexBuilder indexBuider=new IndexBuilder(docnewbuider);
        AbstractDocument abstractDocument=null;
        AbstractIndex absIndex=null;
        while (it.hasNext()) {
            try {
                String s=it.next();
                BufferedReader bfreader=new BufferedReader(new InputStreamReader(new FileInputStream(new File(s))));
                AbstractTermTupleStream ts=new StopWordTermTupleFilter(new LengthTermTupleFilter(new LengthTermTupleFilter(new TermTupleScanner(bfreader))));
                abstractDocument = docnewbuider.build(++docIdindex, s, ts);
                absIndex=indexBuider.buildIndex(Config.DOC_DIR.concat(".tostring"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }*/
        DocumentBuilder docnewbuider=new DocumentBuilder();

        IndexBuilder indexBuider=new IndexBuilder(docnewbuider);
        indexBuider.buildIndex(Config.DOC_DIR);


    }
}
