package sample;



import hospital.MainApp;
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

public class Controller {

    @FXML
    private Button btn_exit,btn_login;
    @FXML
    private TextField field_pswd,field_account;
    @FXML
    private CheckBox ckbox_patient,ckbox_doctor;


    @FXML
    private void on_btn_exit_clicked(ActionEvent event)
    {
        System.out.println("s1ad");
    }
    @FXML
    private void on_btn_login_clicked(ActionEvent event) {
        System.out.println("sad");
    }
}
