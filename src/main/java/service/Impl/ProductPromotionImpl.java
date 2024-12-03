package service.Impl;

import dao.Impl.ProductDAOImpl;
import dao.Impl.PromotionDAOImpl;
import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;
import entity.Product;
import entity.Promotion;
import entity.PromotionProduct;
import jakarta.transaction.Transactional;
import service.IProductPromotion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductPromotionImpl implements IProductPromotion {
    private static final dao.IProductPromotion productPromotion = new dao.Impl.ProductPromotionImpl();
    private  final PromotionDAOImpl promotionDAO= new PromotionDAOImpl();
    private  final ProductDAOImpl productDAO   = new ProductDAOImpl();
    @Override
    public List<PromotionProductDTO> findTop8ProductPromotionNow(LocalDate startDate, LocalDate endDate, int offset, int limit) {
        return productPromotion.findTop8ProductPromotionNow(startDate, endDate, offset, limit);
    }
    public  PromotionProductDTO promotioOnProductInfo (String productName, int loyaty){
        return productPromotion.promotioOnProductInfo(productName, loyaty);
    }

    @Override
    public int countProductPromotion(LocalDate startDate, LocalDate endDate) {
        return productPromotion.countPromotion(startDate, endDate);
    }
    public  PromotionProductDTO promotioOnProductInfo (String productName){
        return productPromotion.promotioOnProductInfo(productName);
    }
    public boolean CheckPromotionProduct(Date start, Date end, List<String> productName) {
        List<PromotionProductDTO> promotionProductList = getAll();

        // Lặp qua tất cả các PromotionProduct
        for(String name :productName){
            for (PromotionProductDTO promotionProduct : promotionProductList) {


                if (name.equals(promotionProduct.getProduct().getProductName())) {
                    Date promotionStart = promotionProduct.getPromotion().getStartDate();
                    Date promotionEnd = promotionProduct.getPromotion().getEndDate();

                    if (!(end.before(promotionStart) || start.after(promotionEnd))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public List<ProductDTO> getProductByName(List<String> productName) {

        List<ProductDTO> listProduct=new ArrayList<>();
        for(String name:productName){

            listProduct.addAll(productDAO.findByNameProduct(name));
        }
        return listProduct;

    }
    @Transactional
    public boolean InsertPromotionProduct(PromotionDTO promotion, List<String> productName) {


        // Tạo entity Promotion từ PromotionDTO
        Promotion promotionEntity = new Promotion();
        promotionEntity.setPromotionName(promotion.getPromotionName());
        promotionEntity.setStartDate(promotion.getStartDate());
        promotionEntity.setEndDate(promotion.getEndDate());
        promotionEntity.setActive(promotion.isActive());
        promotionEntity.setDiscountType(promotion.getDiscountType());
        promotionEntity.setPromotionType(promotion.getPromotionType());
        promotionEntity.setDiscountValue(promotion.getDiscountValue());

        // Thêm promotion vào database
        boolean  check = promotionDAO.addPromotion(promotionEntity);
        if (!check) {
            return false; // Nếu thêm promotion thất bại, trả về false
        }
        // Lấy danh sách sản phẩm từ tên sản phẩm
        List<ProductDTO> list = getProductByName(productName);
        for (ProductDTO product : list) {
            Product productEntity = new Product();
            productEntity.setProductID(product.getProductId());

            // Tạo entity PromotionProduct
            PromotionProduct promotionProduct = new PromotionProduct();
            promotionProduct.setPromotion(promotionEntity);
            promotionProduct.setProduct(productEntity);

            // Thêm PromotionProduct vào database
            check = productPromotion.addPromotionProduct(promotionProduct);
            if (!check) {
                throw new RuntimeException("Failed to add PromotionProduct, rolling back"); // Nếu thêm PromotionProduct thất bại, trả về false
            }
        }

        // Nếu tất cả các bước thành công, trả về true
        return true;
    }
    public List<PromotionProductDTO> getAll()
    {
        return productPromotion.getAll();
    }

}
