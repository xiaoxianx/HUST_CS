package InventoryManage.module;
import javafx.beans.property.SimpleStringProperty;

public class lackRecord {
    private final SimpleStringProperty lno;
    private final SimpleStringProperty lname;
    private final SimpleStringProperty lnum;

    public String getLno() {
        return lno.get();
    }

    public SimpleStringProperty lnoProperty() {
        return lno;
    }

    public void setLno(String lno) {
        this.lno.set(lno);
    }

    public String getLname() {
        return lname.get();
    }

    public SimpleStringProperty lnameProperty() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public String getLnum() {
        return lnum.get();
    }

    public SimpleStringProperty lnumProperty() {
        return lnum;
    }

    public void setLnum(String lnum) {
        this.lnum.set(lnum);
    }

    //参数必须string
    public  lackRecord(String cino1,String ciname1,String cinum1) {
        this.lno=new SimpleStringProperty(cino1);
        this.lname=new SimpleStringProperty(ciname1);
        this.lnum=new SimpleStringProperty(cinum1);

    }

}
