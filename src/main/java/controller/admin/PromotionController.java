package controller.admin;

import com.google.gson.Gson;
import dto.PromotionDTO;
import enums.DiscountType;
import enums.PromotionType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import service.IProductPromotion;
import service.IProductService;
import service.IPromotionService;
import service.Impl.ProductPromotionImpl;
import service.Impl.ProductServiceImpl;
import service.Impl.PromotionServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@WebServlet(urlPatterns = {"/Admin/promotions/insert","/Admin/promotions/delete"})
public class PromotionController  extends HttpServlet {
    IPromotionService promotionService = new PromotionServiceImpl();
    IProductService productService = new ProductServiceImpl();
    IProductPromotion promotionProductService = new ProductPromotionImpl();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/Admin/promotions".equals(path)) {

            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            // Chuyển đổi chuỗi JSON thành JSONObject
            JSONObject jsonObject = new JSONObject(sb.toString());
            int promotionId = Integer.parseInt(jsonObject.getString("promotionId"));
                List<String> listNameProduct = productService.getListNameProductForPromotion(promotionId);
                Gson gson = new Gson();
                String jsonRespone = gson.toJson(listNameProduct);
                String encodedBase64 = Base64.getEncoder().encodeToString(jsonRespone.getBytes());
            resp.setContentType("application/json");
            resp.getWriter().write(encodedBase64);
        }
        else if ("/Admin/promotions/delete".equals(path))
        {
            String ID=  req.getParameter("promotion-id");
            int promotionId = Integer.parseInt(ID);
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.setPromotionId(promotionId);
            promotionDTO.setActive(false);
           boolean status= promotionService.updatePromotion(promotionDTO);
            if(!status)
            {
                req.getSession().setAttribute("message", "unsuccessful");
                req.getSession().setAttribute("messageType", "error");
            }
            else {
                req.getSession().setAttribute("message", "successful");
                req.getSession().setAttribute("messageType", "success");
            }
            resp.sendRedirect(req.getContextPath() + "/Admin");


        }
        else if ("/Admin/promotions/insert".equals(path)) {
            String promotionName = req.getParameter("promotion-name");
            String startDate = req.getParameter("start-date");
            String endDate = req.getParameter("end-date");
            String promotionType = req.getParameter("promotion-type");
            String discountUnit = req.getParameter("discount-unit");
            String[] selectedProducts = req.getParameterValues("selectedProducts");
            int discountValue = Integer.parseInt(req.getParameter("discount-value"));
            String minimumLoyalty= req.getParameter("minimum-loyalty");
            int loyalty;

            if (selectedProducts == null) {
                // Xử lý khi không có sản phẩm nào được chọn
                selectedProducts = new String[0]; // Khởi tạo một mảng rỗng nếu không có sản phẩm nào
            }
            List<String> productNames = Arrays.asList(selectedProducts);


            if (discountUnit.equals("%")) {
                discountUnit = "Percentage";
            } else discountUnit = "VND";
            // Lấy danh sách sản phẩm đã chọn
            if (promotionType.equals("voucher")) {
                promotionType = "VOUCHER_ORDER";
                loyalty= Integer.parseInt(minimumLoyalty);

            } else {
                promotionType = "VOUCHER_PRODUCT";
                loyalty = 0;
            }
            // Tạo PromotionDTO

            PromotionDTO promotion = new PromotionDTO();
            promotion.setPromotionName(promotionName);
            promotion.setStartDate(Date.valueOf(startDate));
            promotion.setEndDate(Date.valueOf(endDate));
            promotion.setPromotionType(PromotionType.valueOf(promotionType));
            promotion.setDiscountType(DiscountType.valueOf(discountUnit));
            promotion.setDiscountValue(discountValue);
            promotion.setMinimumLoyalty(loyalty);
            promotion.setActive(true);


            if (promotionType.equals("VOUCHER_PRODUCT")) {
                boolean checkDate = promotionProductService.CheckPromotionProduct(Date.valueOf(startDate), Date.valueOf(endDate), productNames);
                if (!checkDate) {
                    req.getSession().setAttribute("message", "Products that have been applied to other promotions");
                    req.getSession().setAttribute("messageType", "error");

                }
                else
                {
                    boolean statusPromotion = promotionProductService.InsertPromotionProduct(promotion, productNames);
                    if (!statusPromotion) {
                        req.getSession().setAttribute("messageType", "error");
                        req.getSession().setAttribute("message", "Product already exists");

                    } else {
                        req.getSession().setAttribute("messageType", "success");
                        req.getSession().setAttribute("message", "Success");

                    }
                }

            }
            if(promotionType.equals("VOUCHER_ORDER"))
            {
                boolean statusVoucher = promotionService.addPromotion(promotion);
                if(!statusVoucher)
                {
                    req.getSession().setAttribute("messageType", "error");
                    req.getSession().setAttribute("message", "Product already exists");

                }
                else
                {
                    req.getSession().setAttribute("messageType", "success");
                    req.getSession().setAttribute("message", "Success");

                }

            }
            resp.sendRedirect(req.getContextPath() + "/Admin");




        }


    }

    }

