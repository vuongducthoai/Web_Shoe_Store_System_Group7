package controller.customer;

import com.google.gson.Gson;
import dto.*;
import enums.DiscountType;
import enums.PromotionType;
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
import java.util.stream.Stream;

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
//            cartItemDTOList = new ArrayList<>();
        }

        // Lấy dữ liệu ban đầu
        Map<String, Object> productInfo = getProductInfo(cartItemDTOList);
        Double minPrice = (Double) productInfo.get("minPrice");
        Double maxPrice = (Double) productInfo.get("maxPrice");
        List<Integer> sizes = (List<Integer>) productInfo.get("sizes");
        List<String> colors = (List<String>) productInfo.get("colors");
        List<String> promotions = (List<String>) productInfo.get("promotions");
        List<String> categories = (List<String>) productInfo.get("categories");
        Map<String, String> productNames = (Map<String, String>) productInfo.get("productNames");

        // Lấy các tham số lọc từ query parameters
        String selectedPromotion = req.getParameter("selectedPromotion");
        String selectedCategory = req.getParameter("selectedCategory");
        String selectedSize = req.getParameter("selectedSize");
        String selectedColor = req.getParameter("selectedColor");
        String searchName = req.getParameter("searchName");
        String minPriceParam = req.getParameter("minPrice");
        String maxPriceParam = req.getParameter("maxPrice");
        double filterMinPrice = minPriceParam != null ? Double.parseDouble(minPriceParam) : minPrice;
        double filterMaxPrice = maxPriceParam != null ? Double.parseDouble(maxPriceParam) : maxPrice;
        String sortOption = req.getParameter("sortOption") == null ? "Phổ biến nhất" : req.getParameter("sortOption");

        // Áp dụng logic lọc sản phẩm và thu thập tất cả ProductDTO từ các danh mục
        List<ProductDTO> filteredProducts = sortProducts(
                cartItemDTOList.stream()
                        // 1. Lọc theo tên danh mục (nếu cần)
                        .filter(c -> (selectedCategory == null || selectedCategory.equalsIgnoreCase("") || c.getCategoryName() != null && c.getCategoryName().equalsIgnoreCase(selectedCategory)))

                        // 2. Lọc danh sách sản phẩm trong từng danh mục
                        .flatMap(c -> c.getProductDTOList().stream()
                                // 3. Lọc theo tên, khuyến mãi, kích cỡ, màu sắc, và giá
                                .filter(p ->
                                        (searchName == null || searchName.equalsIgnoreCase("") || (p.getProductName() != null && searchName.equalsIgnoreCase(p.getProductName())))
                                                && (selectedPromotion == null || selectedPromotion.equalsIgnoreCase("") ||
                                                (p.getPromotionProducts() != null && p.getPromotionProducts().stream().anyMatch(promotionProduct ->
                                                        promotionProduct.getPromotion() != null &&
                                                                promotionProduct.getPromotion().getPromotionType() == PromotionType.VOUCHER_PRODUCT &&
                                                                promotionProduct.getPromotion().getPromotionName() != null &&
                                                                promotionProduct.getPromotion().getPromotionName().equalsIgnoreCase(selectedPromotion) &&
                                                                promotionProduct.getPromotion().isActive() &&
                                                                promotionProduct.getPromotion().getStartDate().getTime() <= System.currentTimeMillis() &&
                                                                promotionProduct.getPromotion().getEndDate().getTime() >= System.currentTimeMillis()
                                                )))
                                                && (selectedSize == null || selectedSize.equalsIgnoreCase("") ||
                                                (p.getSize() > 0 && p.getSize() == Integer.parseInt(selectedSize)))  // Kiểm tra kích cỡ
                                                && (selectedColor == null || selectedColor.equalsIgnoreCase("") ||
                                                (p.getColor() != null && p.getColor().equalsIgnoreCase(selectedColor)))  // Kiểm tra màu sắc
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
        String jsonProductNames = gson.toJson(productNames);

        // Gửi các thuộc tính vào request
        req.setAttribute("productsPerPage", productsPerPage);
        req.setAttribute("minSize", totalSize != 0 ? 1 : 0);
        req.setAttribute("totalSize", totalSize);
        req.setAttribute("categoryListJson", jsonCategoryList);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("categories", categories);
        req.setAttribute("jsonProductNames", jsonProductNames);
        req.setAttribute("promotions", promotions);
        req.setAttribute("minPrice", minPrice);
        req.setAttribute("maxPrice", maxPrice);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("sizeList", sizes);
        req.setAttribute("colorList", colors);

        // Gửi các tham số lọc vào request
        req.setAttribute("selectedSize", selectedSize);
        req.setAttribute("selectedColor", selectedColor);
        req.setAttribute("selectedCategory", selectedCategory);
        req.setAttribute("selectedPromotion", selectedPromotion);
        req.setAttribute("filterMinPrice", filterMinPrice);
        req.setAttribute("filterMaxPrice", filterMaxPrice);
        req.setAttribute("sortOption", sortOption);
        req.setAttribute("searchName", searchName);

        RequestDispatcher rq = req.getRequestDispatcher("/view/customer/filter-product.jsp");
        rq.forward(req, resp);
    }

    private Map<String, Object> getProductInfo(List<CategoryDTO> cartItemDTOList) {
        Set<String> colors = new HashSet<>();  // Mảng màu sắc
        Set<Integer> sizes = new HashSet<>();   // Mảng kích thước giày
        Map<String, Double> promotions = new HashMap<>();   // Mảng các chương trình khuyến mãi

        double minPrice = Double.MAX_VALUE; // Giá trị min
        double maxPrice = Double.MIN_VALUE; // Giá trị max

        // Đếm số lượng sản phẩm trong mỗi danh mục
        Map<String, Integer> categoryProductCount = new HashMap<>();

        // Set để lưu tất cả các tên sản phẩm (không trùng lặp)
        Map<String, String> productNames = new HashMap<>();

        for (CategoryDTO category : cartItemDTOList) {
            int productCount = category.getProductDTOList().size();  // Đếm số sản phẩm trong danh mục
            if (productCount > 0) {  // Loại bỏ danh mục có 0 sản phẩm
                categoryProductCount.put(category.getCategoryName(), productCount);
            }

            for (ProductDTO product : category.getProductDTOList()) {
                productNames.putIfAbsent(product.getProductName(), product.getImageBase64());

                // Lưu màu sắc và kích thước vào mảng
                colors.add(product.getColor());
                sizes.add(product.getSize());

                if (product.getPromotionProducts() != null) {  // Kiểm tra danh sách promotionProducts
                    for (PromotionProductDTO promotionProduct : product.getPromotionProducts()) {
                        PromotionDTO promotion = promotionProduct.getPromotion();
                        if (promotion != null && promotion.isActive() && promotion.getPromotionType() == PromotionType.VOUCHER_PRODUCT) {
                            long currentTime = System.currentTimeMillis();  // Thời gian hiện tại
                            long startDate = promotion.getStartDate().getTime();
                            long endDate = promotion.getEndDate().getTime();

                            // Kiểm tra xem chương trình khuyến mãi có còn áp dụng không
                            if (currentTime >= startDate && currentTime <= endDate && promotion.isActive()) {
                                // Lưu tên chương trình và phần trăm giảm giá vào Map
                                promotions.put(promotion.getPromotionName(), promotion.getDiscountValue());
                            }
                        }
                    }
                }


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

        // Sắp xếp danh mục theo số lượng sản phẩm giảm dần
        List<Map.Entry<String, Integer>> sortedCategoryList = categoryProductCount.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue())) // Giảm dần theo số lượng sản phẩm
                .toList();

        // Chuyển danh sách đã sắp xếp thành danh sách tên danh mục
        List<String> sortedCategoryNames = sortedCategoryList.stream()
                .map(Map.Entry::getKey) // Lấy tên danh mục
                .toList();

        // Sắp xếp các chương trình khuyến mãi theo mức độ giảm giá (giảm dần)
        List<Map.Entry<String, Double>> sortedPromotions = promotions.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))  // Giảm dần theo phần trăm giảm giá
                .toList();

        // Tạo danh sách đã sắp xếp từ Set
        List<String> sortedColors = colors.stream().sorted().toList(); // Sắp xếp màu sắc tăng dần (String)
        List<Integer> sortedSizes = sizes.stream().sorted().toList(); // Sắp xếp kích thước tăng dần (Integer)

        // Chuyển danh sách promotion thành danh sách tên chương trình khuyến mãi
        List<String> sortedPromotionNames = sortedPromotions.stream()
                .map(Map.Entry::getKey) // Lấy tên chương trình khuyến mãi
                .toList();

        // Tạo một Map để trả về các giá trị
        Map<String, Object> productInfo = new HashMap<>();
        productInfo.put("colors", sortedColors); // Danh sách màu đã sắp xếp
        productInfo.put("sizes", sortedSizes);   // Danh sách kích thước đã sắp xếp
        productInfo.put("promotions", sortedPromotionNames);  // Danh sách chương trình khuyến mãi đã sắp xếp
        productInfo.put("categories", sortedCategoryNames);  // Danh sách danh mục đã sắp xếp theo số lượng sản phẩm
        productInfo.put("minPrice", minPrice);   // Giá trị min
        productInfo.put("maxPrice", maxPrice);   // Giá trị max

        // Chuyển Set sản phẩm thành chuỗi

        productInfo.put("productNames", productNames);  // Thêm danh sách tên sản phẩm vào map

        return productInfo;
    }

    public List<ProductDTO> sortProducts(List<ProductDTO> filteredProducts, String sortOption) {
        // Sao chép danh sách vào một ArrayList để tránh lỗi khi gọi sort() trên danh sách immutable
        List<ProductDTO> sortableList = new ArrayList<>(filteredProducts);
        Map<String, Integer> soldQuantityMap = new HashMap<>();

        for (ProductDTO product : sortableList) {
            String productName = product.getProductName();

            if (soldQuantityMap.containsKey(productName)) {
                if (!product.isStatus()) {
                    soldQuantityMap.put(productName, soldQuantityMap.get(productName) + 1);
                }
            } else {
                if (!product.isStatus()) {
                    soldQuantityMap.put(productName, 1);
                } else {
                    soldQuantityMap.put(productName, 0);
                }
            }
        }

        switch (sortOption) {
            case "Phổ biến nhất":
                // Sắp xếp theo tổng rating của các reviewDTOList (sản phẩm nào có tổng rating lớn nhất)
                sortableList.sort((p1, p2) -> {
                    int purchased1 = soldQuantityMap.get(p1.getProductName());
                    int purchased2 = soldQuantityMap.get(p2.getProductName());

                    if (purchased2 != purchased1) {
                        return Integer.compare(purchased2, purchased1);
                    }

                    int comparePromotions = comparePromotions(p1, p2);
                    if (comparePromotions != 0) {
                        return comparePromotions;
                    }

                    double ratingSum1 = calculateAverageRating(sortableList, p1.getProductName());
                    double ratingSum2 = calculateAverageRating(sortableList, p2.getProductName());

                    if (Double.compare(ratingSum2, ratingSum1) != 0) {
                        return Double.compare(ratingSum2, ratingSum1);
                    }

                    return p1.getProductName().compareToIgnoreCase(p2.getProductName());
                });
                break;

            case "Giá: Thấp đến Cao":
                // Sắp xếp theo giá tăng dần, sau đó số lượng đã bán giảm dần, giảm giá giảm dần, tổng rating giảm dần và cuối cùng tên A-Z
                sortableList.sort((p1, p2) -> {
                    // Lấy giá sản phẩm
                    double price1 = p1.calculateDiscountedPrice();
                    double price2 = p2.calculateDiscountedPrice();

                    // So sánh giá
                    if (Double.compare(price1, price2) != 0) {
                        return Double.compare(price1, price2); // Sắp xếp giá tăng dần
                    }

                    // Lấy số lượng đã bán
                    int soldQuantity1 = soldQuantityMap.getOrDefault(p1.getProductName(), 0);
                    int soldQuantity2 = soldQuantityMap.getOrDefault(p2.getProductName(), 0);

                    // So sánh số lượng đã bán
                    if (soldQuantity2 != soldQuantity1) {
                        return Integer.compare(soldQuantity2, soldQuantity1);
                    }

                    int comparePromotions = comparePromotions(p1, p2);
                    if (comparePromotions != 0) {
                        return comparePromotions;
                    }
                    // Lấy tổng đánh giá
                    double ratingSum1 = calculateAverageRating(sortableList, p1.getProductName());
                    double ratingSum2 = calculateAverageRating(sortableList, p2.getProductName());

                    // So sánh tổng đánh giá
                    if (Double.compare(ratingSum2, ratingSum1) != 0) {
                        return Double.compare(ratingSum2, ratingSum1);
                    }

                    // Cuối cùng, sắp xếp theo tên sản phẩm A-Z
                    return p1.getProductName().compareToIgnoreCase(p2.getProductName());
                });
                break;

            case "Giá: Cao đến thấp":
                // Sắp xếp theo giá giảm dần, sau đó số lượng đã bán giảm dần, giảm giá giảm dần, tổng rating giảm dần và cuối cùng tên A-Z
                sortableList.sort((p1, p2) -> {
                    // Lấy giá sản phẩm
                    double price1 = p1.calculateDiscountedPrice();
                    double price2 = p2.calculateDiscountedPrice();

                    if (Double.compare(price2, price1) != 0) {
                        return Double.compare(price2, price1); // Sắp xếp giá giảm dần
                    }

                    // Lấy số lượng đã bán
                    int soldQuantity1 = soldQuantityMap.getOrDefault(p1.getProductName(), 0);
                    int soldQuantity2 = soldQuantityMap.getOrDefault(p2.getProductName(), 0);

                    // So sánh số lượng đã bán
                    if (soldQuantity2 != soldQuantity1) {
                        return Integer.compare(soldQuantity2, soldQuantity1);
                    }

                    int comparePromotions = comparePromotions(p1, p2);
                    if (comparePromotions != 0) {
                        return comparePromotions;
                    }

                    // Lấy tổng đánh giá
                    double ratingSum1 = calculateAverageRating(sortableList, p1.getProductName());
                    double ratingSum2 = calculateAverageRating(sortableList, p2.getProductName());

                    // So sánh tổng đánh giá
                    if (Double.compare(ratingSum2, ratingSum1) != 0) {
                        return Double.compare(ratingSum2, ratingSum1);
                    }

                    // Cuối cùng, sắp xếp theo tên sản phẩm A-Z
                    return p1.getProductName().compareToIgnoreCase(p2.getProductName());
                });
                break;

            case "Ưu đãi hấp dẫn nhất":
                // Sắp xếp theo % giảm giá từ cao đến thấp
                sortableList.sort((p1, p2) -> {

                    int comparePromotions = comparePromotions(p1, p2);
                    if (comparePromotions != 0) {
                        return comparePromotions;
                    }

                    int purchased1 = soldQuantityMap.get(p1.getProductName());
                    int purchased2 = soldQuantityMap.get(p2.getProductName());

                    if (purchased2 != purchased1) {
                        return Integer.compare(purchased2, purchased1);
                    }


                    double ratingSum1 = calculateAverageRating(sortableList, p1.getProductName());
                    double ratingSum2 = calculateAverageRating(sortableList, p2.getProductName());

                    if (Double.compare(ratingSum2, ratingSum1) != 0) {
                        return Double.compare(ratingSum2, ratingSum1);
                    }

                    return p1.getProductName().compareToIgnoreCase(p2.getProductName());
                });
                break;
        }
        return sortableList;
    }

    public double calculateAverageRating(List<ProductDTO> productList, String productName) {
        // Lọc các sản phẩm có cùng tên với `productName` và kiểm tra nếu reviewDTOList không phải null
        return productList.stream()
                .filter(product -> product != null && product.getProductName() != null && product.getProductName().equalsIgnoreCase(productName))
                .flatMap(product -> {
                    if (product.getReviewDTO() != null && product.getReviewDTO().getRatingValue() != 0) {
                        // Trả về Stream chứa giá trị ratingValue từ ReviewDTO
                        return Stream.of(product.getReviewDTO().getRatingValue());
                    }
                    return Stream.empty(); // Trả về Stream rỗng nếu không có ReviewDTO hoặc đánh giá không hợp lệ
                })
                .mapToInt(Integer::intValue) // Chuyển đổi Stream thành IntStream để tính toán
                .average() // Tính trung bình
                .orElse(0.0); // Nếu không có giá trị nào thì trả về 0.0
    }

    public int comparePromotions(ProductDTO p1, ProductDTO p2) {
        long currentTime = System.currentTimeMillis(); // Chỉ gọi một lần
        AbstractMap.SimpleEntry<Double, Long> result1 = calculatePromotionValue(p1, currentTime);
        AbstractMap.SimpleEntry<Double, Long> result2 = calculatePromotionValue(p2, currentTime);

        double discountValue1 = result1.getKey();
        long timeRemaining1 = result1.getValue();
        double discountValue2 = result2.getKey();
        long timeRemaining2 = result2.getValue();

        // So sánh các mức giảm giá
        if (Double.compare(discountValue2, discountValue1) != 0) {
            return Double.compare(discountValue2, discountValue1);
        }

        // Nếu mức giảm giá bằng nhau, so sánh thời gian còn lại
        if (timeRemaining1 != timeRemaining2) {
            return Long.compare(timeRemaining1, timeRemaining2);
        }

        return 0;
    }

    public AbstractMap.SimpleEntry<Double, Long> calculatePromotionValue(ProductDTO productDTO, long currentTime) {
        double discountValue = 0;
        long timeRemaining = Long.MAX_VALUE;

        if (productDTO.getPromotionProducts() != null) {
            for (PromotionProductDTO promotionProduct : productDTO.getPromotionProducts()) {
                PromotionDTO promotion = promotionProduct.getPromotion();
                if (promotion != null && promotion.getPromotionType() == PromotionType.VOUCHER_PRODUCT &&
                        promotion.getStartDate() != null && promotion.getEndDate() != null) {

                    // Check if the promotion is active
                    if (currentTime >= promotion.getStartDate().getTime() &&
                            currentTime <= promotion.getEndDate().getTime() &&
                            promotion.isActive()) {
                        if (promotion.getDiscountType() == DiscountType.Percentage) {
                            discountValue = productDTO.getPrice() * (promotion.getDiscountValue() / 100.0);
                        } else if (promotion.getDiscountType() == DiscountType.VND) {
                            discountValue = promotion.getDiscountValue();
                        }
                        timeRemaining = promotion.getEndDate().getTime() - currentTime;
                        break; // Take the first valid promotion
                    }
                }
            }
        }

        return new AbstractMap.SimpleEntry<>(discountValue, timeRemaining);
    }


}
