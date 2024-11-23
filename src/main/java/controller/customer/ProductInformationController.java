package controller.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ProductDTO;
import dto.ResponseDTO;
import dto.ReviewDTO;
import dto.UserDTO;
import entity.Product;
import entity.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IProductService;
import service.IResponseService;
import service.IReviewService;
import service.Impl.ProductServiceImpl;
import service.Impl.ResponseServiceImpl;
import service.Impl.ReviewServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet(urlPatterns = {"/product/details"})
public class ProductInformationController extends HttpServlet {
    private IProductService productService = new ProductServiceImpl();
    private IReviewService reviewService = new ReviewServiceImpl();
    private IResponseService responseService = new ResponseServiceImpl();
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String productName = req.getParameter("productName");
        if (productName == null || productName.trim().isEmpty()) {
            req.setAttribute("error", "Tên sản phẩm không hợp lệ.");
            return;
        }

        // Lấy thông tin sản phẩm từ cơ sở dữ liệu
        List<ProductDTO> productDetails = productService.findByName(productName);

        if (productDetails == null || productDetails.isEmpty()) {
//            req.setAttribute("error", "Không tìm thấy sản phẩm.");
//            req.getRequestDispatcher("/error.jsp").forward(req, resp);
            resp.getWriter().print("Không tìm thấy sản phẩm");
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
            List<ReviewDTO> reviews = reviewService.getReviewsByProductID(IDs);
            req.setAttribute("reviews", reviews);
            req.setAttribute("averageRating", reviewService.averageRating(reviews));


            Map<ProductDTO, Double> RecommendProducts = productService.findRandomProducts(1, 20, productName);
            req.setAttribute("RecommendProducts", RecommendProducts);

            req.setAttribute("role", 1);

            req.setAttribute("images", images);
            req.setAttribute("colors", colors);
            req.setAttribute("sizes", sizes);
            req.setAttribute("price", productDetails.getFirst().getPrice());
            req.setAttribute("name", productDetails.getFirst().getProductName());
            req.setAttribute("description", productDetails.getFirst().getDescription());




            req.getRequestDispatcher("/ProductInformation.jsp").forward(req, resp);
        }

    }

    public void destroy() {
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        try{
            String productName = req.getParameter("productName");
            if (productName == null || productName.trim().isEmpty()) {
                req.setAttribute("error", "Tên sản phẩm không được cung cấp.");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            String reviewIDStr = req.getParameter("reviewID");
            String responseContent = req.getParameter("responseContent");
            String responseIDStr = req.getParameter("responseID");
            int reviewID=0;
            if(reviewIDStr != null || !reviewIDStr.trim().isEmpty()) {
                reviewID = Integer.parseInt(reviewIDStr);
            }
            int responseID=0;
            if(responseIDStr != null || !responseIDStr.trim().isEmpty()) {
                responseID = Integer.parseInt(responseIDStr);
            }

            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setReviewID(reviewID);

            UserDTO userDTO = new UserDTO();
            userDTO.setUserID(48);

            ResponseDTO response = new ResponseDTO();
            response.setResponseID(responseID);
            response.setContent(responseContent);
            response.setReview(reviewDTO);
            response.setAdmin(userDTO);
            Date currentDate = new Date();
            response.setTimeStamp(currentDate);


            if(responseService.addResponse(response)) {
                String redirectURL = req.getContextPath() + "/product/details?productName=" + productName;
                System.out.println("Redirect URL: " + redirectURL);
                resp.sendRedirect(redirectURL);
            }else  req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //doGet(req, resp);
    }
}