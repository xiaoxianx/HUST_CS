package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.util.Config;

import javax.swing.text.Position;
import java.io.*;
import java.util.*;

/**
 * AbstractIndex的具体实现类
 */
public class Index extends AbstractIndex {
    private AbstractPosting newPosting=null;
    private AbstractPostingList ps=null;

    public Index() {
    }
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {

 /*      return
                "docIdToDocPathMapping: " + docIdToDocPathMapping +"\n"+
                "termToPostingListMapping: " + termToPostingListMapping ;*/
        String s="";
        String ss="";
        Iterator<Map.Entry<Integer,String>> it1=docIdToDocPathMapping.entrySet().iterator();
        Iterator<Map.Entry<AbstractTerm, AbstractPostingList>> it2=termToPostingListMapping.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<Integer, String> tempMap1=it1.next();
            ss=tempMap1.getKey()+" "+tempMap1.getValue()+"\n";
            s=s.concat(ss);
        }
        while (it2.hasNext()) {
            Map.Entry<AbstractTerm, AbstractPostingList> tempMap2=it2.next();
            ss=tempMap2.getKey().getContent()+"\n"+ tempMap2.getValue().toString();
            s=s.concat(ss);
        }
        s.trim();
        return s;
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        if (!docIdToDocPathMapping.containsKey(document.getDocId())) {
            docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        }
        for (int j =1 ; j <=document.getTupleSize(); j++) {
            if (!termToPostingListMapping.containsKey(document.getTuple(j-1).term)) {
                List<Integer> emptylis=new LinkedList<>();
                emptylis.add(j);
                Posting np=new Posting(document.getDocId(),1,emptylis);
                ps=new PostingList();
                ps.add(np);
                termToPostingListMapping.put(document.getTuple(j-1).term, ps);

            } else {
                ps = termToPostingListMapping.get(document.getTuple(j - 1).term);
                int indexcDocId = ps.indexOf(document.getDocId());
                if (indexcDocId == -1) {
                    List<Integer> emptylis=new LinkedList<>();
                    Posting np=new Posting(document.getDocId(),0,emptylis);
                    ps.add(np);
                    indexcDocId = ps.indexOf(document.getDocId());
                }
                newPosting = ps.get(indexcDocId);
                newPosting.setFreq(newPosting.getFreq()+1);
                newPosting.getPositions().add(j);
                newPosting.setPositions(newPosting.getPositions());
            }
        }
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try {
            ObjectInputStream a=new ObjectInputStream(new FileInputStream(Config.INDEX_DIR+file.getName()));
            this.docIdToDocPathMapping= (Map<Integer, String>) a.readObject();
            this.termToPostingListMapping=(Map<AbstractTerm, AbstractPostingList>) a.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try {
            ObjectOutputStream a = new ObjectOutputStream(new FileOutputStream(Config.INDEX_DIR+file.getName()));
            a.writeObject(docIdToDocPathMapping);
            a.writeObject(termToPostingListMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
            return termToPostingListMapping.get(term);//找不到自动null
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return termToPostingListMapping.keySet();
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     *      采取获得map.entryset,iterator  得到一个映射修改 put覆盖原来的
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        Iterator<Map.Entry<AbstractTerm, AbstractPostingList>> it=termToPostingListMapping.entrySet().iterator();
        while (it.hasNext()) {
           // Map.Entry<AbstractTerm, AbstractPostingList> entry=it.next();
           // List<AbstractPostingList> lisp= (List<AbstractPostingList>) it.next().getValue();
            AbstractPostingList a=it.next().getValue();
            for (int i = 0; i < a.size(); i++) {
                a.get(i).sort();
               // a.get(i).setPositions(a.get(i).sort());
            }
            a.sort();

            termToPostingListMapping.put(it.next().getKey(), a);
        }

    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
        //Object get(Object k)
        //返回指定键所映射的值；如果此映射不包含该键的映射关系，则返回 null。
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(docIdToDocPathMapping);
            out.writeObject(termToPostingListMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docIdToDocPathMapping=(Map<Integer, String>)in.readObject();
            this.termToPostingListMapping=(Map<AbstractTerm,AbstractPostingList>)in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


