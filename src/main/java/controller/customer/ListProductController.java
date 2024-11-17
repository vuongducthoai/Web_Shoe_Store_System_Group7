package controller.customer;

import com.google.gson.Gson;
import dto.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICategoryService;
import service.Impl.CategoryServiceImpl;

import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/customer/product/filter"})
public class ListProductController extends HttpServlet {
    ICategoryService categoryService = new CategoryServiceImpl();
    List<CategoryDTO> cartItemDTOList = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParam = req.getParameter("page");

        // Kiểm tra nếu tham số "page" không tồn tại hoặc rỗng
        if (pageParam == null || pageParam.isEmpty()) {
            // Chuyển hướng tới URL với tham số "page=1"
            String contextPath = req.getContextPath(); // Lấy context path
            String redirectURL = contextPath + "/customer/product/filter?page=1";
            resp.sendRedirect(redirectURL);
            return; // Dừng xử lý thêm vì đã chuyển hướng
        }

        if (cartItemDTOList == null) {
            cartItemDTOList = categoryService.findAllCategories();
        }

        // Lấy dữ liệu ban đầu
        Map<String, Object> productInfo = getProductInfo(cartItemDTOList);
        Double minPrice = (Double) productInfo.get("minPrice");
        Double maxPrice = (Double) productInfo.get("maxPrice");
        List<Integer> sizes = (List<Integer>) productInfo.get("sizes");
        List<String> colors = (List<String>) productInfo.get("colors");

        // Lấy các tham số lọc từ query parameters
        String selectedCategory = req.getParameter("selectedCategory");
        String selectedSize = req.getParameter("selectedSize");
        String selectedColor = req.getParameter("selectedColor");
        String minPriceParam = req.getParameter("minPrice");
        String maxPriceParam = req.getParameter("maxPrice");
        double filterMinPrice = minPriceParam != null ? Double.parseDouble(minPriceParam) : minPrice;
        double filterMaxPrice = maxPriceParam != null ? Double.parseDouble(maxPriceParam) : maxPrice;
        String sortOption = req.getParameter("sortOption") == null ? "Phổ biến nhất" : req.getParameter("sortOption");

        // Áp dụng logic lọc sản phẩm và thu thập tất cả ProductDTO từ các danh mục
        List<ProductDTO> filteredProducts = sortProducts(cartItemDTOList.stream()
                // 1. Lọc theo tên danh mục (nếu cần)
                .filter(c -> (selectedCategory == null || selectedCategory.equalsIgnoreCase("") || c.getCategoryName().equalsIgnoreCase(selectedCategory)))

                // 2. Lọc danh sách sản phẩm trong từng danh mục
                .flatMap(c -> c.getProductDTOList().stream()
                        // 3. Lọc theo kích cỡ, màu sắc, và giá
                        .filter(p -> (selectedSize == null || selectedSize.equalsIgnoreCase("") || p.getSize() == Integer.parseInt(selectedSize))  // Kiểm tra kích cỡ
                                && (selectedColor == null || selectedColor.equalsIgnoreCase("") || p.getColor().equalsIgnoreCase(selectedColor))  // Kiểm tra màu sắc
                                && (p.getPrice() >= filterMinPrice && p.getPrice() <= filterMaxPrice)  // Kiểm tra giá
                        )
                )
                // 4. Thu thập tất cả sản phẩm phù hợp
                .toList(), sortOption);


        // Phân trang
        int currentPage = Integer.parseInt(pageParam);
        int productsPerPage = 6;
        int startIndex = (currentPage - 1) * productsPerPage;
        int totalSize = filteredProducts.size();
        int endIndex = Math.min(startIndex + productsPerPage, totalSize);
        int totalPages = (int) Math.ceil((double) totalSize / productsPerPage);

        // Lấy danh sách sản phẩm cho trang hiện tại
        List<ProductDTO> paginatedProducts = filteredProducts.subList(startIndex, endIndex);

        // Chuyển đổi danh sách sản phẩm sang JSON
        Gson gson = new Gson();
        String jsonCategoryList = gson.toJson(paginatedProducts);

        // Gửi các thuộc tính vào request
        req.setAttribute("minSize", totalSize != 0 ? 1 : 0);
        req.setAttribute("totalSize", totalSize);
        req.setAttribute("categoryListJson", jsonCategoryList);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("categories", cartItemDTOList);
        req.setAttribute("minPrice", minPrice);
        req.setAttribute("maxPrice", maxPrice);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("sizeList", sizes);
        req.setAttribute("colorList", colors);

        // Gửi các tham số lọc vào request
        req.setAttribute("selectedSize", selectedSize);
        req.setAttribute("selectedColor", selectedColor);
        req.setAttribute("selectedCategory", selectedCategory);
        req.setAttribute("filterMinPrice", filterMinPrice);
        req.setAttribute("filterMaxPrice", filterMaxPrice);
        req.setAttribute("sortOption", sortOption);

        RequestDispatcher rq = req.getRequestDispatcher("/view/customer/filter-product.jsp");
        rq.forward(req, resp);
    }

    // Hàm xử lý lấy thông tin sản phẩm và các giá trị cần thiết
    private Map<String, Object> getProductInfo(List<CategoryDTO> cartItemDTOList) {
        Set<String> colors = new HashSet<>();  // Mảng màu sắc
        Set<Integer> sizes = new HashSet<>();   // Mảng kích thước giày
        double minPrice = Double.MAX_VALUE; // Giá trị min
        double maxPrice = Double.MIN_VALUE; // Giá trị max

        for (CategoryDTO category : cartItemDTOList) {
            for (ProductDTO product : category.getProductDTOList()) {
                // Lưu màu sắc và kích thước vào mảng
                colors.add(product.getColor());
                sizes.add(product.getSize());

                // Cập nhật min và max giá
                double price = product.getPrice();
                if (price < minPrice) {
                    minPrice = price;
                }
                if (price > maxPrice) {
                    maxPrice = price;
                }
            }
        }

        // Tạo một Map để trả về các giá trị
        Map<String, Object> productInfo = new HashMap<>();
        productInfo.put("colors", new ArrayList<>(colors));  // Chuyển Set thành List
        productInfo.put("sizes", new ArrayList<>(sizes));    // Chuyển Set thành List
        productInfo.put("minPrice", minPrice);               // Giá trị min
        productInfo.put("maxPrice", maxPrice);               // Giá trị max

        return productInfo;
    }

    public List<ProductDTO> sortProducts(List<ProductDTO> filteredProducts, String sortOption) {
        // Sao chép danh sách vào một ArrayList để tránh lỗi khi gọi sort() trên danh sách immutable
        List<ProductDTO> sortableList = new ArrayList<>(filteredProducts);

        switch (sortOption) {
            case "Phổ biến nhất":
                // Sắp xếp theo tổng rating của các reviewDTOList (sản phẩm nào có tổng rating lớn nhất)
                sortableList.sort((p1, p2) -> {
                    double ratingSum1 = (p1.getReviewDTOList() != null) ? p1.getReviewDTOList().stream()
                            .mapToDouble(ReviewDTO::getRatingValue)
                            .sum() : 0;
                    double ratingSum2 = (p2.getReviewDTOList() != null) ? p2.getReviewDTOList().stream()
                            .mapToDouble(ReviewDTO::getRatingValue)
                            .sum() : 0;
                    return Double.compare(ratingSum2, ratingSum1); // Sắp xếp theo tổng rating, giảm dần
                });
                break;

            case "Giá: Thấp đến Cao":
                // Sắp xếp theo giá từ thấp đến cao, sau đó theo rating và giảm giá
                sortableList.sort((p1, p2) -> {
                    double price1 = p1.getPrice();
                    double price2 = p2.getPrice();

                    // So sánh giá
                    if (Double.compare(price1, price2) == 0) {
                        // Nếu giá bằng nhau, sắp xếp theo tổng rating của các review
                        double ratingSum1 = (p1.getReviewDTOList() != null) ? p1.getReviewDTOList().stream()
                                .mapToDouble(ReviewDTO::getRatingValue)
                                .sum() : 0;
                        double ratingSum2 = (p2.getReviewDTOList() != null) ? p2.getReviewDTOList().stream()
                                .mapToDouble(ReviewDTO::getRatingValue)
                                .sum() : 0;

                        if (Double.compare(ratingSum2, ratingSum1) == 0) {
                            // Nếu tổng rating bằng nhau, sắp xếp theo giảm giá (ưu đãi)
                            double discount1 = (p1.getPromotionDTO() != null) ? p1.getPromotionDTO().getDiscountValue() : 0;
                            double discount2 = (p2.getPromotionDTO() != null) ? p2.getPromotionDTO().getDiscountValue() : 0;
                            return Double.compare(discount1, discount2);  // Giảm giá cao hơn sẽ được ưu tiên
                        }
                        return Double.compare(ratingSum2, ratingSum1);  // Tổng rating cao hơn sẽ được ưu tiên
                    }
                    return Double.compare(price1, price2);  // Sắp xếp theo giá từ thấp đến cao
                });
                break;

            case "Giá: Cao đến thấp":
                // Sắp xếp theo giá từ thấp đến cao, sau đó theo rating và giảm giá
                sortableList.sort((p1, p2) -> {
                    double price1 = p1.getPrice();
                    double price2 = p2.getPrice();

                    // So sánh giá
                    if (Double.compare(price1, price2) == 0) {
                        // Nếu giá bằng nhau, sắp xếp theo tổng rating của các review
                        double ratingSum1 = (p1.getReviewDTOList() != null) ? p1.getReviewDTOList().stream()
                                .mapToDouble(ReviewDTO::getRatingValue)
                                .sum() : 0;
                        double ratingSum2 = (p2.getReviewDTOList() != null) ? p2.getReviewDTOList().stream()
                                .mapToDouble(ReviewDTO::getRatingValue)
                                .sum() : 0;

                        if (Double.compare(ratingSum2, ratingSum1) == 0) {
                            // Nếu tổng rating bằng nhau, sắp xếp theo giảm giá (ưu đãi)
                            double discount1 = (p1.getPromotionDTO() != null) ? p1.getPromotionDTO().getDiscountValue() : 0;
                            double discount2 = (p2.getPromotionDTO() != null) ? p2.getPromotionDTO().getDiscountValue() : 0;
                            return Double.compare(discount2, discount1);  // Giảm giá cao hơn sẽ được ưu tiên
                        }
                        return Double.compare(ratingSum1, ratingSum2);  // Tổng rating cao hơn sẽ được ưu tiên
                    }
                    return Double.compare(price1, price2);  // Sắp xếp theo giá từ thấp đến cao
                });
                //Từ cao đến thấp
                sortableList = sortableList.reversed();
                break;

            case "Ưu đãi hấp dẫn nhất":
                // Sắp xếp theo % giảm giá từ cao đến thấp
                sortableList.sort((p1, p2) -> {
                    double discount1 = (p1.getPromotionDTO() != null) ? p1.getPromotionDTO().getDiscountValue() : 0;
                    double discount2 = (p2.getPromotionDTO() != null) ? p2.getPromotionDTO().getDiscountValue() : 0;
                    return Double.compare(discount2, discount1);  // Giảm giá cao hơn sẽ được ưu tiên
                });
                break;

            default:
                // Nếu không có lựa chọn phù hợp, giữ nguyên danh sách
                break;
        }
        return sortableList;
    }
}
