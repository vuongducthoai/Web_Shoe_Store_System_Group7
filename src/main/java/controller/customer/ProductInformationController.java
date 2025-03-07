package controller.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import dto.*;
import entity.Product;
import entity.Response;
import entity.User;
import io.vavr.Tuple3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.*;
import service.Impl.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet(urlPatterns = {"/product/details"})
public class ProductInformationController extends HttpServlet {
    private IProductService productService = new ProductServiceImpl();
    private IReviewService reviewService = new ReviewServiceImpl();
    private IResponseService responseService = new ResponseServiceImpl();
    private IProductPromotion productPromotion = new ProductPromotionImpl();
    private IUserService userService = new UserServiceImpl();
    private ICustomerService customerService = new CustomerServiceImpl();

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String productName = req.getParameter("productName");
        if (productName == null || productName.trim().isEmpty()) {
            System.out.print("Ten sp khong hop le");
            return;
        }

        // Lấy thông tin sản phẩm từ cơ sở dữ liệu
        List<ProductDTO> productDetails = productService.findByName(productName);
        System.out.println(productDetails);
        if (productDetails == null || productDetails.isEmpty()) {
            req.setAttribute("error", "Sản phẩm không tồn tại");
            req.getRequestDispatcher("/view/error.jsp").forward(req, resp);
        } else {
            List<String> images = productDetails.stream()
                    .map(ProductDTO::getBase64Image)
                    .filter(image -> image != null)
                    .distinct()
                    .toList();

            List<String> colors = productDetails.stream()
                    .map(ProductDTO::getColor)
                    .filter(color -> color != null)
                    .distinct()
                    .toList();

            List<Integer> sizes = productDetails.stream()
                    .map(ProductDTO::getSize)
                    .distinct()
                    .sorted()
                    .toList();



            List<Integer> IDs = productDetails.stream()
                    .map(ProductDTO::getProductId)
                    .distinct().toList();
            System.out.println(IDs.size());
            List<ReviewDTO> reviews = reviewService.getReviewsByProductID(IDs);

            int loyaty=0;
            HttpSession session = req.getSession();
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            if(userDTO != null){

                loyaty = customerService.getCustomerLoyaty(userDTO.getUserID());
            }


            if(userDTO != null){
                if(userService.checkAdmin(userDTO.getUserID()))
                    req.setAttribute("role", 1);
            }
            PromotionProductDTO promotionProductDTO = new PromotionProductDTO();
            promotionProductDTO = productPromotion.promotioOnProductInfo(productDetails.getFirst().getProductName(),loyaty );

            if(promotionProductDTO!= null) {
                System.out.println("co sp");
                System.out.println(promotionProductDTO.getPromotion().getEndDate());
                req.setAttribute("promotion", promotionProductDTO);
            }

            req.setAttribute("reviews", reviews);
            double rate = reviewService.averageRating(reviews);
            System.out.println(rate);
            req.setAttribute("averageRating", rate);


            List<Tuple3<ProductDTO, Double, PromotionProductDTO>>  RecommendProducts = (List<Tuple3<ProductDTO, Double, PromotionProductDTO>>) productService.findRandomProducts(productName, productDetails.getFirst().getCategoryDTO().getCategoryId(), loyaty);

            if (RecommendProducts == null || RecommendProducts.isEmpty()) {
                System.out.println("RecommendProducts is null or empty.");
                return;
            }

            req.setAttribute("RecommendProducts", RecommendProducts);



            req.setAttribute("images", images);
            req.setAttribute("colors", colors);
            req.setAttribute("sizes", sizes);
            req.setAttribute("price", productDetails.getFirst().getPrice());
            req.setAttribute("name", productDetails.getFirst().getProductName());
            req.setAttribute("description", productDetails.getFirst().getDescription());


            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Chuyển danh sách thành JSON
            String jsonProductDetails = objectMapper.writeValueAsString(productDetails);

            // Gửi JSON qua JSP
            req.setAttribute("productDetails", jsonProductDetails);

            req.getRequestDispatcher("/ProductInformation.jsp").forward(req, resp);
        }

    }

    public void destroy() {
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = "";
        action = req.getParameter("action").trim();
        System.out.println("Action là: " + action);
        String productName = req.getParameter("productName").trim();

        if(action.equals("deleteResponse")){
            try{
                String responseIDStr = req.getParameter("responseID");
                if(responseIDStr == null || responseIDStr.trim().isEmpty()) {
                    System.out.println("responseID is null or empty.");
                    return;
                }
                if(responseService.deleteResponse(Integer.parseInt(responseIDStr))){
                    String redirectURL = req.getContextPath() + "/product/details?productName=" + productName;
                    resp.sendRedirect(redirectURL);
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                req.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());

                return;
            }

        }




        else if (action.equals("updateResponse")){
            try{
                if (productName == null || productName.trim().isEmpty()) {
                    req.setAttribute("error", "Tên sản phẩm không được cung cấp.");
                    req.getRequestDispatcher("/errorNULL.jsp").forward(req, resp);
                    return;
                }

                String reviewIDStr = req.getParameter("reviewID");
                String responseContent = req.getParameter("responseContent");
                String responseIDStr = req.getParameter("ResponseID");

                int reviewID=0;
                if (reviewIDStr != null && !reviewIDStr.trim().isEmpty()) {
                    reviewID = Integer.parseInt(reviewIDStr);
                }


                int responseID = Integer.parseInt(responseIDStr);



                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setReviewID(reviewID);
                HttpSession session = req.getSession();
                AdminDTO adminDTO = new AdminDTO();
                UserDTO user = (UserDTO) session.getAttribute("user");
                adminDTO.setUserID(user.getUserID());

                ResponseDTO response = new ResponseDTO();
                response.setResponseID(responseID);
                response.setContent(responseContent);
                response.setReview(reviewDTO);
                response.setAdmin(adminDTO);
                Date currentDate = new Date();
                response.setTimeStamp(currentDate);


                if(responseService.addResponse(response)) {
                    String redirectURL = req.getContextPath() + "/product/details?productName=" + productName;
                    resp.sendRedirect(redirectURL);
                }else  {
                    req.getRequestDispatcher("/errorAddFalse.jsp").forward(req, resp);
                    return;
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                req.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
                req.getRequestDispatcher("/errorCatch.jsp").forward(req, resp);
                return;
            }
        }


        //doGet(req, resp);
    }

}
