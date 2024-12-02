package controller.admin;

import dto.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import service.IOrderService;
import service.IReviewService;
import service.Impl.OrderServiceImpl;
import service.Impl.ReviewServiceImpl;
import java.io.IOException;
import java.util.List;
@WebServlet(urlPatterns = {"/customer/orders", "/customer/orderDetails"})
@MultipartConfig
public class CustomerOrder extends HttpServlet {
    private IOrderService orderService = new OrderServiceImpl();
    private IReviewService reviewService = new ReviewServiceImpl();
    private String orderIdParam;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        int customerId = 48;

//        String customerIdParam = req.getParameter("id");
        if ("/customer/orders".equals(path)) {

             // Ví dụ, lấy từ session hoặc context xác thực
            //int customerId = Integer.parseInt(customerIdParam);
            // Gọi service để lấy danh sách đơn hàng của khách hàng
            List<OrderDTO> customerOrders = orderService.getOrdersByCustomerId(customerId);
            if (customerOrders != null && !customerOrders.isEmpty()) {
                req.setAttribute("customerOrders", customerOrders);
            } else {
                System.out.println("Lỗi: Không có đơn hàng");
                req.setAttribute("errorMessage", "Không có đơn hàng nào."); // Truyền thông báo lỗi

            }

            // Chuyển hướng đến trang hiển thị đơn hàng
            RequestDispatcher dispatcher = req.getRequestDispatcher("/view/customer/orderHistory.jsp");
            dispatcher.forward(req, resp);
        } else if ("/customer/orderDetails".equals(path)) {
            // Lấy ID đơn hàng từ tham số yêu cầu
               orderIdParam = req.getParameter("idOrder");


            if (orderIdParam != null) {
                int orderId = Integer.parseInt(orderIdParam);

                // Gọi lại service để lấy chi tiết đơn hàng từ database
                OrderDTO orderDetails = orderService.getOrderById(orderId);

                if (orderDetails != null) {
                    // Lấy thông tin review cho từng sản phẩm trong đơn hàng
                    for (OrderItemDTO item : orderDetails.getOrderItems()) {
                        int productId = item.getProductDTO().getProductId();
                        ReviewDTO reviewDTO = reviewService.getReviewsByProductId(productId); // Lấy review cho sản phẩm
                        item.getProductDTO().setReviewDTO(reviewDTO); // Gán review vào sản phẩm
                    }

                    req.setAttribute("orderDetails", orderDetails);
                    req.setAttribute("customerId", customerId);
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/view/customer/orderDetail.jsp");
                    dispatcher.forward(req, resp);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng");
                }
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = request.getParameter("action"); // Lấy tham số "action" trong request
            if ("add".equals(action)) {
                // Xử lý thêm đánh giá
                addReview(request, resp);
            } else if ("update".equals(action)) {
                // Xử lý cập nhật đánh giá
                updateReview(request, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Yêu cầu không hợp lệ.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
        }
    }
    public byte[] convertImageToBytes(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray(); // Trả về mảng byte
        }
    }
    private ReviewDTO prepareReviewDTO(String comment, String ratingValueStr, String productIdStr, String customerIdStr, int reviewId, HttpServletRequest request) throws IOException, ServletException {
        ReviewDTO reviewDTO = new ReviewDTO();
        CustomerDTO customerDTO = new CustomerDTO();
        ProductDTO productDTO = new ProductDTO();

        if (productIdStr != null && !productIdStr.isEmpty()) {
            productDTO.setProductId(Integer.parseInt(productIdStr));
        }

        if (customerIdStr != null && !customerIdStr.isEmpty()) {
            customerDTO.setUserID(Integer.parseInt(customerIdStr));
        }
        Date date;
        String dateStr = request.getParameter("date");
        if (dateStr == null || dateStr.isEmpty()) {
            // Chuyển đổi LocalDate sang Date
            date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            try {
                // Chuyển chuỗi từ request thành java.util.Date
                date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            } catch (Exception e) {
                return null;
            }
        }
        reviewDTO.setReviewID(reviewId);
        reviewDTO.setProductDTO(productDTO);
        reviewDTO.setCustomer(customerDTO);
        reviewDTO.setComment(comment);
        reviewDTO.setDate(date);

        if (ratingValueStr != null && !ratingValueStr.isEmpty()) {
            reviewDTO.setRatingValue(Integer.parseInt(ratingValueStr));
        }

        Part filePart = request.getPart("review-Image");
        if (filePart != null && filePart.getSize() > 0) {
            InputStream inputStream = filePart.getInputStream();
            reviewDTO.setImage(convertImageToBytes(inputStream));
        }

        return reviewDTO;
    }
    private void addReview(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String comment = request.getParameter("comment");
        String ratingValueStr = request.getParameter("rating");
        String productIdStr = request.getParameter("product-ID");
        String customerIdStr = request.getParameter("user-ID");



        ReviewDTO reviewDTO = prepareReviewDTO(comment, ratingValueStr, productIdStr, customerIdStr,0, request);

        ReviewServiceImpl reviewService = new ReviewServiceImpl();
        boolean isAdded = reviewService.addReview(reviewDTO);

        if (isAdded) {
            resp.sendRedirect(request.getContextPath() + "/customer/orderDetails?idOrder="+orderIdParam);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể thêm đánh giá.");
        }
    }
    private void updateReview(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String comment = request.getParameter("editComment");
        String ratingValueStr = request.getParameter("editRating");
        String productIdStr = request.getParameter("editProduct-ID");
        String customerIdStr = request.getParameter("editUser-ID");
        String reviewIdStr = request.getParameter("editReviewID");

        int reviewId = Integer.parseInt(reviewIdStr);

        ReviewDTO reviewDTO = prepareReviewDTO(comment, ratingValueStr, productIdStr, customerIdStr,reviewId, request);

        ReviewServiceImpl reviewService = new ReviewServiceImpl();
        boolean isUpdated = reviewService.updateReview(reviewDTO);

        if (isUpdated) {
            resp.sendRedirect(request.getContextPath() + "/customer/orderDetails?idOrder="+orderIdParam);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể cập nhật đánh giá.");
        }
    }



}
