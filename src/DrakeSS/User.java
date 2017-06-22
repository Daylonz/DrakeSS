package DrakeSS;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Daylon on 6/21/2017.
 */
public class User {

        int id;
        String email;
        int userRights;
        String password = null;
        JSONObject loginCode = null;
        Schedule s;

        public User(int id, String email, int rights) {
            this.id = id;
            this.email = email;
            this.userRights = rights;
            sendPasswordReset();
        }

        public User(int id, JSONObject data) {
            String password = null;
            JSONObject loginCode = null;
            try {
                password = data.getString("Password");
            } catch (JSONException e) {}
            try {
                loginCode = data.getJSONObject("LoginCode");
            } catch (JSONException e) {}

            this.id = id;
            this.email = data.getString("Email");
            this.userRights = data.getInt("UserRights");
            this.password = password;
            this.loginCode = loginCode;
        }

        public void clearLoginCode() {
            this.loginCode = null;
            try {DatabaseHandler.saveData();} catch (Exception e){e.printStackTrace();}
        }

        public void setEmail(String newEmail) {
            System.out.println("Email set to: "+newEmail);
            this.email = newEmail;
            try {DatabaseHandler.saveData();} catch (Exception e){e.printStackTrace();}
        }

        public void setPassword(String newPassword) {
            System.out.println("Password set to :"+newPassword);
            this.password = newPassword;
            loginCode = null;
            try {DatabaseHandler.saveData();} catch (Exception e){e.printStackTrace();}
        }

        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public boolean verifyPassword(String password) {
            return this.password != null && this.password.equals(password);
        }

        public boolean verifyLoginCode(String code) {
            return (this.loginCode == null) ? false : this.loginCode.getString("Code").equals(code);
        }

        private String getPasswordResetTitle() {
            String s = (password == null ? "DrakeSS Account Setup" : "DrakeSS Password Reset");

            return s;
        }

        public boolean emailMatches(String email) {
            return this.email.toLowerCase().equals(email.toLowerCase());
        }

        private String getPasswordResetMessage(String code) {
            String s = (password == null ? "Welcome aboard DrakeSS! Your account has been created and is waiting to be set up!" : "Thank you for using the DrakeSS password reset service.")
                    + "\n\nYour temporary password is: " + code + "\n\nPlease log in to "
                    + (password == null ? "set" : "reset") + " your password."
                    + (password == null ? "" : "The code will expire in 10 minutes.");

            return s;
        }

        public void sendPasswordReset() {
            Random generator = new Random();
            String code = generator.nextInt(1000000)+"";
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            md.update(code.getBytes(), 0, code.length());
            String hashedCode = new BigInteger(1, md.digest()).toString(16);

            JSONObject loginCode = new JSONObject();
            loginCode.put("Code", hashedCode);
            this.loginCode = loginCode;

            try {
                EmailSender.sendEmail(getEmail(), getPasswordResetTitle(), getPasswordResetMessage(""+code));
                DatabaseHandler.saveData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getPasswordJSON() {
            String s = "";

            s += (password == null ? "" : "\"");
            s += password;
            s += (password == null ? "" : "\"");

            return s;
        }

        public String getLoginCodeJSON() {
            String s = "";

            s += (loginCode == null ? null : loginCode.toString());

            return s;
        }

        public String toJSON() {
            String s = "{\"Email\" : \"" + email + "\", \"Password\" : " + getPasswordJSON() +", "
                    + "\"LoginCode\" : " + getLoginCodeJSON() + ", "
                    + "\"UserRights\" : " + userRights
                    + "}";

            return s;
        }
}
