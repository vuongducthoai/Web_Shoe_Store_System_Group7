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

    var responseID = document.getElementById("ResponseID").value;
    document.getElementById("modalResponseID").value = responseID;
}




//Hiển thị size của màu

document.addEventListener('DOMContentLoaded', () => {
    // Parse JSON từ biến được server gửi
    const colorOptions = document.querySelectorAll('.color-option'); // Các nút màu
    const sizeButtons = document.querySelectorAll('.size-option'); // Các nút size

    // Khi người dùng chọn một màu
    colorOptions.forEach(option => {
        option.addEventListener('click', () => {
            const selectedColor = option.getAttribute('data-color'); // Lấy màu được chọn
            const availableSizes = colorSizeMap[selectedColor] || []; // Lấy danh sách size khả dụng

            // Đặt trạng thái cho các nút size
            sizeButtons.forEach(button => {
                const size = parseInt(button.innerText); // Lấy size từ nút
                if (availableSizes.includes(size)) {
                    button.removeAttribute('disabled'); // Bật nút size
                    button.classList.add('active-size'); // Thêm hiệu ứng
                } else {
                    button.setAttribute('disabled', true); // Tắt nút size
                    button.classList.remove('active-size'); // Loại bỏ hiệu ứng
                }
            });
        });
    });
});










