package controller.customer;

import JpaConfig.JpaConfig;
import dto.CartItemDTO;
import entity.Cart;
import entity.CartItem;
import entity.Customer;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import service.ICartService;
import service.Impl.CartServiceImpl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(urlPatterns = {"/Cart","/Cart/Add","/Cart/Remove","/Cart/Delete_Item","/Cart/Add_TWP"})
public class CartController extends HttpServlet {
    ICartService iCartService = new CartServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path){
            case "/Cart":
                Cart_View(req, resp);
                break;
            case "/Cart/Cart_Add":

                break;
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path){
            case "/Cart/Remove":
                Cart_Remove(req, resp,"http://localhost:8080/Cart");
                break;
            case "/Cart/Add":
                Cart_Add(req, resp,"http://localhost:8080/Cart");
                break;
            case "/Cart/Delete_Item":
                Delete_Cart_Item(req, resp,"http://localhost:8080/Cart");
                break;
            default:
                break;
        }
    }

    private void Cart_View(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CartItemDTO> cart = iCartService.findAll(1);
        double total = iCartService.Total_Cart(cart);
        double discount = iCartService.Discount(cart);
        double feeShip = iCartService.FeeShip(1);
        JSONArray jsonArray = new JSONArray();
        for (CartItemDTO cartItemDTO : cart) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idProduct",cartItemDTO.getProductDTO().getProductId());
            jsonObject.put("quantity",cartItemDTO.getQuantity());
            jsonArray.put(jsonObject);
        }
        String encodedCartJson = URLEncoder.encode(jsonArray.toString(), StandardCharsets.UTF_8.toString());
        req.setAttribute("CartList", cart);
        req.setAttribute("total", Double.valueOf(total));
        req.setAttribute("discount", Double.valueOf(discount));
        req.setAttribute("feeShip", Double.valueOf(feeShip));
        req.setAttribute("Sum", Double.valueOf(total-discount+feeShip));
        req.setAttribute("JsonCart", encodedCartJson);
        req.getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
    }

    private void Cart_Remove(HttpServletRequest req, HttpServletResponse resp, String redirect) throws ServletException, IOException {
        int cartItemId = Integer.parseInt(req.getParameter("cartItemId"));
        iCartService.RemoveItem(cartItemId);
        resp.sendRedirect(redirect);
    }
    private void Cart_Add(HttpServletRequest req, HttpServletResponse resp, String redirect) throws ServletException, IOException {
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        iCartService.AddItem(idProduct,1);
        resp.sendRedirect(redirect);
    }
    private void Delete_Cart_Item(HttpServletRequest req, HttpServletResponse resp, String redirect) throws ServletException, IOException {
        int idCartItem = Integer.parseInt(req.getParameter("cartItemId"));
        iCartService.deleteCartItem(idCartItem);
        resp.sendRedirect(redirect);
    }

}
