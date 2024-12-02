package controller.customer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import dto.PromotionProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IProductPromotion;
import service.Impl.ProductPromotionImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@WebServlet("/Paging")
public class Paging extends HttpServlet {
    private final IProductPromotion productPromotion = new ProductPromotionImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Load Promotion
        LocalDate startDate = LocalDate.parse("2024-01-20");
        LocalDate endDate = LocalDate.parse("2025-12-30");
        int page = 8;
        int index = 1;
        int num = productPromotion.countProductPromotion(startDate, endDate);
//        int numpage = num / page;   //So luong trang
//        int num2 = num % page;
//
//        if(num != 0 && num2 != 0){
//            numpage++;
//        }

        try {
            index = Integer.parseInt(req.getParameter("index")); // index = 2
            System.out.println(index);
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        List<PromotionProductDTO> promotionProductDTOList = productPromotion.findTop8ProductPromotionNow(startDate, endDate, index - 1, page);
        for (PromotionProductDTO promotionProductDTO : promotionProductDTOList) {
            if (promotionProductDTO.getProduct().getCreateDate() == null) {
                promotionProductDTO.getProduct().setCreateDate(LocalDateTime.now());
            }

            if (promotionProductDTO.getProduct().getImage() != null && promotionProductDTO.getProduct().getImage().length > 0) {
                String imageBase64 = encodeImage(promotionProductDTO.getProduct().getImage());
                promotionProductDTO.getProduct().setImageBase64(imageBase64);
            }

        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new ListProduct.LocalDateTimeAdapter())
                .create();

        String jsonResponse = gson.toJson(promotionProductDTOList);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(jsonResponse);
//        req.setAttribute("promotionProductDTOList", promotionProductDTOList);
//        req.setAttribute("index", index);
//        req.setAttribute("numpage", numpage);
//        req.getRequestDispatcher("/home").forward(req, resp);
    }


    public static String encodeImage(byte[] imageBytes) {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.format(formatter));
            }
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return LocalDateTime.parse(in.nextString(), formatter);
        }
    }

}
