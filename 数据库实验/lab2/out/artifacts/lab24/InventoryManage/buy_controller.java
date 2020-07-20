package InventoryManage;
import InventoryManage.module.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import javafx.collections.*;

import javax.swing.*;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class buy_controller implements Initializable {
    Statement stmt=null;
    ResultSet namelistResult=null;
    Map<String, Integer> map1=null;
    int bgno=0;
    String bname;

    //tableview的结构
    ObservableList<inventoryRecord> inventoryRecords=FXCollections.observableArrayList();
    ObservableList<buyRecord> buyRecords=FXCollections.observableArrayList();
    ObservableList<lackRecord> lackRecords=FXCollections.observableArrayList();
    ObservableList<buyRecord> buyRecordsWillDelet=FXCollections.observableArrayList();


    @FXML
    private Button btn_logout,btn_exit;
    @FXML
    private void on_btn_exit_clicked(ActionEvent event)
    {
        Event.fireEvent(MainApp.getPrimaryStage(),
                new WindowEvent(MainApp.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    @FXML
    private void on_btn_logout_clicked(ActionEvent event)
    {
        MainApp.setloginUi();
    }


    @FXML
    private Button btn_clear,btn_submit,refreshBtn2,refreshBtn1;
    @FXML
    private TextField buyno,buynum;
    @FXML
    private ComboBox buyname;
    @FXML
    protected TableView buyTable,inventoryTable,inventoryTable1;
    @FXML
    private TableColumn<Parameter,String> lno,lname,lnum;
    @FXML
    private TableColumn<Parameter,String> cino,ciname,cilocation,cinum,ciproducer;
    @FXML
    private TableColumn<Parameter,String> c1,cbno,cbgno,cbgname,cbnum,cbstatus,cbstime,cbetime,cbbuyer,cbchecker;
    @FXML
    private CheckBox hideold,onlyMe;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //进货货品可选项加载
            stmt=MainApp.conn.createStatement();
            namelistResult=stmt.executeQuery("select iname,ino from inventory");
            map1 = new HashMap<String, Integer>();
            while (namelistResult.next()) {
                map1.put(namelistResult.getString(1),namelistResult.getInt(2) );
            }
            ObservableList<String> obslist1=FXCollections.observableArrayList(new ArrayList<>(map1.keySet()));
            //ObservableMap<String, Integer> observableMap1 = FXCollections.observableMap(map1);
            //observableMap.addListener  //加listner 当map1改变，observableMap1相应改变
            buyname.setItems(obslist1);

            //库存初次加载
            ResultSet inventoryRs=stmt.executeQuery("select * from inventory");
            while (inventoryRs.next()) {
                inventoryRecords.add(
                        new inventoryRecord(
                                inventoryRs.getString(1),inventoryRs.getString(2),
                                inventoryRs.getString(3),inventoryRs.getString(4),
                                inventoryRs.getString(5)));
            }
            //前面是fx id 后面 inventoryRecord 里面量
            cino.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cino"));
            ciname.setCellValueFactory(new PropertyValueFactory<Parameter,String>("ciname"));
            cilocation.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cilocation"));
            cinum.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cinum"));
            ciproducer.setCellValueFactory(new PropertyValueFactory<Parameter,String>("ciproducer"));
            inventoryTable.setItems(inventoryRecords);

            //缺货加载
            ResultSet inventoryRs1=stmt.executeQuery("select * from lack");
            while (inventoryRs1.next()) {
                lackRecords.add(
                        new lackRecord(
                                inventoryRs1.getString(1),inventoryRs1.getString(2),
                                inventoryRs1.getString(3)));
            }
            //前面是fx id 后面 inventoryRecord1 里面量
            lno.setCellValueFactory(new PropertyValueFactory<Parameter,String>("lno"));
            lname.setCellValueFactory(new PropertyValueFactory<Parameter,String>("lname"));
            lnum.setCellValueFactory(new PropertyValueFactory<Parameter,String>("lnum"));
            inventoryTable1.setItems(lackRecords);


            //订单初次加载
            ResultSet buyRs=stmt.executeQuery("select * from buy");
            while (buyRs.next()) {
                buyRecords.add(
                        new buyRecord("采购",buyRs.getString(1),buyRs.getString(2),
                                buyRs.getString(3),buyRs.getString(4),buyRs.getString(5),
                                buyRs.getString(6),buyRs.getString(7),buyRs.getString(8),
                                buyRs.getString(9)));
            }
            //前面是fx id 后面 inventoryRecord 里面量
            c1.setCellValueFactory(new PropertyValueFactory<Parameter,String>("c1"));
            cbno.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbno"));
            cbgno.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbgno"));
            cbgname.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbgname"));
            cbnum.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbnum"));
            cbstatus.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbstatus"));
            cbstime.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbstime"));
            cbetime.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbetime"));
            cbbuyer.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbbuyer"));
            cbchecker.setCellValueFactory(new PropertyValueFactory<Parameter,String>("cbchecker"));
            buyTable.setItems(buyRecords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
    buyTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); //可多行选取
    ObservableList<buyRecord> ob_unreg = FXCollections.observableArrayList();
    ob_unreg=buyTable.getSelectionModel().getSelectedItems();
    for(int i=0;i<ob_unreg.size();++i){处理}
    */
    @FXML  //刷新库存
    private  void refreshBtn2Clicked(ActionEvent event) {
        try {
            //库存加载
            ResultSet inventoryRs=stmt.executeQuery("select * from inventory");
            inventoryRecords.clear();
            while (inventoryRs.next()) {
                inventoryRecords.add(
                        new inventoryRecord(
                                inventoryRs.getString(1),inventoryRs.getString(2),
                                inventoryRs.getString(3),inventoryRs.getString(4),
                                inventoryRs.getString(5)));
            }
            inventoryTable.setItems(inventoryRecords);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML  //刷新缺货
    private  void refreshBtn3Clicked(ActionEvent event) {
        try {
            //库存加载
            ResultSet inventoryRs1=stmt.executeQuery("select * from lack");
            lackRecords.clear();
            while (inventoryRs1.next()) {
                lackRecords.add(
                        new lackRecord(
                                inventoryRs1.getString(1),inventoryRs1.getString(2),
                                inventoryRs1.getString(3)));
            }
            inventoryTable1.setItems(lackRecords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML  //刷新采购订单
    private  void refreshBtn1Clicked(ActionEvent event) {
        try {

            String sql1;
            if(hideold.isSelected()&&onlyMe.isSelected())    //不看已经完成的 只看我的
                sql1="select * from buy where bstatus!=1 and bbuyer='"+login_controller.accountID+"'";
            else if(hideold.isSelected())
                sql1="select * from buy where bstatus!=1";
            else if(onlyMe.isSelected())
                sql1="select * from buy where bbuyer='"+login_controller.accountID+"'";
            else
                sql1="select * from buy";
            ResultSet buyRs=stmt.executeQuery(sql1);
            buyRecords.clear();
            while (buyRs.next()) {
                buyRecords.add(
                        new buyRecord("采购",buyRs.getString(1),buyRs.getString(2),
                                buyRs.getString(3),buyRs.getString(4),buyRs.getString(5),
                                buyRs.getString(6),buyRs.getString(7),buyRs.getString(8),
                                buyRs.getString(9)));
            }
            buyTable.setItems(buyRecords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //删除未被审核的订单
    @FXML
    private Button deletBtn;
    @FXML
    private void deletBtnClicked(ActionEvent event) {
       buyRecordsWillDelet=buyTable.getSelectionModel().getSelectedItems();
        if (!buyRecordsWillDelet.get(0).getCbbuyer().equals(login_controller.accountID)) {  //String  要equal啊
            JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                    "只能删除自己的订单", "警告", JOptionPane.WARNING_MESSAGE);
        } else if (buyRecordsWillDelet.get(0).cbstatusProperty().getValue().equals("1")) {
            JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                    "已完成的订单不可删除", "警告", JOptionPane.WARNING_MESSAGE);
           // System.out.println(buyRecordsWillDelet.get(0).cbstatusProperty());
        } else {
            try {
                String no=buyRecordsWillDelet.get(0).getCbno();//单号
                stmt.execute("delete from buy where bno='"+no+"'");
                buyTable.getSelectionModel().getSelectedItems().clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        refreshBtn1.fire();
    }


    //刷新,选了顺便点一下刷新
    @FXML
    private void hideOldCheck(ActionEvent event) {refreshBtn1.fire(); }
    @FXML
    private void onlyMeCheck(ActionEvent event) { refreshBtn1.fire();}


    @FXML //由名字自动map商品号,save，填写
    private void OnNewNameSelect(ActionEvent event){
        bname=buyname.getValue().toString();
        bgno=map1.get( bname);
        buyno.setText(String.valueOf(bgno));
    }



    @FXML
    private void on_btn_clear_clicked(ActionEvent event){
        //TODO //buyname.setValue(null);  如何清空商品名？
        buynum.clear();
    }
    @FXML
    private void  on_btn_submit_clicked(ActionEvent event) {
        int num=0;
        if (!buynum.getText().equals(""))  // javafx swing gettext返回string不是null
             num= Integer.parseInt(buynum.getText().toString());
        if (bgno==0||num<=0) {
            JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                    "请选择商品并输入正整数采购数量", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            stmt=MainApp.conn.createStatement();
            stmt.execute("INSERT INTO buy VALUES(0,'" + bgno + "','" + bname + "','" + num + "',0," +
                    "now(),null,'" + login_controller.accountID + "',null)");
            //上面 true if the first result is a ResultSet object; false if it is an update count or there are no results
            JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                        "成功提交订单", "信息", JOptionPane.INFORMATION_MESSAGE);
            buynum.clear();
            refreshBtn1.fire();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}




