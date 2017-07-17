package DrakeSS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
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
        Map<Integer,User> allowedUsers;
        Map<Integer,User> employees;

        public User(int id, String email, int rights) {
            this.id = id;
            this.email = email;
            this.userRights = rights;
            this.allowedUsers = new HashMap<Integer, User>();
            this.employees = new HashMap<Integer, User>();
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
            this.allowedUsers = new HashMap<Integer, User>();
            this.employees = new HashMap<Integer, User>();
        }



    private JSONArray allowedToJSON() {
        JSONArray result = new JSONArray();
        for (int i = 0; i < allowedUsers.size(); i++)
        {
            if (allowedUsers.get(i) != null)
            {
                result.put(allowedUsers.get(i).getEmail());
            }
        }

        return result;
    }

    private JSONArray employeesToJSON() {
        JSONArray result = new JSONArray();
        for (int i = 0; i < employees.size(); i++)
        {
            if (employees.get(i) != null)
            {
                result.put(employees.get(i).getEmail());
            }
        }

        return result;
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

    public Map<Integer, User> getAllowedUsers()
    {
        return allowedUsers;
    }

    public Map<Integer, User> getEmployees()
    {
        return employees;
    }

        private String getPasswordResetMessage(String code) {
            String s = (password == null ? "Welcome aboard DrakeSS! Your account has been created and is waiting to be set up!" : "Thank you for using the DrakeSS password reset service.")
                    + "\n\nYour temporary password is: " + code + "\n\nPlease log in to "
                    + (password == null ? "set" : "reset") + " your password."
                    + (password == null ? "" : "The code will expire in 10 minutes.");

            return s;
        }

        public void sendPasswordReset() {
            String code = getSaltString();
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

        public int getPermissionLevel()
        {
            return userRights;
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
                    + "\"UserRights\" : " + userRights + ", "
                    + "\"allowedUsers\" : " + allowedToJSON() + ", "
                    + "\"Employees\" : " + employeesToJSON()
                    + "}";
            return s;
        }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
