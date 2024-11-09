package dto;

public class CustomerDTO {
    private Boolean active;
    private LocalDate dateOfBirth;
    private Integer loyalty;
    private Integer userId;
    private Integer addressId;
    private Integer cartCartId;
    private Integer chatId;

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getLoyalty() {
        return this.loyalty;
    }

    public void setLoyalty(Integer loyalty) {
        this.loyalty = loyalty;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return this.addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getCartCartId() {
        return this.cartCartId;
    }

    public void setCartCartId(Integer cartCartId) {
        this.cartCartId = cartCartId;
    }

    public Integer getChatId() {
        return this.chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
