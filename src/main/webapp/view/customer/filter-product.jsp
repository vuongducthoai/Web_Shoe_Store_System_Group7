<%-- Created by IntelliJ IDEA. User: b2h16 Date: 11/12/2024 Time: 9:22 AM To change this template use File | Settings |
    File Templates. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>

<head>
    <title>Shop</title>
    <script src="https://cdn.tailwindcss.com">

    </script>
    <link
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
            rel="stylesheet" />
    <link
            href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&amp;display=swap"
            rel="stylesheet" />
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/filter-product.js" defer></script>
</head>

<body class="bg-gray-100">
<div id="categoryListJson" style="display: none;">${categoryListJson}</div>
<div id="contextPath" data-contextPath="${pageContext.request.contextPath}"></div>
<div id="selectedSize" selectedSize="${selectedSize}"></div>
<div id="selectedColor" selectedColor="${selectedColor}"></div>
<div id="selectedCategory" selectedCategory="${selectedCategory}"></div>
<div id="filterMinPrice" filterMinPrice="${filterMinPrice}"></div>
<div id="filterMaxPrice" filterMaxPrice="${filterMaxPrice}"></div>
<div id="sortOption" sortOption="${sortOption}"></div>

<header class="bg-white shadow-sm">
    <div
            class="container mx-auto px-4 py-4 flex items-center justify-between">
        <div class="text-2xl font-bold">SHOP Bán Giày</div>
        <nav class="space-x-6">
            <a class="text-gray-700" href="#"> Shop </a> <a
                class="text-gray-700" href="#"> On Sale </a> <a
                class="text-gray-700" href="#"> New Arrivals </a> <a
                class="text-gray-700" href="#"> Brands </a>
        </nav>
        <div class="flex items-center space-x-4">
            <input class="border rounded-full px-4 py-2 w-64"
                   placeholder="Search for products..." type="text" /> <i
                class="fas fa-shopping-cart text-xl text-gray-700"> </i> <i
                class="fas fa-user-circle text-xl text-gray-700"> </i>
        </div>
    </div>
</header>

<main class="container mx-auto px-4 py-8">
    <div class="flex">
        <aside class="w-1/4 pr-8">
            <div class="bg-white p-4 rounded-lg shadow-sm mb-6">
                <h2 class="font-semibold mb-4">Bộ lọc</h2>
                <div class="mb-6">
                    <h3 class="font-medium mb-2">Thể loại</h3>
                    <ul class="space-y-2">
                        <!-- Duyệt qua danh sách categories -->
                        <!-- Duyệt qua danh sách categories -->
                        <c:forEach var="category" items="${categories}">
                            <!-- Kiểm tra nếu category có sản phẩm -->
                            <c:if test="${not empty category.productDTOList}">
                                <li>
                                    <a class="text-gray-700 category-btn" href="#"
                                       onclick="selectCategoryAndLoadPage(this)"
                                       data-category="${category.categoryName}">
                                            ${category.categoryName}
                                    </a>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="bg-white p-4 rounded-lg shadow-sm mb-6 border border-gray-200">
                <div class="mb-6">
                    <h3 class="font-medium mb-2">Giá tiền</h3>
                    <div class="range-slide" data-min="${minPrice}"
                         data-max="${maxPrice}">
                        <div class="slide">
                            <div class="line" id="line" style="left: 0%; right: 0%;"></div>
                            <span class="thumb" id="thumbMin" style="left: 0%;"></span> <span
                                class="thumb" id="thumbMax" style="left: 100%;"></span>
                        </div>
                        <input id="rangeMin" type="range" max="${maxPrice}"
                               min="${minPrice}" step="50000" value="${minPrice}"> <input
                            id="rangeMax" type="range" max="${maxPrice}" min="${minPrice}"
                            step="50000" value="${maxPrice}">
                    </div>
                    <div class="flex justify-between text-sm text-gray-500 mt-2">
                        <span id="min">${minPrice}</span> <span id="max">${maxPrice}</span>
                    </div>
                </div>

                <div class="mb-6">
                    <h3 class="font-medium mb-2">Màu sắc</h3>
                    <div class="flex space-x-2">
                        <c:forEach var="color" items="${colorList}">
                            <button
                                    class="color-btn w-6 h-6 rounded-full cursor-pointer border-2 border-gray-300"
                                    style="background-color: ${color};"
                                    onclick="selectColor(this)">
                            </button>
                        </c:forEach>
                    </div>
                </div>

                <div class="mb-6">
                    <h3 class="font-medium mb-2">Kích thước</h3>
                    <div class="flex flex-wrap gap-2">
                        <c:forEach var="size" items="${sizeList}">
                            <button class="border rounded-full px-2 py-2 text-sm size-btn"
                                    data-size="${size}" onclick="selectSize(this)">
                                    ${size}</button>
                        </c:forEach>
                    </div>
                </div>

                <button class="w-full bg-black text-white py-2 rounded-full"
                        onclick="apply()">Áp dụng</button>
            </div>
        </aside>
        <section class="w-3/4">
            <div class="flex justify-between items-center mb-6">
                <div class="flex items-center space-x-4">
						<span class="text-gray-500"> Hiển thị
							${minSize}-${categoryList.size()} trong số ${totalSize} sản phẩm
						</span>
                </div>

                <div class="relative">
                    <button id="dropdownButton"
                            class="border rounded-full px-4 py-2 text-sm flex items-center"
                            onclick="toggleDropdown()"
                    ${totalPages == 0 ? 'style="display: none;"' : ''}>
                        Phổ biến nhất <i class="fas fa-chevron-down ml-2"></i>
                    </button>
                    <ul id="dropdownMenu"
                        class="absolute bg-white border rounded-lg shadow-lg mt-2 hidden"
                    ${totalPages == 0 ? 'style="display: none;"' : ''}>
                    </ul>
                </div>

            </div>
            <div class="grid grid-cols-3 gap-6" id="product-container"></div>

            <div class="flex justify-between items-center mt-8">
                <!-- Nút Previous -->
                <button
                        class="border rounded-full px-4 py-2 text-sm flex items-center"
                        ${totalPages <= 1 ? 'style="display: none;"' : ''}
                        onclick="apply(${currentPage - 1 == 0 ? totalPages : currentPage - 1})">
                    <i class="fas fa-chevron-left mr-2"></i> Trước
                </button>

                <!-- Số trang hiện tại -->
                <span class="px-4 text-sm flex-1 text-center"
                ${totalPages == 0 ? 'style="display: none;"' : ''}
                > Trang ${currentPage} </span>

                <!-- Nút Next -->
                <button
                        class="border rounded-full px-4 py-2 text-sm flex items-center"
                        ${totalPages <= 1 ? 'style="display: none;"' : ''}
                        onclick="apply(${currentPage + 1 > totalPages ? 1 : currentPage + 1})">
                    Sau <i class="fas fa-chevron-right ml-2"></i>
                </button>
            </div>


        </section>
    </div>
</main>
</body>

</html>