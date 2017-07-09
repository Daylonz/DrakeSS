package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.Meeting;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    public Hyperlink email;
    public static Button add;
    public VBox vb;

    public void initialize(URL url, ResourceBundle rb)
    {
        try {
            vb.getChildren().clear();
            if (DatabaseHandler.users == null)
            {
                vb.getChildren().add(new Label("Something went wrong. No users found."));
            }
            else
            {
                for (int i = 0; i < DatabaseHandler.users.size(); i++)
                {
                    Hyperlink h = new Hyperlink(DatabaseHandler.users.get(i).getEmail() + " - ID: " + DatabaseHandler.users.get(i).getId());
                    h.setOnAction(new EventHandler<ActionEvent>() {
                                      @Override
                                      public void handle(ActionEvent e) {
                                          System.out.println("Hyperlink clicked.");
                                      }
                                      });
                    vb.getChildren().add(h);
                }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }


        email.setText(Client.userAccount.getEmail());
    }

    public void createMeeting(ActionEvent event) throws IOException
    {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        DatabaseHandler.mainschedule.addMeeting(new Meeting(DatabaseHandler.mainschedule.meetings.size(), start, end, 1));
        DatabaseHandler.saveData();
    }

    public void openAccount(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Account.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("User Account - " + Client.userAccount.getEmail());
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void addUser(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddUser.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Create User");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }




}

