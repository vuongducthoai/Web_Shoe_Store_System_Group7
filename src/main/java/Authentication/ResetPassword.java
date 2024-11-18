package Authentication;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

public class ResetPassword {
    private final int LIMIT_MINUS = 10;
    private final String from = "thoai12309@gmail.com";
    private final String password = "ihzc nxpv cyye etss";
    public String generateToken(){ // Randon Token
        return UUID.randomUUID().toString();
    }

    public LocalDateTime expiryTime(){
        return LocalDateTime.now().plusMinutes(LIMIT_MINUS); // Thoi gian song cua token
    }

    public boolean isExpireTime(LocalDateTime time){
        return LocalDateTime.now().isAfter(time);
    }

    public boolean sendEmail(String to, String link){
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(from, password);
            }
        };

        Session session = Session.getInstance(props, auth);
        Message msg = new MimeMessage(session);

        try {
            msg.addHeader("Content-Type", "text/html; charset=utf-8");
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject("Reset Password");
            String content = "<h1>Hello "+ to +"</h1>" +
                    "<p>Click the link the reset password <a href="+link+">Click here</a></p>";
            msg.setContent(content, "text/html; charset=utf-8");
            Transport.send(msg);
            System.out.println("Send successfully");
            return true;
        } catch (Exception e){
            System.out.println("Send failed");
            System.out.println(e.getMessage());
            return false;
        }

    }
}
