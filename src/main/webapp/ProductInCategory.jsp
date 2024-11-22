<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 11/21/2024
  Time: 5:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ProductInCategory.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>

<body>
<div id="header">
    <input type="text" name="textName" class="text-Name" placeholder="Tìm kiếm tên giày">
    <!-- Begin: Search Button -->
    <div class="search-btn" style="height: 100%; display: flex; align-items: center;">
        <i class="fa-solid fa-magnifying-glass" style="color:white;"></i>
    </div>
    <!-- Begin: Search Button -->


</div>
<div id="main">
    <div class="content-section">
        <h2 class="section-heading text-black">Giày Nam</h2>
        <h2 class="shoe-quantity text-black">Số lượng: </h2>


        <div class="row shoes-list">
            <div class="col col-third">
                <img src="./assets/img/Adidas1.jpg" alt="Shoe" class="shoe-img">
                <div class="shoe-body">
                    <h2 class="shoe-name">Giày Adidas1</h2>
                    <p class="shoe-price">Price: 120$</p>
                    <p class="shoe-color">Color: White</p>
                    <p class="shoe-size">Size: 30 31 32 33</p>
                    <button class="btn js-buy-ticket">Xóa sản phẩm</button>
                </div>
            </div>

            <div class="col col-third">
                <img src="./assets/img/Adidas2.jpg" alt="Shoe" class="shoe-img">
                <div class="shoe-body">
                    <h2 class="shoe-name">Giày Adidas2</h2>
                    <p class="shoe-price">Price: 120$</p>
                    <p class="shoe-color">Color: White</p>
                    <p class="shoe-size">Size: 30 31 32 33</p>
                    <button class="btn js-buy-ticket">Xóa sản phẩm</button>
                </div>
            </div>

            <div class="col col-third">
                <img src="./assets/img/Adidas3.jpg" alt="Shoe" class="shoe-img">
                <div class="shoe-body">
                    <h2 class="shoe-name">Giày Adidas3</h2>
                    <p class="shoe-price">Price: 120$</p>
                    <p class="shoe-color">Color: White</p>
                    <p class="shoe-size">Size: 30 31 32 33</p>
                    <button class="btn js-buy-ticket">Xóa sản phẩm</button>
                </div>
            </div>

            <div class="col col-third">
                <img src="./assets/img/Nike1.jpg" alt="Shoe" class="shoe-img">
                <div class="shoe-body">
                    <h2 class="shoe-name">Giày Nike1</h2>
                    <p class="shoe-price">Price: 120$</p>
                    <p class="shoe-color">Color: White</p>
                    <p class="shoe-size">Size: 30 31 32 33</p>
                    <button class="btn js-buy-ticket">Xóa sản phẩm</button>
                </div>
            </div>

            <div class="col col-third">
                <img src="./assets/img/Nike2.jpg" alt="Shoe" class="shoe-img">
                <div class="shoe-body">
                    <h2 class="shoe-name">Giày Nike2</h2>
                    <p class="shoe-price">Price: 120$</p>
                    <p class="shoe-color">Color: White</p>
                    <p class="shoe-size">Size: 30 31 32 33</p>
                    <button class="btn js-buy-ticket">Xóa sản phẩm</button>
                </div>
            </div>




        </div>
    </div>
</div>
</body>

</html>