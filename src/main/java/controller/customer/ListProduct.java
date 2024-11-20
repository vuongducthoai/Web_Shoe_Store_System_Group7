package controller.customer;

import java.util.*;

import com.google.gson.Gson;
import dto.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICategoryService;
import service.IProductService;
import service.Impl.CategoryServiceImpl;
import service.Impl.ProductServiceImpl;

import java.io.IOException;

@WebServlet("/loadProducts")
public class ListProduct extends HttpServlet {
    ICategoryService iCategoryService = new CategoryServiceImpl();
    IProductService iProductService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String category = req.getParameter("category");
        List<ProductDTO> productDTOList;
        int offset = Integer.parseInt(req.getParameter("offset"));
        int limit = Integer.parseInt(req.getParameter("limit"));
        if ("all".equalsIgnoreCase(category)) {
            productDTOList = iProductService.findAllWithPagination(offset, limit);
        } else {
            productDTOList = iCategoryService.findAllProductByCategoryWithPagination(Integer.parseInt(category), offset, limit);
        }

        for (ProductDTO product : productDTOList) {
            product.setImage(null);
        }

        // Chuyển đổi sang JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(productDTOList);

        // Trả về JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(jsonResponse);
    }

}
