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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/toastMessage.css">
    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'poppins';
            text-transform: capitalize;
            text-decoration: none;
        }
        li{
            list-style-type: none;
            text-transform: capitalize;
        }

        .nav nav ul{
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 30px;
            width: 100%;
        }
        .nav .nav-icon{
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 20px;
        }
        .fa-solid{
            color: var(--primar-clr);
            transition: var(--transition-one);
        }
        .fa-solid:hover{
            color: var(--first-gra-clr);
        }

        section nav{
            display: flex;
            align-items: center;
            justify-content: space-around;
            box-shadow: 0 0 8px rgba(0,0,0,0.6);
            background: white;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 100;
        }

        section nav .logo{
            font-size: 35px;
            color: #c72092;
            margin: 5px 0;
            cursor: pointer;
            position: relative;
            left: -4%;
        }

        section nav .logo span{
            color: #6c14d0;
            text-decoration: underline;
        }

        section nav ul{
            list-style: none;
        }


        section nav ul li{
            display: inline-block;
            margin: 5px 15px;
        }

        section nav ul li a{
            font-size: 20px;
            text-decoration: none;
            color: black;
            transition: 0.2s;
        }

        section nav ul li a:hover{
            color: #c72092;
        }

        section nav .icons i{
            margin: 0 4px;
            cursor: pointer;
            font-size: 18px;
            transition: 0.3s;
        }

        section nav .icons i:hover{
            color: #c72092;
        }

        nav .icons a {
            color: orangered !important;
            font-size: 20px;
            font-weight: 600;
            margin-left: 10px;
            display: inline-block;
        }

        nav .icons a:hover {
            color: #c72092 !important;
        }


        .user-icon {
            position: relative; /* Ensure the dropdown menu is positioned relative to the icon */
        }

        .dropdown-menu {
            display: none; /* Hide the dropdown by default */
            position: absolute;
            top: 100%; /* Position it right below the icon */
            left: -50px;
            background-color: #fff;
            border: 1px solid #ccc;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 200px;
            z-index: 1; /* Ensure it appears on top */
        }

        .user-icon:hover .dropdown-menu {
            display: block; /* Show the dropdown when hovering over the user icon */
        }

        .dropdown-menu a {
            display: block;
            display: block;
            padding: 10px;
            text-decoration: none;
            color: #333;
        }

        .dropdown-menu a:hover {
            background-color: #f0f0f0; /* Add hover effect for links */
        }

    </style>
</head>
<body>
<section >
    <nav>
        <a style="text-decoration: none" href="/home" class="logo">
            <img src="../image/shoe.png">
        </a>

        <ul>
            <li><a href="/home">Trang Chủ</a></li>
            <li><a href="/customer/product/filter">Sản Phẩm</a></li>
            <li><a href="#Review">Về Chúng Tôi</a></li>
            <li><a href="#Servises">Dịch Vụ</a></li>
        </ul>

        <div class="icons">
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


                <c:choose>
                    <c:when test="${sessionScope.user == null}">
                        <a class="sign-in" href="/sign-in">Đăng nhập</a>
                        <span style="font-size: 20px; font-weight: 900;">|</span>
                        <a class="sign-up" href="/sign-up">Đăng ký</a>
                    </c:when>
                    <c:otherwise>
                         <span class="user-icon">
                            <i style="font-size: 30px; margin-left: 20px;" class="fa-solid fa-user"></i>
                            <div class="dropdown-menu">
                                <a href="#">Tài Khoản Của Tôi</a>
                                <a class="sign-out" href="/logout">Đăng xuất</a>
                            </div>
                         </span>
                    </c:otherwise>
                </c:choose>

        </div>
    </nav>
</section>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/js/toastMessage.js"></script>
<script src="${pageContext.request.contextPath}/js/ItemCart.js"></script>
<script src="${pageContext.request.contextPath}/js/DirectHeader.js"></script>
</body>
</html>