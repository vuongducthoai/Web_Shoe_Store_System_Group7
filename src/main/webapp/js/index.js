document.addEventListener("DOMContentLoaded", function () {
    // Lấy danh sách các nút
    let buttons = document.querySelectorAll(".btn-col");
    let productItems = document.querySelectorAll(".collection-item");

    // Lặp qua các nút và thêm sự kiện click
    buttons.forEach((btn) => {
        btn.addEventListener("click", function () {
            // Xóa class 'active' khỏi tất cả các nút
            buttons.forEach((btn) => btn.classList.remove("active"));

            // Thêm class 'active' cho nút được nhấn
            this.classList.add("active");

            // Lấy danh mục từ thuộc tính 'data-btn'
            const category = this.getAttribute("data-btn");

            // Cập nhật URL
            const newUrl = `/loadProducts?category=${category}`;
            window.history.pushState({ path: newUrl }, '', newUrl);

            // Lọc và hiển thị sản phẩm
            productItems.forEach((item) => {
                const itemCategory = item.getAttribute("data-item");
                if (category === "all" || itemCategory === category) {
                    item.style.display = "block"; // Hiển thị
                } else {
                    item.style.display = "none"; // Ẩn
                }
            });
        });
    });

    // NAVBAR TOGGLE
    let ul = document.querySelector("ul");
    let burger_icon = document.querySelector(".burger_icon");

    burger_icon.addEventListener("click", () => {
        if (burger_icon.classList.contains("fa-bars")) {
            burger_icon.classList.add("fa-xmark");
            burger_icon.classList.remove("fa-bars");
            ul.classList.add("active");
        } else {
            burger_icon.classList.remove("fa-xmark");
            burger_icon.classList.add("fa-bars");
            ul.classList.remove("active");
        }
    });
});
