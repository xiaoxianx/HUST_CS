package hospital;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;

public class login_controller {

    @FXML
    private Button btn_exit,btn_login;
    @FXML
    private TextField field_pswd,field_account;
    @FXML
    private CheckBox ckbox_patient,ckbox_doctor;

    static public boolean logType;/* true if doctor type*/
    //static public char[] accoundID=new char[6];
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
        if ((ckbox_doctor.isSelected() && ckbox_patient.isSelected())||((!ckbox_doctor.isSelected()) &&(!ckbox_patient.isSelected()))) {
            JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                    "请选择一个登录类型！", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (ckbox_patient.isSelected()) {
            logType = false;
            try {
                stmt = MainApp.conn.createStatement();
                rs=stmt.executeQuery("select * from patient where  pid='"+accountID+"' and password='"+pswd+"'");
                if (rs.next()) {
                    MainApp.pat_doc_num=accountID;
                    MainApp.setRegUi();
                    System.out.println("病患登录成功");
                } else {
                    JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                            "账号密码不匹配", "警告", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            logType = true;
            try {
                stmt = MainApp.conn.createStatement();
                rs=stmt.executeQuery("select * from  doctor where  docid='"+accountID+"'  and password='"+pswd+"'");
               // stmt.execute("select * from  doctor where  docid='"+accountID+"'  and password='"+pswd+"'");   //用不对
                if (rs.next()) {
                    MainApp.pat_doc_num=accountID;
                    MainApp.setDocUi();
                    System.out.println(rs.getString(1)+"医生登录成功");
                } else {
                    JOptionPane.showMessageDialog(new JFrame().getContentPane(),
                            "账号密码不匹配", "警告", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
