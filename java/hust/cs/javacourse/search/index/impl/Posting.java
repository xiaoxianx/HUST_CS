package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Posting extends AbstractPosting {

    private Posting inputPosting;
    /**
     * 缺省构造函数
     */
    public Posting() {
    }

    /**
     * 构造函数
     *
     * @param docId     ：包含单词的文档id
     * @param freq      ：单词在文档里出现的次数
     * @param positions ：单词在文档里出现的位置
     */
    public Posting(int docId, int freq, List<Integer> positions) {
        super(docId, freq, positions);
    }

    /**
     * 返回Posting的字符串表示
     *
     * @return 字符串 format:  docId+" "+freq+" "+positions
     */
    @Override
    public String toString() {
        return docId+" "+freq+" "+positions+"\n";
       /* return "Posting{" +
                "docId=" + docId +
                ", freq=" + freq +
                ", positions=" + positions +
                '}';*/
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(docId);
            out.writeInt(freq);
            out.writeObject(positions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return  file 得到的 posting
     */
    public Posting getInputPosting() {
        return inputPosting;
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docId=in.readInt();
            this.freq=in.readInt();
            this.positions=(List<Integer>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断二个Posting内容是否相同
     *
     * @param //Object ：要比较的另外一个Posting
     * @return 如果内容相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posting)) return false;
        Posting posting = (Posting) o;
        return Objects.equals(getInputPosting(), posting.getInputPosting());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInputPosting());
    }

    /**
     * 返回包含单词的文档id
     *
     * @return ：文档id
     */
    @Override
    public int getDocId() {
        return this.docId;
    }

    /**
     * 设置包含单词的文档id
     *
     * @param docId ：包含单词的文档id
     */
    @Override
    public void setDocId(int docId) {
        this.docId=docId;
    }

    /**
     * 返回单词在文档里出现的次数
     *
     * @return ：出现次数
     */
    @Override
    public int getFreq() {
        return this.freq;
    }

    /**
     * 设置单词在文档里出现的次数
     *
     * @param freq :单词在文档里出现的次数
     */
    @Override
    public void setFreq(int freq) {
            this.freq=freq;
    }

    /**
     * 返回单词在文档里出现的位置列表
     *
     * @return ：位置列表
     */
    @Override
    public List<Integer> getPositions() {
        return this.positions;
    }

    /**
     * 设置单词在文档里出现的位置列表
     *
     * @param positions ：单词在文档里出现的位置列表
     */
    @Override
    public void setPositions(List<Integer> positions) {
        this.positions=positions;
    }

    /**
     * 比较二个Posting对象的大小（根据docId）
     *
     * @param o ： 另一个Posting对象
     * @return ：二个Posting对象的docId的差值
     */
    @Override
    public int compareTo(AbstractPosting o) {
        return this.docId-o.getDocId();
    }

    /**
     * 对内部positions从小到大排序
     */
    @Override
    public void sort() {
        Collections.sort(positions);
    }
}
