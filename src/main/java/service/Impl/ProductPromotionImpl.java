package service.Impl;

import dto.PromotionProductDTO;
import service.IProductPromotion;

import java.time.LocalDate;
import java.util.List;

public class ProductPromotionImpl implements IProductPromotion {
    private static final dao.IProductPromotion productPromotion = new dao.Impl.ProductPromotionImpl();
    @Override
    public List<PromotionProductDTO> findTop5ProductPromotionNow(LocalDate startDate, LocalDate endDate) {
        return productPromotion.findTop5ProductPromotionNow(startDate, endDate);
    }
    public  PromotionProductDTO promotioOnProductInfo (String productName){
        return productPromotion.promotioOnProductInfo(productName);
    }
}
