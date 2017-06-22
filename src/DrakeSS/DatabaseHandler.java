package DrakeSS;

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
    public static Map<Integer,Schedule> schedule = new HashMap<>();
    private static String databasePath = getDatabasePath();

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
        dbString += "\t\"Schedule\" : {},\n";
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

    private static void loadUsers(JSONObject refereeList) {
        String[] refereeIds = JSONObject.getNames(refereeList);
        for (int i=0; i < refereeIds.length; i ++) {
            JSONObject refereeData = refereeList.getJSONObject(refereeIds[i]);
            int refereeId = Integer.parseInt(refereeIds[i]);
            users.put(refereeId, new User (refereeId, refereeData));
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


}
