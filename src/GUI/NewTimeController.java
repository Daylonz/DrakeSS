package GUI;

import DrakeSS.*;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class NewTimeController implements Initializable {

    public Button create;
    public VBox vb;
    public ComboBox<String> startcb, endcb;
    public ScrollPane scroll;
    public Label endLabel, time;
    int i = 0;
    public int starthourselected, startminselected, endhourselected, endminselected;
    boolean overlap = false;
    int room = Client.selectedmeeting.getRoom();

    public void initialize(URL url, ResourceBundle rb)
    {
        overlap = false;
        time.setText("Start: " + Client.selectedmeeting.getDisplayStartHour() + ":" +  Client.selectedmeeting.getStartMinutes() + "\n End: " + Client.selectedmeeting.getDisplayEndHour() + ":" + Client.selectedmeeting.getEndMinutes());
        Calendar startA = Calendar.getInstance();
        Calendar endA = Calendar.getInstance();
        Calendar startB = Calendar.getInstance();
        Calendar endB = Calendar.getInstance();


        try {

            vb.getChildren().clear();
            Calendar check = Calendar.getInstance();

            scroll.setVisible(true);
            for (int i = 0; i < DatabaseHandler.mainschedule.meetings.size(); i++) {
                check.set(DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.YEAR), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.MONTH), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.DAY_OF_MONTH));

                if (check.get(Calendar.YEAR) == Client.selectedmeeting.getStart().get(Calendar.YEAR) && check.get(Calendar.MONTH) == (Client.selectedmeeting.getStart().get(Calendar.MONTH)) && check.get(Calendar.DAY_OF_MONTH) == Client.selectedmeeting.getStart().get(Calendar.DAY_OF_MONTH) && Client.selectedmeeting.getRoom() == DatabaseHandler.mainschedule.meetings.get(i).getRoom() && Client.selectedmeeting != DatabaseHandler.mainschedule.meetings.get(i)) {
                    Meeting current = DatabaseHandler.mainschedule.meetings.get(i);
                    Hyperlink h = new Hyperlink(DatabaseHandler.mainschedule.meetings.get(i).getDisplayStartHour() + ":" + DatabaseHandler.mainschedule.meetings.get(i).getStartMinutes() + " - " + DatabaseHandler.mainschedule.meetings.get(i).getDisplayEndHour() + ":" + DatabaseHandler.mainschedule.meetings.get(i).getEndMinutes() + " - Room C" + DatabaseHandler.mainschedule.meetings.get(i).getRoom());
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            Client.selectedmeeting = current;
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DetailMeeting.fxml"));
                                Parent root1 = (Parent) fxmlLoader.load();
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.initStyle(StageStyle.DECORATED);
                                stage.setTitle("Meeting Details");
                                stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
                                Scene scene = new Scene(root1);
                                stage.setScene(scene);
                                scene.getStylesheets().add("css/Style.css");
                                stage.show();
                            } catch (Exception o)
                            {
                                o.printStackTrace();
                            }
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

    }

    public void scheduleProposal(ActionEvent event) throws IOException, MessagingException
    {
        setEndTime();
        if (startcb.getValue() == null || endcb.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Proposal requirements not met");
            alert.setContentText("You must select both a start and end time.");
            alert.showAndWait();
        }
        else {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();

            start.set(Client.selectedmeeting.getStart().get(Calendar.YEAR), Client.selectedmeeting.getStart().get(Calendar.MONTH), Client.selectedmeeting.getStart().get(Calendar.DAY_OF_MONTH), starthourselected, startminselected);
            start.set(Calendar.HOUR_OF_DAY, starthourselected);
            end.set(Client.selectedmeeting.getStart().get(Calendar.YEAR), Client.selectedmeeting.getStart().get(Calendar.MONTH), Client.selectedmeeting.getStart().get(Calendar.DAY_OF_MONTH), endhourselected, endminselected);
            end.set(Calendar.HOUR_OF_DAY, endhourselected);

            Calendar checkStart = Calendar.getInstance();
            Calendar checkEnd = Calendar.getInstance();
            for (int i = 0; i < DatabaseHandler.mainschedule.meetings.size(); i++)
            {
                checkStart.set(DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.YEAR), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.MONTH), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.DAY_OF_MONTH), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.HOUR_OF_DAY), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.MINUTE));
                checkEnd.set(DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.YEAR), DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.MONTH), DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.DAY_OF_MONTH), DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.HOUR_OF_DAY), DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.MINUTE));
                if (checkStart.get(Calendar.YEAR) == Client.selectedmeeting.getStart().get(Calendar.YEAR) && checkStart.get(Calendar.MONTH) == (Client.selectedmeeting.getStart().get(Calendar.MONTH)) && checkStart.get(Calendar.DAY_OF_MONTH) == Client.selectedmeeting.getStart().get(Calendar.DAY_OF_MONTH))
                {
                    if (meetingOverlap(checkStart, checkEnd, start, end, DatabaseHandler.mainschedule.meetings.get(i).getRoom(), room) && Client.selectedmeeting != DatabaseHandler.mainschedule.meetings.get(i))
                    {
                        overlap = true;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Meeting Overlap Detected");
                        alert.setContentText("Please choose a date and room that does not overlap with the current schedule!");
                        alert.showAndWait();

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
                        break;
                    }
                }
            }

            if (!overlap)
            {
                System.out.println("Start: " + starthourselected + ":" + startminselected + " - End: " + endhourselected + ":" + endminselected);
                Proposal p = new Proposal(start, end, Client.userAccount);
                Client.selectedmeeting.getProposals().put(Client.selectedmeeting.getProposals().size(), p);
                p.sendProposalNotification();
                DatabaseHandler.saveData();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Success!");
                alert.setContentText("Your time proposal has successfully been sent to the creator. Thank you.");
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
                endhourselected = 13;
                endminselected = 0;
                break;
            case "1:30 PM":
                endhourselected = 13;
                endminselected = 30;
                break;
            case "2:00 PM":
                endhourselected = 14;
                endminselected = 0;
                break;
            case "2:30 PM":
                endhourselected = 14;
                endminselected = 30;
                break;
            case "3:00 PM":
                endhourselected = 15;
                endminselected = 0;
                break;
            case "3:30 PM":
                endhourselected = 15;
                endminselected = 30;
                break;
            case "4:00 PM":
                endhourselected = 16;
                endminselected = 0;
                break;
            case "4:30 PM":
                endhourselected = 16;
                endminselected = 30;
                break;
            case "5:00 PM":
                endhourselected = 17;
                endminselected = 0;
                break;
        }
    }

    public boolean meetingOverlap(Calendar startA, Calendar endA, Calendar startB, Calendar endB, int roomA, int roomB)
    {
        if (startA.get(Calendar.HOUR_OF_DAY) == startB.get(Calendar.HOUR_OF_DAY) && roomA == roomB)
        {
            return true;
        }
        return (startA.before(endB) && startB.before(endA) && roomA == roomB);
    }





}

