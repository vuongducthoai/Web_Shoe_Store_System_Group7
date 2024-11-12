<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %><%--
  Created by IntelliJ IDEA.
  User: hung5
  Date: 10/11/2024
  Time: 9:38 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>
        SHOP.CO - Cart
    </title>
    <link crossorigin="anonymous" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .navbar {
            background-color: #f8f9fa;
            padding: 1rem;
        }
        .navbar-brand {
            font-weight: bold;
            font-size: 1.5rem;
        }
        .navbar-nav .nav-link {
            margin-right: 1rem;
        }
        .cart-title {
            font-weight: bold;
            font-size: 2rem;
            margin: 2rem 0;
        }
        .cart-item {
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            padding: 0.5rem;
            margin-bottom: 0.5rem;
        }
        .cart-item img {
            width: 60px;
            height: 60px;
            border-radius: 10px;
        }
        .order-summary {
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            padding: 1rem;
        }
        .order-summary h5 {
            font-weight: bold;
        }
        .order-summary .total {
            font-size: 1.5rem;
            font-weight: bold;
        }
        .promo-code {
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            padding: 0.5rem;
            margin-bottom: 1rem;
        }
        .footer {
            background-color: #f8f9fa;
            padding: 2rem 0;
        }
        .footer h5 {
            font-weight: bold;
        }
        .footer a {
            color: #000;
            text-decoration: none;
        }
        .footer a:hover {
            text-decoration: underline;
        }
        .newsletter {
            background-color: #000;
            color: #fff;
            padding: 2rem;
            text-align: center;
        }
        .newsletter input {
            border-radius: 20px;
            padding: 0.5rem 1rem;
            margin-right: 1rem;
        }
        .newsletter button {
            border-radius: 20px;
            padding: 0.5rem 1rem;
            background-color: #fff;
            color: #000;
            border: none;
        }
    </style>
</head>
<body>
<div class="container">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item">
                <a href="#">
                    Home
                </a>
            </li>
            <li aria-current="page" class="breadcrumb-item active">
                Cart
            </li>
        </ol>
    </nav>
    <h2 class="cart-title">
        YOUR CART
    </h2>
    <div class="row">
        <div class="col-md-8">
            <c:forEach var="cart" items="${CartList}">
                <div class="cart-item d-flex align-items-center">
                    <img alt="Gradient Graphic T-shirt" height="60" src="${cart.productDTO.image}" width="60"/>
                    <div class="ms-3">
                        <h5>
                            ${cart.productDTO.productName}
                        </h5>
                        <p>
                            Size: ${cart.productDTO.size}
                            <br/>
                            Color: ${cart.productDTO.color}
                        </p>
                        <p class="fw-bold">
                                ${cart.productDTO.price} VND
                        </p>
                    </div>
                    <div class="ms-auto d-flex align-items-center">
                        <form action="/Cart/Remove" method="post">
                            <input type="text" value="${cart.cartItemId}" name="cartItemId" hidden="hidden"/>
                            <button class="btn btn-outline-secondary">
                                -
                            </button>
                        </form>
                        <span class="mx-2">
                                ${cart.quantity}
                       </span>
                        <form action="/Cart/Add" method="post">
                            <input type="text" value="${cart.productDTO.productId}" name="idProduct" hidden="hidden"/>
                            <button class="btn btn-outline-secondary">
                                +
                            </button>
                        </form>
                        <form action="/Cart/Delete_Item" method="post">
                            <input type="text" value="${cart.cartItemId}" name="cartItemId" hidden="hidden">
                            <button type="submit" class="btn btn-outline-danger ms-3">
                                <i class="fas fa-trash">
                                </i>
                            </button>
                        </form>
                    </div>
                </div>

            </c:forEach>

        </div>
        <div class="col-md-4">
            <div class="order-summary">
                <h5>
                    Order Summary
                </h5>
                <p>
                    Subtotal
                    <span class="float-end">
        ${total} VND
       </span>
                </p>
                <p>
                    Discount
                    <span class="float-end text-danger">
        -${discount} VND
       </span>
                </p>
                <p>
                    Delivery Fee
                    <span class="float-end">
        ${feeShip} VND
       </span>
                </p>
                <hr/>
                <p class="total">
                    Total
                    <span class="float-end">
        ${Sum} VND
       </span>
                </p>
                <form action="Momo_pay" method="post">
                    <input type="text" name="cartItem" value="${JsonCart}" hidden="hidden">
                    <input type="text" name="Total" value="${Sum}" hidden="hidden">
                    <button class="btn btn-dark w-100">
                        Go to Checkout
                        <i class="fas fa-arrow-right">
                        </i>
                    </button>
                </form>
                <form action="Cart/Add" method="post">
                    <input type="text" value="1" name="idProduct" hidden="hidden"/>
                    <button class="btn btn-dark w-100">
                        TWP
                        <i class="fas fa-arrow-right">
                        </i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
