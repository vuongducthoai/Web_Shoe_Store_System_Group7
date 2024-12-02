<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông báo khách hàng</title>
    <link rel="stylesheet" href="./css/notify.css">
</head>
<body>
<!-- Thanh thông báo nổi -->
<div id="notification-banner" class="notification-banner">
    <p id="notification-text">Thông báo của bạn sẽ xuất hiện ở đây.</p>
</div>

<!-- Khu vực thông báo trong tài khoản -->
<div class="notifications">
    <h2>Thông báo của tôi</h2>
    <c:forEach var="notify" items="${notifications}">
        <div class="notification-item">
            <h3>Thông báo về đơn hàng</h3>
            <p>${notify.content}</p>
            <p><strong>Ngày:</strong> ${notify.timeStamp.toString()}</p>
        </div>
    </c:forEach>
</div>
</body>
</html>
