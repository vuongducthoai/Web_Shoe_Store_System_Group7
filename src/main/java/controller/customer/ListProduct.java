package controller.customer;

import dto.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IProductService;
import service.Impl.ProductServiceImpl;
import java.util.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/customer/product/list", "/loadMoreProducts"})
public class ListProduct extends HttpServlet {
    private IProductService productService = new ProductServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if("/customer/product/list".equals(path)) {
            List<ProductDTO> productList = productService.findAll();
            req.setAttribute("listP", productList);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } else if("loadMoreProducts".equals(path)) {
//            int existingProductCount = Integer.parseInt(req.getParameter("exits"));
//            List<ProductDTO> moreProducts = productService.findMoreProducts(existingProductCount);
//            req.setAttribute("listP", moreProducts);
    //        req.getRequestDispatcher("/partials/productList.jsp").forward(req, resp);
        }

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
