package controller.customer;

import ThirdParty.Momo.Momo;
import dto.*;
import enums.RoleType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import service.IAddressService;
import service.IOrderService;
import service.Impl.AddressServiceImpl;
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
    CartController cartController = new CartController();
    IOrderService orderService = new OrderServiceImpl();
    IAddressService addressService = new AddressServiceImpl();
    private Momo momo = new Momo();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO==null ||!userDTO.isActive()){
            session.invalidate();
            resp.sendRedirect("/view/login.jsp");
            return;
        }
        if (userDTO.getAccount().getRole()== RoleType.ADMIN){
            req.setAttribute("error","Trang này không khả dụng");
            req.getRequestDispatcher("/view/errror.jsp").forward(req, resp);
            return;
        }
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
        HttpSession session = req.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO==null || !userDTO.isActive()){
            session.invalidate();
            resp.sendRedirect("/view/login.jsp");
            return;
        }
        if (userDTO.getAccount().getRole()== RoleType.ADMIN){
            req.setAttribute("error","Trang này không khả dụng");
            req.getRequestDispatcher("/view/errror.jsp").forward(req, resp);
            return;
        }
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
        HttpSession session = req.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        int idUser = userDTO.getUserID();
        AddressDTO addressDTO = addressService.getAddressByID(idUser);
        if (addressDTO.getCity()== null){
            req.setAttribute("errCode",1);
            req.setAttribute("message","Không thể lấy được địa chỉ giao hàng, vui lòng cập nhật thông tin cá nhân");
            cartController.Load_Cart_View(req,resp);
            getServletContext().getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
            return;
        }
        String cartItemJson = req.getParameter("cartItem");
        String decodedCartJson = URLDecoder.decode(cartItemJson, StandardCharsets.UTF_8.toString());
        double Total = Double.parseDouble(req.getParameter("Total"));
        double Discount = Double.parseDouble(req.getParameter("Discount"));
        double FeeShip = Double.parseDouble(req.getParameter("feeShip"));
        JSONArray jsonArray = new JSONArray(decodedCartJson);
        int total = (int) Total;
        int discount = (int) Discount;
        int feeShip = (int) FeeShip;
        if (!jsonArray.isEmpty()) {
            List<CartItemDTO> items = new ArrayList<CartItemDTO>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                CartItemDTO item = new CartItemDTO();
                ProductDTO product = new ProductDTO();
                product.setProductId(obj.getInt("idProduct"));
                item.setProductDTO(product);
                item.setQuantity(obj.getInt("quantity"));
                items.add(item);
            }
            if (!orderService.CanCreateOrder(items)){
                req.setAttribute("errCode",1);
                req.setAttribute("message","Not enough stock");
                cartController.Load_Cart_View(req,resp);
                getServletContext().getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
                return;
            }
            HttpResponse<String> response = momo.CallApi(items, idUser, total,discount,feeShip);
            System.out.println(response);
            if (response.statusCode() == 200) {
                JSONObject jsonKQ = new JSONObject((response.body()));
                if (jsonKQ.getInt("resultCode") == 0)
                    resp.sendRedirect(jsonKQ.getString("shortLink"));
            } else {
                req.setAttribute("errCode",1);
                req.setAttribute("message","Phương thức thanh toán đang bị lỗi. Vui lòng thử lại sau.");
                cartController.Load_Cart_View(req,resp);
                getServletContext().getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
            }
        }
        else {
            req.setAttribute("errCode",1);
            req.setAttribute("message","Phải có mặt hàng trong giỏ hàng");
            cartController.Load_Cart_View(req,resp);
            getServletContext().getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
        }
    }
}
