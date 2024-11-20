document.addEventListener("DOMContentLoaded", function () {
    let buttons = document.querySelectorAll(".btn-col");
    const loadMoreBtn = document.querySelector("#load-more-btn"); // chỉ lấy phần tử đầu tiên
    let productItems = document.querySelector(".grid-wrapper");
    let offset = 0;
    const limit = 8;

    buttons.forEach((btn) => {
        btn.addEventListener("click", function () {
            buttons.forEach((btn) => btn.classList.remove("active"));

            this.classList.add("active");

            const category = this.getAttribute("data-btn");

            const newUrl = `/loadProducts?category=${category}&offset=${offset}&limit=${limit}`;
            window.history.pushState({ path: newUrl }, '', newUrl);

            productItems.innerHTML = "";
            offset = 0; // Reset offset khi đổi category

            loadProducts(category);
        });
    });

    function loadProducts(category) {
        fetch(`/loadProducts?category=${category}&offset=${offset}&limit=${limit}`)
            .then(response => response.json())
            .then(data => {
                if (data.length > 0) {
                    loadMoreBtn.style.display = "block";
                    // Lặp qua danh sách sản phẩm và thêm vào grid-wrapper
                    data.forEach(product => {
                        const productHTML = `
                            <div class="col- collection-item" data-item="${product.categoryDTO.categoryId}">
                                <figure><img src="../image/shoes1.png" alt="${product.productName}"></figure>
                                <div class="col-body">
                                    <p class="rating-icon"><i class="fa-solid fa-star"></i> <span class="rating-num">5</span></p>
                                    <h3 class="heading-three">${product.productName}</h3>
                                    <p class="sub-heading">${product.description}</p>
                                    <div class="col-footer">
                                        <p class="shoe-price">$${product.price}</p>
                                        <button class="shoe-btn btn">Add to cart</button>
                                    </div>
                                </div>
                            </div>
                        `;
                        productItems.innerHTML += productHTML;
                    });
                    offset += limit; // Cập nhật offset sau mỗi lần tải
                } else {
                    loadMoreBtn.style.display = "none"; // Nếu không có dữ liệu thì ẩn nút load more
                }
            })
            .catch(error => console.error("Error loading products:", error));
    }

    // Lắng nghe sự kiện click của nút "Load More"
    if (loadMoreBtn) {
        loadMoreBtn.addEventListener("click", function() {
            const category = document.querySelector(".btn-col.active").getAttribute("data-btn");
            loadProducts(category);
        });
    }

    // Tải sản phẩm khi trang lần đầu tiên được tải
    const defaultCategory = document.querySelector(".btn-col.active")?.getAttribute("data-btn") || "all"; // Lấy category mặc định
    loadProducts(defaultCategory);

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
