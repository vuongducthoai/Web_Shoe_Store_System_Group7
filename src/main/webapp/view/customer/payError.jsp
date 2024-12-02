<%--
  Created by IntelliJ IDEA.
  User: hung5
  Date: 11/11/2024
  Time: 1:15 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Payment Error</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"></link>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
        }
    </style>
</head>
<body class="bg-gray-100 flex items-center justify-center h-screen">
<div class="bg-white p-8 rounded-lg shadow-lg text-center">
    <i class="fas fa-exclamation-triangle text-red-500 text-6xl mb-4"></i>
    <h1 class="text-2xl font-bold text-red-500 mb-2">Lỗi giao dịch</h1>
    <p class="text-gray-700 mb-4">Đã có vấn đề trong việc xử lý giao dịch của bạn. Vui lòng liên hệ với chủ cửa hàng để hoàn lại tiền</p>
    <a href="/home" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Quay về trang chủ</a>
</div>
</body>
</html>
