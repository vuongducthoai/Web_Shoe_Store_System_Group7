

var images = document.querySelectorAll(".product-images-item")
var imageFocus = document.querySelector(".product-image-focus")

images.forEach(image =>{
    image.onclick = () =>{
        imageFocus.src = image.src

        images.forEach(image =>image.classList.remove("border"))
        if (!image.classList.contains("border") || !image.classList.contains("border-3")) {
            image.classList.add("border", "border-3");
        }

    }
})

var circles = document.querySelectorAll(".circle");
circles.forEach(circle => {
    circle.onclick = () => {
        console.log("akakakak")
        circles.forEach(c => c.style.border = "none");
        circle.style.border = "4px solid #ccc";
    };
});

var sizes = document.querySelectorAll(".size");
sizes.forEach(size => {
    size.onclick =  () => {
        sizes.forEach(s => s.style.border = "none")

        size.style.border = "3px solid black";
    }
})

var fa_plus = document.querySelector(".fa-plus");
var fa_minus = document.querySelector(".fa-minus");
var quantity = document.querySelector(".quantity");

fa_plus.onclick = () => {
    var qual = Number(quantity.innerText) +1;
    quantity.innerText = qual.toString();
}
fa_minus.onclick = () => {
    var qual = Number(quantity.innerText) -1;
    if(qual <= 0 ) {
        custom_toast({
            title: "Cảnh báo",
            message: "Số lượng sản phẩm phải lớn hơn 1.",
            type: "warning",
            duration: 6000
        });
        return;
    }
    else
        quantity.innerText = qual.toString();
}

function setResponseContentAndResponseID(){
    var responseText = document.getElementById("responseText").value;
    document.getElementById("responseContent").value= responseText;
}







const colorElements = document.querySelectorAll('.circle'); // Các nút màu
const sizeElements = document.querySelectorAll('.size'); // Các nút size
const quantityDisplay = document.getElementById('quantity-display'); // Nơi hiển thị số lượng
var label = document.querySelector(".label-quantity-remain");
var selectedQuantity = document.querySelector(".quantity");
let selectedColor = null;
let selectedSize = null;

document.addEventListener('DOMContentLoaded', () => {


    function updateQuantity() {
        if (selectedColor && selectedSize) {
            const sizeNumber = Number(selectedSize); // Chuyển size sang số (nếu cần)

            // Lọc sản phẩm khớp với `productName`, `color`, và `size`
            const matchedProducts = productDetails.filter(item =>
                item.color === selectedColor &&
                item.size === sizeNumber
            );

            console.log('Matched Products:', matchedProducts);
            console.log('so luong:', matchedProducts.length);
            // Hiển thị số lượng sản phẩm tìm thấy
            label.textContent = "Số lượng còn lại: ";
            quantityDisplay.textContent = matchedProducts.length; // Số lượng khớp
        } else {
            quantityDisplay.textContent = 0; // Không đủ thông tin để tìm
        }
    }



    colorElements.forEach(circle => {
        circle.addEventListener('click', function () {
            selectedColor = this.getAttribute('data-color'); // Lấy màu
            console.log('Selected Color:', selectedColor);
            updateQuantity(); // Cập nhật số lượng
        });
    });

    sizeElements.forEach(button => {
        button.addEventListener('click', function () {
            selectedSize = Number(this.getAttribute('data-size')); // Lấy size và ép kiểu thành số
            console.log('Selected Size:', selectedSize);
            updateQuantity(); // Cập nhật số lượng
        });
    });

});



var addToCartBtn = document.querySelector(".add-to-cart-btn");

addToCartBtn.onclick = function () {
    // Lấy giá trị số lượng hiển thị
    const quantityDisplay = document.getElementById('quantity-display').innerText;

    // Kiểm tra nếu người dùng chưa chọn sản phẩm (màu hoặc size)
    if (!selectedColor || !selectedSize) {
        custom_toast({
            title: "Cảnh báo",
            message: "Vui lòng chọn màu sắc và kích thước.",
            type: "warning",
            duration: 6000
        });
        return;
    }

    if (Number(quantityDisplay) === 0) {
        custom_toast({
            title: "Cảnh báo",
            message: "Sản phẩm bạn chọn đã hết hàng.",
            type: "warning",
            duration: 6000
        });
        return;
    }

    if(quantityDisplay < Number(selectedQuantity.innerHTML)) {
        custom_toast({
            title: "Cảnh báo",
            message: "Sản phẩm không đủ số lượng.",
            type: "warning",
            duration: 6000
        });
        return;
    }


    var matchedProduct = productDetails.find(item=>
        item.color === selectedColor &&
        item.size === selectedSize
    );

    if(!matchedProduct) {
        alert("Không tìm thấy sản phẩm")
        return;
    }
    var productID = matchedProduct.productId;
    const form = document.createElement('form')
    form.method = 'POST';
    form.action = '${pageContext.request.contextPath}/AddCartQuantity';

    const inputProductId = document.createElement('input')
    inputProductId.type = 'hidden'
    inputProductId.name = 'productID'
    inputProductId.value =  productID;

    const inputQuantity = document.createElement('input');
    inputQuantity.type= 'hidden';
    inputQuantity.name = 'quantity'
    inputQuantity.value = selectedQuantity.innerHTML;

    form.appendChild(inputProductId);
    form.appendChild(inputQuantity);

    document.body.appendChild(form);
    // alert("ID: " + productID.toString() + " so luong: " + selectedQuantity.innerHTML);
    // form.submit();
    AddItemWithQuantity(productID.toString(), selectedQuantity.innerHTML);

};


const reviewImages = document.querySelectorAll('.review-image');
reviewImages.forEach(image => {
    image.addEventListener('click', function() {
        const modalImage = document.getElementById('modalImage');
        modalImage.src = this.src; // Set modal image to the clicked image
    });
});













