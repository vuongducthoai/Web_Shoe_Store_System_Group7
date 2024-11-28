document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".btn-col");
    const loadMoreBtn = document.querySelector("#load-more-btns");
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

    // Sử dụng jQuery AJAX thay vì fetch
    function loadProducts(category) {
        const cacheKey = `${category}-${offset}-${limit}`;
        if (cache.has(cacheKey)) {
            renderProducts(cache.get(cacheKey));
            return;
        }

        $.ajax({
            url: `/loadProducts?category=${category}&offset=${offset}&limit=${limit}`,
            method: 'GET',
            dataType: 'json', // Nhận dữ liệu ở định dạng JSON
            success: function (data) {
                cache.set(cacheKey, data);
                renderProducts(data);
            },
            error: function (error) {
                console.error("Error loading products:", error);
            }
        });
    }

    function renderProducts(data) {
        if (data.length > 0) {
            loadMoreBtn.style.display = "block";

            const fragment = document.createDocumentFragment();
            data.forEach(product => {
                const productDiv = document.createElement("div");
                productDiv.className = "col- collection-item";
                fragment.appendChild(productDiv);
                productDiv.innerHTML = `
                    <figure><img src="../image/shoes1.png" alt="${product.productName}"></figure>
                    <div class="col-body">
                        <h3 class="two">${product.productName}</h3>
                        <h3 class="heading-three">${product.description}</h3>
                        <div class="col-footer">
                            <p class="shoe-price">${product.price}₫</p>
                            <p class="sub-heading">Đã bán: ${product.quantity}</p>
                            <p class="rating-icon"><i class="fa-solid fa-star"></i> <span class="rating-num">4.9</span></p>
                        </div>
                    </div>
                `;
            });
            productItems.appendChild(fragment);
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

    // Debounce function để giảm tần suất gọi loadProducts
    function debounce(func, delay) {
        let timer;
        return function (...args) {
            clearTimeout(timer);
            timer = setTimeout(() => func.apply(this, args), delay);
        };
    }

    // Lắng nghe sự kiện click của nút "Load More" với debounce
    if (loadMoreBtn) {
        // Gắn sự kiện chỉ một lần khi trang tải
        loadMoreBtn.addEventListener("click", debounce(function () {
            const category = document.querySelector(".btn-col.active").getAttribute("data-btn");
            loadProducts(category);
        }, 500)); // Điều chỉnh thời gian debounce nếu cần thiết
    }
});
