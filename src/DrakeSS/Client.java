package DrakeSS;

import javafx.application.Application;
import org.json.*;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Daylon on 6/21/2017.
 */
public class Client {
    public static int permissionLevel = 0;

    public static void main(String[] args) throws UnknownHostException, IOException {
                (new Thread(){
                    public void run(){
                        Application.launch(GUI.Landing.class, args);
                    }
                }).start();
    }
}
