package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.EmailSender;
import DrakeSS.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class UserDetailController implements Initializable {

    public Label mstart, mend, mcreator, mroom;
    public Button option;
    int hosted = 0;
    int attended = 0;

    public void initialize(URL url, ResourceBundle rb)
    {

        if (Client.userAccount.getAllowedUsers().containsValue(Client.selecteduser))
        {
            option.setText("Revoke");
        }
        else
        {
            option.setText("Allow");
        }
        for (int i = 0; i < DatabaseHandler.mainschedule.meetings.size(); i++)
        {
            if (Client.selecteduser.getEmail().equalsIgnoreCase(DatabaseHandler.mainschedule.meetings.get(i).getCreatorEmail()))
            {
                hosted++;
                attended++;
            }
            if (DatabaseHandler.mainschedule.meetings.get(i).getAttendingUsers().containsValue(Client.selecteduser))
            {
                attended++;
            }
        }


        mstart.setText("User Email: " + Client.selecteduser.getEmail());
        mend.setText("Account Type: " + getAccountType(Client.selecteduser));
        mcreator.setText("Meetings Hosted: " + hosted);
        mroom.setText("Meetings Attended: " + attended);
    }

    public void allowDeny(ActionEvent event) throws IOException, MessagingException
    {
        if (Client.userAccount.getAllowedUsers().containsValue(Client.selecteduser))
        {
            for (int i = 0; i < Client.userAccount.getAllowedUsers().size(); i++)
            {
                if (Client.userAccount.getAllowedUsers().get(i) == Client.selecteduser)
                {
                    Client.userAccount.getAllowedUsers().remove(i, Client.selecteduser);
                }
            }
        }
        else
        {
            Client.userAccount.getAllowedUsers().put(Client.userAccount.getAllowedUsers().size(), Client.selecteduser);
        }
        DatabaseHandler.saveData();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("User Modification Successful");
        alert.setContentText("You will now be taken back to your permissions management page.");
        alert.showAndWait();
        Stage current = (Stage) mroom.getScene().getWindow();
        current.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Permissions.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("My Schedule Permissions");
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Permissions.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("My Schedule Permissions");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();
    }

    public String getAccountType(User user)
    {
        switch (user.getPermissionLevel())
        {
            case 0:
            {
                return "Employee";
            }
            case 1:
            {
                return "Manager";
            }
            case 2:
            {
                return "Administrative Assistant";
            }

            case 3:
            {
                return "Administrator";
            }
        }
        return "null";
    }
}
