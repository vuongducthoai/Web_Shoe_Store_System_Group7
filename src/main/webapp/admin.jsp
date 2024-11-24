<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 11/21/2024
  Time: 5:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminPage.css">
    <title>Document</title>

    <!-- bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">



</head>

<body>
<div class="container">
    <header>
        <div class="header-content">
            <div class="logo">Admin</div>
            <nav>
                <a href="#" class="active">Home</a>
                <a href="#">Contact</a>
                <a href="#">About</a>
                <a href="#">Sign Up</a>
            </nav>
            <div class="search-and-icons">
                <input type="text" placeholder="What are you looking for?">
                <i class="icon">🔍</i>
                <i class="icon">❤️</i>
                <i class="icon">🛒</i>
            </div>
        </div>
        <div class="full-width-line"></div>
    </header>

    <!-- Main -->
    <div class="main-content">
        <aside class="sidebar">
            <ul>
                <li><a href="#" id="manage-products-btn">Quản lý sản phẩm</a></li>
                <li><a href="#" id="manage-categrories-btn">Quản lý danh mục</a></li>
                <li><a href="#" id="account-management-btn">Quản lý tài khoản khách hàng</a></li>
                <li><a href="#" id="order-management-btn">Quản lý đơn hàng</a></li>
                <li><a href="#" id="promotion-management-btn">Quản lý khuyến mãi</a></li>
            </ul>
        </aside>
        <section class="main-panel" id="panel">

            <!-- ảnh trang chủ  -->
            <img id="banner-section" src="./assets/img/CR7-Banner.jpg" alt="Banner" width="100%" height="10s0%">


            <!-- chức năng quản lý sản Phẩm -->
            <div class="product-management" id="product-management">
                <div class="product-management-header">
                    <div class="title">Quản lý sản phẩm</div>
                    <div class="table-container">
                        <table class="table table-bordered table-hover">
                            <tr class="row-dark">
                                <th>ID</th>
                                <th>Tên Sản Phẩm</th>
                                <th>Giá</th>
                                <th>Hình ảnh</th>
                                <th>màu sắc</th>
                                <th>size</th>
                                <th>Danh mục</th>
                                <th>Mô tả</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td>${product.productId}</td>
                                    <td>${product.productName}</td>
                                    <td>${product.price}</td>
                                    <td>
                                        <img  src="${product.getBase64Image()}" alt="${product.productName}" width="100" height="100">
                                    </td>
                                    <td>${product.color}</td>
                                    <td>${product.size}</td>
                                    <td>
                                        <c:if test="${not empty product.categoryDTO}">
                                            ${product.categoryDTO.categoryName}
                                        </c:if>
                                    </td>

                                    <td>${product.description}</td>
                                    <td>${product.status}</td>
                                    <td>
                                        <a href="#" class="btn-edit"
                                           data-product-id="${product.productId}"
                                           data-product-name="${product.productName}"
                                           data-product-price="${product.price}"
                                           data-product-color="${product.color}"
                                           data-product-size="${product.size}"
                                           data-product-img = "${product.getBase64Image()}"
                                           data-product-description="${product.description}"
                                           data-category-name="${product.categoryDTO.categoryName}"
                                           onclick="editProduct(this)">Sửa</a>
                                    </td>
                                </tr>
                            </c:forEach>

                        </table>
                    </div>
                    <br>
                    <div class="product-management-actions">
                        <button class="action-btn" id="btn-product-management-actions-add">Thêm</button>
                        <button class="action-btn" id="btn-product-management-actions-edit">Sửa</button>
                        <button class="action-btn" id="btn-product-management-actions-delete">Xóa</button>

                    </div>

                    <!-- nút thêm sản phẩm -->
                    <form action="ProductController" method="post">
                    <div class="product-management-form" id="add-product-management-form">
                        <h3>Thêm sản phẩm mới</h3>
                        <label for="product-id">ID</label>
                        <input type="text" id="add-product-id" name="productId" placeholder="Nhập ID sản phẩm">

                        <label for="product-name">Tên sản phẩm</label>
                        <input type="text" id="product-name" name="productName" placeholder="Nhập tên sản phẩm">

                        <label for="add-product-price">Giá</label>
                        <input type="text" id="add-product-price" name="productPrice" placeholder="Nhập giá sản phẩm">

                        <!-- <label for="product-image">Hình ảnh (URL)</label>
                        <input type="text" id="product-image" placeholder="Nhập URL hình ảnh"> -->

                        <div class="LoadImageContent">
                            <label >Hình ảnh</label>
                            <div class="picturebox">
                                <img class="imageDisplay" src="" alt="No image" />
                            </div>
                            <button class="loadImageBtn">Load Image</button>
                            <input type="file" name="productImage" class="imageInput" style="display: none;" accept="image/*">
                            <button class="cancelBtn">Cancel Image</button>
                        </div>

                        <label for="add-product-color">Màu sắc</label>
                        <input type="text" id="add-product-color" name="productColor" placeholder="Nhập màu sắc sản phẩm">

                        <label for="product-size">Size</label>
                        <input type="text" id="product-size" name="productSize" placeholder="Nhập size sản phẩm">

                        <label for="add-product-category">Danh mục</label>
                        <select id="add-product-category" name="CategoryName">
                            <c:forEach var="category" items="${CategoryList}">
                                <option value="${category.getCategoryName()}">
                                        ${category.getCategoryName()}
                                </option>
                            </c:forEach>
                        </select>

                        <label for="add-product-description">Mô tả</label>
                        <input type="text" id="add-product-description" name="productDescription" placeholder="Nhập mô tả sản phẩm">

                        <button class="action-btn" name="submitAction" value="add-product">Thêm sản phẩm</button>
                    </div>
                    </form>

<%--                    Nút sửa sản phẩm--%>
                    <form action="ProductController" method="post">
                    <div class="product-management-form" id="edit-product-management-form">
                        <h3>Sửa sản phẩm</h3>
                        <label for="product-id">ID</label>
                        <input type="text" id="product-id" placeholder="Nhập ID sản phẩm">

                        <label for="product-name">Tên sản phẩm</label>
                        <input type="text" id="edit-product-name" placeholder="Nhập tên sản phẩm">

                        <label for="edit-product-price">Giá</label>
                        <input type="text" id="edit-product-price" placeholder="Nhập giá sản phẩm">

                        <div class="LoadImageContent">
                            <label >Hình ảnh</label>
                            <div class="picturebox">
                                <img class="imageDisplay" src="" alt="No image" />
                            </div>
                            <button class="loadImageBtn">Load Image</button>
                            <input type="file" class="imageInput" style="display: none;" accept="image/*">
                            <button class="cancelBtn">Cancel Image</button>
                        </div>

                        <label for="product-color">Màu sắc</label>
                        <input type="text" id="product-color" placeholder="Nhập màu sắc sản phẩm">

                        <label for="product-size">Size</label>
                        <input type="text" id="edit-product-size" placeholder="Nhập size sản phẩm">

                        <label for="edit-product-category">Danh mục</label>
                        <select id="edit-product-category" name="CategoryName">
                            <c:forEach var="category" items="${CategoryList}">
                                <option value="${category.getCategoryName()}">
                                        ${category.getCategoryName()}
                                </option>
                            </c:forEach>
                        </select>

                        <label for="edit-product-description">Mô tả</label>
                        <input type="text" id="edit-product-description" placeholder="Nhập mô tả sản phẩm">

                        <button class="action-btn">Sửa sản phẩm</button>
                    </div>
                    </form>

                    <%-- Nút xóa sản phẩm--%>
                    <div class="product-management-form" id="delete-product-management-form">
                        <h3>Xóa sản phẩm</h3>
                        <label for="product-id">ID</label>
                        <input type="text" id="delete-product-id" placeholder="Nhập ID sản phẩm">
                        <button class="action-btn">Xóa sản phẩm</button>
                    </div>
                </div>
            </div>
            <!-- kết thúc quản lý khách hàng -->

            <!-- Quản lý danh mục sản phẩm -->
            <div class="category-management" id="category-management">
                <div class="category-management-header">
                    <div class="title">Quản lý danh mục sản phẩm</div>
                    <div class="table-container">
                        <table class="table table-bordered table-hover ">
                            <tr class="row-dark">
                                <th>ID</th>
                                <th>Tên danh mục</th>
                                <th>Sản phẩm trong danh mục</th>

                            </tr>
                            <tr>
                                <td>1</td>
                                <td>Nam</td>
                                <td><a href="ThongTinSanPham.html" class="view-info-btn">Xem</a></td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>Nữ</td>
                                <td><a href="ThongTinSanPham.html" class="view-info-btn">Xem</a></td>
                            </tr>

                        </table>
                    </div>
                    <br>
                    <div class="category-management-actions">
                        <button class="action-btn" id="btn-category-management-actions-add">Thêm</button>
                        <button class="action-btn" id="btn-category-management-actions-edit">Sửa</button>
                        <button class="action-btn" id="btn-category-management-actions-delete">Xóa</button>

                    </div>

                    <!-- nút thêm sản phẩm -->
                    <div class="category-management-form" id="add-category-management-form">
                        <h3>Thêm danh mục mới</h3>
                        <label for="add-category-id">ID</label>
                        <input type="text" id="add-category-id" placeholder="Nhập ID danh mục">

                        <label for="add-category-name">Tên danh mục</label>
                        <input type="text" id="add-category-name" placeholder="Nhập tên danh mục">


                        <button class="action-btn">Thêm danh mục</button>

                    </div>
                    <%-- sửa--%>
                    <div class="category-management-form" id="edit-category-management-form">
                        <h3>Sửa danh mục</h3>
                        <label for="edit-category-id">ID</label>
                        <input type="text" id="edit-category-id" placeholder="Nhập ID danh mục">

                        <label for="edit-category-name">Tên danh mục</label>
                        <input type="text" id="edit-category-name" placeholder="Nhập tên danh mục">
                        <button class="action-btn">Sửa danh mục</button>


                    </div>
                    <div class="category-management-form" id="delete-category-management-form">
                        <h3>Xóa danh mục</h3>
                        <label for="delete-category-id">ID</label>
                        <input type="text" id="delete-category-id" placeholder="Nhập ID danh mục">
                        <button class="action-btn">Xóa danh mục</button>
                    </div>
                </div>
            </div>
            <!-- Kết thúc quản lý danh mục sản phẩm -->

            <!-- Quản lý tài khoản khách hàng  -->
            <div class="account-management" id="account-management">
                <div class="title">Quản lý tài khoản khách hàng</div>
                <input type="text" id="name-custommer" placeholder="Nhập họ tên khách hàng">
                <button class="action-btn">Tìm</button>
                <div class="table-container">
                    <table class="table table-bordered table-hover">
                        <tr class="row-dark">
                            <th>ID</th>
                            <th>Tên Khách Hàng</th>
                            <th>Email</th>
                            <th>Mật khẩu</th>
                            <th>Số điện thoại</th>
                        </tr>
                        <tr>
                            <td>1</td>
                            <td>Luc</td>
                            <td>lelamluc1234@gmail.com</td>
                            <td>12345</td>
                            <td>0947332839</td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>Luc2</td>
                            <td>lela31mluc1234@gmail.com</td>
                            <td>12345</td>
                            <td>0947332839</td>
                        </tr>
                    </table>
                </div>
                <br>
                <div class="account-management-actions">
                    <button class="action-btn" id="btn-account-management-actions-view">Xem thông tin chi tiết khách hàng</button>
                    <button class="action-btn" id="btn-account-management-actions-edit">Sửa thông tin khách hàng</button>
                    <button class="action-btn" id="btn-account-management-actions-delete">Chặn khách hàng</button>
                </div>

                <!-- xem thông tin khách hàng  -->
                <div class="account-management-form" id="view-account-management-form">
                    <h3>Thông tin chi tiết</h3>
                    <label for="view-account-id">ID</label>
                    <input type="text" id="view-account-id">

                    <label for="view-account-name">Họ Tên</label>
                    <input type="text" id="view-account-name">

                    <label for="view-account-email">Email</label>
                    <input type="text" id="view-account-email">

                    <label for="view-account-pass">Mật Khẩu</label>
                    <input type="text" id="view-account-pass">

                    <label for="view-account-phone">SĐT</label>
                    <input type="text" id="view-account-phone">

                    <label for="account-buy">Lịch sử mua hàng</label>
                    <input type="text" id="account-buy">
                </div>

                <!-- sửa thông tin khách hàng  -->
                <div class="account-management-form" id="edit-account-management-form">
                    <h3>Sửa thông tin khách hàng</h3>
                    <label for="edit-account-id">ID</label>
                    <input type="text" id="edit-account-id">

                    <label for="edit-account-name">Họ Tên</label>
                    <input type="text" id="edit-account-name">

                    <label for="edit-account-email">Email</label>
                    <input type="text" id="edit-account-email">

                    <label for="edit-account-pass">Mật Khẩu</label>
                    <input type="text" id="edit-account-pass">

                    <label for="edit-account-phone">SĐT</label>
                    <input type="text" id="edit-account-phone">

                    <button class="action-btn">Sửa thông tin</button>
                </div>
            </div>
            <!-- kết thúc quản lý tài khoản khách hàng -->

            <!-- chức năng quản lý đơn hàng -->
            <div class="order-management" id="order-management">
                <div class="title">Quản lý đơn hàng</div>
                <input type="text" id="order-search" placeholder="Nhập ID đơn hàng">
                <button class="action-btn" id="btn-search-order">Tìm</button>
                <div class="table-container">
                    <table class="table table-bordered table-hover">
                        <tr class="row-dark">
                            <th>ID Đơn Hàng</th>
                            <th>Tên Khách Hàng</th>
                            <th>Ngày Đặt</th>
                            <th>Trạng Thái</th>
                            <th>Tổng Giá Trị</th>
                            <th>Thao Tác</th>
                        </tr>
                        <tr>
                            <td>1001</td>
                            <td>Nguyễn Văn A</td>
                            <td>2023-10-01</td>
                            <td>Đang xử lý</td>
                            <td>1,500,000₫</td>
                            <td><button class="action-btn">Xem chi tiết</button></td>
                        </tr>
                        <tr>
                            <td>1002</td>
                            <td>Trần Thị B</td>
                            <td>2023-10-02</td>
                            <td>Hoàn thành</td>
                            <td>800,000₫</td>
                            <td><button class="action-btn">Xem chi tiết</button></td>
                        </tr>
                    </table>
                </div>
                <div class="order-management-actions">
                    <button class="action-btn" id="btn-order-view">Xem chi tiết đơn hàng</button>
                    <button class="action-btn" id="btn-order-edit">Cập nhật trạng thái</button>
                    <button class="action-btn" id="btn-order-delete">Hủy đơn hàng</button>
                </div>
            </div>
            <!-- kết thúc chức năng quản lý đơn hàng -->

            <!-- chức năng quản lý khuyến mãi -->
            <div class="promotion-management" id="promotion-management">
                <div class="title">Quản lý khuyến mãi</div>
                <div class="button-group">
                    <button type="button" onclick="showSection('promotions')">Promotion</button>
                    <button type="button" onclick="showSection('vouchers')">Voucher</button>
                </div>
                <br>
                <!-- promotion  -->
                <div class="promotions-container" id="promotions-container" >
                    <!-- Promotions Content -->
                    <div class="promotions-content">
                        <div class="promotions-items">
                            <%
                                int repeatCount = 20;
                                for (int i = 0; i < repeatCount; i++) {
                            %>
                            <div class="promotions-item">
                                <div class="item-info">
                                    <img src="KM.png" alt="Sample Promotion 1" class="item-img">
                                    <div class="item-details">

                                        <div class="item-title">Promotion Name: Birthday Sale</div>
                                        <div>Applicable period: 2023-12-01 - 2023-12-31</div>
                                        <div class="item-price">Discount: 10%</div>
                                    </div>
                                </div>
                            </div>
                            <div class="promotions-item">
                                <div class="item-info">
                                    <img src="KM.png" alt="Sample Promotion 1" class="item-img">
                                    <div class="item-details">
                                        <div class="item-title">Promotion Name: Summer Sale</div>
                                        <div>Applicable period: 2023-12-01 - 2023-12-31</div>
                                        <div class="item-price">Discount: 10%</div>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
                <!-- voucher  -->
                <div class="vouchers-container" id="vouchers-container" >
                    <!-- Vouchers Content -->
                    <div class="vouchers-items">
                        <div class="voucher-item">
                            <div class="item-info">
                                <img src="Voucher.png" alt="Voucher 1" class="item-img">
                                <div class="item-details">
                                    <div class="item-title">Voucher 1</div>
                                    <div>Voucher Code: WINTER20</div>
                                    <div>Discount: 20%</div>
                                    <div>Expiry: 2023-12-31</div>
                                </div>
                            </div>
                        </div>
                        <div class="voucher-item">
                            <div class="item-info">
                                <img src="Voucher.png" alt="Voucher 2" class="item-img">
                                <div class="item-details">
                                    <div class="item-title">Voucher 2</div>
                                    <div>Voucher Code: SUMMER15</div>
                                    <div>Discount: 15%</div>
                                    <div>Expiry: 2023-12-31</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Order Summary (This section remains unchanged) -->

                <div class="order-summary">
                    <div class="summary-title">Information</div>
                    <div class="summary-item"><span>Subtotal</span> <span>$565</span></div>
                    <div class="summary-item"><span>Discount (-20%)</span> <span>-$113</span></div>
                    <div class="summary-item"><span>Delivery Fee</span> <span>$15</span></div>
                    <div class="summary-total">Total: $467</div>

                    <!-- Promo Code -->
                    <div class="promo-code">
                        <input type="text" class="promo-input" placeholder="Add promo code">
                        <button class="apply-btn">Apply</button>
                    </div>

                    <!-- Checkout Button -->
                    <button class="checkout-btn">Go to Checkout</button>
                </div>
                <button class="add-promotion-btn">Add Promotion</button>

                <div class="overlay" id="overlay">
                    <div id="add-promotion-form">
                        <form>
                            <!-- Promotion Name -->
                            <div>
                                <div class="form-group">
                                    <label for="promotion-name">Promotion Name: </label>
                                    <input type="text" id="promotion-name" name="promotion-name" required>
                                </div>
                            </div>

                            <!-- Start Date and End Date -->
                            <div>
                                <div class="form-group">
                                    <label for="start-date">Start Date: </label>
                                    <input type="date" id="start-date" name="start-date" required>
                                </div>
                            </div>
                            <div>
                                <div class="form-group">
                                    <label for="end-date">End Date: </label>
                                    <input type="date" id="end-date" name="end-date" required>
                                </div>
                            </div>

                            <!-- Application Products -->
                            <div class="dropdown">
                                <!-- Button to show the dropdown menu -->
                                <label for="start-date">Application Products: </label>
                                <button type="button" class="dropdown-button" onclick="toggleDropdown()">Select Products</button>

                                <!-- Dropdown menu with checkboxes -->
                                <div class="dropdown-menu">
                                    <label><input type="checkbox" value="product1"> Product 1</label>
                                    <label><input type="checkbox" value="product2"> Product 2</label>
                                    <label><input type="checkbox" value="product3"> Product 3</label>
                                    <label><input type="checkbox" value="product4"> Product 4</label>
                                    <label><input type="checkbox" value="product5"> Product 5</label>
                                </div>
                            </div>

                            <!-- Submit Button to see selected items -->

                            <!-- Display the selected items -->
                            <div id="selected-products" style="margin-top: 20px;"></div>

                            <!-- Promotion Type -->
                            <div>
                                <div class="form-group">
                                    <label for="promotion-type">Promotion Type: </label>
                                    <select id="promotion-type" name="promotion-type" required>
                                        <option value="" disabled selected>Select Promotion Type</option>
                                        <option value="voucher">Voucher</option>
                                        <option value="promotion">Promotion</option>
                                    </select>
                                </div>
                            </div>

                            <!-- Discount Value and Unit -->
                            <div>
                                <div class="form-group">
                                    <label for="discount-value">Discount Value: </label>
                                    <input type="number" id="discount-value" name="discount-value" required>
                                    <select id="discount-unit" name="discount-unit" required>
                                        <option value="" disabled selected>Select Unit</option>
                                        <option value="%">%</option>
                                        <option value="VND">VND</option>
                                    </select>
                                </div>
                            </div>

                            <!-- Minimum Loyalty -->
                            <div>
                                <div class="form-group">
                                    <label for="minimum-loyalty">Minimum Loyalty: </label>
                                    <input type="number" id="minimum-loyalty" name="minimum-loyalty" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <button type="submit">Add</button>
                                <button type="button" onclick="toggleForm()">Cancel</button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </section>
    </div>
</div>



<script>

    var banner = document.getElementById("banner-section");
    var productManagement = document.getElementById("product-management");
    var categoryManagement = document.getElementById("category-management");
    var accountManagement = document.getElementById("account-management");
    var orderManagement =document.getElementById("order-management");
    var promotionManagement =document.getElementById("promotion-management")
    // Lắng nghe sự kiện khi người dùng click vào "Quản lý sản phẩm"
    document.getElementById("manage-products-btn").addEventListener("click", function (event) {

        banner.style.display = "none";
        productManagement.style.display = "block";
        accountManagement.style.display = "none";
        categoryManagement.style.display = "none";
        orderManagement.style.display="none";
        promotionManagement.style.display="none";
        // nút thêm sửa xóa
        var addForm = document.querySelector("#add-product-management-form");
        var editForm = document.querySelector("#edit-product-management-form");
        var deleteForm = document.querySelector("#delete-product-management-form");

        document.getElementById("btn-product-management-actions-add").addEventListener("click", function (event) {
            editForm.style.display = "none";
            deleteForm.style.display = "none";
            addForm.style.display = "flex";
        });

        document.getElementById("btn-product-management-actions-edit").addEventListener("click", function (event) {
            addForm.style.display = "none";
            deleteForm.style.display = "none";
            editForm.style.display = "flex";
        });

        document.getElementById("btn-product-management-actions-delete").addEventListener("click", function (event) {
            addForm.style.display = "none";
            editForm.style.display = "none";
            deleteForm.style.display = "flex";
        });
    });




    // Lắng nghe sự kiện khi người dùng click vào "Quản lý danh mục sản phẩm"
    document.getElementById("manage-categrories-btn").addEventListener("click", function (event) {

        banner.style.display = "none";
        categoryManagement.style.display = "block";
        accountManagement.style.display = "none";
        productManagement.style.display = "none";
        orderManagement.style.display="none";
        promotionManagement.style.display="none";



        // nút thêm sửa xóa
        var addForm = document.querySelector("#add-category-management-form");
        var editForm = document.querySelector("#edit-category-management-form");
        var deleteForm = document.querySelector("#delete-category-management-form");

        document.getElementById("btn-category-management-actions-add").addEventListener("click", function (event) {
            editForm.style.display = "none";
            deleteForm.style.display = "none";
            addForm.style.display = "flex";
        });

        document.getElementById("btn-category-management-actions-edit").addEventListener("click", function (event) {
            addForm.style.display = "none";
            deleteForm.style.display = "none";
            editForm.style.display = "flex";
        });

        document.getElementById("btn-category-management-actions-delete").addEventListener("click", function (event) {
            addForm.style.display = "none";
            editForm.style.display = "none";
            deleteForm.style.display = "flex";
        });
    });




    // Lắng nghe sự kiện khi người dùng click vào "Quản lý tài khoản"
    document.getElementById("account-management-btn").addEventListener("click", function(event) {
        banner.style.display = "none";
        productManagement.style.display = "none";
        accountManagement.style.display ="block";
        orderManagement.style.display="none";
        categoryManagement.style.display="none";
        promotionManagement.style.display="none";



        var viewInfor = document.querySelector("#view-account-management-form");
        var editInfor = document.querySelector("#edit-account-management-form");


        document.getElementById("btn-account-management-actions-view").addEventListener("click", function(event) {
            viewInfor.style.display="flex";
            editInfor.style.display="none";

        });
        document.getElementById("btn-account-management-actions-edit").addEventListener("click", function(event) {
            viewInfor.style.display="none";
            editInfor.style.display="flex";

        });
    });

    // Lắng nghe sự kiện khi người dùng click vào "Quản lý đơn hàng"
    document.getElementById("order-management-btn").addEventListener("click", function(event) {
        banner.style.display = "none";
        productManagement.style.display = "none";
        accountManagement.style.display ="none";
        orderManagement.style.display="block";
        categoryManagement.style.display="none";
        promotionManagement.style.display="none";


        var viewOrder
        var editOrder
        var deleteOrder

    })
    // Load image lên picturebox

    const loadImageBtns = document.querySelectorAll('.loadImageBtn');
    const cancelBtns = document.querySelectorAll('.cancelBtn');
    const imageInputs = document.querySelectorAll('.imageInput');
    const imageDisplays = document.querySelectorAll('.imageDisplay');

    // Hàm để tải hình ảnh
    loadImageBtns.forEach((btn, index) => {
        btn.addEventListener('click', () => {
            event.preventDefault(); // Ngừng hành động mặc định (reload trang)
            imageInputs[index].click();
        });
    });

    // Xử lý sự kiện khi người dùng chọn file
    imageInputs.forEach((input, index) => {
        input.addEventListener('change', (event) => {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    imageDisplays[index].src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    });

    // Hàm để hủy hình ảnh
    cancelBtns.forEach((btn, index) => {
        btn.addEventListener('click', () => {
            imageDisplays[index].src = '';
            imageInputs[index].value = ''; // Đặt lại input để có thể chọn cùng hình ảnh lần nữa
        });
    });


    // Quản lý khuyến mãi
    function showSection(section) {
        const promotionsContainer = document.getElementById('promotions-container');
        const vouchersContainer = document.getElementById('vouchers-container');

        if (section === 'promotions') {
            promotionsContainer.style.display = 'block';
            vouchersContainer.style.display = 'none';
        } else {
            promotionsContainer.style.display = 'none';
            vouchersContainer.style.display = 'block';
        }
    }
    showSection('promotions');
    function toggleForm() {
        const overlay = document.getElementById('overlay');
        overlay.style.display = overlay.style.display === 'flex' ? 'none' : 'flex';
    }

    // Thêm sự kiện cho nút Add Promotion
    document.querySelector('.add-promotion-btn').addEventListener('click', toggleForm);
    function toggleDropdown() {
        const dropdownMenu = document.querySelector('.dropdown-menu');
        dropdownMenu.style.display = dropdownMenu.style.display === 'block' ? 'none' : 'block';
    }

    // Close the dropdown if clicked outside
    document.addEventListener('click', function(event) {
        const dropdownButton = document.querySelector('.dropdown-button');
        const dropdownMenu = document.querySelector('.dropdown-menu');
        if (!dropdownButton.contains(event.target) && !dropdownMenu.contains(event.target)) {
            dropdownMenu.style.display = 'none';
        }
    });

    // Function to handle form submission and display selected products

    document.getElementById("promotion-management-btn").addEventListener("click", function(event) {
        banner.style.display = "none";
        productManagement.style.display = "none";
        accountManagement.style.display ="none";
        orderManagement.style.display="none";
        categoryManagement.style.display="none";
        promotionManagement.style.display="block";

    });


    function editProduct(button) {
        event.preventDefault();
        // Lấy thông tin sản phẩm từ thuộc tính data- của nút sửa
        var productId = button.getAttribute('data-product-id');
        var productName = button.getAttribute('data-product-name');
        var productPrice = button.getAttribute('data-product-price');
        var productColor = button.getAttribute('data-product-color');
        var productSize = button.getAttribute('data-product-size');
        var imgURL = button.getAttribute('data-product-img');
        var productDescription = button.getAttribute('data-product-description');
        var categoryName = button.getAttribute('data-category-name');

        // Điền thông tin vào form sửa sản phẩm
        document.getElementById('product-id').value = productId;
        document.getElementById('edit-product-name').value = productName;
        document.getElementById('edit-product-price').value = productPrice;
        document.getElementById('product-color').value = productColor;
        document.getElementById('edit-product-size').value = productSize;
        document.getElementById('edit-product-description').value = productDescription;
        document.querySelector('.imageDisplay').src = imgURL;
        // Điền thông tin danh mục vào dropdown
        var categorySelect = document.getElementById('edit-product-category');
        for (var i = 0; i < categorySelect.options.length; i++) {
            if (categorySelect.options[i].text === categoryName) {
                categorySelect.selectedIndex = i;
                break;
            }
        }
    }

</script>
</body>

</html>



