package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Landing extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Landing.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/Style.css");
        primaryStage.setTitle("DrakeSS - Login");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
