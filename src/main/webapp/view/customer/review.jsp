<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 11/23/2024
  Time: 2:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link
            href="https://fonts.googleapis.com/css2?family=Libre+Bodoni:wght@500&family=Plus+Jakarta+Sans:wght@400;600&display=swap"
            rel="stylesheet" />
    <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="../../css/index.css">
    <style>
        /* customer review section */
        .testimonial .sub-heading{
            color: var(--first-gra-clr);
            font-weight: bold;
        }
        .testimonial .wrapper{
            gap: 10px;
            align-items: center;
            /* width: calc(100% * 8); */
        }

        .testimonial figure{
            width: 100px;
        }

        .testimonial .col{
            gap: 5px;
            padding: 1.2rem 1.4rem;
            border-radius: var(--btn-radius);
            background-color: var(--light-bg);
            backdrop-filter: blur(10px);
            z-index: 200;
            height: 250px;
        }


        /* Slide */
        .slide-review {
            flex: 0 0 calc(100% / 3);  /* Mỗi slide chiếm 1/3 chiều rộng */
            box-sizing: border-box;
            padding: 10px; /* Khoảng cách giữa các slide */
        }

        .slides-review{
            transition: transform 0.6s ease-in-out;
        }

        .review-container {
            position: relative;
            overflow: hidden; /* Ẩn các phần slide vượt khỏi container */
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }


        .testimonial .col p{
            margin-top: 10px;
            font-size: 20px;
            white-space: nowrap;
        }

        .testimonial .col h3{
            padding-bottom: 10px;
            color: var(--first-gra-clr);
        }


        .testimonial .fa-star{
            color: #ffd700;
            margin-right: 4px;
        }

        .slider-view-controls {
            z-index: 999;
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            display: flex;
            justify-content: space-between;
            transform: translateY(-50%);
        }


        .slider-view-controls button {
            background-color: #0f0cee;
            border: none;
            border-radius: 50%;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 10px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .slider-view-controls button:hover {
            background-color: #ffa500;

        }

        .slider-view-controls i:hover {
            color: #000;
        }

        .slider-view-controls i {
            font-size: 16px;
            color: #fff;
        }

        .product_message {
            white-space: nowrap;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<section class="container testimonial" id="testimonial-slider">
    <div class="section-heading">
        <div class="heading">
            <p class="sub-heading">testimonial</p>
            <h2 class="heading-two">what our customer says</h2>
        </div>
    </div>

    <div class="review-container">
        <div class="wrapper slides-review">
            <c:forEach var="review" items="${reviewDTOList}" varStatus="status">
                <div class="slide-review">
                    <div class="col" data-aos="zoom-in-up">
                        <div class="block-header" style="display: flex; align-items: center; gap: 30px;">
                            <figure><img style="height: 100px; border-radius: 50%" src="../../image/tes-${status.index + 1}.png" alt=""></figure>
                            <div>
                                <h3 class="heading-three" style="text-align: left;">${review.customer.fullName}</h3>
                                <span class="ratting">
                                  <c:forEach var="i" begin="1" end="${review.ratingValue}">
                                      <i class="fa-solid fa-star"></i>
                                  </c:forEach>
                                </span>
                                <p style="color: #333333">${review.date}</p>
                            </div>
                        </div>
                        <p class="product_message" style="font-size: 20px">Sản phẩm: ${review.productDTO.productName} </p> <span>Size: ${review.productDTO.size}</span>
                        <p class="testi-message">${review.comment}</p>

                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="slider-view-controls">
            <button class="prev-view"><i class="fa-solid fa-chevron-left"></i></button>
            <button class="next-view"><i class="fa-solid fa-chevron-right"></i></button>
        </div>
    </div>
</section>
<script src="../../js/slider.js">
</script>
<script src="https://unpkg.com/aos@next/dist/aos.js"></script>
<script>
    AOS.init();
</script>

</body>
</html>

