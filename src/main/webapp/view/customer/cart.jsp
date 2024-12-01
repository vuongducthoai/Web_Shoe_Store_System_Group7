<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.text.NumberFormat" %><%--
  Created by IntelliJ IDEA.
  User: hung5
  Date: 10/11/2024
  Time: 9:38 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>
        SHOP.CO - Cart
    </title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/toastMessage.css">
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
            margin: 7rem 0 2rem 0;
        }
        .cart-item {
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            padding: 0.5rem;
            margin-bottom: 0.5rem;
        }
        .cart-item img {
            width: 150px;
            height: 150px;
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
        .custom-bg {
            background-color: rgba(255, 255, 255, 0.9); /* Trắng pha chút trong suốt */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Thêm chút bóng đen nhẹ */
        }
    </style>
</head>
<body>
<div id="custom-toast"></div>
<header>
    <jsp:include page="../../view/header.jsp"></jsp:include>
</header>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<main>
<div class="container" style="margin: 69px;">
    <h2 class="cart-title">
        GIỎ HÀNG
    </h2>
    <div class="row">
        <div class="col-md-8">
            <c:forEach var="cart" items="${CartList}">
                <div class="cart-item d-flex align-items-center custom-bg">
                    <img class="border border-dark" alt="Gradient Graphic T-shirt" height="150" src="${cart.productDTO.getBase64Image()}" width="150"/>
                    <div class="ms-3">
                        <h4>
                            ${cart.productDTO.productName}
                        </h4>
                        <p>
                            Kích cỡ: ${cart.productDTO.size}
                            <br/>
                        <div class="d-flex align-items-center">
                            <span>Màu sắc:</span>
                            <div class="border rounded ms-2"
                                 style="background-color: ${cart.productDTO.color}; width: 30px; height: 30px;"></div>
                        </div>
                        </p>
                        <p class="text-muted">
                            Số lượng còn lại: ${cart.productDTO.quantity}
                        </p>
                        <p class="fw-bold">
                            <fmt:formatNumber value="${cart.productDTO.price}" groupingUsed="true"/> VND
                        </p>
                    </div>
                    <div class="ms-auto d-flex align-items-center">
                        <form action="/Cart/Remove" method="post">
                            <input type="text" value="${cart.cartItemId}" name="cartItemId" hidden="hidden"/>
                            <input type="text" value="${idPr}" name="idPr" hidden="hidden"/>
                            <button class="btn btn-outline-secondary">
                                -
                            </button>
                        </form>
                        <span class="mx-2">
                                ${cart.quantity}
                       </span>
                        <form action="/Cart/Add" method="post">
                            <input type="text" value="${cart.productDTO.productId}" name="idProduct" hidden="hidden"/>
                            <input type="text" value="${idPr}" name="idPr" hidden="hidden"/>
                            <button class="btn btn-outline-secondary">
                                +
                            </button>
                        </form>
                        <form action="/Cart/Delete_Item" method="post">
                            <input type="text" value="${cart.cartItemId}" name="cartItemId" hidden="hidden">
                            <input type="text" value="${idPr}" name="idPr" hidden="hidden"/>
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
                    Tổng quan đơn hàng
                </h5>
                <p>
                    Tạm tính
                    <span class="float-end">
                        <fmt:formatNumber value="${total}" groupingUsed="true"/> VND
                    </span>
                </p>
                <p>
                    Giảm giá
                    <span class="float-end text-danger">
                        -<fmt:formatNumber value="${discount}" groupingUsed="true"/> VND
                    </span>
                </p>
                <p>
                    Phí giao hàng
                    <span class="float-end">
                        <fmt:formatNumber value="${feeShip}" groupingUsed="true"/> VND
                    </span>
                </p>
                <p class="d-flex align-items-center" >
                    <span class="me-2" style="white-space: nowrap;">Mã giảm giá</span>
                    <select name="voucher" class="form-select" aria-label="Voucher select" onchange="ChangeSelect()">
                        <option value="-1">Chọn mã giảm giá</option>
                        <c:forEach var="item" items="${promotion}">
                            <option value="${item.promotionId}"
                                    <c:if test="${item.promotionId == idPr}">selected</c:if>
                            >${item.promotionName} -
                                <c:choose>
                                    <c:when test="${item.discountType=='VND'}">
                                        <fmt:formatNumber value="${item.discountValue}" groupingUsed="true"/>VND
                                    </c:when>
                                    <c:otherwise>
                                        ${item.discountValue}%
                                    </c:otherwise>
                                </c:choose>
                            </option>
                        </c:forEach>
                    </select>
                </p>
                <hr/>
                <p class="total">
                    Tổng
                    <span class="float-end">
                        <fmt:formatNumber value="${Sum}" groupingUsed="true"/> VND
                    </span>
                </p>
                <form action="/Momo_pay" method="post">
                    <input type="text" name="cartItem" value="${JsonCart}" hidden="hidden">
                    <input type="text" name="Total" value="${Sum}" hidden="hidden">
                    <input type="text" name="Discount" value="${discount}" hidden="hidden">
                    <input type="text" name="feeShip" value="${feeShip}" hidden="hidden">
                    <button class="btn btn-dark w-100">
                        Tiến hành thanh toán
                        <i class="fas fa-arrow-right">
                        </i>
                    </button>
                </form>
<%--                <form action="/Cart/Add" method="post">--%>
                    <input type="text" value="17" name="idProduct" class = "inputID"/>
                    <input type="text" value="17" name="idProduct" class = "inpQuantity"/>
                    <button class="btn btn-dark w-100" onclick="AddItemQuantity()">
                        TWP
                        <i class="fas fa-arrow-right">
                        </i>
                    </button>
<%--                </form>--%>
            </div>
        </div>
    </div>
</div>
</main>
<footer>
<jsp:include page="../../view/footer.jsp"></jsp:include>
</footer>
<script src="${pageContext.request.contextPath}/js/cart.js"></script>
<script>
    var errCode = "${errCode}";
    var message = "${message}";
    if (errCode.length>0){
        code = parseInt(errCode,10)
        showError(code,message)
    }
</script>
</body>
</html>
