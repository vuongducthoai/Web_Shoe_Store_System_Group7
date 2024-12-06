<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Shoe Store</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link
            href="https://fonts.googleapis.com/css2?family=Libre+Bodoni:wght@500&family=Plus+Jakarta+Sans:wght@400;600&display=swap"
            rel="stylesheet" />

    <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="./css/index.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="./css/toastMessage.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css"
          integrity="sha512-+4zCK9k+qNFUR5X+cKL9EIR+ZOhtIloNl9GIKS57V1MyNsYpYcUrUeQc9vNfzsWfV28IaLL3i96P9sdNyeRssA=="
          crossorigin="anonymous" />
    <style>
        /* Tổng quan về phân trang */
        .pagination {
            display: flex;
            justify-content: center;  /* Canh giữa phân trang */
            list-style: none;
            padding: 0;
            margin-top: 50px;
        }

        /* Các item của phân trang */
        .page-item {
            margin: 0 5px;
        }

        /* Kiểu dáng cho liên kết của các trang */
        .page-link {
            display: inline-block;
            padding: 8px 16px;
            text-decoration: none;
            color: #007bff;  /* Màu chữ cho các trang */
            background-color: #fff;  /* Màu nền */
            border: 1px solid #ccc;  /* Đường viền */
            border-radius: 4px;  /* Bo góc */
            cursor: pointer;
            transition: background-color 0.2s ease, color 0.2s ease;
        }

        /* Kiểu dáng khi hover (di chuột vào) */
        .page-link:hover {
            background-color: #f1f1f1;
            color: #0056b3;
        }

        /* Kiểu dáng cho các trang hiện tại (được chọn) */
        .page-item.active .page-link {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }

        /* Kiểu cho các mũi tên (Next và Previous) */
        .page-item a.page-link span {
            font-size: 16px;
        }

        /* Kiểu cho các mũi tên (Previous) */
        .page-item a.page-link[aria-label="Previous"]:hover {
            background-color: #f1f1f1;
            color: #0056b3;
        }

        /* Kiểu cho các mũi tên (Next) */
        .page-item a.page-link[aria-label="Next"]:hover {
            background-color: #f1f1f1;
            color: #0056b3;
        }
        .shoe-price {
            font-size: 16px; /* Kích thước chữ */
            color: red;
            font-weight: 600;
        }

        .strike-through {
            text-decoration: line-through; /* Tạo hiệu ứng gạch giữa */
            color: #999; /* Màu cho giá đã bị gạch */
        }

        /*  Discount  */
        .home-product-item_sale-off {
            position: absolute;
            right: 0;
            top: 0;
            width: 45px;
            height: 50px;
            background-color: rgba(255, 216, 64, 0.93);
            text-align: center;

        }

        .home-product-item_sale-off:after{
            content: "";
            position: absolute;
            left: 0;
            bottom: -6px;
            border-width: 0 23px 6px;
            border-style: solid;
            border-color: transparent #FFD840ED transparent #FFD840ED;
        }

        .home-product-item_sale-off-percent{
            color: #EE4D2D;
            font-weight: 600;
            font-size: 1.2rem;
            line-height: 1.3rem;
        }
        .home-product-item_sale-off-label{
            color: #1155e6;
            font-weight: 600;
            line-height: 1.4rem;
        }

        .countdown {
            margin-bottom: 2px;
            margin-left: 40px;
            color: black;
            font-size: 34px;
            font-weight: bold;
        }

        .star-rating::before {
            content: '⭐⭐⭐⭐⭐';
            font-size: 16px;  /* Điều chỉnh kích thước sao */
            color: orangered;     /* Đặt màu sao */
        }

        .product_desc {
            white-space: nowrap;            /* Đảm bảo không có xuống dòng */
            overflow: hidden;               /* Ẩn phần vượt quá chiều rộng */
            text-overflow: ellipsis;        /* Hiển thị dấu ba chấm khi nội dung bị cắt */
            width: 100%;                    /* Đảm bảo phần tử không vượt quá chiều rộng của nó */
            display: block;                 /* Đảm bảo phần tử có block-level để áp dụng */
        }

    </style>
</head>

<body>
<%--Header--%>
<div id="custom-toast"></div>
<jsp:include page="./view/header.jsp"></jsp:include>
<%--Slider--%>
<jsp:include page="./view/slider.jsp"></jsp:include>
<!-- arrival section-->
<section class="container arrival">
    <div class="section-heading" style="padding: 0; margin-bottom: 20px; color: orange">
        <div style="display: flex; align-items: center">
            <img src="./image/flashSale.png">
            <div class="countdown" id="countdown">ACBB</div>
        </div>
    </div>
    <div class="wrapper" style="flex-wrap: wrap" id="product-list">
        <c:forEach var="promotionProduct" items="${promotionProductDTOList}">
            <div class="product" style="position: relative; flex: 0 0 23%; max-width: 25%; height: 480px">
                <form action="${pageContext.request.contextPath}/product/details" method="GET">
                    <input type="hidden" name="productName" value="${promotionProduct.product.productName}">
                    <div class="product-image">
                        <figure><img src="${promotionProduct.product.getBase64Image()}" alt="Foundation"></figure>
                    </div>
                    <div class="product-details">
                        <div style="display: flex; align-items: center; justify-content: space-between">
                            <p class="product_name">${promotionProduct.product.productName}</p>
                            <span class="star-rating"></span>
                        </div>

                        <p class="product_desc" style="margin-top: 5px">${promotionProduct.product.description}</p>
                        <div class="col-footer" style="display: flex; align-items: center; justify-content: space-between; margin-top: 10px">
                            <p class="shoe-price">${promotionProduct.product.sellingPrice}₫ <span style="color: #999; margin-left: 10px" class="shoe-price strike-through">${promotionProduct.product.price}₫</span>
                        </div>
                    </div>
                    <div class="home-product-item_sale-off">
                        <span class="home-product-item_sale-off-percent">${promotionProduct.promotion.discountValue}%</span>
                        <span class="home-product-item_sale-off-label">GIẢM</span>
                    </div>
                    <button type="submit" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; opacity: 0;  cursor: pointer"></button> <!-- Invisible button for clicking -->
                </form>
            </div>
        </c:forEach>
    </div>

    <div>
        <ul class="pagination" id="pagination">
            <li class="page-item">
                <a class="page-link" href="#" aria-label="Previous" id="previous">
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            </li>
            <c:forEach begin="1" end ="${numpage}" var = "i">
                <%--                Paging?index=${i}--%>
                <li class="page-item"><a class="page-link page-link-item" href="#">${i}</a></li>
            </c:forEach>
            <li class="page-item">
                <a class="page-link" href="#" aria-label="Next" id="next">
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                </a>
            </li>
        </ul>
    </div>


</section>


<!-- new collection -->
<section class="container collection">
    <!-- section header -->
    <div class="section-heading">
        <div class="heading">
            <p class="sub-heading">best collection</p>
            <h2 class="heading-two">our new collection</h2>
        </div>


        <div class="btn-section">
            <button class="btn-col active" data-btn="all">all</button>
            <c:forEach var="category" items="${categoryDTOList}">
                <button class="btn-col" data-btn="${category.categoryId}">
                        ${category.categoryName}
                </button>
            </c:forEach>
        </div>
    </div>

    <div class="wrapper wrapper1" style="flex-wrap: wrap">
        <c:forEach var="product" items="${productDTOList}">
            <div class="product" style="flex: 0 0 23%; max-width: 25%; height: 480px; position: relative;">
                <form action="/product/details" method="GET">
                    <input type="hidden" name="productName" value="${product.productName}">
                    <div class="product-image">
                        <figure><img src="${product.getBase64Image()}" alt="Image of ${product.productName}"></figure>
                    </div>
                    <div class="product-details">
                        <div style="display: flex; align-items: center; justify-content: space-between">
                            <p class="product_name">${product.productName}</p>
                            <span class="star-rating"></span> <!-- You can add stars dynamically here -->
                        </div>
                        <p class="product_desc" style="margin-top: 5px">${product.description}</p>
                        <div class="col-footer" style="display: flex; align-items: center; justify-content: space-between; margin-top: 10px">
                            <p class="shoe-price">${product.price}₫</p>
                            <span style="color: #999; margin-left: 10px" class="shoe-sold">Đã bán: ${product.quantity}</span>
                        </div>
                    </div>
                    <!-- Invisible button for submitting the form -->
                    <button type="submit" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; opacity: 0; cursor: pointer"></button>
                </form>
            </div>
        </c:forEach>
    </div>


    <button style="margin-top: 30px; background-color: #baba14;" id="load-more-btns" class="btn">Xem thêm</button>
</section>
<%
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
%>

<script type="text/javascript">
    var startDate = "<%= startDate != null ? startDate : "" %>";
    var endDate = "<%= endDate != null ? endDate : "" %>";

    if (startDate && endDate) {
        var start = new Date(startDate);
        var end = new Date(endDate);


        function updateCountdown() {
            var now = new Date().getTime();
            var distance = end - now;

            var days = Math.floor(distance / (1000 * 60 * 60 * 24));
            var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((distance % (1000 * 60)) / 1000);

            document.getElementById("countdown").innerHTML = days + "d " + hours + "h " + minutes + "m " + seconds + "s ";

            if (distance < 0) {
                clearInterval(x);
                document.getElementById("countdown").innerHTML = "EXPIRED";
            }
        }

        var x = setInterval(updateCountdown, 1000);
    }
</script>

<!-- customer review -->
<jsp:include page="view/customer/review.jsp"></jsp:include>
<script src="./js/toastMessage.js"></script>
<%--Footer--%>
<jsp:include page="./view/footer.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="./js/promotionProduct.js"></script>
<script src="./js/index.js"></script>


<script src="https://unpkg.com/aos@next/dist/aos.js"></script>
<script>
    AOS.init();
</script>
</body>

</html>