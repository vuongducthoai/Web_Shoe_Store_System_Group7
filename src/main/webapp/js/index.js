document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".btn-col");
    const loadMoreBtn = document.querySelector("#load-more-btn");
    const productItems = document.querySelector(".grid-wrapper");
    let offset = 8;
    const limit = 8;

    buttons.forEach((btn) => {
        btn.addEventListener("click", function () {
            buttons.forEach((btn) => btn.classList.remove("active"));
            this.classList.add("active");

            const category = this.getAttribute("data-btn");
            offset = 0; // Reset offset khi đổi category
            const newUrl = `/loadProducts?category=${category}&offset=${offset}&limit=${limit}`;
            window.history.pushState({ path: newUrl }, '', newUrl);
            productItems.innerHTML = "";
            loadProducts(category);
        });
    });

    const cache = new Map();
    function loadProducts(category) {
        const cacheKey = `${category}-${offset}-${limit}`;
        if(cache.has(cacheKey)) {
            renderProducts(cache.get(cacheKey));
            return;
        }
        fetch(`/loadProducts?category=${category}&offset=${offset}&limit=${limit}`)
            .then(response => response.json())
            .then(data => {
                cache.set(cacheKey, data);
                renderProducts(data);
            })
            .catch(error => console.error("Error loading products:", error));
    }

    function renderProducts(data){
        if (data.length > 0) {
            loadMoreBtn.style.display = "block";

            // Sử dụng DocumentFragment để batch cập nhật DOM
            const fragment = document.createDocumentFragment();
            data.forEach(product => {
                const productDiv = document.createElement("div");
                productDiv.className = "col- collection-item";
                productDiv.dataset.item = product.categoryDTO.categoryId;
                productDiv.innerHTML = `
                        <figure><img src="../image/shoes1.png" alt="${product.productName}"></figure>
                        <div class="col-body">
                            <h3 class="heading-three">${product.productName}</h3>
                            <div class="col-footer">
                                <p class="shoe-price">$${product.price}₫</p>
                                <p class="sub-heading">Đã bán: ${product.quantity}</p>
                                <p class="rating-icon"><i class="fa-solid fa-star"></i> <span class="rating-num">4.9</span></p>
                            </div>
                        </div>
                    `;
                fragment.appendChild(productDiv);
            });
            productItems.appendChild(fragment); // Chỉ cập nhật DOM một lần
            offset += limit;
        } else {
            loadMoreBtn.style.display = "none";
            showSuccessToast({
                title: "Tất cả sản phẩm đã được tải!",
                message: "Cảm ơn bạn đã xem tất cả sản phẩm. Hãy quay lại sau để xem thêm.",
                type: "info"
            });
        }
    }


    function debounce(func, delay) {
        let timer;
        return function (...args) {
            clearTimeout(timer);
            timer = setTimeout(() => func.apply(this, args), delay);
        };
    }

    // Lắng nghe sự kiện click của nút "Load More"
    if (loadMoreBtn) {
        console.log("123")
        loadMoreBtn.addEventListener("click", debounce(function() {
            const category = document.querySelector(".btn-col.active").getAttribute("data-btn");
            loadProducts(category);
        }, 300));
    }

    // // Tải sản phẩm khi trang lần đầu tiên được tải
    // const defaultCategory = document.querySelector(".btn-col.active")?.getAttribute("data-btn") || "all"; // Lấy category mặc định
    // loadProducts(defaultCategory);

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