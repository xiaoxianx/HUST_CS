package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main extends Application {

       static Stage primarystage = null;
//    private SplitPane root_reg = null;
      private AnchorPane root_login = null;
//    private SplitPane root_doc = null;
//    private static Scene scene_reg = null;
//    private static Scene scene_login = null;
//    private static Scene scene_doc = null;
//    static String pat_doc_num;

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, SQLException {
        try {
            primarystage = primaryStage;
            primarystage.setTitle("华中科技大学医院挂号系统");
            root_login = FXMLLoader.load(getClass().getResource("sample.fxml"));
 //           root_login = FXMLLoader.load(getClass().getResource("view/login.fxml"));
//            root_reg = FXMLLoader.load(getClass().getResource("view/register.fxml"));
//            root_doc = FXMLLoader.load(getClass().getResource("view/doctor.fxml"));
            // rootreg 作为pane
            Scene scene_login = new Scene(root_login);
//            scene_reg = new Scene(root_reg);
//            scene_doc = new Scene(root_doc);
            primarystage.setScene(scene_login);
            primarystage.show();
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
//    public static void setLoginUi()
//    {
//        primarystage.setScene(scene_login);
//    }
//    public static void setRegUi()
//    {
//        primarystage.setScene(scene_reg);
//    }
//    public static void setDocUi()
//    {
//        primarystage.setScene(scene_doc);
//    }

}
