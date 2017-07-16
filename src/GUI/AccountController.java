package GUI;

import DrakeSS.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    public Text email;
    public Text level;
    public Hyperlink back;
    public Button manageSchedule, manageUsers, viewMeetings, manageEmployees;

    public void initialize(URL url, ResourceBundle rb) {

        if (Client.permissionLevel > 0)
        {
            manageEmployees.setVisible(true);
        }
        else
        {
            manageEmployees.setVisible(false);
        }

        if (Client.permissionLevel == 3)
        {
            manageUsers.setVisible(true);
        }
        else
        {
            manageUsers.setVisible(false);
        }

        level.setFill(Color.GREEN);
        email.setFill(Color.GREEN);



        email.setText(Client.userAccount.getEmail());
        switch (Client.permissionLevel)
        {
            case 0:
            {
                level.setText("Employee");
            }
            case 1:
            {
                level.setText("Manager");
            }
            case 2:
            {
                level.setText("Administrative Assistant");
            }

            case 3:
            {
                level.setText("Administrator");
            }
        }

    }

    public void openMeetings(ActionEvent event) throws IOException
    {
        Client.currentPersonal = Client.userAccount.getEmail();
        Stage current = (Stage) email.getScene().getWindow();
        current.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Meetings.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Personal Schedule - " + Client.currentPersonal);
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();

    }

    public void openInvites(ActionEvent event) throws IOException
    {
        Stage current = (Stage) email.getScene().getWindow();
        current.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Invitations.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("My Meeting Invitations");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();

    }

    public void openEmployees(ActionEvent event) throws IOException
    {
        Stage current = (Stage) email.getScene().getWindow();
        current.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Employees.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Employee Management");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();

    }

    public void openUsers(ActionEvent event) throws IOException
    {
        Stage current = (Stage) email.getScene().getWindow();
        current.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Users.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Admin User Management");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();

    }

    public void openMain(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Main Menu");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();

    }

    public void openSchedulePermissions(ActionEvent event) throws IOException
    {
        Stage current = (Stage) email.getScene().getWindow();
        current.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SchedulePermissions.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("My Schedule Permissions");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();

    }

}
