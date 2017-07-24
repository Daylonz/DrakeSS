package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyDetailMeetingController implements Initializable {

    public Label mstart, mend, mcreator, mroom;
    public Button rs, vtp;

    public void initialize(URL url, ResourceBundle rb)
    {
        mstart.setText("Meeting Start: " + Client.selectedmeeting.getDisplayStartHour() + ":" + Client.selectedmeeting.getStartMinutes());
        mend.setText("Meeting End: " + Client.selectedmeeting.getDisplayEndHour() + ":" + Client.selectedmeeting.getEndMinutes());
        mcreator.setText("Meeting Creator: " + Client.selectedmeeting.getCreatorEmail());
        mroom.setText("Meeting Location: Room C" + Client.selectedmeeting.getRoom());
    }

    public void reschedule(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) rs.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Reschedule.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Reschedule Meeting");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void viewProposals(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) rs.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewProposals.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Time Proposals");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void cancelMeeting(ActionEvent event) throws IOException
    {
        DatabaseHandler.deleteMeeting();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Meeting Successfully deleted");
        alert.setContentText("The meeting has been deleted successfully. Returning to meeting management page.");
        alert.showAndWait();
        Stage stage = (Stage) mstart.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InitiatedMeetings.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("My Meeting Manager");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void goBack(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) rs.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InitiatedMeetings.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("My Meeting Manager");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }

}
