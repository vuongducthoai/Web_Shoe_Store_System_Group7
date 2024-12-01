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
        <div class="heading">
            <h2 class="heading-two" style="color: orangered">FLASH SALE</h2>
        </div>
    </div>
    <div class="wrapper" style="flex-wrap: wrap">
        <c:forEach var="promotionProduct" items="${promotionProductDTOList}">
            <div class="product" style="position: relative; flex: 0 0 23%; max-width: 25%">
                <div class="product-image">
                    <figure><img src="${promotionProduct.product.getBase64Image()}" alt="Foundation"></figure>
                </div>
                <div class="product-details">
                    <p class="product_name">${promotionProduct.product.productName}</p>
                    <p class="product_desc" style="margin-top: 5px">${promotionProduct.product.description}</p>
                    <div class="col-footer" style="display: flex; align-items: center; justify-content: space-between; margin-top: 10px">
                        <p class="shoe-price">${promotionProduct.product.sellingPrice}₫ <span style="color: #999; margin-left: 10px" class="shoe-price strike-through">${promotionProduct.product.price}₫</span>
                    </div>
                </div>
                <div class="home-product-item_sale-off">
                    <span class="home-product-item_sale-off-percent">${promotionProduct.promotion.discountValue}%</span>
                    <span class="home-product-item_sale-off-label">GIẢM</span>
                </div>
            </div>

        </c:forEach>
    </div>
    <div>
        <ul class="pagination">
            <li class="page-item">
                <a class="page-link" href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            </li>
            <c:forEach begin="1" end ="${numpage}" var = "i">
                <li class="page-item"><a class="page-link" href="Paging?index=${i}">${i}</a></li>
            </c:forEach>
            <li class="page-item">
                <a class="page-link" href="#" aria-label="Next">
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
    <!-- section wrapper and col -->
    <div class="grid-wrapper">
        <c:forEach var="product" items="${productDTOList}">
            <div class="product">
                <div class="product-image">
                    <figure><img src="./image/shoes3.png" alt="Foundation"></figure>
                </div>
                <div class="product-details">
                    <p class="product_name">${product.productName}</p>
                    <p class="product_desc" style="margin-top: 5px">${product.description}</p>
                    <div class="col-footer" style="display: flex; align-items: center; justify-content: space-between; margin-top: 10px">
                        <p class="shoe-price">${product.price}₫</p
                        <p class="shoe-sold">Đã bán :${product.quantity}
                    </div>

                </div>
            </div>
        </c:forEach>
    </div>
    <button style="margin-top: 30px; background-color: #baba14;" id="load-more-btns" class="btn">Xem thêm</button>
</section>
<!-- customer review -->
<jsp:include page="view/customer/review.jsp"></jsp:include>
<script src="./js/toastMessage.js"></script>
<%--Footer--%>
<jsp:include page="./view/footer.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="./js/index.js"></script>
<script src="https://unpkg.com/aos@next/dist/aos.js"></script>
<script>
    AOS.init();
</script>

</body>

</html>