package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StopWords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopWordTermTupleFilter extends AbstractTermTupleFilter {
    public StopWordTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 获得下一个三元组
     *
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple a=input.next();
        List<String> ListSring= Arrays.asList(StopWords.STOP_WORDS);
        // tostring.indexof(xx!=-1)
        while (a != null) {
            if(!ListSring.contains(a.term.getContent()))
                return a;
            a=input.next();
        }
        return null;
    }
}
