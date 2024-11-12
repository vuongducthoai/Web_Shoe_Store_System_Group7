package controller.customer;

import ThirdParty.Momo.Momo;
import dto.CartItemDTO;
import dto.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import service.IOrderService;
import service.Impl.OrderServiceImpl;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/Momo_pay","/CallBack"})
public class MomoController extends HttpServlet {
    IOrderService orderService = new OrderServiceImpl();
    private Momo momo = new Momo();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/Momo_pay":
                Momo_pay(req, resp);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/CallBack":
                Callback(req, resp);
                break;
            default:
                break;
        }
    }

    protected void Callback(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        String requestId = req.getParameter("requestId");
        String partnerCode = req.getParameter("partnerCode");
        String key = "accessKey=F8BBA842ECF85&orderId="+orderId+"&partnerCode="+partnerCode +
                "&requestId="+requestId;
        String signature = Momo.hmacSHA256(key,"K951B6PE1waDMi640xX08PD3vg6EkVlz");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("partnerCode",partnerCode);
        jsonObject.put("requestId",requestId);
        jsonObject.put("orderId",orderId);
        jsonObject.put("signature",signature);
        jsonObject.put("lang","vi");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://test-payment.momo.vn/v2/gateway/api/query"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            System.out.println(response.statusCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (response.statusCode() == 200) {
            JSONObject jsonKQ = new JSONObject((response.body()));
            if (jsonKQ.getInt("resultCode")==0){
                if (orderService.CreateOrder(jsonKQ.toString()))
                    getServletContext().getRequestDispatcher("/view/customer/paySuccess.jsp").forward(req, resp);
                else
                    getServletContext().getRequestDispatcher("/view/customer/payError.jsp").forward(req, resp);
            }
            else {
                getServletContext().getRequestDispatcher("/view/customer/payError.jsp").forward(req, resp);
            }
        }
        else
            getServletContext().getRequestDispatcher("/view/customer/payError.jsp").forward(req, resp);
    }

    protected void Momo_pay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idUser = 1;
        String cartItemJson = req.getParameter("cartItem");
        String decodedCartJson = URLDecoder.decode(cartItemJson, StandardCharsets.UTF_8.toString());
        double Total = Double.parseDouble(req.getParameter("Total"));
        int total = (int) Total;
        if (total>0) {
            JSONArray jsonArray = new JSONArray(decodedCartJson);

            List<CartItemDTO> items = new ArrayList<CartItemDTO>();
            System.out.println(jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                CartItemDTO item = new CartItemDTO();
                ProductDTO product = new ProductDTO();
                product.setProductId(obj.getInt("idProduct"));
                item.setProductDTO(product);
                item.setQuantity(obj.getInt("quantity"));
                items.add(item);
            }
            HttpResponse<String> response = momo.CallApi(items, idUser, total);
            System.out.println(response);
            if (response.statusCode() == 200) {
                JSONObject jsonKQ = new JSONObject((response.body()));
                if (jsonKQ.getInt("resultCode") == 0)
                    resp.sendRedirect(jsonKQ.getString("shortLink"));
            } else {
                resp.sendRedirect("http://localhost:8080/Cart");
            }
        }
        else
            resp.sendRedirect("http://localhost:8080/Cart");
    }
}
