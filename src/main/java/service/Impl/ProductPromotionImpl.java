package service.Impl;

import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;
import entity.PromotionProduct;
import enums.DiscountType;
import service.IProductPromotion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductPromotionImpl implements IProductPromotion {
    private static final dao.IProductPromotion productPromotion = new dao.Impl.ProductPromotionImpl();
    @Override
    public List<PromotionProductDTO> findTop5ProductPromotionNow(LocalDate startDate, LocalDate endDate) {
//        List<PromotionProductDTO> promotionProductDTOList = new ArrayList<>();
//        for(PromotionProduct promotionProduct : promotionProductList){
//            PromotionProductDTO promotionProductDTO = new PromotionProductDTO();
//
//            PromotionDTO promotionDTO = new PromotionDTO();
//            promotionDTO.setPromotionName(promotionProduct.getPromotion().getPromotionName());
//            promotionDTO.setDiscountValue(promotionProduct.getPromotion().getDiscountValue());
//            promotionDTO.setDiscountType(promotionProduct.getPromotion().getDiscountType());
//            promotionDTO.setEndDate(promotionProduct.getPromotion().getEndDate());
//            promotionDTO.setStartDate(promotionProduct.getPromotion().getStartDate());
//            promotionProductDTO.setPromotion(promotionDTO);
//
//            ProductDTO productDTO = new ProductDTO();
//            productDTO.setProductName(promotionProduct.getProduct().getProductName());
//            if(promotionProduct.getPromotion().getDiscountType().equals(DiscountType.VND)){
//                productDTO.setPrice(promotionProduct.getProduct().getPrice() - promotionProduct.getPromotion().getDiscountValue());
//            } else {
//                productDTO.setPrice(promotionProduct.getProduct().getPrice()*promotionProduct.getPromotion().getDiscountValue());
//            }
//            productDTO.setImage(promotionProduct.getProduct().getImage());
//            productDTO.setDescription(promotionProduct.getProduct().getDescription());
//            promotionProductDTO.setProduct(productDTO);
//            promotionProductDTOList.add(promotionProductDTO);
//        }
//        return promotionProductDTOList;
        return null;
    }
}
