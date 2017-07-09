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

public class MenuController implements Initializable {

    public Hyperlink email;
    public DatePicker dp;
    public Button create;
    public VBox vb;

    public void initialize(URL url, ResourceBundle rb)
    {

        try {
            vb.getChildren().clear();
            if (DatabaseHandler.mainschedule.meetings == null)
            {
                vb.getChildren().add(new Label("There are currently no meetings scheduled for this date."));
            }
            else
            {
                for (int i = 0; i < DatabaseHandler.mainschedule.meetings.size(); i++)
                {
                    Hyperlink h = new Hyperlink(DatabaseHandler.mainschedule.meetings.get(i).getStartHour() + ":" + DatabaseHandler.mainschedule.meetings.get(i).getStartMinutes() + " - " + DatabaseHandler.mainschedule.meetings.get(i).getEndHour() + ":" + DatabaseHandler.mainschedule.meetings.get(i).getEndMinutes());
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

        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isBefore(LocalDate.now()) || item.isAfter(LocalDate.now().plusMonths(10)) || item.getDayOfWeek() == DayOfWeek.SATURDAY || item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setStyle("-fx-background-color: #4D0000;");
                    setDisable(true);
                }
            }
        };

        dp.setDayCellFactory(dayCellFactory);
        email.setText(Client.userAccount.getEmail());
    }

    public JSONObject importMeetings() throws IOException {
        String message = DatabaseHandler.mainschedule.toJSON();
        System.out.println(message);
        if (message.equals("{}"))
        {
            return null;
        }
        return new JSONObject(message);
    }

    public void createMeeting(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateMeeting.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Schedule Meeting");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
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




}

