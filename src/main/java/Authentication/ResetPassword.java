package Authentication;

import dto.CartItemDTO;
import dto.OrderDTO;
import dto.OrderItemDTO;
import entity.OrderItem;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
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
    public boolean sendBill(String to, int total, int discount, int deliver, OrderDTO order,String orderID,String address){
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        DecimalFormat formatter = new DecimalFormat("#,###");
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
            msg.setSubject("Xác nhận đơn hàng");
            String content =
                    "<html>" +
                    " <head>" +
                    "  <style>" +
                    "   body {" +
                    "            font-family: 'Arial', sans-serif;" +
                    "            margin: 0;" +
                    "            padding: 20px;" +
                    "            background-color: #f9f9f9;" +
                    "        }" +
                    "        .container {" +
                    "            max-width: 1200px;" +
                    "            margin: 0 auto;" +
                    "            background-color: #fff;" +
                    "            padding: 20px;" +
                    "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                    "            border-radius: 8px;" +
                    "        }" +
                    "        .header {" +
                    "            text-align: right;" +
                    "            border-bottom: 2px solid #eee;" +
                    "            padding-bottom: 10px;" +
                    "            margin-bottom: 20px;" +
                    "        }" +
                    "        .header h1 {" +
                    "            font-size: 24px;" +
                    "            font-weight: bold;" +
                    "            color: #333;" +
                    "        }" +
                    "        .header p {" +
                    "            font-size: 16px;" +
                    "            color: #666;" +
                    "        }" +
                    "        .message {" +
                    "            text-align: center;" +
                    "            margin: 20px 0;" +
                    "        }" +
                    "        .message p {" +
                    "            font-size: 16px;" +
                    "            color: #666;" +
                    "        }" +
                    "        .table-container {" +
                    "            width: 100%;" +
                    "            border-collapse: collapse;" +
                    "            margin-bottom: 20px;" +
                    "        }" +
                    "        .table-container th, .table-container td {" +
                    "            border: 1px solid #ddd;" +
                    "            padding: 12px;" +
                    "            text-align: left;" +
                    "        }" +
                    "        .table-container th {" +
                    "            background-color: #f2f2f2;" +
                    "            color: #333;" +
                    "        }" +
                    "        .table-container td img {" +
                    "            width: 50px;" +
                    "            height: 50px;" +
                    "            margin-right: 10px;" +
                    "            vertical-align: middle;" +
                    "        }" +
                    "        .summary {" +
                    "            text-align: right;" +
                    "            margin-bottom: 20px;" +
                    "        }" +
                    "        .summary p {" +
                    "            font-size: 16px;" +
                    "            color: #666;" +
                    "        }" +
                    "        .summary .total {" +
                    "            font-weight: bold;" +
                    "            color: red;" +
                    "            font-size: 18px;" +
                    "        }" +
                    "        .summary-table {" +
                    "            width: 100%;" +
                    "            border-collapse: collapse;" +
                    "        }" +
                    "        .summary-table td {" +
                    "            padding: 5px 0;" +
                    "        }" +
                    "        .summary-table .label {" +
                    "            text-align: left;" +
                    "            padding-right: 10px;" +
                    "        }" +
                    "        .summary-table .value {" +
                    "            text-align: right;" +
                    "        }" +
                    "        .info {" +
                    "            margin-top: 20px;" +
                    "        }" +
                    "        .info p {" +
                    "            font-size: 16px;" +
                    "            color: #666;" +
                    "        }" +
                    "        .info strong {" +
                    "            color: #333;" +
                    "        }" +
                    "        @media (max-width: 600px) {" +
                    "            .header, .message, .summary, .info {" +
                    "                text-align: center;" +
                    "            }" +
                    "            .table-container th, .table-container td {" +
                    "                display: block;" +
                    "                width: 100%;" +
                    "            }" +
                    "            .table-container td img {" +
                    "                display: block;" +
                    "                margin: 0 auto;" +
                    "            }" +
                    "        }" +
                    "  </style>" +
                    " </head>" +
                    " <body>" +
                    "  <div class='container'>" +
                    "   <div class='header'>" +
                    "    <h1>" +
                    "     Xin chào," +
                    "    </h1>" +
                    "    <p>" +
                    "     Đơn hàng của bạn" +
                    "     <strong>" +
                    orderID +
                    "     </strong>" +
                    "     có giá trị là" +
                    "     <strong>" +
                    formatter.format(total) +" đ"+
                    "     </strong>" +
                    "     đã được xác nhận." +
                    "    </p>" +
                    "    <p>" +
                    "     Cảm ơn bạn đã trao gửi niềm tin!" +
                    "    </p>" +
                    "   </div>" +
                    "   <div class='message'>" +
                    "    <p>" +
                    "     Đừng ngần ngại liên hệ chúng tôi, nếu bạn có bất kỳ thắc mắc nào cần giải đáp." +
                    "    </p>" +
                    "   </div>" +
                    "   <table class='table-container'>" +
                    "    <thead>" +
                    "     <tr>" +
                    "      <th>" +
                    "       Tên Sản phẩm" +
                    "      </th>" +
                    "      <th>" +
                    "       Số lượng" +
                    "      </th>" +
                    "      <th>" +
                    "       Giá" +
                    "      </th>" +
                    "     </tr>" +
                    "    </thead>" +
                    "    <tbody>" ;
            for (OrderItemDTO item : order.getOrderItems()) {
                content +=
                        "     <tr>" +
                                "      <td>" +
                                item.getProductDTO().getProductName() +"(size: "+item.getProductDTO().getSize()+" - color"+
                                " <div style='border: 1px solid ; border-radius: 5px; background-color: "+item.getProductDTO().getColor()+"; width: 13px; height: 13px; display: inline-block;'></div>)"+
                                "      </td>" +
                                "      <td>" +
                                item.getQuantity() +
                                "      </td>" +
                                "      <td>" +
                                formatter.format((int)item.getProductDTO().getPrice())+" đ/sp" +
                                "      </td>" +
                                "     </tr>";
            }
            content+=
                    "    </tbody>" +
                    "   </table>" +
                    "   <div class='summary'>" +
                    "    <table class='summary-table'>" +
                    "     <tr>" +
                    "      <td class='label'>" +
                    "       Tạm tính:" +
                    "      </td>" +
                    "      <td class='value'>" +
                    formatter.format(total-deliver+discount)+" đ" +
                    "      </td>" +
                    "     </tr>" +
                    "     <tr>" +
                    "      <td class='label'>" +
                    "       Giảm giá:" +
                    "      </td>" +
                    "      <td class='value'>" +
                    "- "+ formatter.format(discount)+" đ" +
                    "      </td>" +
                    "     </tr>" +
                    "     <tr>" +
                    "      <td class='label'>" +
                    "       Phí vận chuyển:" +
                    "      </td>" +
                    "      <td class='value'>" +
                    formatter.format(deliver)+" đ" +
                    "      </td>" +
                    "     </tr>" +
                    "     <tr>" +
                    "      <td class='label total'>" +
                    "       Tổng cộng:" +
                    "      </td>" +
                    "      <td class='value total'>" +
                    formatter.format(total)+" đ" +
                    "      </td>" +
                    "     </tr>" +
                    "    </table>" +
                    "   </div>" +
                    "   <div class='info'>" +
                    "    <p>" +
                    "     <strong>" +
                    "      Thông tin giao hàng và hóa đơn" +
                    "     </strong>" +
                    "    </p>" +
                    "    <p>" +
                    "     Hóa đơn gửi về: " +address+
                    "    </p>" +
                    "    <p>" +
                    "     Phương thức thanh toán: Momo ("+formatter.format(total)+" đ)" +
                    "    </p>" +
                    "    <p>" +
                    "     Giao hàng tới: " +address+
                    "    </p>" +
                    "   </div>" +
                    "  </div>" +
                    " </body>" +
                    "</html>";
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
