<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Chi Tiết Đơn Hàng</title>
  <style>
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
      width: 400px;
      position: relative;
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
    .overlay img {
      max-width: 100%;
      height: auto;
      margin-bottom: 20px;
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
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
  </style>
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
      box-sizing: border-box;
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

    .order-detail-header .btn-back {
      background-color: #4CAF50;
      color: white;
      border: none;
      padding: 12px 25px;
      font-size: 16px;
      border-radius: 5px;
      text-decoration: none;
      transition: background-color 0.3s ease;
    }

    .order-detail-header .btn-back:hover {
      background-color: #45a049;
    }

    /* Thanh tìm kiếm */
    .search-bar {
      margin-bottom: 20px;
    }

    .search-bar input {
      padding: 10px;
      width: 80%;
      border: 1px solid #ddd;
      border-radius: 5px;
    }

    .search-bar button {
      padding: 10px 20px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 5px;
    }

    /* Hiển thị chi tiết các sản phẩm trong đơn hàng */
    .order-detail-content {
      display: block; /* Đảm bảo mỗi sản phẩm chiếm một hàng */
      gap: 30px;
    }

    .order-detail-item {
      background-color: #f9f9f9;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      transition: box-shadow 0.3s ease;
      margin-bottom: 20px; /* Thêm khoảng cách giữa các sản phẩm */
    }

    .order-detail-item:hover {
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    }

    .order-detail-item img {
      width: 100%; /* Đảm bảo ảnh chiếm hết chiều rộng của thẻ chứa */
      max-width: 200px; /* Giới hạn chiều rộng tối đa của ảnh */
      height: auto; /* Giữ tỷ lệ của ảnh */
      object-fit: cover; /* Cắt và điều chỉnh ảnh sao cho vừa với khung mà không bị biến dạng */
      border-radius: 8px; /* Bo góc ảnh */
    }

    .order-detail-item .product-name {
      font-size: 20px;
      font-weight: bold;
      color: #333;
    }

    .order-detail-item .product-info {
      font-size: 14px;
      color: #777;
    }

    .order-status {
      margin-top: 20px;
      font-size: 18px;
      font-weight: bold;
    }

    .status.success {
      color: #4CAF50;
    }

    .status.completed {
      color: #FF5733;
    }

    .order-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 30px;
      border-top: 2px solid #eee;
      padding-top: 20px;
    }

    .order-footer .order-total {
      font-size: 20px;
      font-weight: bold;
      color: #333;
    }

    .order-footer .btn {
      padding: 12px 25px;
      font-size: 16px;
      cursor: pointer;
      border: none;
      border-radius: 5px;
      transition: background-color 0.3s ease;
    }

    .order-footer .btn-rate {
      background-color: #FF5733;
      color: white;
    }

    .order-footer .btn-contact {
      background-color: #f1f1f1;
      color: #555;
    }

    .order-footer .btn:hover {
      background-color: #ddd;
    }

    /* Responsive Design */
    @media (max-width: 768px) {
      .order-detail {
        width: 90%;
        padding: 20px;
      }

      .order-detail-header h2 {
        font-size: 22px;
      }

      .order-footer .order-total {
        font-size: 18px;
      }

      .order-detail-item {
        margin-bottom: 20px;
      }
    }
  </style>
</head>
<body>
<!-- Kiểm tra nếu orderDetails tồn tại -->
<c:if test="${not empty orderDetails}">
  <div class="order-detail">
    <div class="order-detail-header">
      <h2>Chi Tiết Đơn Hàng #${orderDetails.orderId}</h2>
      <a href="/JPAExample_war_exploded/customer/orders" class="btn-back">Quay Lại</a>
    </div>

    <!-- Thanh tìm kiếm -->
    <div class="search-bar">
      <input id="searchInput" type="text" placeholder="Tìm sản phẩm...">
      <button onclick="filterProducts()">Tìm</button>
    </div>

    <!-- Hiển thị chi tiết các sản phẩm trong đơn hàng -->
    <div id="orderDetailContent" class="order-detail-content">
      <c:forEach var="item" items="${orderDetails.orderItems}">
        <div
                class="order-detail-item"
                data-order-item-id="${item.orderItemId}"
                data-product-name="${item.productDTO.productName}"
                data-product-image="${item.productDTO.getBase64Image()}"
                data-product-color="${item.productDTO.color}"
                data-product-size="${item.productDTO.size}"
                data-product-quantity="${item.quantity}"
                onclick="showForm(this)"
        >
          <img src="${item.productDTO.getBase64Image()}" alt="${item.productDTO.productName}" />
          <div>
            <p class="product-name">${item.productDTO.productName}</p>
            <p class="product-info">Phân loại: ${item.productDTO.color} - Size: ${item.productDTO.size}</p>
            <p class="product-info">Số lượng: x${item.quantity}</p>

            <!-- Kiểm tra nếu reviewDTO không null -->
            <c:if test="${not empty item.ProductDTO.reviewDTO}">
              <p class="product-info"><b>Đánh giá chung:</b> ${item.ProductDTO.reviewDTO.comment}</p>
              <p class="product-info"><b>Số sao:</b> ${item.ProductDTO.ratingValue}</p>
            </c:if>
            <!-- Nếu reviewDTO null -->
            <c:if test="${empty item.ProductDTO.reviewDTO}">
              <button class="btn-rate" onclick="showForm2(this)">Đánh giá chung</button>
            </c:if>
          </div>
        </div>
      </c:forEach>
    </div>

    <!-- Hiển thị trạng thái đơn hàng -->
    <div class="order-status">
      Trạng thái đơn hàng: <span class="status success">${orderDetails.orderStatus}</span>
    </div>

    <!-- Hiển thị tổng tiền của đơn hàng -->
    <div class="order-footer">
      <p class="order-total">Tổng tiền:
        <span><fmt:formatNumber value="${orderDetails.payment.amount}" groupingUsed="true"/> VND
        </span></p>
      <c:if test="${orderDetails.orderStatus == 'COMPLETED'}">
        <button class="btn btn-rate">Đánh giá</button>
      </c:if>
      <button class="btn btn-contact">Liên hệ với shop</button>
    </div>
  </div>


  <div id="productFormOverlay" class="overlay" style="display: none;">
    <div class="overlay-content">
      <button class="close-btn" onclick="closeForm()">X</button>
      <h2>Thông tin sản phẩm</h2>
      <form id="productForm">
        <img id="formImage" src="" alt="Product Image" />
        <div class="form-group">
          <label for="formProductName">Tên sản phẩm:</label>
          <input type="text" id="formProductName" name="productName" readonly />
        </div>
        <div class="form-group">
          <label for="formProductColor">Phân loại:</label>
          <input type="text" id="formProductColor" name="productColor" readonly />
        </div>
        <div class="form-group">
          <label for="formProductSize">Size:</label>
          <input type="text" id="formProductSize" name="productSize" readonly />
        </div>
        <div class="form-group">
          <label for="formProductQuantity">Số lượng:</label>
          <input type="text" id="formProductQuantity" name="productQuantity" readonly />
        </div>
        <div class="form-group">
          <label for="formComment">Đánh giá:</label>
          <textarea id="formComment" name="comment" placeholder="Viết đánh giá của bạn..."></textarea>
        </div>
        <button type="submit">Gửi đánh giá</button>
      </form>
    </div>
  </div>
</c:if>

<!-- Nếu không có đơn hàng -->
<c:if test="${empty orderDetails}">
  <div class="order-detail">
    <p>Không tìm thấy chi tiết đơn hàng.</p>
  </div>
</c:if>

<script>
  // Hàm lọc sản phẩm theo tên
  function filterProducts() {
    const input = document.getElementById("searchInput").value.toLowerCase();
    const items = document.querySelectorAll(".order-detail-item");

    items.forEach(item => {
      const productName = item.getAttribute("data-product-name").toLowerCase();
      if (productName.includes(input)) {
        item.style.display = "block"; // Hiển thị sản phẩm trên một hàng
      } else {
        item.style.display = "none"; // Ẩn sản phẩm
      }
    });
  }
</script>
<script>
  function showForm2(element) {
    // Lấy thông tin sản phẩm từ các thuộc tính data-*
    const productName = element.getAttribute("data-product-name");
    const productImage = element.getAttribute("data-product-image");
    const productColor = element.getAttribute("data-product-color");
    const productSize = element.getAttribute("data-product-size");
    const productQuantity = element.getAttribute("data-product-quantity");

    // Gán dữ liệu vào các trường của form
    document.getElementById("formProductName").value = productName;
    document.getElementById("formImage").src = productImage;
    document.getElementById("formProductColor").value = productColor;
    document.getElementById("formProductSize").value = productSize;
    document.getElementById("formProductQuantity").value = productQuantity;

    // Hiển thị overlay
    document.getElementById("productFormOverlay").style.display = "flex";
  }

  function closeForm() {
    // Ẩn overlay
    document.getElementById("productFormOverlay").style.display = "none";
  }

  function showForm(element ) {
    // Lấy thông tin sản phẩm từ các thuộc tính data-*
    const productName = element.getAttribute("data-product-name");
    const productImage = element.getAttribute("data-product-image");
    const productColor = element.getAttribute("data-product-color");
    const productSize = element.getAttribute("data-product-size");
    const productQuantity = element.getAttribute("data-product-quantity");

    // Gán dữ liệu vào các trường của form
    document.getElementById("formProductName").value = productName;
    document.getElementById("formImage").src = productImage;
    document.getElementById("formProductColor").value = productColor;
    document.getElementById("formProductSize").value = productSize;
    document.getElementById("formProductQuantity").value = productQuantity;

    // Hiển thị overlay
    document.getElementById("productFormOverlay").style.display = "flex";
    //   lay trang thai của button document.getElementById("productFormOverlay")

  }

  function closeForm() {
    // Ẩn overlay
    document.getElementById("productFormOverlay").style.display = "none";
  }
</script>
</body>
</html>
