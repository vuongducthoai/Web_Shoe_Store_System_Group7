let quantityCart = document.querySelector(".quantityItemCart");
let RenderQuantityCart = ()=>{
    try{
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
    })}
    catch (e) {
        quantityCart.textContent = 0
    }
}
let AddItemWithQuantity = (idProduct,Quantity)=>{
    console.log(idProduct+" ---- "+Quantity)
    $.ajax({
        url:'/AddCartQuantity',
        method:"POST",
        dataType: 'json', // Nhận dữ liệu ở định dạng JSON
        data:JSON.stringify({
            idProduct:idProduct,
            quantity:Quantity
        }),
        success: function (data) {
            console.log(data)
            if (data.errCode===1){
                custom_toast({
                    title: "Chúng tôi rất xin lỗi vì sự bất tiện",
                    message: data.message,
                    type: "error",
                    duration: 6000
                });
                return
            }
            if (data.errCode===2){
                window.location.href = "/view/login.jsp";
                return;
            }
            if (data.errCode === 0)
            {
                custom_toast({
                    title: "Thêm hàng thành công",
                    message: data.message,
                    type: "success",
                    duration: 6000
                });
                RenderQuantityCart();
                return
            }
        },
        error: function (error) {
            console.error("Error loading products:", error);
        }
    })
}
RenderQuantityCart();

///Test ham nay khong dung