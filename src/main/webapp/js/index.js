document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".btn-col");
    const loadMoreBtn = document.querySelector("#load-more-btns");
    const productItems = document.querySelector(".wrapper1");
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
                productDiv.className = "product";
                productDiv.style.flex = "0 0 23%";
                productDiv.style.maxWidth = "25%";
                productDiv.style.height = "400px";
                fragment.appendChild(productDiv);

                productDiv.innerHTML = `
            <form action="/product/details" method="GET">
                <input type="hidden" name="productName" value="${product.productName}">
                <div class="product-image">
                     <figure><img src="${product.imageBase64}" alt="${product.productName}"></figure>
                </div>
                <div class="product-details">
                    <p class="product_name">${product.productName}</p>
                    <p class="product_desc" style="margin-top: 5px">${product.description}</p>
                    <div class="col-footer" style="display: flex; align-items: center; justify-content: space-between; margin-top: 10px">
                        <p class="shoe-price">${product.price}₫</p>
                        <p class="shoe-sold">Đã bán: ${product.quantity}</p>
                    </div>
                </div>
                 <button type="submit" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; opacity: 0;  cursor: pointer"></button> <!-- Invisible button for clicking -->
               </form>
            `;
            });
            // Assuming productItems is the container where products should be added
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
