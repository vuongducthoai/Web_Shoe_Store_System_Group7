<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <div class="order-detail-item" data-product-name="${item.productDTO.productName}">
          <img src="https://th.bing.com/th/id/OIP.pl0SIxw_ezFPUpgrzWrjBgAAAA?w=383&h=383&rs=1&pid=ImgDetMain" alt="Product Image">
          <div>
            <p class="product-name">${item.productDTO.productName}</p>
            <p class="product-info">Phân loại: ${item.productDTO.color} - Size: ${item.productDTO.size}</p>
            <p class="product-info">Số lượng: x${item.quantity}</p>
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
      <p class="order-total">Tổng tiền: <span>${orderDetails.payment.amount}</span></p>
      <c:if test="${orderDetails.orderStatus == 'COMPLETED'}">
        <button class="btn btn-rate">Đánh giá</button>
      </c:if>
      <button class="btn btn-contact">Liên hệ với shop</button>
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
</body>
</html>
