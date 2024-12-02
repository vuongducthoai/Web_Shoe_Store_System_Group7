<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 11/20/2024
  Time: 1:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <link rel="stylesheet" href="../css/slider.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
  <style>
    .view_more {
      position: absolute;
      font-weight: 700;
      width: 203px;
      font-size: 20px;
      height: 68px;
      right: 200px;
      bottom: 217px;
      background: greenyellow;
      border-radius: 20px;
      color: #000;
      border: 3px solid red;
    }

    .view_more:hover {
      cursor: pointer;
      background-color: aqua;
    }

    .slide {
      position: relative;
    }
  </style>
</head>
<body>
<section class="hero" id="hero-slider">
  <div class="main" id="Home">
    <div class="slider">

      <div class="slides-container">
        <div class="slide" style="position: relative;;">
          <img style="width: 100%; height: 100vh" src="../image/bannerShoeSport.webp">
          <button  class="view_more">View More</button>
        </div>
        <div class="slide" style="position: relative;;">
          <img style="width: 100%; height: 100vh" src="../image/banner2.jpg">
          <button class="view_more" style="         position: absolute;
             ">View More</button>
        </div>
        <div class="slide" style="position: relative;;">
          <img style="width: 100%; height: 100vh" src="../image/banner3.webp">
          <button class="view_more" style="         position: absolute;
             ">View More</button>
        </div>
        <div class="slide" style="position: relative;;">
          <img style="width: 100%; height: 100vh" src="../image/banner4.webp">
          <button class="view_more" style="         position: absolute;
             ">View More</button>
        </div>
      </div>
    </div>
    <div class="slider-controls">
      <button class="prev"><i style="color: white" class="fa-solid fa-chevron-left"></i></button>
      <button class="next"><i style="color: white" class="fa-solid fa-chevron-right"></i></button>
    </div>
  </div>
</section>
<script src="../js/slider.js">
</script>
</body>
</html>