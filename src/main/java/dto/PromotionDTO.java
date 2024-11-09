package dto;

public class PromotionDTO {
    private Integer promotionId;
    private String discountType;
    private Double discountValue;
    private LocalDateTime endDate;
    private Boolean isActive;
    private Integer minimumLoyalty;
    private String promotionName;
    private LocalDateTime startDate;

    public Integer getPromotionId() {
        return this.promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return this.discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMinimumLoyalty() {
        return this.minimumLoyalty;
    }

    public void setMinimumLoyalty(Integer minimumLoyalty) {
        this.minimumLoyalty = minimumLoyalty;
    }

    public String getPromotionName() {
        return this.promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
