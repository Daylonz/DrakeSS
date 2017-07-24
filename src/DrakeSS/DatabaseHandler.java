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
        deleteMeetings();
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
            for (int j = 0; j < b.length(); j++)
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

    public static void deleteMeetings()
    {
        for (int i = 0; i < mainschedule.meetings.size(); i++)
        {
            try
            {
                if (mainschedule.meetings.get(i).getAttendingUsers().isEmpty() && mainschedule.meetings.get(i).getPendingUsers().isEmpty())
                {
                    mainschedule.meetings.remove(i);
                    for (int j = i+1; j < mainschedule.meetings.size()+1; j++)
                    {
                        mainschedule.meetings.put(j-1, mainschedule.meetings.get(j));
                    }
                }
            } catch (Exception e)
            {
            }
            mainschedule.meetings.values().remove(null);
        }
    }

    public static void wipeSchedule() throws IOException
    {
        mainschedule.meetings =  new HashMap<>();
        saveData();
    }

    public static void deleteUser() throws IOException
    {
        //Delete permission data regarding the deleted user
        for (int i = 0; i < users.size(); i++)
        {
            if (users.get(i).getAllowedUsers().containsValue(Client.selecteduser))
            {
                for (int j = 0; j < users.get(i).getAllowedUsers().size(); j++)
                {
                    if (users.get(i).getAllowedUsers().get(j) == Client.selecteduser)
                    {
                        users.get(i).getAllowedUsers().remove(j, Client.selecteduser);
                    }
                }
                users.get(i).getAllowedUsers().values().remove(null);
            }
        }

        //Delete manager employee data regarding the deleted user
        for (int i = 0; i < users.size(); i++)
        {
            if (users.get(i).getEmployees().containsValue(Client.selecteduser))
            {
                for (int j = 0; j < users.get(i).getEmployees().size(); j++)
                {
                    if (users.get(i).getEmployees().get(j) == Client.selecteduser)
                    {
                        users.get(i).getEmployees().remove(j, Client.selecteduser);
                    }
                }
                users.get(i).getEmployees().values().remove(null);
            }
        }

        //Delete meetings data regarding pending invites of this user
        for (int i = 0; i < mainschedule.meetings.size(); i++)
        {
            if (mainschedule.meetings.get(i).getPendingUsers().containsValue(Client.selecteduser))
            {
                for (int j = 0; j < mainschedule.meetings.get(i).getPendingUsers().size(); j++)
                {
                    if (mainschedule.meetings.get(i).getPendingUsers().get(j) == Client.selecteduser)
                    {
                        mainschedule.meetings.get(i).getPendingUsers().remove(j, Client.selecteduser);
                    }
                }
                mainschedule.meetings.get(i).getPendingUsers().values().remove(null);
            }
        }

        //Delete meetings data regarding this user attending a meeting
        for (int i = 0; i < mainschedule.meetings.size(); i++)
        {
            if (mainschedule.meetings.get(i).getAttendingUsers().containsValue(Client.selecteduser))
            {
                for (int j = 0; j < mainschedule.meetings.get(i).getAttendingUsers().size(); j++)
                {
                    if (mainschedule.meetings.get(i).getAttendingUsers().get(j) == Client.selecteduser)
                    {
                        mainschedule.meetings.get(i).getAttendingUsers().remove(j, Client.selecteduser);
                    }
                }
                mainschedule.meetings.get(i).getAttendingUsers().values().remove(null);
            }
        }

        //Delete User
        for (int i = 0; i < users.size(); i++)
        {
            try
            {
                if (users.get(i) == Client.selecteduser)
                {
                    users.remove(i);
                    for (int j = i+1; j < users.size()+1; j++)
                    {
                        users.put(j-1, users.get(j));
                    }
                }
            } catch (Exception e)
            {
            }
            users.values().remove(null);
        }

        saveData();
    }

    public static void deleteMeeting() throws IOException
    {
        //Delete Meeting
        for (int i = 0; i < mainschedule.meetings.size(); i++)
        {
            try
            {
                if (mainschedule.meetings.get(i) == Client.selectedmeeting)
                {
                    mainschedule.meetings.remove(i);
                    for (int j = i+1; j < mainschedule.meetings.size()+1; j++)
                    {
                        mainschedule.meetings.put(j-1, mainschedule.meetings.get(j));
                    }
                }
            } catch (Exception e)
            {
            }
        }
        mainschedule.meetings.values().remove(null);
        saveData();
    }


}
