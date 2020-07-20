package InventoryManage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MainApp extends Application {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static Connection conn;

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/StorageManagement?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true",
                            "root",
                            "5953"
                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Stage primarystage = null;
    private static Scene scene_login = null;
    private static Scene scene_check = null;
    private static Scene scene_buy = null;
    private static Scene scene_sell = null;


    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, SQLException {
        try {
            // pane 返回类型一定要 同最fxml外层pane相同
            AnchorPane root_login = FXMLLoader.load(getClass().getResource("view/login.fxml"));
            scene_login = new Scene(root_login);
            SplitPane  root_buy = FXMLLoader.load(getClass().getResource("view/buy.fxml"));
            scene_buy = new Scene(root_buy);
            SplitPane  root_check = FXMLLoader.load(getClass().getResource("view/check.fxml"));
            scene_check = new Scene(root_check);
            SplitPane  root_sell = FXMLLoader.load(getClass().getResource("view/sell.fxml"));
            scene_sell = new Scene(root_sell);


            primaryStage.setTitle("库存管理系统1.0");
            primarystage = primaryStage;
            setloginUi();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    public void setPrimaryStage(Stage stage)
    {
        primarystage = stage;
    }
    public static Stage getPrimaryStage() {
        return primarystage;
    }
    public static void setloginUi()
    {
        primarystage.setScene(scene_login);
    }
    public static void setBuyUi()
    {
        primarystage.setScene(scene_buy);
    }
    public static void setSellinUi()
    {
        primarystage.setScene(scene_sell);
    }
    public static void setChecknUi()
    {
        primarystage.setScene(scene_check);
    }
}


//mysql 同步时间
//show variables like'%time_zone';
//set global time_zone = '+8:00';
