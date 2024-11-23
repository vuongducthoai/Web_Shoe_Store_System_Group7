<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Lịch Sử Mua Hàng</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f5f5f5;
    }

    .order-history {
      width: 80%;
      margin: 20px auto;
      background-color: #fff;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .order-filter ul {
      display: flex;
      list-style: none;
      padding: 0;
      margin: 0 0 20px 0;
      border-bottom: 2px solid #f5f5f5;
    }

    .order-filter li {
      padding: 10px 20px;
      cursor: pointer;
      color: #555;
    }

    .order-filter li.active {
      font-weight: bold;
      color: #ee4d2d;
      border-bottom: 2px solid #ee4d2d;
    }

    .order-list {
      padding: 10px 0;
    }

    .order-item {
      border-bottom: 1px solid #f5f5f5;
      padding: 15px 0;
    }

    .order-item:last-child {
      border-bottom: none;
    }

    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
    }

    .order-header span {
      font-size: 16px;
      font-weight: bold;
    }

    .order-header .btn-shop {
      background-color: #f5f5f5;
      border: 1px solid #ddd;
      padding: 5px 10px;
      cursor: pointer;
      font-size: 14px;
      border-radius: 4px;
    }

    .order-content {
      display: flex;
      align-items: center;
    }

    .order-content img {
      width: 100px;
      height: 100px;
      margin-right: 15px;
      border: 1px solid #ddd;
      border-radius: 4px;
    }

    .product-details {
      flex-grow: 1;
    }

    .product-name {
      font-size: 16px;
      font-weight: bold;
      margin: 0;
    }

    .product-variant,
    .product-quantity {
      font-size: 14px;
      color: #666;
    }

    .order-status {
      text-align: right;
    }

    .status {
      display: block;
      font-size: 14px;
      margin-bottom: 5px;
    }

    .status.success {
      color: #2fb917;
    }

    .status.completed {
      color: #ee4d2d;
      font-weight: bold;
    }

    .order-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 10px;
    }

    .order-footer .order-total {
      font-size: 16px;
      font-weight: bold;
    }

    .order-footer .btn {
      padding: 5px 10px;
      font-size: 14px;
      cursor: pointer;
      border: none;
      border-radius: 4px;
    }

    .order-footer .btn-rate {
      background-color: #ee4d2d;
      color: white;
    }

    .order-footer .btn-refund {
      background-color: #f5f5f5;
      color: #555;
    }

    .order-footer .btn-more {
      background-color: #f5f5f5;
      color: #555;
    }
  </style>
</head>
<body>
<div class="order-history">
  <h2>Lịch sử mua hàng</h2>
  <div class="order-list">
    <!-- Duyệt qua danh sách đơn hàng từ request -->
    <c:forEach var="order" items="${customerOrders}">
      <div class="order-item">
        <div class="order-header">
          <span>Mã đơn hàng: ${order.orderId}</span>
          <span>Ngày đặt: ${order.orderDate}</span>
          <span>Trạng thái: ${order.orderStatus}</span>
          <span>Tổng tiền: ${order.payment.amount} VNĐ</span>
        </div>
        <div class="order-content">
          <c:forEach var="orderItem" items="${order.orderItems}">
            <div class="product-details">
              <p class="product-name">${orderItem.productName}</p>
              <p class="product-variant">Kích cỡ: ${orderItem.size}, Màu: ${orderItem.color}</p>
              <p class="product-quantity">Số lượng: ${orderItem.quantity}</p>
            </div>
          </c:forEach>
        </div>
        <div class="order-status">
          <button class="btn btn-dark btn-sm">Xem</button>
        </div>
      </div>
    </c:forEach>
  </div>
</div>
</body>
</html>
