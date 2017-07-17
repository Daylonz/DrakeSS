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

public class ManageEmployeeController implements Initializable {

    public static Button add;
    public VBox vb, vbmgr;
    int i;

    public void initialize(URL url, ResourceBundle rb)
    {
        try {
                for (i = 0; i < DatabaseHandler.users.size(); i++) {
                    String s = DatabaseHandler.users.get(i).getEmail();
                    if (!Client.selecteduser.getEmail().equalsIgnoreCase(s) && !Client.selecteduser.getEmployees().containsValue(Client.userFromEmail(s)) && Client.userFromEmail(s).getPermissionLevel() == 0)
                    {
                        Hyperlink h = new Hyperlink(DatabaseHandler.users.get(i).getEmail());
                        h.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                try {
                                    Client.selecteduser.getEmployees().put(Client.selecteduser.getEmployees().size(), Client.userFromEmail(s));
                                    DatabaseHandler.saveData();
                                    Stage stage = (Stage) vb.getScene().getWindow();
                                    stage.hide();
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManageEmployee.fxml"));
                                    Parent root1 = (Parent) fxmlLoader.load();
                                    stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.initStyle(StageStyle.DECORATED);
                                    stage.setTitle("Manager Configuration");
                                    stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
                                    Scene scene = new Scene(root1);
                                    stage.setScene(scene);
                                    scene.getStylesheets().add("css/Style.css");
                                    stage.show();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        vb.getChildren().add(h);
                    }
                }
            if (vb.getChildren().isEmpty())
            {
                vb.getChildren().add(new Label("There are currently no more Employees."));

            }



            for (i = 0; i < Client.selecteduser.getEmployees().size(); i++)
            {
                Hyperlink h = new Hyperlink(Client.selecteduser.getEmployees().get(i).getEmail());
                String s = Client.selecteduser.getEmployees().get(i).getEmail();
                h.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        try {

                            if (Client.selecteduser.getEmployees().containsValue(Client.userFromEmail(s)))
                            {
                                for (int i = 0; i < Client.selecteduser.getEmployees().size(); i++)
                                {
                                    if (Client.selecteduser.getEmployees().get(i) == Client.userFromEmail(s))
                                    {
                                        Client.selecteduser.getEmployees().remove(i, Client.userFromEmail(s));
                                    }
                                }
                            }

                            DatabaseHandler.saveData();
                            Stage stage = (Stage) vb.getScene().getWindow();
                            stage.hide();
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManageEmployee.fxml"));
                            Parent root1 = (Parent) fxmlLoader.load();
                            stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.initStyle(StageStyle.DECORATED);
                            stage.setTitle("Manager Configuration");
                            stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
                            Scene scene = new Scene(root1);
                            stage.setScene(scene);
                            scene.getStylesheets().add("css/Style.css");
                            stage.show();
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                });
               vbmgr.getChildren().add(h);
            }
            if (vbmgr.getChildren().isEmpty())
            {
                vbmgr.getChildren().add(new Label("Click an employee to add them to this list."));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBack(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManagerList.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Manager List");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }




}

