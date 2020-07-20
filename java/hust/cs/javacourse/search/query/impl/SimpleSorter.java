package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.Sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleSorter implements Sort {
    /**
     * 对命中结果集合根据文档得分排序
     *
     * @param hits ：命中结果集合
     */
    @Override
    public void sort(List<AbstractHit> hits) {
/*        //hits.sort(Comparator.comparingDouble(Hit::getScore));
        Collections.sort(hits, new Comparator<AbstractHit>() {
            @Override
            public int compare(AbstractHit abstractHit, AbstractHit t1) {
                return (int) (score(abstractHit) - score(t1));

                     *//*            @Override
                    public boolean equals(Object o) {
                        return false;
                    }

                    @Override
                    public Comparator<AbstractHit> reversed() {
                        return null;
                    }*//*
            }
        });*/
        Collections.sort(hits);
    }

    /**
     * <pre>
     * 计算命中文档的得分, 作为命中结果排序的依据.
     *      计算文档的得分可以采取不同的策略, 因此这里的设计模式采用了策略模式，没有把这个方法放到AbstractHit及其子类里.
     *      而是放到接口Sort里，当我们需要不同的排序策略，只需要重新实现Sort的子类即可。即排序策略与被排序的对象(AbstractHit及其子类)应该分开。
     *      比如如果不排序，只需实现一个最简单的Sort接口实现类，比如叫NullSort类，在这个类里把所有文档的得分设置成一样的值。
     *      文档的得分值计算出来后要设置到AbstractHit子类对象里.
     * @param hit ：命中文档
     * @return ：命中文档的得分
     * </pre>
     */
    @Override
    public double score(AbstractHit hit) {
        return hit.getScore();
    }

    public SimpleSorter() {
    }
}


/*
计算文档的得分可以采取不同的策略, 因此这里的设计模式采用了策略模式，没有把这个方法放到AbstractHit及其子类里。而是放到接口Sort里
        当我们需要不同的排序策略，只需要重新实现Sort的子类即可。即排序策略与被排序的对象(AbstractHit及其子类)应该分开
        比如如果不排序，只需实现一个最简单的Sort接口实现类，比如叫NullSort类，在这个类里把所有文档的得分设置成一样的值。又例如，如果把文档的得分值设置成等于文档id，就是实现了按文档id排序的简单策略。
        文档的得分值计算出来后要设置到AbstractHit子类对象里.
        Tips：如果集合里每个元素都实现了Comparable接口，那么只需要调用
        Collections.sort(hits）
        方法就可实现从小到大排序。这也是为什么很多抽象类都规定子类必须实现Comparable接
        口的原因。但我们需要的是按得分从大到小排序怎么办？把文档得分都取  负值即可。
*/
