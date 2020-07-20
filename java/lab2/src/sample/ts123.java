package sample;



        import javafx.application.Application;
        import javafx.scene.paint.Color;
        import javafx.scene.control.*;
        import javafx.scene.layout.*;
        import javafx.scene.text.*;
        import javafx.stage.Stage;
        import javafx.scene.Scene;

public class ts123 extends Application {
    @Override
    public void start(Stage primaryStage) {
        //Panes
        StackPane stackPane=new StackPane();
        GridPane gridPane=new GridPane();
        //Nodes
        Button button=new Button("OK");
        TextField textUsername=new TextField();
        textUsername.setPromptText("Please Enter your Username...");
        PasswordField textPassword=new PasswordField();
        textPassword.setPromptText("Please Enter your Password...");
        //Panes
        gridPane.add(new Label("Username:"),0,0);
        gridPane.add(textUsername,1,0);
        gridPane.add(new Label("Password:"),0,1);
        gridPane.add(textPassword,1,1);
        gridPane.add(button,2,1);
        stackPane.getChildren().add(gridPane);
        Scene scene=new Scene(stackPane,200,200);
        //Stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("MyJavaFX");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
