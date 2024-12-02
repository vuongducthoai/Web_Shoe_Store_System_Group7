<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 11/20/2024
  Time: 10:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="../css/header.css">
</head>
<body>
<section >
    <nav>
        <div class="logo">
            <h1>Shoe<span>Store</span></h1>
        </div>

        <ul>
            <li><a href="#Home">Home</a></li>
            <li><a href="#Products">Products</a></li>
            <li><a href="#About">About</a></li>
            <li><a href="#Review">Review</a></li>
            <li><a href="#Servises">Servises</a></li>
        </ul>

        <div class="icons">
<<<<<<< Updated upstream
            <i style="font-size: 30px; position: relative;" class="fa-solid fa-bell"><span style="position: absolute;left: 20px; top: -10px;font-size: 18px; color: red">3</span></i>
            <i onclick="redirectToCartPage()" style="font-size: 30px; margin-left: 20px; position: relative;" class="fa-solid fa-cart-shopping">
                <span class="quantityItemCart" style="position: absolute;left: 35px; top: -10px;font-size: 18px; color: red">
                    0
                </span></i>
            <i style="font-size: 30px; margin-left: 20px;" class="fa-solid fa-user"></i>
=======
            <i onclick = "redirectToChatPage()" class="fa-solid fa-message" style="font-size: 30px; position: relative; margin-right: 15px">
                <span style="position: absolute;left: 30px; top: -10px;font-size: 18px; color: red">0</span>
            </i>
            <i onclick = "redirectToNotifyPage()" style="font-size: 30px; position: relative;" class="fa-solid fa-bell">
                <span style="position: absolute;left: 20px; top: -10px;font-size: 18px; color: red">0
                </span>
            </i>
            <i onclick="redirectToCartPage()" style="font-size: 30px; margin-left: 15px; position: relative;" class="fa-solid fa-cart-shopping">
                    <span class="quantityItemCart" style="position: absolute;left: 35px; top: -10px;font-size: 18px; color: red">
                        0
                    </span></i>
>>>>>>> Stashed changes


            <c:choose>
                <c:when test="${sessionScope.user == null}">
                    <a class="sign-in" href="/sign-in">Đăng nhập</a>
                    <span style="font-size: 20px; font-weight: 900;">|</span>
                    <a class="sign-up" href="/sign-up">Đăng ký</a>
                </c:when>
                <c:otherwise>
                    <a class="sign-out" href="/logout">Đăng xuất</a>
                </c:otherwise>
            </c:choose>

        </div>
    </nav>
</section>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/js/ItemCart.js"></script>
</html>