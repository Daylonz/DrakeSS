package GUI;

import DrakeSS.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class LandingController {

    public TextField loginUser;
    public PasswordField loginPassword;
    public Button loginButton;
    public Label invalidCredentials;


    public void attemptLogin(ActionEvent event)
    {
        String loginResult;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(loginPassword.getText().getBytes(), 0, loginPassword.getText().length());
            String md5 = new BigInteger(1, md.digest()).toString(16);

            loginResult = Client.attemptLogin(loginUser.getText(), md5);

            if (loginResult.equals("codesuccess")) {
                try{
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.hide();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ResetPassword.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.DECORATED);
                    stage.setTitle("Password Reset");
                    stage.setScene(new Scene(root1));
                    stage.show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (loginResult.equals("success")) {
                try{
                    System.out.println(Client.userAccount.getEmail() + " successfully logged in with permission level of " + Client.permissionLevel);
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.hide();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SuperUser.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.DECORATED);
                    stage.setTitle("Referee Menu");
                    stage.setScene(new Scene(root1));
                    stage.show();
                } catch (Exception e) {}
            } else {
                invalidCredentials.setVisible(true);
                loginUser.setText("");
                loginPassword.setText("");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
