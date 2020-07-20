package hospital;

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
                    ("jdbc:mysql://localhost:3306/javalab2_hospital?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true",
                            "root",
                            "5953"
                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Stage primarystage = null;
    private static Scene scene_reg = null;
    private static Scene scene_login = null;
    private static Scene scene_doc = null;
    static String pat_doc_num;

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, SQLException {
        try {
            primaryStage.setTitle("华中科技大学医院挂号系统");
            primarystage = primaryStage;
            AnchorPane root_login = FXMLLoader.load(getClass().getResource("view/login.fxml"));
            SplitPane root_reg = FXMLLoader.load(getClass().getResource("view/register.fxml"));
            SplitPane root_doc = FXMLLoader.load(getClass().getResource("view/doctor.fxml"));
            // rootreg 作为pane
            //返回类型一定要 同最外层pane相同
            scene_login = new Scene(root_login);
            scene_reg = new Scene(root_reg);
            scene_doc = new Scene(root_doc);
            primaryStage.setScene(scene_login);
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
    public static void setLoginUi()
    {
        primarystage.setScene(scene_login);
    }
    public static void setRegUi()
    {
        primarystage.setScene(scene_reg);
    }
    public static void setDocUi()
    {
        primarystage.setScene(scene_doc);
    }

}


//mysql 同步时间
//show variables like'%time_zone';
//set global time_zone = '+8:00';

/*
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(“sample.fxml"));
                    Pane root = loader.load();


            Controller controller = loader.getController();
            controller.setMyApp(this);


            Scene scene = new Scene(root,400,600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
*/



/*
public class Controller {
    @FXML
    private TextField text;

    private Main myApp;

    public void setMyApp(Main myApp) {
        this.myApp = myApp;
    }

    @FXML
    public void onClick(ActionEvent e) {
        text.setText("Button Cliked");
    }
}
*/
