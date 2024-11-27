let quantityCart = document.querySelector(".quantityItemCart");
let RenderQuantityCart = ()=>{
    $.ajax({
        url:'/Count',
        method:"POST",
        dataType: 'json', // Nhận dữ liệu ở định dạng JSON
        success: function (data) {
            console.log(data)
            quantityCart.textContent = data.quantityItemCart
        },
        error: function (error) {
            console.error("Error loading products:", error);
        }
    })
}
function redirectToCartPage() {
    window.location.href = "/Cart";  // Thay "link-to-cart-page" bằng URL thực tế
}
RenderQuantityCart();