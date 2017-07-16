package GUI;

import DrakeSS.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class DetailMeetingController implements Initializable {

    public Label mstart, mend, mcreator, mattending, mpending, mroom;

    public void initialize(URL url, ResourceBundle rb)
    {
        mstart.setText("Meeting Start: " + Client.selectedmeeting.getStartHour() + ":" + Client.selectedmeeting.getStartMinutes());
        mend.setText("Meeting End: " + Client.selectedmeeting.getEndHour() + ":" + Client.selectedmeeting.getEndMinutes());
        mcreator.setText("Meeting Creator: " + Client.selectedmeeting.getCreatorEmail());
        mroom.setText("Meeting Location: Room C" + Client.selectedmeeting.getRoom());

        String s = "Attending Users: ";
        if (Client.selectedmeeting.getAttendingUsers().size() > 0)
        {
            s += Client.selectedmeeting.getAttendingUsers().get(0).getEmail();
        }
        if (Client.selectedmeeting.getAttendingUsers().size() > 1)
        {
            for (int i = 1; i < Client.selectedmeeting.getAttendingUsers().size(); i++) {
                s+= ", ";
                s += Client.selectedmeeting.getAttendingUsers().get(i).getEmail();
            }
        }
        mattending.setText(s);

        s = "Pending Users: ";
        if (Client.selectedmeeting.getPendingUsers().size() > 0)
        {
            s += Client.selectedmeeting.getPendingUsers().get(0).getEmail();
        }
        if (Client.selectedmeeting.getPendingUsers().size() > 1)
        {
            for (int i = 1; i < Client.selectedmeeting.getPendingUsers().size(); i++) {
                s+= ", ";
                s += Client.selectedmeeting.getPendingUsers().get(i).getEmail();
            }
        }
        mpending.setText(s);
    }

}
