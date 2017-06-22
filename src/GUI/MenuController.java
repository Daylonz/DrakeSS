package GUI;

import DrakeSS.Client;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;


import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    public Hyperlink email;

    public void initialize(URL url, ResourceBundle rb)
    {
        email.setText(Client.userAccount.getEmail());
    }


}

