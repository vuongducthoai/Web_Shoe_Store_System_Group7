package service.Impl;

import com.google.gson.Gson;
import dao.IProductDAO;
import dao.Impl.ProductDAOImpl;
import dto.CategoryDTO;
import dao.ICategoryDao;
import dao.Impl.CategoryDaoImpl;
import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;
import entity.Category;
import entity.Product;
import enums.DiscountType;
import enums.PromotionType;
import service.ICategoryService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryServiceImpl implements ICategoryService {
    ICategoryDao categoryDao = new CategoryDaoImpl();
    IProductDAO productService = new ProductDAOImpl();

    @Override
    public List<CategoryDTO> listCategory() {
        List<Category> list = categoryDao.categoryList();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : list) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryId(category.getCategoryID());
            categoryDTO.setCategoryName(category.getCategoryName());
            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }

    @Override
    public List<ProductDTO> findAllProductByCategoryWithPagination(int categoryId, int offset, int limit) {
        return categoryDao.findAllProductByCategoryWithPagination(categoryId, offset, limit);
    }

    @Override
    public List<Category> categoryList() {
        return List.of();
    }

    @Override
    public List<CategoryDTO> categoryDTOList() {
        return categoryDao.categoryDTOList();
    }

    @Override
    public void insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        categoryDao.insert(category);
    }

    @Override
    public boolean remove(int categoryId) {
        return false;
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
        return categoryDao.findAllCategories();
    }

    @Override
    public Map<String, Object> getFilteredAndSortedProducts(
            List<CategoryDTO> categoryDTOList,
            String selectedPromotion,
            String selectedCategory,
            String selectedSize,
            String selectedColor,
            String searchName,
            String minPriceParam,
            String maxPriceParam,
            String sortOption,
            String pageParam
    ) {
        // Get initial product data from the service
        Map<String, Object> productInfo = getProductInfo(categoryDTOList);
        Double minPrice = (Double) productInfo.get("minPrice");
        Double maxPrice = (Double) productInfo.get("maxPrice");
        List<Integer> sizes = (List<Integer>) productInfo.get("sizes");
        List<String> colors = (List<String>) productInfo.get("colors");
        List<String> promotions = (List<String>) productInfo.get("promotions");
        List<String> categories = (List<String>) productInfo.get("categories");
        Map<String, String> productNames = (Map<String, String>) productInfo.get("productNames");

        // Calculate filter min and max prices
        Double filterMinPrice = minPriceParam != null ? Double.parseDouble(minPriceParam) : minPrice;
        Double filterMaxPrice = maxPriceParam != null ? Double.parseDouble(maxPriceParam) : maxPrice;
        sortOption = (sortOption == null) ? "Phổ biến nhất" : sortOption;

        // Filter products based on parameters
        List<ProductDTO> filterProducts = filter(categoryDTOList, selectedCategory, filterMinPrice, filterMaxPrice, selectedColor, selectedSize, selectedPromotion, searchName);
        String jsonSoldQuantityMap = jsonGetSoldQuantities(categoryDTOList);
        String jsonGetAvgReviewMap = jsonGetAvgReview(categoryDTOList);
        filterProducts = distinctName(filterProducts);
        List<ProductDTO> sortProducts = sortProducts(categoryDTOList, filterProducts, sortOption);

        // Pagination logic
        int currentPage = Integer.parseInt(pageParam);
        int productsPerPage = 6;
        int startIndex = (currentPage - 1) * productsPerPage;
        int totalSize = (int) sortProducts.stream().filter(ProductDTO::isStatus).count();
        int endIndex = Math.min(startIndex + productsPerPage, totalSize);
        int totalPages = (int) Math.ceil((double) totalSize / productsPerPage);

        // Get paginated products for the current page
        String jsonCategoryList = jsonSaginatedProducts(sortProducts, startIndex, endIndex);

        // Prepare the data to return
        Map<String, Object> result = new HashMap<>();
        result.put("soldQuantityMapJson", jsonSoldQuantityMap);
        result.put("jsonGetAvgReviewMap", jsonGetAvgReviewMap);
        result.put("productsPerPage", productsPerPage);
        result.put("totalSize", totalSize);
        result.put("categoryListJson", jsonCategoryList);
        result.put("currentPage", currentPage);
        result.put("categories", categories);
        result.put("jsonProductNames", new Gson().toJson(productNames));
        result.put("promotions", promotions);
        result.put("minPrice", minPrice.intValue());
        result.put("maxPrice", maxPrice.intValue());
        result.put("totalPages", totalPages);
        result.put("sizeList", sizes);
        result.put("colorList", colors);
        result.put("selectedSize", selectedSize);
        result.put("selectedColor", selectedColor);
        result.put("selectedCategory", selectedCategory);
        result.put("selectedPromotion", selectedPromotion);
        result.put("filterMinPrice", filterMinPrice.intValue());
        result.put("filterMaxPrice", filterMaxPrice.intValue());
        result.put("sortOption", sortOption);
        result.put("searchName", searchName);

        return result;
    }


    private List<ProductDTO> distinctName(List<ProductDTO> products) {
        long currentTime = System.currentTimeMillis();  // Lấy thời gian hiện tại

        return products.stream()
                .filter(Objects::nonNull)  // Kiểm tra sản phẩm không null
                .filter(ProductDTO::isStatus)
                .collect(Collectors.toMap(
                        ProductDTO::getProductName,  // Sử dụng tên sản phẩm làm khóa
                        p -> p,  // Giá trị là chính sản phẩm
                        (existing, replacement) -> {
                            // Kiểm tra nếu cả hai sản phẩm đều có khuyến mãi đang hoạt động
                            boolean existingHasPromotion = existing.getPromotionProducts() != null &&
                                    existing.getPromotionProducts().stream()
                                            .filter(Objects::nonNull)
                                            .anyMatch(promotionProduct -> {
                                                PromotionDTO promotion = promotionProduct.getPromotion();
                                                return promotion != null &&
                                                        promotion.getStartDate() != null &&
                                                        promotion.getEndDate() != null &&
                                                        currentTime >= promotion.getStartDate().getTime() &&
                                                        currentTime <= promotion.getEndDate().getTime();
                                            });

                            boolean replacementHasPromotion = replacement.getPromotionProducts() != null &&
                                    replacement.getPromotionProducts().stream()
                                            .filter(Objects::nonNull)
                                            .anyMatch(promotionProduct -> {
                                                PromotionDTO promotion = promotionProduct.getPromotion();
                                                return promotion != null &&
                                                        promotion.getStartDate() != null &&
                                                        promotion.getEndDate() != null &&
                                                        currentTime >= promotion.getStartDate().getTime() &&
                                                        currentTime <= promotion.getEndDate().getTime();
                                            });

                            // Nếu cả hai đều có khuyến mãi đang hoạt động, chọn cái có số lượng khuyến mãi nhiều hơn
                            if (existingHasPromotion && replacementHasPromotion) {
                                return (replacement.getPromotionProducts().size() > existing.getPromotionProducts().size()) ? replacement : existing;
                            }

                            // Nếu chỉ có một trong hai cái có khuyến mãi đang hoạt động, chọn cái có khuyến mãi
                            if (replacementHasPromotion) {
                                return replacement;
                            } else {
                                return existing;
                            }
                        }
                ))
                .values()
                .stream()
                .toList();  // Chuyển thành danh sách (Java 16+)
    }



    private List<ProductDTO> filter(List<CategoryDTO> cartItemDTOList, String selectedCategory, Double filterMinPrice, Double filterMaxPrice, String selectedColor, String selectedSize, String selectedPromotion, String searchName) {
        return cartItemDTOList.stream()
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
                .toList();
    }

    private String jsonGetSoldQuantities(List<CategoryDTO> categories) {
        Map<String, Integer> soldQuantityMap = new HashMap<>();

        for (CategoryDTO category : categories) {
            if (category.getProductDTOList() != null) {
                for (ProductDTO product : category.getProductDTOList()) {
                    String productName = product.getProductName();

                    if (!soldQuantityMap.containsKey(productName)) {
                        soldQuantityMap.put(productName, 0);
                    }

                    // Giả sử product.getStatus() trả về true nếu chưa bán và false nếu đã bán
                    if (!product.isStatus()) {
                        soldQuantityMap.put(productName, soldQuantityMap.get(productName) + 1);
                    }
                }
            }
        }

        return  new Gson().toJson(soldQuantityMap);
    }

    private String jsonGetAvgReview(List<CategoryDTO> categories) {
        // Map để lưu tổng điểm đánh giá và số lượng đánh giá của từng sản phẩm
        Map<String, int[]> reviewStatsMap = new HashMap<>();

        for (CategoryDTO category : categories) {
            if (category.getProductDTOList() != null) {
                for (ProductDTO product : category.getProductDTOList()) {
                    String productName = product.getProductName();

                    // Nếu sản phẩm chưa tồn tại trong map, thêm mới với [tổng điểm, số lượng]
                    if (!reviewStatsMap.containsKey(productName)) {
                        reviewStatsMap.put(productName, new int[]{0, 0});
                    }

                    // Đảm bảo sản phẩm có đánh giá
                    if (product.getReviewDTO() != null) {
                        int ratingValue = product.getReviewDTO().getRatingValue();

                        // Cập nhật tổng điểm và số lượng đánh giá
                        int[] stats = reviewStatsMap.get(productName);
                        stats[0] += ratingValue; // Cộng tổng điểm
                        stats[1] += 1;          // Tăng số lượng đánh giá
                    }
                }
            }
        }

        // Tính điểm trung bình và lưu vào map mới
        Map<String, Double> avgReviewMap = new HashMap<>();
        for (Map.Entry<String, int[]> entry : reviewStatsMap.entrySet()) {
            String productName = entry.getKey();
            int[] stats = entry.getValue();
            double avgRating = stats[1] > 0 ? (double) stats[0] / stats[1] : 0.0; // Ép kiểu để phép chia đúng
            avgReviewMap.put(productName, avgRating);
        }

        // Chuyển kết quả thành JSON
        return new Gson().toJson(avgReviewMap);
    }

    private Map<String, Object> getProductInfo(List<CategoryDTO> cartItemDTOList) {
        Set<String> colors = new HashSet<>();  // Mảng màu sắc
        Set<Integer> sizes = new HashSet<>();   // Mảng kích thước giày
        Map<String, Integer> promotions = new HashMap<>();   // Mảng các chương trình khuyến mãi

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
                productNames.putIfAbsent(product.getProductName(), product.getBase64Image());

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
        List<Map.Entry<String, Integer>> sortedPromotions = promotions.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))  // Giảm dần theo phần trăm giảm giá
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

    private List<ProductDTO> sortProducts(List<CategoryDTO> categories, List<ProductDTO> filteredProducts, String sortOption) {
        // Sao chép danh sách vào một ArrayList để tránh lỗi khi gọi sort() trên danh sách immutable
        List<ProductDTO> sortableList = new ArrayList<>(filteredProducts);

        Map<String, Integer> soldQuantityMap = new HashMap<>();

        for (CategoryDTO category : categories) {
            if (category.getProductDTOList() != null) {
                for (ProductDTO product : category.getProductDTOList()) {
                    String productName = product.getProductName();

                    if (!soldQuantityMap.containsKey(productName)) {
                        soldQuantityMap.put(productName, 0);
                    }

                    // Giả sử product.getStatus() trả về true nếu chưa bán và false nếu đã bán
                    if (!product.isStatus()) {
                        soldQuantityMap.put(productName, soldQuantityMap.get(productName) + 1);
                    }
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
                // Sắp xếp theo tổng rating của các reviewDTOList (sản phẩm nào có tổng rating lớn nhất)
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

    private double calculateAverageRating(List<ProductDTO> productList, String productName) {
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

    private int comparePromotions(ProductDTO p1, ProductDTO p2) {
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

    private AbstractMap.SimpleEntry<Double, Long> calculatePromotionValue(ProductDTO productDTO, long currentTime) {
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

    private String jsonSaginatedProducts(List<ProductDTO> products, int startIndex, int endIndex){
        List<ProductDTO> paginatedProducts = new ArrayList<>();

        if (startIndex >= 0 && endIndex <= products.stream().filter(ProductDTO::isStatus).count() && startIndex < endIndex) {
            paginatedProducts = products.stream().filter(ProductDTO::isStatus).toList().subList(startIndex, endIndex);
        }

        return  new Gson().toJson(paginatedProducts);
    }
}
