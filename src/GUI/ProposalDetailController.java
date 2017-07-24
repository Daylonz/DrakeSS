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

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ProposalDetailController implements Initializable {

    public Label mstart, mend, cstart, cend;
    public Button accept, decline;
    boolean overlap = false;

    public void initialize(URL url, ResourceBundle rb)
    {
        overlap = false;
        cstart.setText("Current Meeting Start: " + Client.selectedmeeting.getDisplayStartHour() + ":" + Client.selectedmeeting.getStartMinutes());
        cend.setText("Current Meeting End: " + Client.selectedmeeting.getDisplayEndHour() + ":" + Client.selectedmeeting.getEndMinutes());
        mstart.setText("New Meeting Start: " + Client.selectedproposal.getDisplayStartHour() + ":" + Client.selectedproposal.getStartMinutes());
        mend.setText("New Meeting End: " + Client.selectedproposal.getDisplayEndHour() + ":" + Client.selectedproposal.getEndMinutes());
    }

    public void doAccept(ActionEvent event) throws IOException, MessagingException
    {
        if (meetingOverlap())
        {
            for (int i = 0; i < Client.selectedmeeting.getProposals().size(); i++)
            {
                if (Client.selectedmeeting.getProposals().get(i) == Client.selectedproposal)
                {
                    Client.selectedmeeting.getProposals().remove(i);
                }
            }
            DatabaseHandler.saveData();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Meeting Overlap Detected");
            alert.setContentText("When the user created this proposal, there was no schedule conflict. However, someone has since scheduled a meeting during the proposed time so this proposal is no longer valid. Our apologies. Deleting proposal.");
            alert.showAndWait();
        }
        else {
            Client.selectedmeeting.setStart(Client.selectedproposal.getStart());
            Client.selectedmeeting.setEnd(Client.selectedproposal.getEnd());
            Client.selectedmeeting.sendReschedule();
            for (int i = 0; i < Client.selectedmeeting.getProposals().size(); i++)
            {
                if (Client.selectedmeeting.getProposals().get(i) == Client.selectedproposal)
                {
                    Client.selectedmeeting.getProposals().remove(i);
                }
            }
            DatabaseHandler.saveData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Proposal Successfully Accepted");
            alert.setContentText("Thank you for your response. Your meeting has been rescheduled successfully. You will now be taken back to your meeting manager.");
            alert.showAndWait();

            Stage current = (Stage) decline.getScene().getWindow();
            current.hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InitiatedMeetings.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("My Meeting Manager");
            stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
            Scene scene = new Scene(root1);
            stage.setScene(scene);
            scene.getStylesheets().add("css/Style.css");
            stage.show();
        }

        }

    public void doDecline(ActionEvent event) throws IOException
    {
        for (int i = 0; i < Client.selectedmeeting.getProposals().size(); i++)
        {
            System.out.println("Iteration "+i);
            if (Client.selectedmeeting.getProposals().get(i) == Client.selectedproposal)
            {
                Client.selectedmeeting.getProposals().remove(i);
            }
        }
        DatabaseHandler.saveData();

        Stage stage = (Stage) decline.getScene().getWindow();
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

    public void goBack(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) decline.getScene().getWindow();
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

    public boolean meetingOverlap()
    {
        Calendar checkStart = Calendar.getInstance();
        Calendar checkEnd = Calendar.getInstance();
        for (int i = 0; i < DatabaseHandler.mainschedule.meetings.size(); i++)
        {
            checkStart.set(DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.YEAR), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.MONTH), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.DAY_OF_MONTH), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.HOUR_OF_DAY), DatabaseHandler.mainschedule.meetings.get(i).getStart().get(Calendar.MINUTE));
            checkEnd.set(DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.YEAR), DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.MONTH), DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.DAY_OF_MONTH), DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.HOUR_OF_DAY), DatabaseHandler.mainschedule.meetings.get(i).getEnd().get(Calendar.MINUTE));
            if (checkStart.get(Calendar.YEAR) == Client.selectedproposal.getStart().get(Calendar.YEAR) && checkStart.get(Calendar.MONTH) == (Client.selectedproposal.getStart().get(Calendar.MONTH)) && checkStart.get(Calendar.DAY_OF_MONTH) == Client.selectedproposal.getStart().get(Calendar.DAY_OF_MONTH))
            {
                if (Client.selectedproposal.getStart().before(checkEnd) && checkStart.before(Client.selectedproposal.getEnd()) && Client.selectedmeeting.getRoom() == DatabaseHandler.mainschedule.meetings.get(i).getRoom())
                {
                    return true;
                }
            }
        }
                    return false;
    }

}
