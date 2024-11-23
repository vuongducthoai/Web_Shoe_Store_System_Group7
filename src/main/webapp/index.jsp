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
</head>

<body>
<%--Header--%>
<div id="custom-toast"></div>
<jsp:include page="./view/header.jsp"></jsp:include>
<%--Slider--%>
<jsp:include page="./view/slider.jsp"></jsp:include>
<!-- arrival section-->
<section class="container arrival">
    <div class="section-heading">
        <div class="heading">
            <h2 class="heading-two">new <span>arrival</span> </h2>
            <p class="sub-heading">our new collection</p>
        </div>
    </div>
    <div class="wrapper">
        <div class="col" data-aos="zoom-in-up">
            <figure>
                <img src="/image/ar1.png" alt="nike-shoe">
            </figure>
            <div class="col-body">
                <p class="rating-icon"><i class="fa-solid fa-star"></i> <span class="rating-num">4.9</span></p>
                <h3 class="heading-three">nike running</h3>
                <p class="sub-heading">air zoom pagasus</p>
                <div class="col-footer">
                    <p class="shoe-price">$350</p>
                    <button class="shoe-btn btn">Add to Cart</button>
                </div>
            </div>

        </div>

        <div class="col" data-aos="zoom-in-up">
            <figure>
                <img src="image/ar2.png" alt="nike-shoe">
            </figure>
            <div class="col-body">
                <p class="rating-icon"><i class="fa-solid fa-star"></i> <span class="rating-num">4.9</span></p>
                <h3 class="heading-three">nike running</h3>
                <p class="sub-heading">air zoom pagasus</p>
                <div class="col-footer">
                    <p class="shoe-price">$350</p>
                    <button class="shoe-btn btn">Add to Cart</button>
                </div>
            </div>

        </div>

        <div class="col" data-aos="zoom-in-up">
            <figure>
                <img src="image/ar3.png" alt="nike-shoe">
            </figure>
            <div class="col-body">
                <p class="rating-icon"><i class="fa-solid fa-star"></i> <span class="rating-num">4.9</span></p>
                <h3 class="heading-three">nike running</h3>
                <p class="sub-heading">air zoom pagasus</p>
                <div class="col-footer">
                    <p class="shoe-price">$350</p>
                    <button class="shoe-btn btn">Add to Cart</button>
                </div>
            </div>

        </div>

        <div class="col" data-aos="zoom-in-up">
            <figure>
                <img src="image/ar4.png" alt="nike-shoe">
            </figure>
            <div class="col-body">
                <p class="rating-icon"><i class="fa-solid fa-star"></i> <span class="rating-num">4.9</span></p>
                <h3 class="heading-three">nike running</h3>
                <p class="sub-heading">air zoom pagasus</p>
                <div class="col-footer">
                    <p class="shoe-price">$350</p>
                    <button class="shoe-btn btn">Add to Cart</button>
                </div>
            </div>

        </div>
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
            <div class="col- collection-item" data-item="" data-aos="zoom-in-up">
                <figure><img src="./image/shoes2.png" alt=""></figure>
                <div class="col-body">

                    <h3  class="heading-three">${product.productName}</h3>

                    <div class="col-footer">
                        <p  class="shoe-price">${product.price}₫</p>
                        <p  class="sub-heading">Đã bán :${product.quantity}</p>
                        <p class="rating-icon"><i class="fa-solid fa-star"></i> <span class="rating-num">4.9</span></p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <button style="margin-top: 20px; background-color: aqua;" id="load-more-btn" class="btn">Load More</button>
</section>
<!-- customer review -->
<jsp:include page="view/customer/review.jsp"></jsp:include>
<script src="./js/toastMessage.js"></script>
<%--Footer--%>
<jsp:include page="./view/footer.jsp"></jsp:include>
<script src="./js/index.js"></script>
<script src="https://unpkg.com/aos@next/dist/aos.js"></script>
<script>
    AOS.init();
</script>

</body>

</html>