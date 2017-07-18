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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    public static Button add;
    public VBox vb;

    public void initialize(URL url, ResourceBundle rb)
    {
        try {
            vb.getChildren().clear();
            if (DatabaseHandler.users == null)
            {
                vb.getChildren().add(new Label("Something went wrong. No users found."));
            }
            else
            {
                for (int i = 0; i < DatabaseHandler.users.size(); i++)
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
                                              FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminUserDetail.fxml"));
                                              Parent root1 = (Parent) fxmlLoader.load();
                                              stage = new Stage();
                                              stage.initModality(Modality.APPLICATION_MODAL);
                                              stage.initStyle(StageStyle.DECORATED);
                                              stage.setTitle("User Details - Admin View");
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

    public void configureManagers(ActionEvent event) throws IOException
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

    public void addUser(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) vb.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddUser.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Create User");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }




}

