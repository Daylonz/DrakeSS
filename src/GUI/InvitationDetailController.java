package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.EmailSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class InvitationDetailController implements Initializable {

    public Label mstart, mend, mcreator, mroom;

    public void initialize(URL url, ResourceBundle rb)
    {
        mstart.setText("Meeting Start: " + Client.selectedmeeting.getDisplayStartHour() + ":" + Client.selectedmeeting.getStartMinutes());
        mend.setText("Meeting End: " + Client.selectedmeeting.getDisplayEndHour() + ":" + Client.selectedmeeting.getEndMinutes());
        mcreator.setText("Meeting Creator: " + Client.selectedmeeting.getCreatorEmail());
        mroom.setText("Meeting Location: Room C" + Client.selectedmeeting.getRoom());
    }

    public void acceptInvite(ActionEvent event) throws IOException, MessagingException
    {
            for (int i = 0; i < Client.selectedmeeting.getAttendingUsers().size(); i++)
            {
                if (Client.selectedmeeting.getAttendingUsers().get(i) == Client.userAccount)
                {
                    Client.selectedmeeting.getAttendingUsers().remove(i);
                }
            }
        for (int i = 0; i < Client.selectedmeeting.getPendingUsers().size(); i++)
        {
            if (Client.selectedmeeting.getPendingUsers().get(i) == Client.userAccount)
            {
                Client.selectedmeeting.getPendingUsers().remove(i, Client.userAccount);
            }
        }
        Client.selectedmeeting.getAttendingUsers().put(Client.selectedmeeting.getAttendingUsers().size(), Client.userAccount);
        DatabaseHandler.saveData();
        EmailSender.sendEmail(Client.selectedmeeting.getCreatorEmail(), "DrakeSS Meeting Notification", Client.userAccount.getEmail() + " has accepted an invitation to one of your scheduled meetings!\n\n"
                + "\n\nMeeting Details:"
                + "\nDate: " + Client.selectedmeeting.getStart().get(Calendar.DAY_OF_MONTH) + "-" + Client.selectedmeeting.getStart().get(Calendar.MONTH) + "-" + Client.selectedmeeting.getStart().get(Calendar.YEAR)
                + "\nStart Time: " + Client.selectedmeeting.getStartHour() + ":" + Client.selectedmeeting.getStartMinutes()
                + "\nEnd Time: " + Client.selectedmeeting.getEndHour() + ":" + Client.selectedmeeting.getEndMinutes()
                + "\nLocation:  Room C" + Client.selectedmeeting.getRoom());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Meeting Invitation Successfully Accepted");
        alert.setContentText("Thank you for your response. You will now be taken back to your invitation listing.");
        alert.showAndWait();
        Stage current = (Stage) mroom.getScene().getWindow();
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

    public void declineInvite(ActionEvent event) throws IOException, MessagingException
    {
        for (int i = 0; i < Client.selectedmeeting.getAttendingUsers().size(); i++)
        {
            if (Client.selectedmeeting.getAttendingUsers().get(i) == Client.userAccount)
            {
                Client.selectedmeeting.getAttendingUsers().remove(i);
            }
        }
        for (int i = 0; i < Client.selectedmeeting.getPendingUsers().size(); i++)
        {
            if (Client.selectedmeeting.getPendingUsers().get(i) == Client.userAccount)
            {
                Client.selectedmeeting.getPendingUsers().remove(i, Client.userAccount);
            }
        }
        DatabaseHandler.saveData();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Meeting Invitation Successfully Declined");
        alert.setContentText("Thank you for your response. You will now be taken back to your invitation listing.");
        alert.showAndWait();
        Stage current = (Stage) mroom.getScene().getWindow();
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

    public void goBack(ActionEvent event) throws IOException
    {
        Stage current = (Stage) mroom.getScene().getWindow();
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

    public void proposeNewTime(ActionEvent event) throws IOException
    {
        Client.selected = Client.selectedmeeting.getStart();
        Stage current = (Stage) mroom.getScene().getWindow();
        current.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newTime.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Meeting Time Proposal");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();
    }
}
