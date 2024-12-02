<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Đơn Hàng</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f7f7f7;
        }

        .order-detail {
            width: 80%;
            max-width: 1200px;
            margin: 30px auto;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }

        .order-detail-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #eee;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }

        .order-detail-header h2 {
            font-size: 26px;
            color: #333;
        }

        .btn-back {
            background-color: #4CAF50;
            color: white;
            padding: 12px 25px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            text-decoration: none;
        }

        .btn-back:hover {
            background-color: #45a049;
        }

        .order-detail-content {
            display: block;
            gap: 30px;
        }

        .order-detail-item {
            background-color: #f9f9f9;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex; /* Thêm display flex để bố trí ảnh và thông tin sản phẩm */
            align-items: flex-start;
            gap: 20px; /* Khoảng cách giữa ảnh và thông tin */
            cursor: pointer;
        }

        .order-detail-item:hover {
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .order-detail-item img {
            width: 150px; /* Đặt kích thước cố định cho ảnh */
            height: 150px;
            object-fit: cover; /* Đảm bảo ảnh không bị méo */
            border-radius: 8px;
        }

        .overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }


        .overlay-content {
            background: white;
            padding: 20px;
            border-radius: 5px;
            text-align: center;
            width: 90%;
            max-width: 600px;
            max-height: 80%;
            overflow-y: auto; /* Cho phép cuộn nếu nội dung quá dài */
            position: relative;
            box-sizing: border-box;
        }

        .close-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            background: red;
            color: white;
            border: none;
            border-radius: 50%;
            width: 30px;
            height: 30px;
            cursor: pointer;
        }

        .form-group {
            margin-bottom: 15px;
            text-align: left;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
        }

        .form-group input,
        .form-group textarea {
            width: 100%;
            height: 120px; /* Chiều cao mặc định */
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            resize: vertical; /* Cho phép người dùng thay đổi chiều cao của textarea */
            box-sizing: border-box;
            overflow-y: auto; /* Cho phép cuộn trong textarea khi nội dung dài */
        }

        .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .overlay-content img {
            max-width: 100%;
            max-height: 300px;
            object-fit: contain; /* Giữ tỷ lệ hình ảnh */
            width: auto;
            height: auto;
        }

        button {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
        }

        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<header>
    <jsp:include page="/view/header.jsp"></jsp:include>
</header>
<!-- Kiểm tra nếu có thông tin đơn hàng -->
<c:if test="${not empty orderDetails}">
    <div class="order-detail">
        <div class="order-detail-header">
            <h2>Chi Tiết Đơn Hàng #${orderDetails.orderId}</h2>
            <h2>Trạng Thái:  ${orderDetails.orderStatus}</h2>
            <a href="/customer/orders" class="btn-back">Quay Lại</a>
        </div>

        <!-- Chi tiết các sản phẩm trong đơn hàng -->
        <div class="order-detail-content">
            <c:forEach var="item" items="${orderDetails.orderItems}">
                <div
                        class="order-detail-item"
                        data-product-name="${item.productDTO.productName}"
                        data-product-image="${item.productDTO.getBase64Image()}"
                        data-user-id="${customerId}"
                        data-product-id="${item.productDTO.productId}"
                        data-old-image="${item.productDTO.reviewDTO.getBase64Image()}"
                        data-review-id="${item.productDTO.reviewDTO.reviewID}"
                        >
                    <input type="hidden" name="userID" value="${customerId}"/>
                    <input type="hidden" name="productID" value="${item.productDTO.productId}"/>
                    <input type="hidden" name="reviewID" value="${item.productDTO.reviewDTO.reviewID}"/>
                    <img src="${item.productDTO.getBase64Image()}" alt="${item.productDTO.productName}"/>
                    <div>
                        <p><b>${item.productDTO.productName}</b></p>
                        <p>Phân loại: ${item.productDTO.color} - Size: ${item.productDTO.size}</p>
                        <p>Số lượng: x${item.quantity}</p>

                        <!-- Hiển thị review nếu có -->
                        <c:if test="${not empty item.productDTO.reviewDTO}">
                            <p><strong>Đánh giá:</strong> ${item.productDTO.reviewDTO.comment}</p>
                            <p><strong>Số sao:</strong> ${item.productDTO.reviewDTO.ratingValue}</p>
                            <p><strong>Ngày đánh giá:</strong> ${item.productDTO.reviewDTO.date}</p>
                            <p><strong>Ảnh đánh giá:</strong> </p>

                            <!-- Hiển thị ảnh trong review -->
                            <c:if test="${not empty item.productDTO.reviewDTO}">
                                <img src="${item.productDTO.reviewDTO.getBase64Image()}" alt="Review Image" />
                            </c:if>
                            <button class="edit-review-button"
                                    onclick="openEditReviewOverlay(${item.productDTO.productId}, '${item.productDTO.productName}', '${item.productDTO.getBase64Image()}', '${item.productDTO.reviewDTO.ratingValue}', '${item.productDTO.reviewDTO.comment}','${item.productDTO.reviewDTO.getBase64Image()}' , '${item.productDTO.reviewDTO.reviewID}')">
                                Sửa Đánh Giá
                            </button>
                        </c:if>

                        <!-- Nếu không có review -->
                        <c:if test="${empty item.productDTO.reviewDTO}">
                            <c:if test="${orderDetails.orderStatus == 'COMPLETED'}">
                                <p><strong>Chưa có đánh giá</strong></p>
                                <button class="review-button" onclick="openReviewOverlay(${item.productDTO.productId})">Đánh giá</button>
                            </c:if>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Hiển thị tổng tiền -->
        <div>
            <h3>Tổng tiền: <fmt:formatNumber value="${orderDetails.payment.amount}" groupingUsed="true"/> VND</h3>
        </div>
    </div>
    <!-- Overlay sửa đánh giá sản phẩm -->

    <div id="editReviewOverlay" class="overlay" style="display: none;">
        <div class="overlay-content">
            <button class="close-btn" onclick="closeEditReviewForm()">X</button>
            <h2>Sửa Đánh Giá</h2>
            <form id="editReviewForm" method="post" action="${pageContext.request.contextPath}/customer/orderDetails?idOrder=${orderDetails.orderId}" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update" />
                <input type="hidden" id="editReviewID" name="editReviewID" />
                <input type="hidden" id="editUser-ID" name="editUser-ID" value="${customerId}"/>
                <input type="hidden" id="editProduct-ID" name="editProduct-ID" />
                <img id="editReviewFormImage" src="" alt="Product Image"/>
                <div class="form-group">
                    <label for="editReviewProductName">Ảnh cũ:</label>
                </div>
                <img id="editOldImage" src="" alt="Review Image" />
                <div class="form-group">
                    <label for="editReviewProductName">Tên sản phẩm:</label>
                    <input type="text" id="editReviewProductName" name="productName" readonly/>
                </div>
                <div class="form-group">
                    <label for="editReviewStars">Số sao:</label>
                    <select id="editReviewStars" name="editRating">
                        <option value="5">5 Sao</option>
                        <option value="4">4 Sao</option>
                        <option value="3">3 Sao</option>
                        <option value="2">2 Sao</option>
                        <option value="1">1 Sao</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="editReviewComment">Đánh giá:</label>
                    <textarea id="editReviewComment" name="editComment" placeholder="Cập nhật đánh giá của bạn..." required></textarea>
                </div>
                <div class="form-group">
                    <label for="editReviewImage">Tải ảnh mới:</label>
                    <input type="file" id="editReviewImage" name="review-Image" accept="image/*"/>
                    <button type="button" id="cancelImageBtn" style="display: none;">Hủy ảnh</button>
                </div>
                <button type="submit">Cập nhật đánh giá</button>
            </form>
        </div>
    </div>
    <!-- Overlay đánh giá sản phẩm -->
    <div id="reviewFormOverlay" class="overlay" style="display: none;">
        <div class="overlay-content">
            <button class="close-btn" onclick="closeReviewForm()">X</button>
            <h2>Đánh Giá Sản Phẩm</h2>
            <form id="reviewForm" method="post" action="${pageContext.request.contextPath}/customer/orderDetails?id=${orderDetails.orderId}" enctype="multipart/form-data">
                <input type="hidden" name="action" value="add" />
                <input type="hidden" id="user-ID" name="user-ID" value="${customerId}"/>
                <input type="hidden" id="product-ID" name="product-ID" />
                <img id="reviewFormImage" src="" alt="Product Image"/>
                <div class="form-group">
                    <label for="reviewProductName">Tên sản phẩm:</label>
                    <input type="text" id="reviewProductName" name="productName" readonly/>
                </div>
                <div class="form-group">
                    <label for="reviewStars">Số sao:</label>
                    <select id="reviewStars" name="rating">
                        <option value="5">5 Sao</option>
                        <option value="4">4 Sao</option>
                        <option value="3">3 Sao</option>
                        <option value="2">2 Sao</option>
                        <option value="1">1 Sao</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="reviewComment">Đánh giá:</label>
                    <textarea id="reviewComment" name="comment" placeholder="Nhập đánh giá của bạn..." required></textarea>
                </div>
                <div class="form-group">
                    <label for="reviewImage">Tải ảnh mới:</label>
                    <input type="file" id="reviewImage" name="review-Image" accept="image/*"/>
                    <button type="button" id="cancelReviewImageBtn" style="display: none;">Hủy ảnh</button>
                </div>
                <button type="submit">Gửi đánh giá</button>
            </form>
        </div>
    </div>
</c:if>

<!-- Nếu không có đơn hàng -->
<c:if test="${empty orderDetails}">
    <p>Không tìm thấy chi tiết đơn hàng.</p>
</c:if>

<script>
    // Load image lên picturebox

    const loadImageBtns = document.querySelectorAll('.loadImageBtn');
    const cancelBtns = document.querySelectorAll('.cancelBtn');
    const imageInputs = document.querySelectorAll('.imageInput');
    const imageDisplays = document.querySelectorAll('.imageDisplay');


    // Hàm để tải hình ảnh
    loadImageBtns.forEach((btn, index) => {
        btn.addEventListener('click', () => {
            event.preventDefault(); // Ngừng hành động mặc định (reload trang)
            imageInputs[index].click();
        });
    });

    // Xử lý sự kiện khi người dùng chọn file
    imageInputs.forEach((input, index) => {
        input.addEventListener('change', (event) => {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    imageDisplays[index].src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    });

    // Hàm để hủy hình ảnh
    cancelBtns.forEach((btn, index) => {
        btn.addEventListener('click', () => {
            imageDisplays[index].src = '';
            imageInputs[index].value = ''; // Đặt lại input để có thể chọn cùng hình ảnh lần nữa
        });
    });


    function openEditReviewOverlay(productId, productName, productImage, rating, comment, oldImage, reviewID) {
        // Fill in the form fields with existing data
        document.getElementById('editReviewProductName').value = productName;
        document.getElementById('editReviewFormImage').src = productImage; // This shows the product image
        document.getElementById('editProduct-ID').value = productId;
        document.getElementById('editReviewID').value = reviewID;
        document.getElementById('editReviewStars').value = rating;
        document.getElementById('editReviewComment').value = comment;
        document.getElementById('editOldImage').src = oldImage; // This sets the old review image to the form

        // Show the edit review overlay
        document.getElementById('editReviewOverlay').style.display = 'flex';
    }

    function closeEditReviewForm() {
        document.getElementById('editReviewOverlay').style.display = 'none';
    }

    // Thêm sự kiện click cho tất cả các nút Đánh giá
    document.querySelectorAll('.review-button').forEach(function (button) {
        button.addEventListener('click', function (event) {
            const productElement = event.target.closest('.order-detail-item');
            const productName = productElement.getAttribute('data-product-name');
            const productImage = productElement.getAttribute('data-product-image');
            const userID = productElement.getAttribute('data-user-id');
            const productID = productElement.getAttribute('data-product-id');
            const reviewID = productElement.getAttribute('data-review-id');

            // Cập nhật thông tin vào form đánh giá
            document.getElementById('reviewProductName').value = productName;
            document.getElementById('reviewFormImage').src = productImage;
            document.getElementById('user-ID').value = userID;
            document.getElementById('product-ID').value = productID;


            // Hiển thị overlay
            document.getElementById('reviewFormOverlay').style.display = 'flex';
        });
    });

    function closeReviewForm() {
        document.getElementById('reviewFormOverlay').style.display = 'none';
    }


</script>
</body>
<%@ include file="/view/footer.jsp" %> <!-- Include footer.jsp -->

</html>
