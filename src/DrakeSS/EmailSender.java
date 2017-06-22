package DrakeSS;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
	private static String from = "frontrightcorner@gmail.com";
	private static String password = "teamfrc123";
	
	public static void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);
        
        Transport transport = session.getTransport("smtp");
        
        (new Thread() {//This part can take a couple seconds to complete
			public void run() {
				try {
					transport.connect(host, from, password);
					transport.sendMessage(message, message.getAllRecipients());
			        transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
		        
			}
		}).start();
        
        
    }
}
