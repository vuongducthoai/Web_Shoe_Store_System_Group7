<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
 Created by IntelliJ IDEA.
 User: Admin
 Date: 11/21/2024
 Time: 5:03 PM
 To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/promotionStyle.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/statistics.css">

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
    <jsp:include page="../header.jsp"></jsp:include>


    <!-- Main -->
    <div class="main-content">
        <aside class="sidebar">
            <ul>
                <li><a href="#" id="manage-products-btn">Quản lý sản phẩm</a></li>
                <li><a href="#" id="manage-categrories-btn">Quản lý danh mục</a></li>
                <li><a href="#" id="account-management-btn">Quản lý tài khoản khách hàng</a></li>
                <li><a href="#" id="order-management-btn">Quản lý đơn hàng</a></li>
                <li><a href="#" id="promotion-management-btn">Quản lý khuyến mãi</a></li>
                <li><a href="#" id="statistics-management-btn">Thống kê</a></li>
            </ul>
        </aside>
        <section class="main-panel" id="panel">


            <!-- ảnh trang chủ  -->
            <img id="banner-section" src="./image/CR7-Banner.jpg" alt="Banner" width="100%" height="10s0%">


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
                                        <button class="action-btn" id="btn-edit-product" onclick="sendEditProduct('${product.productName}')">Sửa</button>
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
                    <form action="${pageContext.request.contextPath}/ProductController" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
                        <div class="product-management-form" id="add-product-management-form">
                            <h3>Thêm sản phẩm mới</h3>

                            <label for="add-product-name">Tên sản phẩm</label>
                            <input type="text" id="add-product-name" name="productName" placeholder="Nhập tên sản phẩm" required>

                            <label for="add-product-price">Giá</label>
                            <input type="text" id="add-product-price" name="productPrice" placeholder="Nhập giá sản phẩm" required>

                            <label for="add-product-category">Danh mục</label>
                            <select id="add-product-category" name="CategoryName">
                                <c:forEach var="category" items="${CategoryList}">
                                    <option value="${category.getCategoryName()}">
                                            ${category.getCategoryName()}
                                    </option>
                                </c:forEach>
                            </select>

                            <label for="add-product-description">Mô tả</label>
                            <input type="text" id="add-product-description" name="productDescription" placeholder="Nhập mô tả sản phẩm" required>

                            <!-- Khu vực thêm biến thể (màu sắc) -->
                            <h4>Danh sách biến thể</h4>
                            <div id="color-container"></div>
                            <button type="button" id="add-color-btn">Thêm biến thể</button>

                            <button class="action-btn" name="submitAction" value="add-product" >Thêm sản phẩm</button>
                        </div>
                    </form>


                    <%--  Nút sửa sản phẩm--%>
                    <form action="${pageContext.request.contextPath}/ProductController" method="post" enctype="multipart/form-data" onsubmit="return validateFormEditProduct()">
                        <div class="product-management-form" id="edit-product-management-form">
                            <h3>Sửa sản phẩm</h3>

                            <!-- Dữ liệu chung -->

                            <label for="edit-product-name">Tên sản phẩm</label>
                            <input type="text" id="edit-product-name" name="productName" value="${productName}" placeholder="Nhập tên sản phẩm" required>

                            <label for="edit-product-price">Giá</label>
                            <input type="text" id="edit-product-price" name="productPrice" value="${productPrice}" placeholder="Nhập giá sản phẩm" required>

                            <label for="edit-product-category">Danh mục</label>
                            <select id="edit-product-category" name="CategoryName">
                                <c:forEach var="category" items="${CategoryList}">
                                    <option value="${category.getCategoryName()}" ${category.getCategoryName() == productCategory ? 'selected' : ''}>
                                            ${category.getCategoryName()}
                                    </option>
                                </c:forEach>
                            </select>

                            <label for="edit-product-description">Mô tả</label>
                            <textarea id="edit-product-description" name="productDescription" placeholder="Nhập mô tả sản phẩm"  required>${productDescription} </textarea>

                            <!-- Khu vực sửa biến thể (màu sắc) -->
                            <h4>Danh sách biến thể</h4>
                            <div id="color-container-edit">
                            </div>
                            <button type="button" id="add-color-btnEdit">Thêm biến thể</button>
                            <button class="action-btn" name="submitAction" value="edit-product">Lưu thay đổi</button>

                        </div>
                    </form>


                    <%-- Nút xóa sản phẩm--%>
                    <form action="${pageContext.request.contextPath}/ProductController" method="post" >
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
                            <c:forEach var="category" items="${CategoryList}">
                                <tr>
                                    <td>${category.categoryId}</td>
                                    <td>${category.categoryName}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/CategoryController" method="post" style="display:inline;">
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
                    <form action="${pageContext.request.contextPath}/CategoryController" method="post" >
                        <div class="category-management-form" id="add-category-management-form">
                            <h3>Thêm danh mục mới</h3>


                            <label for="add-category-name">Tên danh mục</label>
                            <input type="text" id="add-category-name" name="add-categoryName" placeholder="Nhập tên danh mục">




                            <button class="action-btn" name="submitAction" value="add-category" >Thêm danh mục</button>
                        </div>
                    </form>




                    <%-- sửa danh mục sản phẩm--%>
                    <form action="${pageContext.request.contextPath}/CategoryController" method="post" >
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
                    <form action="${pageContext.request.contextPath}/CategoryController" method="post" >
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
                        <tr class="row-dark">
                            <th>ID</th>
                            <th>Tên Khách Hàng</th>
                            <th>Email</th>
                            <th>Số điện thoại</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody id="account-table-body">
                        <c:forEach var="account" items="${accounts}">
                            <tr
                                    data-account-name="${account.user.fullName}"
                                    data-account-email="${account.email}">


                                <td>${account.accountID}</td>
                                <td>${fn:substring(account.user.fullName, 0, 17)}<c:if test="${fn:length(account.user.fullName) > 17}">...</c:if></td>
                                <td>${fn:substring(account.email, 0, 25)}<c:if test="${fn:length(account.email) > 25}">...</c:if></td>
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


                    <label for="view-account-phone">SĐT</label>
                    <input type="text" id="view-account-phone">


                </div>


                <!-- sửa thông tin khách hàng  -->
                <form action="${pageContext.request.contextPath}/Admin/account" method="POST" onsubmit="return validateAccountForm(this)">
                    <div class="account-management-form" id="edit-account-management-form">
                        <h3>Sửa thông tin khách hàng</h3>
                        <input type="hidden" name="action" value="update">

                        <label for="edit-account-id-display">ID</label>
                        <input type="text" id="edit-account-id-display" name="accountID" readonly>

                        <label for="edit-account-name">Họ Tên</label>
                        <input type="text" name="fullName" id="edit-account-name">

                        <label for="edit-account-email">Email</label>
                        <input type="text" name="email" id="edit-account-email">

                        <label for="edit-account-phone">SĐT</label>
                        <input type="text" name="phone" id="edit-account-phone">


                        <button class="action-btn" type="submit">Sửa thông tin</button>


                    </div>
                </form>
                <!-- chặn khách hàng  -->


                <form action="${pageContext.request.contextPath}/Admin/account" method="POST">
                    <div class="account-management-form" id="block-account-management-form">
                        <h3>Chặn tài khoản khách hàng</h3>

                        <input type="hidden" name="action" value="block">
                        <label for="block-account-id">ID</label>

                        <input type="text" id="block-account-id" name="accountID" readonly>
                        <button class="action-btn" type="submit">Chặn</button>
                    </div>
                </form>

                <!--bỏ chặn khách hàng  -->
                <form action="${pageContext.request.contextPath}/Admin/account" method="POST">
                    <div class="account-management-form" id="unBlock-account-management-form">
                        <h3> Bỏ chặn tài khoản khách hàng</h3>

                        <input type="hidden" name="action" value="unblock">
                        <label for="unBlock-account-id">ID</label>

                        <input type="text" id="unBlock-account-id" name="accountID" readonly>
                        <button class="action-btn" type="submit">Bỏ chặn</button>
                    </div>
                </form>
            </div>




            <!-- chức năng quản lý đơn hàng -->
            <div class="order-management" id="order-management">

                    <div>   <jsp:include page="orders.jsp" />  </div>
            </div>
            <!-- kết thúc chức năng quản lý đơn hàng -->


            <div class="promotion-management" id="promotion-management">
                <jsp:include page="promotions.jsp" />
                          <div>

                        </div>
            </div>
            <div class="statistics-management" id="statistics-management">
                <div>   <jsp:include page="statistics.jsp" />  </div>
            </div>
        </section>
    </div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/promotion.js" defer></script>
<script src="${pageContext.request.contextPath}/js/admin.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/js/statistics.js" defer></script>
</body>


</html>


















