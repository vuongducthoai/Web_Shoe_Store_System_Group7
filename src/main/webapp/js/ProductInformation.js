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










