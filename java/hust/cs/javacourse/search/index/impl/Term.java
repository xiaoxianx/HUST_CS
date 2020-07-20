package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.*;

public class Term extends AbstractTerm {
    private String inputTerm;

    /**
     * 因为要作为HashMap里面的key，因此必须要覆盖hashCode方法
     * 返回对象的HashCode
     *
     * @return ：对象的HashCode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * 构造函数
     *
     * @param content ：Term内容
     */
    public Term(String content) {
        super(content);
    }

    /**
     * 构造函数
     *
     * @param content ：Term内容
     */
    public Term(String content, String inputTerm) {
        super(content);
        this.inputTerm = inputTerm;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getContent()==(String) obj)
            return true;
        return false;
    }

    /**
     *
     * @return format term
     */
    @Override
    public String toString() {
        return getContent();
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public void setContent(String content) {
        this.content=content;
    }

    /**
     * 比较二个Term大小（按字典序）
     * @param o： 要比较的Term对象
     * @return ： 返回二个Term对象的字典序差值
     */
    @Override
    public int compareTo(AbstractTerm o) {
        return this.content.compareTo(o.getContent());
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeUTF(this.content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readObject(ObjectInputStream in) {
        try {
            inputTerm=in.readUTF();
            this.content=inputTerm;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getInputTerm() {
        return inputTerm;
    }
}

/*
ObjectOutputStream output=new ObjectOutputStream(new FileOutputStream("term.dat"));
            output.writexxx(out);
但必须要注意的是本实验要求是作为类的方法来实现下面二个方法：
public abstract void writeObject(ObjectOutputStream out);
public abstract void readObject(ObjectInputStream in);
        另外要注意的是对象序列化文件后是二进制文件不是文本文件。因此如果要完成本实验要求的将内存中的索引写到文本文件，不能采用上面二个方法。
        推荐的做法是：在AbstractIndex的子类覆盖toString方法，将内存的内容转换成格式良好的字符串，再用FileUtils类的write方法写到文本文件里。*/
