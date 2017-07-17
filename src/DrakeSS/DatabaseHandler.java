package DrakeSS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daylon on 6/21/2017.
 */
public class DatabaseHandler {

    public static Map<Integer,User> users = new HashMap<>();
    //public static Map<Integer,Meeting> meetings = new HashMap<>();
    private static String databasePath = getDatabasePath();
    public static Schedule mainschedule = new Schedule();

    private static String getDatabasePath() {
        File resources = new File("database");
        if (!resources.exists()) {
            resources.mkdir();
        }
        return resources.getPath()+"/Database.json";
    }

    private static void createDatabase() throws IOException {
        System.out.println("Creating database");
        String dbString = "";
        dbString += "{\n";
        dbString += "\t\"Schedule\" : {\n";
        dbString += "},\n";
        dbString += "\t\"Users\" : {\n";
        dbString += "\t\t0 : " + new User(0, "DrakeSS@mail.com", 3).toJSON() + "\n";
        dbString += "\t}\n";
        dbString += "}";

        saveData(dbString);
    }

    public static void loadData() throws IOException, NoSuchAlgorithmException {
        File dbFile = new File(databasePath);
        if (!dbFile.exists()) {
            createDatabase();
        }

        JSONObject database = new JSONObject(readFile(databasePath, StandardCharsets.UTF_8));
        loadUsers(database.getJSONObject("Users"));
        loadUserSpecifics(database.getJSONObject("Users"));
        loadSchedule(database.getJSONObject("Schedule"));

    }

    public static void saveData() throws IOException {
        saveData(generateDatabaseJSON());
    }

    private static void saveData(String dbString) throws IOException {
        PrintWriter writer = new PrintWriter(databasePath, "UTF-8");
        writer.println(dbString);
        writer.close();
    }

    private static String generateDatabaseJSON() {
        String s = "{\n";
        int i;

        s += "\t\"Schedule\" : {\n";

        i=0;
        if (mainschedule.meetings != null) {
            for (Map.Entry<Integer, Meeting> entry : mainschedule.meetings.entrySet()) {
                int id = entry.getKey();
                Meeting m = entry.getValue();
                s += "\t\t" + id + " : " + m.toJSON() + (i + 1 == mainschedule.meetings.size() ? "" : ",") + "\n";
                i++;
            }
        }

        s += "},\n";

        s += "\t\"Users\" : {\n";

        i=0;
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            int id = entry.getKey();
            User u = entry.getValue();
            s += "\t\t" + id + " : " + u.toJSON() + (i+1 == users.size() ? "" : ",") + "\n";
            i++;
        }

        s += "\t}\n";
        s += "}";

        return s;
    }

    private static void loadUsers(JSONObject userList) {
        String[] userIds = JSONObject.getNames(userList);
        for (int i=0; i < userIds.length; i ++) {
            JSONObject userData = userList.getJSONObject(userIds[i]);
            int userId = Integer.parseInt(userIds[i]);
            users.put(userId, new User (userId, userData));
        }
    }

    public static void loadUserSpecifics(JSONObject userList)
    {
        String[] userIds = JSONObject.getNames(userList);
        for (int i=0; i < userIds.length; i ++)
        {
            JSONObject userData = userList.getJSONObject(userIds[i]);
            JSONArray a = userData.getJSONArray("allowedUsers");
            User user = Client.userFromEmail(userData.getString("Email"));
            for (int j = 0; j < a.length(); j++)
            {
                user.getAllowedUsers().put(user.getAllowedUsers().size(), Client.userFromEmail(a.getString(j)));
            }
            JSONArray b = userData.getJSONArray("Employees");
            User u = Client.userFromEmail(userData.getString("Email"));
            for (int j = 0; j < a.length(); j++)
            {
                u.getEmployees().put(u.getEmployees().size(), Client.userFromEmail(b.getString(j)));
            }

        }
    }

    private static void loadSchedule(JSONObject meetingList) {
        String[] meetingIds = JSONObject.getNames(meetingList);
        if (meetingIds != null) {
            for (int i = 0; i < meetingIds.length; i++) {
                JSONObject meetingData = meetingList.getJSONObject(meetingIds[i]);
                int meetingId = Integer.parseInt(meetingIds[i]);
                mainschedule.meetings.put(meetingId, new Meeting(meetingId, meetingData));
            }
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


}
