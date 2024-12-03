package Authentication;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class SendOTPEmail {
    private final  String from = "thoai12309@gmail.com";
    private final String password = "ihzc nxpv cyye etss";
    public String getRandom(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }


    public boolean sendEmail(String to, String code){
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
                    "<p>Vui lòng nhập mã: " +code + "để xác thưc email";
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
