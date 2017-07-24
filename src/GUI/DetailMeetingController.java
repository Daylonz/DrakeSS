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

    public Label mstart, mend, mcreator, mroom;

    public void initialize(URL url, ResourceBundle rb)
    {
        mstart.setText("Meeting Start: " + Client.selectedmeeting.getDisplayStartHour() + ":" + Client.selectedmeeting.getStartMinutes());
        mend.setText("Meeting End: " + Client.selectedmeeting.getDisplayEndHour() + ":" + Client.selectedmeeting.getEndMinutes());
        mcreator.setText("Meeting Creator: " + Client.selectedmeeting.getCreatorEmail());
        mroom.setText("Meeting Location: Room C" + Client.selectedmeeting.getRoom());

    }
}
