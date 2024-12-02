<%--
  Created by IntelliJ IDEA.
  User: hung5
  Date: 11/11/2024
  Time: 1:13 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Success</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"></link>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f8f9fa;
        }
        .success-icon {
            font-size: 5rem;
            color: #28a745;
        }
        .container {
            margin-top: 100px;
        }
    </style>
</head>
<body>
<div class="container text-center">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <i class="fas fa-check-circle success-icon"></i>
                    <h1 class="card-title mt-3">Giao dịch thành công!</h1>
                    <p class="card-text">Cảm ơn bạn đã mua hàng. Đơn hàng của bạn đã được xử lý thành công.</p>
                    <a href="/home" class="btn btn-primary mt-3">Quay về trang chủ</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
