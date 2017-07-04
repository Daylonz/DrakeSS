package DrakeSS;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Daylon on 7/1/2017.
 */
public class Meeting {

    int id;
    Calendar start;
    Calendar end;
    int room;
    static Map<Integer,Meeting> pendingUsers;
    static Map<Integer,Meeting> attendingUsers;

    public Meeting(int id, Calendar start, Calendar end, int room)
    {
        this.id = id;
        this.start = start;
        this.end = end;
        this.room = room;
    }

    public Meeting(int id, JSONObject meetingData)
    {
        this.id = id;

        this.start = Calendar.getInstance();
        start.set(Calendar.YEAR, meetingData.getJSONObject("startDate").getInt("Year"));
        start.set(Calendar.MONTH, meetingData.getJSONObject("startDate").getInt("Month"));
        start.set(Calendar.DAY_OF_MONTH, meetingData.getJSONObject("startDate").getInt("Day"));
        start.set(Calendar.HOUR, meetingData.getJSONObject("startDate").getInt("Hour"));
        start.set(Calendar.MINUTE, meetingData.getJSONObject("startDate").getInt("Minute"));

        this.end = Calendar.getInstance();
        end.set(Calendar.YEAR, meetingData.getJSONObject("endDate").getInt("Year"));
        end.set(Calendar.MONTH, meetingData.getJSONObject("endDate").getInt("Month"));
        end.set(Calendar.DAY_OF_MONTH, meetingData.getJSONObject("endDate").getInt("Day"));
        end.set(Calendar.HOUR, meetingData.getJSONObject("endDate").getInt("Hour"));
        end.set(Calendar.MINUTE, meetingData.getJSONObject("endDate").getInt("Minute"));

        this.room = meetingData.getInt("Room");

    }

    public int getStartHour()
    {
        return start.get(Calendar.HOUR);
    }

    public int getStartMinutes()
    {
        return start.get(Calendar.MINUTE);
    }

    public int getEndHour()
    {
        return end.get(Calendar.HOUR);
    }

    public int getEndMinutes()
    {
        return end.get(Calendar.MINUTE);
    }


    private String startToJSON() {
        String s = "{"
                + "\"Year\" : " + start.get(start.YEAR) + ", "
                + "\"Month\" : " + start.get(start.MONTH) + ", "
                + "\"Day\" : " + start.get(start.DAY_OF_MONTH) + ","
                + "\"Hour\" : " + start.get(start.HOUR) + ","
                + "\"Minute\" : " + start.get(start.MINUTE) + ","
                + "}";

        return s;
    }

    private String endToJSON() {
        String s = "{"
                + "\"Year\" : " + end.get(end.YEAR) + ", "
                + "\"Month\" : " + end.get(end.MONTH) + ", "
                + "\"Day\" : " + end.get(end.DAY_OF_MONTH) + ","
                + "\"Hour\" : " + end.get(end.HOUR) + ","
                + "\"Minute\" : " + end.get(end.MINUTE) + ","
                + "}";

        return s;
    }

    public String toJSON() {
        String s = "{\"startDate\" : " + startToJSON() + ", "
                  + "\"endDate\" : " + endToJSON() + ", "
                  + "\"Room\" : " + room + ", " + "}";
        return s;
    }

}
