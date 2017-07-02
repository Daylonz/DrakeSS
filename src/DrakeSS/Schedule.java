package DrakeSS;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daylon on 6/21/2017.
 */
public class Schedule {

    User user;
    public static Map<Integer,Meeting> meetings;

    public Schedule(User user)
    {
        this.user = user;
    }

    public Schedule()
    {
        this.user = null;
        meetings = new HashMap<>();
    }

    public void addMeeting(Meeting meeting)
    {
        meetings.put(meetings.size(), meeting);
    }

    public void removeMeeting(Meeting meeting)
    {
        meetings.remove(meeting);
    }

    public String toJSON() {
        String s = "{\n";

        s += "\"User\" : " + user + ", \n";

        int i=0;
        for (Map.Entry<Integer, Meeting> entry : meetings.entrySet()) {
            int id = entry.getKey();
            Meeting meeting = entry.getValue();
            s += "\t\t\t\t" + id + " : " + meeting.toJSON() + (i+1 == meetings.size() ? "" : ",") + "\n";
            i++;
        }

        s += "\t\t\t}";
        return s;
    }
}
