package DrakeSS;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by Daylon on 7/23/2017.
 */

public class Proposal {
    Calendar start;
    Calendar end;
    User user;

    public Proposal(Calendar start, Calendar end, User user) {
        this.start = start;
        this.end = end;
        this.user = user;
    }

    public String getUserEmail()
    {
        return user.getEmail();
    }

    public Calendar getStart()
    {
        return start;
    }

    public Calendar getEnd()
    {
        return end;
    }


    private String getProposalMessage() {
        String s = Client.userAccount.getEmail() + " has proposed a new meeting time for one of your meetings."
                + "\n\nPlease access DrakeSS from the main computer to view more details";
        return s;
    }

    private String getProposalTitle() {
        return "New DrakeSS Meeting Time Proposal";
    }

    public void sendProposalNotification() {
        try {
            EmailSender.sendEmail(Client.selectedmeeting.getCreatorEmail(), getProposalTitle(), getProposalMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDisplayEndHour()
    {
        if (end.get(Calendar.HOUR) == 0)
        {
            return 12;
        }
        return end.get(Calendar.HOUR);
    }

    public int getDisplayStartHour()
    {
        if (start.get(Calendar.HOUR) == 0)
        {
            return 12;
        }
        return start.get(Calendar.HOUR);
    }

    public String getStartMinutes()
    {
        if (start.get(Calendar.MINUTE) == 0)
        {
            return "00";
        }
        return Integer.toString(start.get(Calendar.MINUTE));
    }

    public String getEndMinutes()
    {
        if (end.get(Calendar.MINUTE) == 0)
        {
            return "00";
        }
        return Integer.toString(end.get(Calendar.MINUTE));
    }
}

