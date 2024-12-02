package controller.customer;

import JpaConfig.JpaConfig;
import dto.AccountDTO;
import dto.CartItemDTO;
import dto.PromotionDTO;
import dto.UserDTO;
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
import service.IAccountService;
import service.ICartService;
import service.Impl.AccountServiceImpl;
import service.Impl.CartServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@WebServlet(urlPatterns = {"/Cart","/Cart/Add","/Cart/Remove","/Cart/Delete_Item","/TWP_ACCOUNT","/Count","/AddCartQuantity"})
public class CartController extends HttpServlet {
    ICartService iCartService = new CartServiceImpl();
    private IAccountService accountService = new AccountServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if (Objects.equals(path, "/TWP_ACCOUNT")){
            AccountDTO account = accountService.findAccountByEmail("thoai1234@gmail.com");
            //Tao mot session moi hoac lay session hien co
            HttpSession session = req.getSession();
            //Luu thong tin nguoi dung vao session
            session.setAttribute("user", account);
            return;
        }
        HttpSession session = req.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO==null ||!userDTO.isActive()){
            // Xóa session
            session.invalidate();
            resp.sendRedirect("/view/login.jsp");
            return;
        }
        if (userDTO.getAccount().getRole()== RoleType.ADMIN){
            req.setAttribute("error","Trang này không khả dụng");
            req.getRequestDispatcher("/view/errror.jsp").forward(req, resp);
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
        switch (path){
            case "/Cart":
                Cart_View(req, resp);
                return;
            case "/Count":
                Count(req, resp);
                return;
            case "/AddCartQuantity":
                Cart_Add_Quantity(req,resp);
                return;
        }
        HttpSession session = req.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO==null ||!userDTO.isActive()){
            // Xóa session
            session.invalidate();
            resp.sendRedirect("/view/login.jsp");
            return;
        }
        if (userDTO.getAccount().getRole()== RoleType.ADMIN){
            req.setAttribute("error","Trang này không khả dụng");
            req.getRequestDispatcher("/view/errror.jsp").forward(req, resp);
            return;
        }
        switch (path){
            case "/Cart/Remove":
                Cart_Remove(req, resp);
                break;
            case "/Cart/Add":
                Add_Cart(req, resp);
                break;
            case "/Cart/Delete_Item":
                Delete_Cart_Item(req, resp);
                break;
            default:
                break;
        }
    }
    public void Count(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            HttpSession session = req.getSession();
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            if (userDTO == null || !userDTO.isActive()) {
                JSONObject json = new JSONObject();
                json.put("quantityItemCart", 0);
                resp.getWriter().println(json.toString());
                return;
            }
            int idUser = userDTO.getUserID();
            int quantity = iCartService.CountQuantityCartItem(idUser);
            JSONObject json = new JSONObject();
            json.put("quantityItemCart", quantity);
            resp.getWriter().println(json.toString());
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("quantityItemCart", 0);
            resp.getWriter().println(json.toString());
            return;
        }
    }
    public void Load_Cart_View(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        int idUser = userDTO.getUserID();
        int idPromotion = -1;
        try{
            idPromotion = Integer.parseInt(req.getParameter("idPr"));
        }catch (Exception e){}
        List<CartItemDTO> cart = iCartService.findAll(idUser);
        List<PromotionDTO> promotionDTOS = iCartService.GetAllPromotionByLoayti(idUser);
        double total = iCartService.Total_Cart(cart);
        double discount = iCartService.CalculateDiscount(cart,idUser,idPromotion);
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
        req.setAttribute("idPr",idPromotion);
        req.setAttribute("promotion", promotionDTOS);
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
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        int idUser = userDTO.getUserID();
        Cart_Add(req,resp,idUser,idProduct,"Thêm hàng thành công","Không đủ hàng trong kho");
        Load_Cart_View(req,resp);
        req.getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
    }

    private void Cart_Remove(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cartItemId = Integer.parseInt(req.getParameter("cartItemId"));
        iCartService.RemoveItem(cartItemId);
        Load_Cart_View(req,resp);
        req.getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
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
    public void Cart_Add_Quantity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
        int idProduct;
        int quantity;
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            JSONObject json1 = new JSONObject(sb.toString());
            idProduct = json1.getInt("idProduct");
            quantity = json1.getInt("quantity");
        }catch (NumberFormatException e){
            json.put("errCode",1);
            json.put("message","Có vấn đề khi thêm hàng");
            resp.getWriter().println(json.toString());
            return;
        }
        HttpSession session = req.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO==null || !userDTO.isActive()){
            json.put("errCode",2);
            json.put("message","Không có quyền truy cập");
            resp.getWriter().println(json.toString());
            return;
        }
        int idUser = userDTO.getUserID();
        if (iCartService.AddItemWithQuantity(idProduct,idUser,quantity)) {
            json.put("errCode",0);
            json.put("message","Thêm thành công");
            resp.getWriter().println(json.toString());
            return;
        }
        else {
            json.put("errCode",1);
            json.put("message","Không đủ hàng trong kho");
            resp.getWriter().println(json.toString());
            return;
        }
    }
    private void Delete_Cart_Item(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idCartItem = Integer.parseInt(req.getParameter("cartItemId"));
        iCartService.deleteCartItem(idCartItem);
        Load_Cart_View(req,resp);
        req.getRequestDispatcher("/view/customer/cart.jsp").forward(req, resp);
    }

}
