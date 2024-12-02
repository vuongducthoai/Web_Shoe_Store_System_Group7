<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
 Created by IntelliJ IDEA.
 User: Admin
 Date: 11/21/2024
 Time: 5:03 PM
 To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>




<!DOCTYPE html>
<html lang="en">


<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminPage.css">


    <title>Document</title>


    <!-- bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>


        /* Cố định độ rộng cho các cột */
        .table th:nth-child(1), .table td:nth-child(1) {
        }
        .table th:nth-child(2), .table td:nth-child(2) {
            min-width: 165px; /* Cột Tên Khách Hàng */
        }
        .table th:nth-child(3), .table td:nth-child(3) {
            min-width: 240px; /* Cột Email */
        }
        .table th:nth-child(4), .table td:nth-child(4) {
            min-width: 130px; /* Cột Mật khẩu */
        }
        .table th:nth-child(5), .table td:nth-child(5) {
            min-width: 120px;/* Cột Số điện thoại */
        }
        .table th:nth-child(6), .table td:nth-child(6) {
            min-width: 100px; /* Cột Trạng thái */
        }
        .table th:nth-child(7), .table td:nth-child(7) {
            min-width: 150px; /* Cột Hành động */
        }
    </style>




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
            <div class="product-management" id="product-management" >
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
                                    <td>
                                        <form action="ProductController" method="post" >
                                            <input type="hidden" name="productName" value="${product.productName}" >
                                            <button class="action-btn" name="submitAction" value="showInfo" >Sửa</button>
                                        </form>

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
                    <form action="ProductController" method="post" enctype="multipart/form-data">
                        <div class="product-management-form" id="add-product-management-form">
                            <h3>Thêm sản phẩm mới</h3>

                            <label for="add-product-name">Tên sản phẩm</label>
                            <input type="text" id="add-product-name" name="productName" placeholder="Nhập tên sản phẩm">

                            <label for="add-product-price">Giá</label>
                            <input type="text" id="add-product-price" name="productPrice" placeholder="Nhập giá sản phẩm">

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

                            <!-- Khu vực thêm biến thể (màu sắc) -->
                            <h4>Danh sách biến thể</h4>
                            <div id="color-container"></div>
                            <button type="button" id="add-color-btn">Thêm biến thể</button>

                            <button class="action-btn" name="submitAction" value="add-product" >Thêm sản phẩm</button>
                        </div>
                    </form>


                    <%--  Nút sửa sản phẩm--%>
                    <form action="ProductController" method="post" enctype="multipart/form-data">
                        <div class="product-management-form" id="edit-product-management-form">
                            <h3>Sửa sản phẩm</h3>

                            <!-- Dữ liệu chung -->

                            <label for="edit-product-name">Tên sản phẩm</label>
                            <input type="text" id="edit-product-name" name="productName" value="${productName}" placeholder="Nhập tên sản phẩm">

                            <label for="edit-product-price">Giá</label>
                            <input type="text" id="edit-product-price" name="productPrice" value="${productPrice}" placeholder="Nhập giá sản phẩm">

                            <label for="edit-product-category">Danh mục</label>
                            <select id="edit-product-category" name="CategoryName">
                                <c:forEach var="category" items="${CategoryList}">
                                    <option value="${category.getCategoryName()}" ${category.getCategoryName() == productCategory ? 'selected' : ''}>
                                            ${category.getCategoryName()}
                                    </option>
                                </c:forEach>
                            </select>

                            <label for="edit-product-description">Mô tả</label>
                            <textarea id="edit-product-description" name="productDescription" placeholder="Nhập mô tả sản phẩm">${productDescription}</textarea>

                            <!-- Khu vực sửa biến thể (màu sắc) -->
                            <h4>Danh sách biến thể</h4>
                            <div id="color-container-edit">
                                <!-- Lặp qua Map colorIdToNameMap -->
                                <c:forEach var="entry" items="${colorIdToNameMap}">
                                    <c:set var="colorId" value="${entry.key}" />
                                    <c:set var="colorName" value="${entry.value}" />

                                    <div class="color-block" data-color-id="${colorId}">
                                        <h4>Màu ${colorId}</h4>

                                        <label for="color-name-${colorId}">Tên Màu:</label>
                                        <input type="text" name="color-name-${colorId}" value="${colorName}" id="color-name-${colorId}" required>

                                        <label for="image-color-${colorId}">Hình ảnh:</label>
                                        <div class="LoadImageContent">
                                            <div class="picturebox">
                                                <img class="imageDisplay" src="${colorIdToImageMap[colorId]}" alt="Current image" />
                                            </div>
                                            <button type="button" class="loadImageBtn">Load Image</button>
                                            <input type="file" name="image-color-${colorId}" class="imageInput" style="display: none;" accept="image/*">
                                            <button type="button" class="cancelBtn">Cancel Image</button>
                                        </div>

                                        <label>Size và Số lượng:</label>
                                        <div class="size-container" id="size-container-${colorId}">
                                            <!-- Lặp qua Map sizeQuantityMap -->
                                            <c:forEach var="sizeEntry" items="${sizeQuantityMap[colorId]}">
                                                <div class="size-block">
                                                    <input type="text" name="size-${colorId}[]" value="${sizeEntry.key}" placeholder="Nhập size">
                                                    <input type="text" name="quantity-${colorId}[]" value="${sizeEntry.value}" placeholder="Nhập số lượng">
                                                    <button type="button" class="remove-size-btnEdit">Xóa Size</button>
                                                </div>
                                            </c:forEach>
                                        </div>
                                        <button type="button" class="add-size-btnEdit" data-color="${colorId}">Thêm Size</button>

                                        <button type="button" class="remove-color-btnEdit">Xóa biến thể</button>
                                    </div>
                                </c:forEach>
                            </div>
                            <button type="button" id="add-color-btnEdit">Thêm biến thể</button>
                            <button class="action-btn" name="submitAction" value="edit-product">Lưu thay đổi</button>

                        </div>
                    </form>


                <%-- Nút xóa sản phẩm--%>
                    <form action="ProductController" method="post" >
                    <div class="product-management-form" id="delete-product-management-form">
                        <h3>Xóa sản phẩm</h3>
                        <input type="text" name="productName" id="delete-product-id" placeholder="Nhập tên sản phẩm">
                        <button class="action-btn" name="submitAction" value="delete-product">Xóa sản phẩm</button>
                    </div>
                    </form>
                </div>
            </div>
            <!-- kết thúc quản lý sản phẩm -->


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
                            <c:forEach var="category" items="${categoryList}">
                                <tr>
                                    <td>${category.categoryId}</td>
                                    <td>${category.categoryName}</td>
                                    <td>
                                        <form action="CategoryController" method="post" style="display:inline;">
                                            <input type="hidden" name="submitAction" value="viewProducts">
                                            <input type="hidden" name="categoryId" value="${category.categoryId}">
                                            <input type="hidden" name="categoryName" value="${category.categoryName}">
                                            <input type="submit" class="view-info-btn" value="Xem">
                                        </form>
                                    </td>


                                </tr>
                            </c:forEach>


                        </table>
                    </div>
                    <br>
                    <div class="category-management-actions">
                        <button class="action-btn" id="btn-category-management-actions-add">Thêm</button>
                        <button class="action-btn" id="btn-category-management-actions-edit">Sửa</button>
                        <button class="action-btn" id="btn-category-management-actions-delete">Xóa</button>


                    </div>


                    <!-- nút thêm danh mục sản phẩm -->
                    <form action="CategoryController" method="post" >
                        <div class="category-management-form" id="add-category-management-form">
                            <h3>Thêm danh mục mới</h3>


                            <label for="add-category-name">Tên danh mục</label>
                            <input type="text" id="add-category-name" name="add-categoryName" placeholder="Nhập tên danh mục">




                            <button class="action-btn" name="submitAction" value="add-category" >Thêm danh mục</button>
                        </div>
                    </form>




                    <%-- sửa danh mục sản phẩm--%>
                    <form action="CategoryController" method="post" >
                        <div class="category-management-form" id="edit-category-management-form">
                            <h3>Sửa danh mục</h3>
                            <label for="edit-category-id">ID</label>
                            <input type="text" id="edit-category-id" name="edit-categoryID" placeholder="Nhập ID danh mục">


                            <label for="edit-category-name">Tên danh mục</label>
                            <input type="text" id="edit-category-name" name="edit-categoryName" placeholder="Nhập tên danh mục">
                            <button class="action-btn" name="submitAction" value="edit-category">Sửa danh mục</button>
                        </div>
                    </form>


                    <%--nút xóa danh mục sản phẩm--%>
                    <form action="CategoryController" method="post" >
                        <div class="category-management-form" id="delete-category-management-form">
                            <h3>Xóa danh mục</h3>
                            <label for="delete-category-id">ID</label>
                            <input type="text" id="delete-category-id" name="delete-categoryID" placeholder="Nhập ID danh mục">
                            <button class="action-btn" name="submitAction" value="delete-category">Xóa danh mục</button>
                        </div>
                    </form>
                </div>
            </div>
            <!-- Kết thúc quản lý danh mục sản phẩm -->


            <!-- Bắt đầu quản lý tài khoản -->
            <div class="account-management" id="account-management" style="display: none;">
                <div class="title">Quản lý tài khoản khách hàng</div>
                <input type="text" id="search-input" placeholder="Tìm kiếm theo tên khách hàng" >
                <button class="action-btn" onclick="searchCustomer()">Tìm</button>
                <div class="table-container">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên Khách Hàng</th>
                            <th>Email</th>
                            <th>Mật khẩu</th>
                            <th>Số điện thoại</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody id="account-table-body">
                        <c:forEach var="account" items="${accounts}">
                            <tr
                                <%--ghi nhớ dữ liệu full của 3 cột--%>
                                    data-account-name="${account.user.fullName}"
                                    data-account-email="${account.email}"
                                    data-account-password="${account.password}">


                                <td>${account.accountID}</td>
                                <td>${fn:substring(account.user.fullName, 0, 17)}<c:if test="${fn:length(account.user.fullName) > 17}">...</c:if></td>
                                <td>${fn:substring(account.email, 0, 25)}<c:if test="${fn:length(account.email) > 25}">...</c:if></td>
                                <td>${fn:substring(account.password, 0, 10)}<c:if test="${fn:length(account.password) > 10}">...</c:if></td>
                                <td>${account.user.phone}</td>
                                <td class="status">${account.user.active ? 'Hoạt động' : 'Bị chặn'}</td>
                                <td><button class="btn btn-primary btn-history" data-user-id="${account.user.userID}" onclick="viewHistory(this)">Xem lịch sử</button></td>

                            </tr>
                        </c:forEach>
                        </tbody>


                    </table>
                </div>
                <br>
                <div class="account-management-actions">
                    <button class="action-btn" id="btn-account-management-actions-view">Xem thông tin chi tiết khách hàng</button>
                    <button class="action-btn" id="btn-account-management-actions-edit">Sửa thông tin khách hàng</button>
                    <button class="action-btn" id="btn-account-management-actions-block">Chặn khách hàng</button>
                    <button class="action-btn" id="btn-account-management-actions-unblock">Bỏ chặn khách hàng</button>
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


                </div>


                <!-- sửa thông tin khách hàng  -->
                <form action="${pageContext.request.contextPath}/AccountController" method="POST">
                    <div class="account-management-form" id="edit-account-management-form">
                        <h3>Sửa thông tin khách hàng</h3>
                        <input type="hidden" name="action" value="update">
                        <label for="edit-account-id-display">ID</label>
                        <input type="text" id="edit-account-id-display" name="accountID" readonly> <!-- ID chỉ để xem -->
                        <label for="edit-account-name">Họ Tên</label>
                        <input type="text" name="fullName" id="edit-account-name">
                        <label for="edit-account-email">Email</label>
                        <input type="text" name="email" id="edit-account-email">


                        <label for="edit-account-pass">Mật Khẩu</label>
                        <input type="text" name="password" id="edit-account-pass">


                        <label for="edit-account-phone">SĐT</label>
                        <input type="text" name="phone" id="edit-account-phone">


                        <button class="action-btn" type="submit">Sửa thông tin</button>


                    </div>
                </form>
                <!-- chặn khách hàng  -->


                <form action="${pageContext.request.contextPath}/AccountController" method="POST">
                    <div class="account-management-form" id="block-account-management-form">
                        <h3>Chặn tài khoản khách hàng</h3>
                        <input type="hidden" name="action" value="block">
                        <label for="block-account-id">ID</label>
                        <input type="text" id="block-account-id" name="accountID" readonly>
                        <button class="action-btn" type="submit">Chặn</button>
                    </div>
                </form>


                <!--bỏ chặn khách hàng  -->
                <form action="${pageContext.request.contextPath}/AccountController" method="POST">
                    <div class="account-management-form" id="unBlock-account-management-form">
                        <h3> Bỏ chặn tài khoản khách hàng</h3>
                        <input type="hidden" name="action" value="unblock">
                        <label for="unBlock-account-id">ID</label>
                        <input type="text" id="unBlock-account-id" name="accountID" readonly>
                        <button class="action-btn" type="submit">Bỏ chặn</button>
                    </div>
                </form>
                <!-- Popup lịch sử đơn hàng -->
                <div class="order-history-popup" id="order-history-popup" style="display: none;">
                    <h3>Lịch sử đơn hàng</h3>
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>ID đơn hàng</th>
                            <th>Ngày mua</th>
                            <th>Sản phẩm</th>
                            <th>Tổng tiền</th>
                        </tr>
                        </thead>
                        <tbody id="order-history-content">
                        <!-- Nội dung sẽ được cập nhật bằng JavaScript -->
                        </tbody>
                    </table>
                    <button class="btn btn-secondary" id="close-history-popup">Đóng</button>
                </div>
            </div>




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
<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>


</html>


















