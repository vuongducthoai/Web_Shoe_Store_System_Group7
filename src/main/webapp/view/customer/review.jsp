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
        /* ============== Resest HTML TAGS ================= */

        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'poppins';
            text-transform: capitalize;
            text-decoration: none;
        }


        body{
            background-color: #e6e6e6;
        }

        :root{
            /* padding */
            --p-xm: 4px;
            --p-sm:10px 14px;
            --p-md:12px 16px;
            --p-lg:16px 24px;
            --container-pad:60px 40px;
            --section-heading-pad:44px 40px;

            /* color */
            --primar-clr: #333; /* Màu chính sáng hơn (màu chữ đen đậm) */
            --primar-clr-light: #0e0d0d; /* Màu sáng nhạt hơn */
            --first-gra-clr: #ff5e00; /* Màu gradient đầu tiên sáng hơn */
            --second-gra-clr: #ffba00; /* Màu gradient thứ hai sáng hơn */
            --dark-clr: #f8f8f8; /* Thay đổi màu nền tối thành sáng */
            --light-bg: #ffffff; /*


    /* border radius */

            --border-radius-full:50%;
            --section-rad:12px;
            --btn-radius:4px;
            --card-rad:8px;

            /* transition */
            --transition-one: all .25s ease;
            --transition-two: all .50s linear;
        }




        /* =================== Custome Universal classes========== */
        .container{
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            position: relative;
            padding: var(--container-pad);
            width: 100%;
        }

        .wrapper{
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 30px;
            position: relative;
            width: 1280px;
        }

        .col{
            flex: 1;
            display: flex;
            justify-content: center;
            flex-direction: column;
            position: relative;
        }

        figure{
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        figure img{
            width: 100%;
            object-fit: cover;
        }

        .section-heading{
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 1280px;
            padding: var(--section-heading-pad);
            z-index: 100;
        }
        .section-heading .sub-heading{
            font-size: 18px;
            color: var(--primar-clr-light);
        }

        .heading-one{
            font-size: 4rem;
            text-transform: capitalize;
            color: var(--primar-clr);
        }
        .heading-two{
            font-size: 2.8rem;
            text-transform: capitalize;
        }
        .heading-three{
            font-size: 1.4rem;
            text-transform: capitalize;
        }


        /* ============================== */
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
            word-break: break-all;
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
            <div class="slide-review">
                <div class="col" data-aos="zoom-in-up">
                    <div class="block-header" style="display: flex; align-items: center; gap: 30px;">
                        <figure><img src="../../image/tes-1.png" alt=""></figure>
                        <div>
                            <h3 class="heading-three" style="text-align: left;">thor</h3>
                            <span class="ratting"><i class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i></span>
                            <p class="sub-heading">2024-11-17 00:00</p>
                        </div>
                    </div>
                    <p class="product_message">Sản phẩm: </p> <span>Size:</span>
                    <p class="testi-message">Sản phẩm tuyệt vời, chất lượng tốt!</p>

                </div>
            </div>

            <div class="slide-review">
                <div class="col" data-aos="zoom-in-up">
                    <div class="block-header" style="display: flex; align-items: center; gap: 30px;">
                        <figure><img src="../../image/tes-2.png" alt=""></figure>
                        <div>
                            <h3 class="heading-three" style="text-align: left;"> john</h3>
                            <span class="ratting"><i class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i></span>
                            <p class="sub-heading">2024-11-17 00:00</p>
                        </div>
                    </div>
                    <p class="product_message"> Sản phẩm: </p> <span>Size:</span>
                    <p class="testi-message">Sản phẩm tuyệt vời, chất lượng tốt!</p>

                </div>
            </div>
            <div class="slide-review">
                <div class="col" data-aos="zoom-in-up">
                    <div class="block-header" style="display: flex; align-items: center; gap: 30px;">
                        <figure><img src="../../image/tes-3.png" alt=""></figure>
                        <div>
                            <h3 class="heading-three" style="text-align: left;">ABC</h3>
                            <span class="ratting"><i class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i></span>
                            <p class="sub-heading">2024-11-17 00:00</p>
                        </div>
                    </div>
                    <span class="product_message">Sản phẩm: </span> <span>Size:</span>
                    <span class="testi-message">Đánh giá: Sản phẩm tuyệt vời, chất lượng tốt!</span>

                </div>
            </div>

            <div class="slide-review">
                <div class="col" data-aos="zoom-in-up">
                    <div class="block-header" style="display: flex; align-items: center; gap: 30px;">
                        <figure><img style="border-radius: 50%; width: 100px; height: 100px" src="../../image/tes-4.jpg" alt=""></figure>
                        <div>
                            <h3 class="heading-three" style="text-align: left;">BCD</h3>
                            <span class="ratting"><i class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i></span>
                            <p class="sub-heading">2024-11-17 00:00</p>
                        </div>
                    </div>
                    <p class="product_message">Sản phẩm: </p> <span>Size:</span>
                    <p class="testi-message">Sản phẩm tuyệt vời, chất lượng tốt!</p>

                </div>
            </div>


            <div class="slide-review">
                <div class="col" data-aos="zoom-in-up">
                    <div class="block-header" style="display: flex; align-items: center; gap: 30px;">
                        <figure><img style="border-radius: 50%;width: 100px; height: 100px" src="../../image/tes-5.jpg" alt=""></figure>
                        <div>
                            <h3 class="heading-three" style="text-align: left;">BCD</h3>
                            <span class="ratting"><i class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i> <i class="fa-solid fa-star"></i> <i
                                    class="fa-solid fa-star"></i></span>
                            <p class="sub-heading">2024-11-17 00:00</p>
                        </div>
                    </div>
                    <p class="product_message">Sản phẩm: </p> <span>Size:</span>
                    <p class="testi-message">Sản phẩm tuyệt vời, chất lượng tốt!</p>

                </div>
            </div>
        </div>
        <div class="slider-view-controls">
            <button class="prev-view"><i class="fa-solid fa-chevron-left"></i></button>
            <button class="next-view"><i class="fa-solid fa-chevron-right"></i></button>
        </div>
    </div>
</section>
<script src="../../js/slider.js">
</script>
<script src="../../js/index.js"></script>
<script src="https://unpkg.com/aos@next/dist/aos.js"></script>
<script>
    AOS.init();
</script>

</body>
</html>

