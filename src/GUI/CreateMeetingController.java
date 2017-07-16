package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.Meeting;
import DrakeSS.User;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CreateMeetingController implements Initializable {

    public DatePicker dp;
    public Button create;
    public VBox vb, all, invite;
    public ComboBox<String> startcb, endcb, roomcb;
    public ScrollPane scroll, allscroll, invitescroll;
    public Label allLabel, inviteLabel, endLabel, arrows, roomLabel;
    int i = 0;
    public int starthourselected, startminselected, endhourselected, endminselected;

    public void initialize(URL url, ResourceBundle rb)
    {
        try {
            if (Client.userRefresh)
            {
                startcb.setValue(Client.selectedStart);
                endcb.setValue(Client.selectedEnd);
                roomcb.setValue(Client.selectedRoom);
                startcb.setDisable(true);
                endcb.setDisable(true);
                endcb.setVisible(true);
                endLabel.setVisible(true);
                allLabel.setVisible(true);
                allscroll.setVisible(true);
                arrows.setVisible(true);
                invitescroll.setVisible(true);
                inviteLabel.setVisible(true);
                create.setVisible(true);
                roomcb.setVisible(true);
                roomLabel.setVisible(true);
                setStartTime();
                setEndTime();
            }

            LocalDate date = Client.selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dp.setValue(date);

            vb.getChildren().clear();
            Calendar check = Calendar.getInstance();

            scroll.setVisible(true);
            for (int i = 0; i < DatabaseHandler.mainschedule.meetings.size(); i++) {
                check.set(DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.YEAR), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.MONTH), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.DAY_OF_MONTH));

                if (check.get(Calendar.YEAR) == Client.selected.get(Calendar.YEAR) && check.get(Calendar.MONTH) == (Client.selected.get(Calendar.MONTH)) && check.get(Calendar.DAY_OF_MONTH) == Client.selected.get(Calendar.DAY_OF_MONTH)) {
                    Hyperlink h = new Hyperlink(DatabaseHandler.mainschedule.meetings.get(i).getStartHour() + ":" + DatabaseHandler.mainschedule.meetings.get(i).getStartMinutes() + " - " + DatabaseHandler.mainschedule.meetings.get(i).getEndHour() + ":" + DatabaseHandler.mainschedule.meetings.get(i).getEndMinutes() + " - Room " + DatabaseHandler.mainschedule.meetings.get(i).getRoom());
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            System.out.println("Hyperlink clicked.");
                        }
                    });
                    vb.getChildren().add(h);
                }
            }
            if (vb.getChildren().isEmpty())
            {
                vb.getChildren().add(new Label("No meetings scheduled for this date."));

            }
            startcb.getItems().clear();
            startcb.getItems().addAll(
                    "8:00 AM",
                    "8:30 AM",
                    "9:00 AM",
                    "9:30 AM",
                    "10:00 AM",
                    "10:30 AM",
                    "11:00 AM",
                    "11:30 AM",
                    "12:00 PM",
                    "12:30 PM",
                    "1:00 PM",
                    "1:30 PM",
                    "2:00 PM",
                    "2:30 PM",
                    "3:00 PM",
                    "3:30 PM",
                    "4:00 PM",
                    "4:30 PM"
            );

            if (roomcb.getItems().isEmpty())
            {
                roomcb.getItems().addAll(
                        "C1",
                        "C2",
                        "C3"
                );
            }

            all.getChildren().clear();
            invite.getChildren().clear();

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
                                    Client.selectedStart = startcb.getValue();
                                    Client.selectedEnd = endcb.getValue();
                                    Client.selectedRoom = roomcb.getValue();
                                    Client.userRefresh = true;
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
                                Client.selectedStart = startcb.getValue();
                                Client.selectedEnd = endcb.getValue();
                                Client.userRefresh = true;
                                Client.selectedRoom = roomcb.getValue();
                                Client.usersToInvite.remove(s);
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

                if (item.isBefore(LocalDate.now()) || item.isAfter(LocalDate.now().plusMonths(1)) || item.getDayOfWeek() == DayOfWeek.SATURDAY || item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setStyle("-fx-background-color: #4D0000;");
                    setDisable(true);
                }
            }
        };

        dp.setDayCellFactory(dayCellFactory);
    }

    public void scheduleMeeting(ActionEvent event) throws IOException, MessagingException
    {
        if (Client.usersToInvite.size() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Meeting Requires Users");
            alert.setContentText("Please invite at least one user.");
            alert.showAndWait();
        }
        else {
            Client.userRefresh = false;
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            int room = getRoom();
            start.set(Client.selected.get(Calendar.YEAR), Client.selected.get(Calendar.MONTH), Client.selected.get(Calendar.DAY_OF_MONTH), starthourselected, startminselected);
            end.set(Client.selected.get(Calendar.YEAR), Client.selected.get(Calendar.MONTH), Client.selected.get(Calendar.DAY_OF_MONTH), endhourselected, endminselected);

            DatabaseHandler.mainschedule.addMeeting(new Meeting(DatabaseHandler.mainschedule.meetings.size(), start, end, room, Client.userAccount, getPending()));
            DatabaseHandler.saveData();

            Client.usersToInvite.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Success!");
            alert.setContentText("Your meeting has been successfully scheduled. Returning to main schedule.");
            alert.showAndWait();

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

    public void goBack(ActionEvent event) throws IOException
    {
        Client.userRefresh = false;
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

    public Map<Integer, User> getPending()
    {
        Map<Integer, User> result = new HashMap<Integer, User>();
        for (i = 0; i < invite.getChildren().size(); i++)
        {
            result.put(result.size(), Client.userFromEmail(Client.usersToInvite.get(i)));
        }
        return result;
    }

    public void setRoom(ActionEvent event)
    {
        setEndTime();
        roomcb.setVisible(true);
        roomLabel.setVisible(true);
        endcb.setDisable(true);
    }

    public void setUsers(ActionEvent event)
    {
        allscroll.setVisible(true);
        invitescroll.setVisible(true);
        arrows.setVisible(true);
        allLabel.setVisible(true);
        inviteLabel.setVisible(true);
        create.setVisible(true);
    }

    public void configureEnd(ActionEvent event)
    {
        setStartTime();
        switch (startcb.getValue())
        {
            case "8:00 AM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "8:30 AM",
                        "9:00 AM",
                        "9:30 AM",
                        "10:00 AM",
                        "10:30 AM",
                        "11:00 AM",
                        "11:30 AM",
                        "12:00 PM",
                        "12:30 PM",
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "8:30 AM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "9:00 AM",
                        "9:30 AM",
                        "10:00 AM",
                        "10:30 AM",
                        "11:00 AM",
                        "11:30 AM",
                        "12:00 PM",
                        "12:30 PM",
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "9:00 AM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "9:30 AM",
                        "10:00 AM",
                        "10:30 AM",
                        "11:00 AM",
                        "11:30 AM",
                        "12:00 PM",
                        "12:30 PM",
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "9:30 AM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "10:00 AM",
                        "10:30 AM",
                        "11:00 AM",
                        "11:30 AM",
                        "12:00 PM",
                        "12:30 PM",
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "10:00 AM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "10:30 AM",
                        "11:00 AM",
                        "11:30 AM",
                        "12:00 PM",
                        "12:30 PM",
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "10:30 AM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "11:00 AM",
                        "11:30 AM",
                        "12:00 PM",
                        "12:30 PM",
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "11:00 AM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "11:30 AM",
                        "12:00 PM",
                        "12:30 PM",
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "11:30 AM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "12:00 PM",
                        "12:30 PM",
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "12:00 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
            startcb.setDisable(true);
            endcb.getItems().clear();
            endcb.getItems().addAll(
                    "12:30 PM",
                    "1:00 PM",
                    "1:30 PM",
                    "2:00 PM",
                    "2:30 PM",
                    "3:00 PM",
                    "3:30 PM",
                    "4:00 PM",
                    "4:30 PM",
                    "5:00 PM"
            );
            break;
            case "12:30 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "1:00 PM",
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "1:00 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "1:30 PM",
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "1:30 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "2:00 PM",
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "2:00 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "2:30 PM",
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "2:30 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "3:00 PM",
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "3:00 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "3:30 PM",
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "3:30 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "4:00 PM",
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "4:00 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "4:30 PM",
                        "5:00 PM"
                );
                break;
            case "4:30 PM":
                endcb.setVisible(true);
                endLabel.setVisible(true);
                startcb.setDisable(true);
                endcb.getItems().clear();
                endcb.getItems().addAll(
                        "5:00 PM"
                );
                break;
        }
    }

    public int getRoom()
    {
        switch (roomcb.getValue())
        {
            case "C1":
                return 1;
            case "C2":
                return 2;
            case "C3":
                return 3;
        }
        return -1;
    }

    public void setStartTime()
    {
        switch (startcb.getValue())
        {
            case "8:00 AM":
                starthourselected = 8;
                startminselected = 0;
                break;
            case "8:30 AM":
                starthourselected = 8;
                startminselected = 30;
                break;
            case "9:00 AM":
                starthourselected = 9;
                startminselected = 0;
                break;
            case "9:30 AM":
                starthourselected = 9;
                startminselected = 30;
                break;
            case "10:00 AM":
                starthourselected = 10;
                startminselected = 0;
                break;
            case "10:30 AM":
                starthourselected = 10;
                startminselected = 30;
                break;
            case "11:00 AM":
                starthourselected = 11;
                startminselected = 0;
                break;
            case "11:30 AM":
                starthourselected = 11;
                startminselected = 30;
                break;
            case "12:00 PM":
                starthourselected = 12;
                startminselected = 0;
                break;
            case "12:30 PM":
                starthourselected = 12;
                startminselected = 30;
                break;
            case "1:00 PM":
                starthourselected = 1;
                startminselected = 0;
                break;
            case "1:30 PM":
                starthourselected = 1;
                startminselected = 30;
                break;
            case "2:00 PM":
                starthourselected = 2;
                startminselected = 0;
                break;
            case "2:30 PM":
                starthourselected = 2;
                startminselected = 30;
                break;
            case "3:00 PM":
                starthourselected = 3;
                startminselected = 0;
                break;
            case "3:30 PM":
                starthourselected = 3;
                startminselected = 30;
                break;
            case "4:00 PM":
                starthourselected = 4;
                startminselected = 0;
                break;
            case "4:30 PM":
                starthourselected = 4;
                startminselected = 30;
                break;
            case "5:00 PM":
                starthourselected = 5;
                startminselected = 0;
                break;
        }
    }

    public void setEndTime()
    {
        switch (endcb.getValue())
        {
            case "8:00 AM":
                endhourselected = 8;
                endminselected = 0;
                break;
            case "8:30 AM":
                endhourselected = 8;
                endminselected = 30;
                break;
            case "9:00 AM":
                endhourselected = 9;
                endminselected = 0;
                break;
            case "9:30 AM":
                endhourselected = 9;
                endminselected = 30;
                break;
            case "10:00 AM":
                endhourselected = 10;
                endminselected = 0;
                break;
            case "10:30 AM":
                endhourselected = 10;
                endminselected = 30;
                break;
            case "11:00 AM":
                endhourselected = 11;
                endminselected = 0;
                break;
            case "11:30 AM":
                endhourselected = 11;
                endminselected = 30;
                break;
            case "12:00 PM":
                endhourselected = 12;
                endminselected = 0;
                break;
            case "12:30 PM":
                endhourselected = 12;
                endminselected = 30;
                break;
            case "1:00 PM":
                endhourselected = 1;
                endminselected = 0;
                break;
            case "1:30 PM":
                endhourselected = 1;
                endminselected = 30;
                break;
            case "2:00 PM":
                endhourselected = 2;
                endminselected = 0;
                break;
            case "2:30 PM":
                endhourselected = 2;
                endminselected = 30;
                break;
            case "3:00 PM":
                endhourselected = 3;
                endminselected = 0;
                break;
            case "3:30 PM":
                endhourselected = 3;
                endminselected = 30;
                break;
            case "4:00 PM":
                endhourselected = 4;
                endminselected = 0;
                break;
            case "4:30 PM":
                endhourselected = 4;
                endminselected = 30;
                break;
            case "5:00 PM":
                endhourselected = 5;
                endminselected = 0;
                break;
        }
    }





}

