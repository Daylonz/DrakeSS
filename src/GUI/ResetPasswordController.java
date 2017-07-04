package GUI;

import DrakeSS.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class ResetPasswordController {

    public PasswordField pass1;
    public PasswordField pass2;
    public Button changeButton;
    public Label invalid;



    public void attemptChange(ActionEvent event) throws NoSuchAlgorithmException
    {
        if (checkValid())
        {
            try{
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(pass2.getText().getBytes(), 0, pass2.getText().length());
                String md5 = new BigInteger(1, md.digest()).toString(16);
                Client.userAccount.setPassword(md5);
                Stage stage = (Stage) changeButton.getScene().getWindow();
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
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            invalid.setVisible(true);
        }
    }

    private boolean checkValid()
    {
        return (pass1.getText().equals(pass2.getText()) && (pass1.getLength() < 21));
    }

}
