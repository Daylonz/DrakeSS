package GUI;

import DrakeSS.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

public class LandingController implements Initializable {

    public TextField loginUser;
    public TextField resetEmail;
    public PasswordField loginPassword;
    public Button loginButton;
    public Label invalidCredentials;

    public void initialize(URL url, ResourceBundle rb) {

        if (loginUser != null) {
            loginUser.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                    if (loginUser.getText().length() > 30) {
                        String s = loginUser.getText().substring(0, 30);
                        loginUser.setText(s);
                    }
                }
            });
        }

        if (loginPassword != null) {
            loginPassword.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                    if (loginPassword.getText().length() > 20) {
                        String s = loginPassword.getText().substring(0, 20);
                        loginPassword.setText(s);
                    }
                }
            });
        }

    }

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
                    stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
                    Scene scene = new Scene(root1);
                    scene.getStylesheets().add("css/Style.css");
                    stage.setScene(scene);
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
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.DECORATED);
                    stage.setTitle("Main Menu");
                    stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
                    Scene scene = new Scene(root1);
                    scene.getStylesheets().add("css/Style.css");
                    stage.setScene(scene);
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

    public void forgotPass(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Forgot.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Password Recovery");
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        stage.setScene(scene);
        scene.getStylesheets().add("css/Style.css");
        stage.show();

    }

    public void attemptSend(ActionEvent event) throws IOException
    {
        if (resetEmail.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email Required");
            alert.setContentText("Please enter an Email.");
            alert.showAndWait();
        }
        else if (Client.attemptReset(resetEmail.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Password Reset");
            alert.setContentText("Email successfully sent. Please check your email for instructions on how to reset your password.");
            alert.showAndWait();
            Stage stage = (Stage) resetEmail.getScene().getWindow();
            stage.hide();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email Invalid");
            alert.setContentText("Please enter a valid Email.");
            alert.showAndWait();
        }

    }

}
