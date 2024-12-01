package ThirdParty.Momo;

import dto.CartItemDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

public class Momo {
    public  String keyS = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    public  String extraData(List<CartItemDTO> items,int idUser,int discount,int feeShip){
        JSONObject obj = new JSONObject();
        obj.put("idUser",idUser);
        obj.put("discount",discount);
        obj.put("feeShip",feeShip);
        JSONArray jsonArray = new JSONArray();

        for (CartItemDTO itemCartDTO : items) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idProduct",itemCartDTO.getProductDTO().getProductId());
            jsonObject.put("quantity",itemCartDTO.getQuantity());
            jsonArray.put(jsonObject);
        }
        obj.put("ListItem",jsonArray);
        return Base64.getEncoder().encodeToString(obj.toString().getBytes(StandardCharsets.UTF_8));
    }
    public static String hmacSHA256(String data, String key) {
        try {
            // Tạo SecretKeySpec từ khóa bí mật
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            // Khởi tạo Mac với thuật toán HmacSHA256
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            // Tạo chữ ký HMAC SHA256
            byte[] hmacBytes = mac.doFinal(data.getBytes("UTF-8"));
            // Chuyển đổi chữ ký từ byte sang chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hmacBytes) {
                String hex = Integer.toHexString(b & 0xFF); // Chuyển byte thành hex
                if (hex.length() == 1) {
                    hexString.append("0"); // Thêm 0 nếu chuỗi hex có 1 ký tự
                }
                hexString.append(hex);

            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC SHA256 signature", e);
        }
    }
    public JSONObject jsonKey(String requestId,int amount,String orderId,List<CartItemDTO> items,int idUser,int discount,int feeShip) {
        String signature ="accessKey=F8BBA842ECF85&amount="+amount+"&extraData=" +extraData(items,idUser,discount,feeShip)+
                "&ipnUrl=https://momo.vn&orderId="+orderId+"&orderInfo=" +
                "Thanh Toán Đơn hàng&partnerCode=MOMO&redirectUrl=http://localhost:8080/CallBack" +
                "&requestId="+requestId+"&requestType=payWithMethod";
        String hash = hmacSHA256(signature,keyS);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("partnerCode","MOMO");
        jsonObject.put("partnerName","Test");
        jsonObject.put("requestType","payWithMethod");
        jsonObject.put("ipnUrl","https://momo.vn");
        jsonObject.put("redirectUrl","http://localhost:8080/CallBack");
        jsonObject.put("orderId",orderId);
        jsonObject.put("amount",amount);
        jsonObject.put("lang","vi");
        jsonObject.put("orderInfo","Thanh Toán Đơn hàng");
        jsonObject.put("requestId",requestId);
        jsonObject.put("extraData",extraData(items,idUser,discount,feeShip));
        jsonObject.put("signature",hash);
        System.out.println(jsonObject.toString());
        return jsonObject;
    }
    public HttpResponse<String> CallApi(List<CartItemDTO> items,int idUser,int amount,int discount,int feeShip){
        String id = LocalDateTime.now().toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://test-payment.momo.vn/v2/gateway/api/create"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonKey(id,amount, id,items,idUser,discount,feeShip).toString()))
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
