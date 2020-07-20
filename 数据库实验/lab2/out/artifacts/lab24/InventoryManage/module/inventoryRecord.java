package InventoryManage.module;
import javafx.beans.property.SimpleStringProperty;

public class inventoryRecord {
    private final SimpleStringProperty cino;
    private final SimpleStringProperty ciname;
    private final SimpleStringProperty cilocation;
    private final SimpleStringProperty cinum;
    private final SimpleStringProperty ciproducer;

    //参数必须string
    public  inventoryRecord(String cino1,String ciname1,String cilocation1,String cinum1,String ciproducer1 ) {
        this.cino=new SimpleStringProperty(cino1);
        this.ciname=new SimpleStringProperty(ciname1);
        this.cilocation=new SimpleStringProperty(cilocation1);
        this.cinum=new SimpleStringProperty(cinum1);
        this.ciproducer=new SimpleStringProperty(ciproducer1);
    }

    public String getCino() {
        return cino.get();
    }

    public SimpleStringProperty cinoProperty() {
        return cino;
    }

    public void setCino(String cino) {
        this.cino.set(cino);
    }

    public String getCiname() {
        return ciname.get();
    }

    public SimpleStringProperty cinameProperty() {
        return ciname;
    }

    public void setCiname(String ciname) {
        this.ciname.set(ciname);
    }

    public String getCilocation() {
        return cilocation.get();
    }

    public SimpleStringProperty cilocationProperty() {
        return cilocation;
    }

    public void setCilocation(String cilocation) {
        this.cilocation.set(cilocation);
    }

    public String getCinum() {
        return cinum.get();
    }

    public SimpleStringProperty cinumProperty() {
        return cinum;
    }

    public void setCinum(String cinum) {
        this.cinum.set(cinum);
    }

    public String getCiproducer() {
        return ciproducer.get();
    }

    public SimpleStringProperty ciproducerProperty() {
        return ciproducer;
    }

    public void setCiproducer(String ciproducer) {
        this.ciproducer.set(ciproducer);
    }
}
