package service.Impl;

import dto.PromotionProductDTO;
import service.IProductPromotion;

import java.time.LocalDate;
import java.util.List;

public class ProductPromotionImpl implements IProductPromotion {
    private static final dao.IProductPromotion productPromotion = new dao.Impl.ProductPromotionImpl();
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
}
