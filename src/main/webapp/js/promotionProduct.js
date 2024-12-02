document.addEventListener("DOMContentLoaded", () => {

    // Lấy tất cả các phần tử sản phẩm và liên kết phân trang
    const productList = document.querySelector("#product-list");
    const pageLinkItems = document.querySelectorAll(".page-link-item");

    // Duyệt qua tất cả các nút phân trang
    pageLinkItems.forEach(pageLink => {
        pageLink.addEventListener("click", function (e) {
            e.preventDefault(); // Ngăn không cho trình duyệt thực hiện hành động mặc định

            // Lấy số trang từ nút nhấn
            const pageIndex = this.textContent;
            const newUrl = `/Paging?index=${pageIndex}`;
            window.history.pushState({path:newUrl}, '', newUrl);
            productList.innerHTML = "";
            // Gọi hàm tải sản phẩm cho trang đã chọn
            loadProducts(pageIndex);
        });
    });

    const cache = new Map();

    // Hàm tải sản phẩm qua AJAX
    function loadProducts(index) {
        const cacheKey = `${index}`
        if(cache.has(cacheKey)) {
            renderProducts(cache.get(cacheKey));
            return;
        }
        // Gửi yêu cầu AJAX đến server để tải sản phẩm
        $.ajax({
            url: `/Paging?index=${index}`,
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                cache.set(cacheKey, data);
                renderProducts(data);
            },
            error: function (error) {
                console.error("Error loading products:", error);
            }
        });
    }

    function renderProducts(data){
        if(data.length > 0){
            const fragment = document.createDocumentFragment();
            data.forEach(promotionProduct => {
                const productDiv = document.createElement("div");
                productDiv.className = "product";
                productDiv.style.flex = "0 0 23%";
                productDiv.style.maxWidth = "25%";
                fragment.appendChild(productDiv);

                productDiv.innerHTML = `
                   <form action="/product/details" method="GET">
                        <input type="hidden" name="productName" value="${promotionProduct.product.productName}">
                        <div class="product-image">
                            <figure><img src="${promotionProduct.product.imageBase64}" alt="Foundation"></figure>
                        </div>
                        <div class="product-details">
                            <div style="display: flex; align-items: center; justify-content: space-between">
                                <p class="product_name">${promotionProduct.product.productName}</p>
                                <span class="star-rating"></span>
                            </div>

                            <p class="product_desc" style="margin-top: 5px">${promotionProduct.product.description}</p>
                            <div class="col-footer" style="display: flex; align-items: center; justify-content: space-between; margin-top: 10px">
                                <p class="shoe-price">${promotionProduct.product.sellingPrice}₫ <span style="color: #999; margin-left: 10px" class="shoe-price strike-through">${promotionProduct.product.price}₫</span>
                            </div>
                        </div>
                        <div class="home-product-item_sale-off" style="  position: absolute;
                            right: 0;
                            top: 0;
                            width: 45px;
                            height: 50px;
                            background-color: rgba(255, 216, 64, 0.93);
                            text-align: center;">
                            <span style="color: #EE4D2D;
                                font-weight: 600;
                                font-size: 1.2rem;
                                line-height: 1.3rem;" class="home-product-item_sale-off-percent">${promotionProduct.promotion.discountValue}%</span>
                            <span style=" color: #1155e6;
                                font-weight: 600;
                                line-height: 1.4rem;" class="home-product-item_sale-off-label">GIẢM</span>
                        </div>
                        <button type="submit" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; opacity: 0;  cursor: pointer"></button> <!-- Invisible button for clicking -->
                    </form>
                `;

            });
            productList.appendChild(fragment);
            productList.appendChild(fragment);
        } else {
            showSuccessToast({
                title: "Tất cả sản phẩm đã được tải!",
                message: "Cảm ơn bạn đã xem tất cả sản phẩm. Hãy quay lại sau để xem thêm.",
                type: "info"
            });
        }
    }
});
