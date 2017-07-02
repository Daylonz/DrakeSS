package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.Meeting;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.util.Callback;


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

    public void initialize(URL url, ResourceBundle rb)
    {

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

    public void createMeeting(ActionEvent event) throws IOException
    {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        DatabaseHandler.mainschedule.addMeeting(new Meeting(DatabaseHandler.mainschedule.meetings.size(), start, end, 1));
        DatabaseHandler.saveData();
    }




}

