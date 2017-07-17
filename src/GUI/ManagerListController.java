package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerListController implements Initializable {

    public static Button add;
    public VBox vb;

    public void initialize(URL url, ResourceBundle rb)
    {
        try {
            vb.getChildren().clear();
                for (int i = 0; i < DatabaseHandler.users.size(); i++)
                {
                    if (DatabaseHandler.users.get(i).getPermissionLevel() == 1)
                    {
                        User current = DatabaseHandler.users.get(i);
                        Hyperlink h = new Hyperlink(DatabaseHandler.users.get(i).getEmail() + " - ID: " + DatabaseHandler.users.get(i).getId());
                        h.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                Client.selecteduser = current;
                                try {
                                    Stage stage = (Stage) vb.getScene().getWindow();
                                    stage.hide();
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManageEmployee.fxml"));
                                    Parent root1 = (Parent) fxmlLoader.load();
                                    stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.initStyle(StageStyle.DECORATED);
                                    stage.setTitle("Manage Employee");
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
                vb.getChildren().add(new Label("There are currently no managers in the system."));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBack(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) vb.getScene().getWindow();
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




}

