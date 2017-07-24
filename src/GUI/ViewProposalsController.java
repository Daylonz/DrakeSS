package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.Meeting;
import DrakeSS.Proposal;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ViewProposalsController implements Initializable {

    public Button back;
    public VBox vb;
    public ScrollPane scroll;
    public Label title;
    int i = 0;

    public void initialize(URL url, ResourceBundle rb)
    {
        vb.getChildren().clear();
        for (i = 0; i < Client.selectedmeeting.getProposals().size(); i++) {
            if (Client.selectedmeeting.getProposals().get(i) != null)
            {
                Proposal current = Client.selectedmeeting.getProposals().get(i);
                Hyperlink h = new Hyperlink("Start: " + current.getDisplayStartHour() + ":" + current.getStartMinutes() + " End: " + current.getDisplayEndHour() + ":" + current.getEndMinutes() + " Proposed by: " + current.getUserEmail());
                h.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Client.selectedproposal = current;
                        try {
                            Stage stage = (Stage) back.getScene().getWindow();
                            stage.hide();
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProposalDetail.fxml"));
                            Parent root1 = (Parent) fxmlLoader.load();
                            stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.initStyle(StageStyle.DECORATED);
                            stage.setTitle("Proposal Details");
                            stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
                            Scene scene = new Scene(root1);
                            stage.setScene(scene);
                            scene.getStylesheets().add("css/Style.css");
                            stage.show();
                        } catch (Exception o) {
                            o.printStackTrace();
                        }
                    }
                });
                vb.getChildren().add(h);
            }
        }
        if (vb.getChildren().isEmpty())
        {
            vb.getChildren().add(new Label("You currently have no pending proposals."));

        }
    }

    public void goBack(ActionEvent event) throws IOException
    {
        Stage current = (Stage) vb.getScene().getWindow();
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

