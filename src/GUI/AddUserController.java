package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddUserController implements Initializable {

    public TextField email;
    public Button create;
    public ComboBox<String> cb;

    public static final Pattern VALID_EMAIL =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void initialize(URL url, ResourceBundle rb)
    {
        cb.getItems().clear();
        cb.getItems().addAll(
                "Employee",
                "Manager",
                "Administrative Assistant",
                "Administrator"
        );
    }

    public void createUser(ActionEvent event) throws NoSuchAlgorithmException, IOException
    {
        if (duplicateEmail(email.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Duplicate Email");
            alert.setContentText("This email address is already in the system. Let's try another, shall we?");
            alert.showAndWait();
        }
        else if (validEmail(email.getText())) {
            int r = 0;
            if (cb.getValue() != null) {
                switch (cb.getValue()) {
                    case "Manager": {
                        r = 1;
                        break;
                    }
                    case "Administrative Assistant": {
                        r = 2;
                        break;
                    }
                    case "Administrator": {
                        r = 3;
                        break;
                    }
                }
            }
            DatabaseHandler.users.put(DatabaseHandler.users.size(), new User(DatabaseHandler.users.size(), email.getText(), r));
            DatabaseHandler.saveData();
            Stage stage = (Stage) email.getScene().getWindow();
            stage.hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Users.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Admin User Management");
            stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
            Scene scene = new Scene(root1);
            stage.setScene(scene);
            scene.getStylesheets().add("css/Style.css");
            stage.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Email");
            alert.setContentText("Please enter a valid email address.");
            alert.showAndWait();
        }
    }

    public void goBack(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Users.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Admin User Management");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();
    }

    private static boolean validEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL .matcher(emailStr);
        return matcher.find();
    }

    private static boolean duplicateEmail(String email)
    {
        for (int i = 0; i < DatabaseHandler.users.size(); i++)
        {
            if (DatabaseHandler.users.get(i).getEmail().equalsIgnoreCase(email))
            {
                return true;
            }
        }
        return false;
    }

}
