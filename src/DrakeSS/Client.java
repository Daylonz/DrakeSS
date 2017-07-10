package DrakeSS;

import javafx.application.Application;
import org.json.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Daylon on 6/21/2017.
 */
public class Client {
    public static int permissionLevel = 0;
    public static User userAccount = null;
    public static ArrayList<String> usersToInvite = new ArrayList<String>();

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        DatabaseHandler.loadData();
        (new Thread(){
            public void run(){
                Application.launch(GUI.Landing.class, args);
            }
        }).start();
    }

    public static String attemptLogin(String user, String pass)
    {
        permissionLevel = 0;

        User loginAs = userFromEmail(user);

        if (loginAs == null)
        {
            return ("failure");
        }

        System.out.println("Logging in, "+user+", "+loginAs.getEmail());
        if (loginAs.verifyLoginCode(pass)) {
            System.out.println("Correct login code");
            userAccount = loginAs;
            permissionLevel = userAccount.userRights;
            return ("codesuccess");
        } else {
            System.out.println("No login code");
        }

        if (loginAs.verifyPassword(pass)) {
            userAccount = loginAs;
            userAccount.clearLoginCode();
            permissionLevel = userAccount.userRights;
            return ("success");
        } else {
            System.out.println("Verification failed");
            return ("failure");
        }
    }

    private static User userFromEmail(String email) {
        User u;

        for (Map.Entry<Integer, User> entry : DatabaseHandler.users.entrySet()) {
            u = entry.getValue();
            if (u.emailMatches(email)) {
                return u;
            }
        }
        return null;
    }

    public static boolean attemptReset(String email)
    {
        User u = userFromEmail(email);
        if (u != null)
        {
            u.sendPasswordReset();
            return true;
        }
        return false;
    }
}
