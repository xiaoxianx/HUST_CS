package InventoryManage;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class login_controller {

    @FXML
    private Button btn_exit,btn_login;
    @FXML
    private TextField field_pswd,field_account;

    static public char logType; // c 采购 s 审核 x 销售
    static public String accountID=null;
    static public String pswd=null;
    Statement stmt = null;
    ResultSet rs=null;

    @FXML
    private void on_btn_exit_clicked(ActionEvent event) throws SQLException
    {
        Event.fireEvent(MainApp.getPrimaryStage(),
                new WindowEvent(MainApp.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    @FXML
    private void on_btn_login_clicked(ActionEvent event) {
        accountID = field_account.getText();
        pswd = field_pswd.getText();
        if (accountID.isEmpty() || pswd.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                    "请先输入账户和密码！", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else if (accountID.length() != 6 || pswd.length() != 6) {
            JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                    "账户密码格式错误", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else{
            try {
                stmt = MainApp.conn.createStatement();
                rs=stmt.executeQuery("select * from account where  sno='"+accountID+"' and spswd='"+pswd+"'");
                if(!rs.first()){
                    JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                            "账户密码不匹配！", "警告", JOptionPane.WARNING_MESSAGE);
                    return;}
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(accountID.charAt(0)=='s'||accountID.charAt(0)=='x'||accountID.charAt(0)=='c')
            logType=accountID.charAt(0);
        switch (logType) {
            case 'c':
                MainApp.setBuyUi();
                break;
            case 's':
                MainApp.setChecknUi();
                break;
            case 'x':
                MainApp.setSellinUi();
                break;
        }
    }
}
