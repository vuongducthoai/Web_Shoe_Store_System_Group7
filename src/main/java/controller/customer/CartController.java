package controller.customer;

import JpaConfig.JpaConfig;
import dto.AccountDTO;
import dto.CartItemDTO;
import entity.Cart;
import entity.CartItem;
import entity.Customer;
import entity.Product;
import enums.RoleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        HttpSession session = req.getSession();
        AccountDTO accountDTO = (AccountDTO) session.getAttribute("user");
        if (accountDTO==null || accountDTO.getUser()==null||!accountDTO.getUser().isActive()){
            // Xóa session
            session.invalidate();
            resp.sendRedirect("/view/login.jsp");
        }
        if (accountDTO.getRole()== RoleType.ADMIN){
            return;
        }
        switch (path){
            case "/Cart":
                Cart_View(req, resp);
                break;
            case "/Cart/Add":
                resp.sendRedirect("http://localhost:8080/Cart");
                break;
            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        AccountDTO accountDTO = (AccountDTO) session.getAttribute("user");
        if (accountDTO==null || accountDTO.getUser()==null||!accountDTO.getUser().isActive()){
            session.invalidate();
            resp.sendRedirect("/view/login.jsp");
        }
        if (accountDTO.getRole()== RoleType.ADMIN){
            return;
        }
        switch (path){
            case "/Cart/Remove":
                Cart_Remove(req, resp,"http://localhost:8080/Cart");
                break;
            case "/Cart/Add":
                Add_Cart(req, resp);
                break;
            case "/Cart/Delete_Item":
                Delete_Cart_Item(req, resp,"http://localhost:8080/Cart");
                break;
            default:
                break;
        }
    }
    public void Load_Cart_View(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        AccountDTO accountDTO = (AccountDTO) session.getAttribute("user");
        int idUser = accountDTO.getUser().getUserID();
        List<CartItemDTO> cart = iCartService.findAll(idUser);
        double total = iCartService.Total_Cart(cart);
        double discount = iCartService.CalculateDiscount(cart,idUser);
        double feeShip = iCartService.FeeShip(idUser);
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
    }
    private void Cart_View(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Load_Cart_View(req,resp);
        req.getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
    }

    private void Add_Cart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        HttpSession session = req.getSession();
        AccountDTO accountDTO = (AccountDTO) session.getAttribute("user");
        int idUser = accountDTO.getUser().getUserID();
        Cart_Add(req,resp,idUser,idProduct,"Product added to cart successfully","This product cannot be added anymore.");
        Load_Cart_View(req,resp);
        req.getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
    }

    private void Cart_Remove(HttpServletRequest req, HttpServletResponse resp, String redirect) throws ServletException, IOException {
        int cartItemId = Integer.parseInt(req.getParameter("cartItemId"));
        iCartService.RemoveItem(cartItemId);
        resp.sendRedirect(redirect);
    }
    //errCode = 0 : thanh cong , errCode = 1 : That bai
    public void Cart_Add(HttpServletRequest req, HttpServletResponse resp,int userId,int idProduct,String messageSuccess,String messageError) throws ServletException, IOException {
        if (iCartService.AddItem(idProduct,userId)) {
            req.setAttribute("errCode",0);
            req.setAttribute("message",messageSuccess);
        }
        else {
            req.setAttribute("errCode",1);
            req.setAttribute("message",messageError);
        }
    }
    private void Delete_Cart_Item(HttpServletRequest req, HttpServletResponse resp, String redirect) throws ServletException, IOException {
        int idCartItem = Integer.parseInt(req.getParameter("cartItemId"));
        iCartService.deleteCartItem(idCartItem);
        resp.sendRedirect(redirect);
    }

}
