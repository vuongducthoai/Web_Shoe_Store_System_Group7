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

        if (pageParam == null || pageParam.isEmpty()) {
            String contextPath = req.getContextPath(); // Lấy context path
            String redirectURL = contextPath + "/customer/product/filter?page=1";
            resp.sendRedirect(redirectURL);
            return;
        }

        if (cartItemDTOList == null) {
            cartItemDTOList = categoryService.findAllCategories();
        }

        // Lấy dữ liệu ban đầu
        Map<String, Object> productInfo = categoryService.getProductInfo(cartItemDTOList);
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


        List<ProductDTO> filterProducts = categoryService.filter(cartItemDTOList, selectedCategory,  filterMinPrice, filterMaxPrice, selectedColor, selectedSize, selectedPromotion, searchName);
        String jsonSoldQuantityMap = categoryService.jsonGetSoldQuantities(cartItemDTOList);
        filterProducts = categoryService.distinctName(filterProducts);
        List<ProductDTO> sortProducts = categoryService.sortProducts(filterProducts, sortOption);

        // Phân trang
        int currentPage = Integer.parseInt(pageParam);
        int productsPerPage = 6;
        int startIndex = (currentPage - 1) * productsPerPage;
        int totalSize = (int)sortProducts.stream().filter(ProductDTO::isStatus).count();
        int endIndex = Math.min(startIndex + productsPerPage, totalSize);
        int totalPages = (int) Math.ceil((double) totalSize / productsPerPage);

        // Lấy danh sách sản phẩm cho trang hiện tại
        String jsonCategoryList = categoryService.jsonSaginatedProducts(sortProducts, startIndex, endIndex); new ArrayList<>();
        String jsonProductNames = new Gson().toJson(productNames);


        req.setAttribute("soldQuantityMapJson", jsonSoldQuantityMap);
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
}
