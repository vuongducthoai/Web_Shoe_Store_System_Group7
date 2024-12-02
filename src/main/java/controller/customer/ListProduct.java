package controller.customer;

import java.awt.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.time.LocalDateTime;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonToken;
import java.time.format.DateTimeFormatter;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javax.imageio.ImageIO;

@WebServlet("/loadProducts")
public class ListProduct extends HttpServlet {
    ICategoryService iCategoryService = new CategoryServiceImpl();
    IProductService iProductService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String category = req.getParameter("category");
            int offset = Integer.parseInt(req.getParameter("offset"));
            int limit = Integer.parseInt(req.getParameter("limit"));

            List<ProductDTO> productDTOList;
            if ("all".equalsIgnoreCase(category)) {
                productDTOList = iProductService.findAllWithPagination(offset, limit);
            } else {
                try {
                    int categoryId = Integer.parseInt(category);
                    productDTOList = iCategoryService.findAllProductByCategoryWithPagination(categoryId, offset, limit);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("{\"error\":\"Invalid category ID\"}");
                    return;
                }
            }

            for (ProductDTO product : productDTOList) {
                if (product.getCreateDate() == null) {
                    product.setCreateDate(LocalDateTime.now());
                }

                if (product.getImage() != null && product.getImage().length > 0) {
//                    byte[] resizedImageBytes = resizePNG(product.getImage(), 50, 50);
                    String imageBase64 = encodeImage(product.getImage());
                    product.setImageBase64(imageBase64);
                }

            }

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            String jsonResponse = gson.toJson(productDTOList);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().println(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("{\"error\":\"Internal Server Error\"}");
        }
    }

//    public static byte[] resizePNG(byte[] originalImageBytes, int targetWidth, int targetHeight) throws IOException {
//        if (originalImageBytes == null || originalImageBytes.length == 0) {
//            throw new IOException("No image data present to read");
//        }
//
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(originalImageBytes);
//        BufferedImage originalImage = ImageIO.read(inputStream);
//
//        if (originalImage == null) {
//            throw new IOException("Failed to read image from byte array");
//        }
//
//        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
//        Graphics g2d = resizedImage.createGraphics();
//        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
//        g2d.dispose();
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ImageIO.write(resizedImage, "png", outputStream);
//
//        return outputStream.toByteArray();
//    }

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
