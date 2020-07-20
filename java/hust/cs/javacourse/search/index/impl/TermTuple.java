package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;

public class TermTuple extends AbstractTermTuple {
    public TermTuple() {
        this.term=new Term(" ");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        TermTuple a= (TermTuple) obj;
        if(this.term==a.term&&this.curPos==a.curPos)
            return true;
        return false;
    }

    /**
     *
     * @return  format  term+sp+freq+sp+curpos
     */
    @Override
    public String toString() {
        return this.term+" "+this.freq+" "+this.curPos+"\n";
    }

}
