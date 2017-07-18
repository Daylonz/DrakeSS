package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
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
import java.util.ResourceBundle;

public class AdminUserDetailController implements Initializable {

    public Label mstart, mend, mcreator, mroom;
    public Button delete;
    int hosted = 0;
    int attended = 0;

    public void initialize(URL url, ResourceBundle rb)
    {
        if (Client.selecteduser.getEmail().equalsIgnoreCase("drakess@mail.com") || Client.selecteduser == Client.userAccount)
        {
           delete.setVisible(false);
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

    public void deleteUser(ActionEvent event) throws IOException, MessagingException
    {
        DatabaseHandler.deleteUser();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("User Successfully deleted");
        alert.setContentText("The user has been deleted successfully. Returning to user management page.");
        alert.showAndWait();
        Stage stage = (Stage) mstart.getScene().getWindow();
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

    public void goBack(ActionEvent event) throws IOException
    {
        Stage current = (Stage) mstart.getScene().getWindow();
        current.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Users.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Admin User Management");
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
