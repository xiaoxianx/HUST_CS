package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.index.impl.PostingList;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IndexSeacher extends AbstractIndexSearcher {
    public IndexSeacher() {
    }

    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     *
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        this.index.load(new File(indexFile));
    }

    /**
     * 根据单个检索词进行搜索
     *
     * @param queryTerm ：检索词
     * @param sorter    ：排序器
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        //AbstractHit[] hitList=new AbstractHit[Config.MAX_SEARCG_FILE_NUM]; // sort compare NPE

        AbstractPostingList plst=this.index.search(queryTerm);
        if(plst==null) return null;
        AbstractHit[] hitList=new AbstractHit[plst.size()];
        if (plst==null) return null;
        AbstractPosting tempPosting=null;


        double d=0.0; int i=0;
        tempPosting=plst.get(i);
        Map<AbstractTerm,AbstractPosting> mp=new HashMap<>();
        while (tempPosting!= null) {
            mp.put(queryTerm,tempPosting);
                hitList[i]=new Hit(tempPosting.getDocId(), index.getDocName(tempPosting.getDocId()), mp) ;
                hitList[i].setScore(mp.entrySet().iterator().next().getValue().getFreq());
                hitList[i].setContent(FileUtil.read(index.getDocName(tempPosting.getDocId())));
                tempPosting=plst.get(++i);

        }
        sorter.sort(Arrays.asList(hitList));
        return hitList;


    }

    /**
     * 根据二个检索词进行搜索
     *
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter     ：    排序器
     * @param combine    ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractHit[] hitList1=search(queryTerm1,sorter);
        AbstractHit[] hitList2=search(queryTerm2,sorter);
        AbstractHit[] hitlist3;
        ArrayList<AbstractHit> hitlist=new ArrayList<>();
        AbstractHit temphit;

//        AbstractPostingList plst1=this.index.search(queryTerm1);
//        AbstractPostingList plst2=this.index.search(queryTerm2);
        AbstractPostingList plst3=null;
        // removeall contain addall 不合适，应该只参考docid；
        switch (combine) {
            case ADN:
                /*int i=0;
                while (plst1.get(i) != null) */
                if(hitList1==null||hitList2==null) return  null;
                double tepscore=0.0;
                for (AbstractHit a : hitList1) {
                    int id=a.getDocId();
                    for (AbstractHit b : hitList2) {
                        if (b.getDocId() == id) {
                            Map<AbstractTerm,AbstractPosting> mp=new HashMap<>();
                            mp.putAll(a.getTermPostingMapping());
                            mp.putAll(b.getTermPostingMapping());
                            temphit=new Hit(id,b.getDocPath(),mp);
                            temphit.setContent(b.getContent());
                            temphit.setScore(a.getScore()+b.getScore());
                            hitlist.add(temphit);
                        }
                    }
                }
                hitlist3=hitlist.toArray(new AbstractHit[hitlist.size()]);
                break;
            case OR:
                    hitlist3=new AbstractHit[hitList1.length+hitList2.length];
                    int j=0;
                    for(;j<hitList1.length;j++)
                        hitlist3[j]=hitList1[j];
                    for(int k=0;k<hitList2.length;k++,j++)
                        hitlist3[j]=hitList2[k];
                break;
            default:
                return null;
        }
        sorter.sort(Arrays.asList(hitlist3));
        return hitlist3;
    }

}
