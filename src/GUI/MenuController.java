package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.Meeting;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    public Hyperlink email;
    public DatePicker dp;
    public Button create;
    public VBox vb;
    public ScrollPane scroll;
    int i = 0;
    public Map<Integer, Meeting> listed = new HashMap<Integer, Meeting>();

    public void initialize(URL url, ResourceBundle rb)
    {

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
        email.setText(Client.userAccount.getEmail());
    }

    public void createMeeting(ActionEvent event) throws IOException
    {
        Client.selected = Calendar.getInstance();
        Client.selected.set(dp.getValue().getYear(), dp.getValue().getMonthValue(), dp.getValue().getDayOfMonth());
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

    public void updateMeetings(ActionEvent event) throws IOException {
        scroll.setVisible(true);
        java.time.LocalDate targetLocalDate = LocalDate.now();
        LocalDate localDatePicked = dp.getValue();
        if (localDatePicked.equals(targetLocalDate))
        {
            create.setVisible(false);
        }
        else
        {
            create.setVisible(true);
        }
        vb.getChildren().clear();
        Calendar selection = Calendar.getInstance();
        Calendar check = Calendar.getInstance();
        selection.set(dp.getValue().getYear(), dp.getValue().getMonthValue(), dp.getValue().getDayOfMonth());

            scroll.setVisible(true);
            for (i = 0; i < DatabaseHandler.mainschedule.meetings.size(); i++) {
                check.set(DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.YEAR), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.MONTH), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.DAY_OF_MONTH));
                if (check.get(Calendar.YEAR) == selection.get(Calendar.YEAR) && check.get(Calendar.MONTH) == (selection.get(Calendar.MONTH)) && check.get(Calendar.DAY_OF_MONTH) == selection.get(Calendar.DAY_OF_MONTH))
                {
                    Meeting current = DatabaseHandler.mainschedule.meetings.get(i);
                    Hyperlink h = new Hyperlink(DatabaseHandler.mainschedule.meetings.get(i).getDisplayStartHour() + ":" + DatabaseHandler.mainschedule.meetings.get(i).getStartMinutes() + " - " + DatabaseHandler.mainschedule.meetings.get(i).getDisplayEndHour() + ":" + DatabaseHandler.mainschedule.meetings.get(i).getEndMinutes() + " - Room C" + DatabaseHandler.mainschedule.meetings.get(i).getRoom());
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e){
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
    }




}

