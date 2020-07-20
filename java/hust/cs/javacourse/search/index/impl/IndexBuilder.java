package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static hust.cs.javacourse.search.util.Config.*;

public class IndexBuilder extends AbstractIndexBuilder {


    public IndexBuilder(AbstractDocumentBuilder docBuilder) {
        super(docBuilder);
    }

    /**
     * <pre>
     * 构建指定目录下的所有文本文件的倒排索引.
     *      需要遍历和解析目录下的每个文本文件, 得到对应的Document对象，再依次加入到索引，并将索引保存到文件.
     * @param rootDirectory ：指定save目录
     * @return ：构建好的索引
     * </pre>
     */
    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        AbstractIndex index = new Index();
        File file = new File(rootDirectory);
        List<String> filepaths = FileUtil.list(rootDirectory, ".txt");
        Iterator<String> it = filepaths.iterator();

        AbstractIndex absIndex = null;
        String tempString=null;
        while (it.hasNext()) {
            try {
                String s = it.next();
                BufferedReader bfreader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(s))));
                AbstractTermTupleStream ts = new StopWordTermTupleFilter(new LengthTermTupleFilter(new PatternTermTupleFilter(new TermTupleScanner(bfreader))));
/*                 // 测试查看扫描和过滤效果
                while (true) {
                    AbstractTermTuple a=ts.next();
                    if (a==null) break;
                    System.out.println(a.term.getContent());
                }*/
                AbstractDocument abstractDocument = docBuilder.build(++docId, s, ts);
/*              //测试构造 document
                List<AbstractTermTuple> lsp=abstractDocument.getTuples();
                for (AbstractTermTuple tple : lsp) {
                    System.out.println(tple.toString());
                }*/
                index.addDocument(abstractDocument);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        //存二进制index问文件INDEX_DIR+"txtSaveIndex.bin"
        index.save(new File("savaIndex.bin"));

      //  System.out.println(index.toString());

        //存文件
        FileWriter fw= null;
        try {
            fw = new FileWriter(new File(INDEX_DIR+"indexsave.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw=new BufferedWriter(fw);
        try {
            bw.write(index.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return index;
    }
}

