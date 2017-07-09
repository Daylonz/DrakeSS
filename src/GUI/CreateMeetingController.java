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

public class CreateMeetingController implements Initializable {

    public DatePicker dp;
    public Button create;
    public VBox vb, all, invite;
    int i = 0;

    public void initialize(URL url, ResourceBundle rb)
    {

        try {
            vb.getChildren().clear();
            all.getChildren().clear();
            invite.getChildren().clear();
            if (DatabaseHandler.mainschedule.meetings == null)
            {
                vb.getChildren().add(new Label("There are currently no meetings scheduled for this date."));
            }
            else
            {
                for (i = 0; i < DatabaseHandler.mainschedule.meetings.size(); i++)
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

            if (DatabaseHandler.users == null)
            {
                all.getChildren().add(new Label("Something went wrong. No users found."));
            }
            else
            {
                for (i = 0; i < DatabaseHandler.users.size(); i++) {
                    String s = DatabaseHandler.users.get(i).getEmail();
                    if (!Client.usersToInvite.contains(DatabaseHandler.users.get(i).getEmail()) && !Client.userAccount.getEmail().equalsIgnoreCase(s))
                    {
                        Hyperlink h = new Hyperlink(DatabaseHandler.users.get(i).getEmail());
                        h.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                try {
                                    Client.usersToInvite.add(s);
                                    Stage stage = (Stage) vb.getScene().getWindow();
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
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        all.getChildren().add(h);
                    }
                }
            }

            if (Client.usersToInvite.size() == 0)
            {
                invite.getChildren().add(new Label("Click user(s) to invite."));
            }
            else
            {
                for (i = 0; i < Client.usersToInvite.size(); i++)
                {
                    Hyperlink h = new Hyperlink(Client.usersToInvite.get(i));
                    String s = Client.usersToInvite.get(i);
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            try {
                                System.out.println("Hyperlink clicked.");
                                Client.usersToInvite.remove(s);
                                Stage stage = (Stage) vb.getScene().getWindow();
                                stage.hide();
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateMeeting.fxml"));
                                Parent root1 = (Parent) fxmlLoader.load();
                                stage = new Stage();
                                System.out.println("Stage Hidden.");
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.initStyle(StageStyle.DECORATED);
                                stage.setTitle("Schedule Meeting");
                                stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
                                Scene scene = new Scene(root1);
                                scene.getStylesheets().add("css/Style.css");
                                stage.setScene(scene);
                                stage.show();
                            }
                            catch (Exception exception)
                            {
                                exception.printStackTrace();
                            }
                        }
                    });
                    invite.getChildren().add(h);
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
    }

    public void scheduleMeeting(ActionEvent event) throws IOException
    {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        DatabaseHandler.mainschedule.addMeeting(new Meeting(DatabaseHandler.mainschedule.meetings.size(), start, end, 1));
        DatabaseHandler.saveData();



        Client.usersToInvite.clear();

        Stage stage = (Stage) vb.getScene().getWindow();
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




}

