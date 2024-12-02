<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Lịch Sử Mua Hàng</title>
  <style>
    body {
      font-family: 'Roboto', Arial, sans-serif;
      margin: 0;
      padding: 0;
      padding-top: 80px;
      background-color: #f9f9f9;
    }

    h2 {
      text-align: center;
      margin: 20px 0;
      color: #333;
      font-size: 24px;
    }

    .order-history {
      width: 90%;
      max-width: 1200px;
      margin: 20px auto;
      background-color: #fff;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    .search-bar {
      margin-bottom: 20px;
      display: flex;
      justify-content: center;
    }

    .search-input {
      padding: 8px;
      width: 60%;
      font-size: 16px;
      border-radius: 5px;
      border: 1px solid #ccc;
    }

    .search-button {
      padding: 8px 15px;
      font-size: 16px;
      border-radius: 5px;
      border: 1px solid #ccc;
      background-color: #ee4d2d;
      color: white;
      cursor: pointer;
      margin-left: 10px;
    }

    .order-filter {
      display: flex;
      justify-content: space-around;
      margin-bottom: 20px;
      padding: 0;
      list-style: none;
    }

    .order-filter li {
      cursor: pointer;
      font-size: 14px;
      font-weight: bold;
      color: #555;
      padding: 10px 15px;
      border: 1px solid transparent;
      border-radius: 5px;
      transition: all 0.3s ease;
    }

    .order-filter li.active {
      background-color: #ee4d2d;
      color: white;
      border-color: #ee4d2d;
    }

    .order-filter li:hover {
      background-color: #ff7043;
      color: white;
    }

    .order-item {
      border-bottom: 1px solid #e0e0e0;
      padding: 20px 0;
      display: flex;
      flex-direction: column;
    }

    .order-item:last-child {
      border-bottom: none;
    }

    .order-header span {
      font-size: 18px;
      font-weight: bold;
      color: #555;
    }

    .order-content {
      display: flex;
      align-items: center;
      gap: 15px;
      margin-top: 10px;
    }

    .order-content img {
      width: 100px;
      height: 100px;
      border-radius: 8px;
      border: 1px solid #ddd;
      object-fit: cover;
    }

    .product-details {
      flex-grow: 1;
    }

    .product-name {
      font-size: 16px;
      font-weight: bold;
      color: #333;
      margin: 0 0 5px 0;
    }

    .product-variant {
      font-size: 14px;
      color: #666;
      margin-bottom: 5px;
    }

    .product-quantity {
      font-size: 14px;
      color: #888;
    }

    .order-status {
      text-align: right;
      min-width: 100px;
    }

    .status {
      font-size: 14px;
      font-weight: bold;
    }

    .status.WAITING_CONFIRMATION {
      color: #ff9800;
    }

    .status.CONFIRMED {
      color: #2196f3;
    }

    .status.SHIPPED {
      color: #ff5722;
    }

    .status.COMPLETED {
      color: #4caf50;
    }

    .status.CANCELLED {
      color: #f44336;
    }

    .order-footer .btn {
      text-decoration: none;
      padding: 8px 15px;
      font-size: 14px;
      font-weight: bold;
      border-radius: 5px;
      transition: all 0.3s ease;
      border: 1px solid transparent;
    }

    .btn-more {
      background-color: #ee4d2d;
      justify-content: center;
      color: #fff;
      border-color: #ee4d2d;
    }

    .btn-more:hover {
      background-color: #d94425;
    }

    .btn-rate {
      background-color: #f5f5f5;
      color: #555;
      border-color: #ddd;
    }
    .no-orders-message {
      text-align: center;
      color: #f44336; /* Màu đỏ nổi bật */
      font-weight: bold;
      font-size: 18px; /* Kích thước chữ vừa phải */
      margin-top: 20px; /* Khoảng cách phía trên */
      padding: 10px;
      background-color: #ffe6e6; /* Nền màu đỏ nhạt để làm nổi bật thông báo */
      border: 1px solid #f44336; /* Viền màu đỏ */
      border-radius: 5px; /* Bo góc cho mềm mại */
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Đổ bóng nhẹ */
    }
    .btn-rate:hover {
      background-color: #e0e0e0;
    }

    .order-footer {
      display: flex;
      justify-content: space-between;



      align-items: center;
    }

    .order-total {
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }

  </style>
  <script>
    // Hiển thị các đơn hàng theo trạng thái
    function filterOrdersByStatus(status) {
      const orders = document.querySelectorAll('.order-item');
      orders.forEach(order => {
        if (status === 'ALL' || order.dataset.status === status) {
          order.style.display = 'block';
        } else {
          order.style.display = 'none';
        }
      });
    }

    // Tìm kiếm đơn hàng theo tên khách hàng hoặc ID đơn hàng
    function searchOrders() {
      const searchTerm = document.querySelector('.search-input').value.toLowerCase();
      const orders = document.querySelectorAll('.order-item');
      orders.forEach(order => {
        // Lấy tên sản phẩm trong đơn hànzg
        const productName = order.querySelector('.product-name').innerText.toLowerCase();

        // Kiểm tra xem tên sản phẩm có chứa từ khóa tìm kiếm không
        if (productName.includes(searchTerm)) {
          order.style.display = 'block'; // Hiển thị sản phẩm nếu tìm thấy
        } else {
          order.style.display = 'none'; // Ẩn sản phẩm nếu không tìm thấy
        }
      });
    }

    // Thiết lập sự kiện cho bộ lọc trạng thái và tìm kiếm
    window.onload = () => {
      const filters = document.querySelectorAll('.order-filter li');
      filters.forEach(filter => {
        filter.addEventListener('click', () => {
          filters.forEach(f => f.classList.remove('active'));
          filter.classList.add('active');
          filterOrdersByStatus(filter.dataset.status);
        });
      });
      // Mặc định hiển thị tất cả
      filterOrdersByStatus('ALL');

      // Thiết lập sự kiện cho thanh tìm kiếm
      document.querySelector('.search-button').addEventListener('click', searchOrders);
    };
  </script>
</head>
<body>
<header>
  <jsp:include page="/view/header.jsp"></jsp:include>
</header>
<h2>Lịch Sử Mua Hàng</h2>

<div class="order-history">
  <!-- Thanh tìm kiếm -->
  <div class="search-bar">
    <input type="text" class="search-input" placeholder="Tìm kiếm theo tên sản phẩm...">
    <button class="search-button">Tìm kiếm</button>
  </div>
  <!-- Kiểm tra thông báo lỗi -->
  <c:if test="${not empty errorMessage}">
    <div class="no-orders-message">
        ${errorMessage}
    </div>
  </c:if>

  <!-- Thanh điều hướng trạng thái -->
  <ul class="order-filter">
    <li class="active" data-status="ALL">Tất cả</li>
    <li data-status="WAITING_CONFIRMATION">Chờ xác nhận</li>
    <li data-status="CONFIRMED">Đã xác nhận</li>
    <li data-status="SHIPPED">Đang giao</li>
    <li data-status="COMPLETED">Hoàn thành</li>
    <li data-status="CANCELLED">Đã hủy</li>
  </ul>

  <!-- Hiển thị danh sách đơn hàng -->
  <c:forEach var="order" items="${customerOrders}">
    <div class="order-item" data-status="${order.orderStatus}" data-order-id="${order.orderId}">
      <div class="order-header">
        <span>${order.customer.fullName}</span>
      </div>
      <div class="order-content">
        <c:if test="${not empty order.orderItems}">
          <c:set var="firstItem" value="${order.orderItems[0]}" />
          <img src="${firstItem.productDTO.getBase64Image()}" alt="${firstItem.productDTO.productName}" />
          <div class="product-details">
            <p class="product-name">${firstItem.productDTO.productName}</p>
            <p class="product-variant">Phân loại hàng: ${firstItem.productDTO.color} - Size: ${firstItem.productDTO.size}</p>
            <p class="product-quantity">x${firstItem.quantity}</p>
          </div>
        </c:if>
      </div>

      <!-- Trạng thái đơn hàng -->
      <div class="order-status">
        <span class="status ${order.orderStatus}">${order.orderStatus}</span>
      </div>

      <!-- Footer đơn hàng -->
      <div class="order-footer">
        <a href="orderDetails?idOrder=${order.orderId}" class="btn btn-more">Xem chi tiết</a>
      </div>
    </div>
  </c:forEach>
</div>
</body>
<%@ include file="/view/footer.jsp" %> <!-- Include footer.jsp -->
</html>
