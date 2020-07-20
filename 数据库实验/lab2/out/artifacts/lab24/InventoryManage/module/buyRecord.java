package InventoryManage.module;

import javafx.beans.property.SimpleStringProperty;

public class buyRecord {
    private final SimpleStringProperty c1;
    private final SimpleStringProperty  cbno;
    private final SimpleStringProperty  cbgno;
    private final SimpleStringProperty  cbgname;
    private final SimpleStringProperty  cbnum;
    private final SimpleStringProperty  cbstatus;
    private final SimpleStringProperty  cbstime;
    private final SimpleStringProperty  cbetime;
    private final SimpleStringProperty  cbbuyer;
    private final SimpleStringProperty  cbchecker;

    public buyRecord(String c1, String cbno, String cbgno, String cbgname, String cbnum, String cbstatus, String cbstime, String cbetime, String cbbuyer, String cbchecker) {
        this.c1 =new SimpleStringProperty( c1);
        this.cbno = new SimpleStringProperty(cbno) ;
        this.cbgno = new SimpleStringProperty(cbgno) ;
        this.cbgname = new SimpleStringProperty(cbgname) ;
        this.cbnum = new SimpleStringProperty(cbnum) ;
        this.cbstatus =new SimpleStringProperty(cbstatus)  ;
        this.cbstime = new SimpleStringProperty(cbstime) ;
        this.cbetime = new SimpleStringProperty(cbetime) ;
        this.cbbuyer = new SimpleStringProperty(cbbuyer) ;
        this.cbchecker = new SimpleStringProperty(cbchecker) ;
    }

    public String getC1() {
        return c1.get();
    }

    public SimpleStringProperty c1Property() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1.set(c1);
    }

    public String getCbno() {
        return cbno.get();
    }

    public SimpleStringProperty cbnoProperty() {
        return cbno;
    }

    public void setCbno(String cbno) {
        this.cbno.set(cbno);
    }

    public String getCbgno() {
        return cbgno.get();
    }

    public SimpleStringProperty cbgnoProperty() {
        return cbgno;
    }

    public void setCbgno(String cbgno) {
        this.cbgno.set(cbgno);
    }

    public String getCbgname() {
        return cbgname.get();
    }

    public SimpleStringProperty cbgnameProperty() {
        return cbgname;
    }

    public void setCbgname(String cbgname) {
        this.cbgname.set(cbgname);
    }

    public String getCbnum() {
        return cbnum.get();
    }

    public SimpleStringProperty cbnumProperty() {
        return cbnum;
    }

    public void setCbnum(String cbnum) {
        this.cbnum.set(cbnum);
    }

    public String getCbstatus() {
        return cbstatus.get();
    }

    public SimpleStringProperty cbstatusProperty() {
        return cbstatus;
    }

    public void setCbstatus(String cbstatus) {
        this.cbstatus.set(cbstatus);
    }

    public String getCbstime() {
        return cbstime.get();
    }

    public SimpleStringProperty cbstimeProperty() {
        return cbstime;
    }

    public void setCbstime(String cbstime) {
        this.cbstime.set(cbstime);
    }

    public String getCbetime() {
        return cbetime.get();
    }

    public SimpleStringProperty cbetimeProperty() {
        return cbetime;
    }

    public void setCbetime(String cbetime) {
        this.cbetime.set(cbetime);
    }

    public String getCbbuyer() {
        return cbbuyer.get();
    }

    public SimpleStringProperty cbbuyerProperty() {
        return cbbuyer;
    }

    public void setCbbuyer(String cbbuyer) {
        this.cbbuyer.set(cbbuyer);
    }

    public String getCbchecker() {
        return cbchecker.get();
    }

    public SimpleStringProperty cbcheckerProperty() {
        return cbchecker;
    }

    public void setCbchecker(String cbchecker) {
        this.cbchecker.set(cbchecker);
    }
}
