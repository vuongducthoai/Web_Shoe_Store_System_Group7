

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
    if(qual <= 0 ) alert("Không được nhỏ hơn 1")
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
        showSuccessToast({ title: "Warning", message: "Vui lòng chọn màu sắc và kích thước.", type: "Warning" });

        return;
    }

    if (Number(quantityDisplay) === 0) {
        showSuccessToast({ title: "Warning", message: "Sản phẩm bạn chọn đã hết hàng.", type: "Warning" });

        return;
    }

    if(quantityDisplay < Number(selectedQuantity.innerHTML)) {
        showSuccessToast({ title: "Warning", message: "Sản phẩm không đủ số lượng.", type: "Warning" });

        return;
    }


    // Nếu tất cả điều kiện đều hợp lệ
    showSuccessToast({ title: "Warning", message: "Thêm vào giỏ hàng thành công.", type: "Warning" });

};










