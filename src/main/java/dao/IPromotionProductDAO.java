package dao;

import dto.PromotionProductDTO;
import entity.PromotionProduct;

import java.util.List;

public interface IPromotionProductDAO {
    List<PromotionProductDTO> getAll ();
    boolean addPromotionProduct(PromotionProduct promotionProduct);
}
