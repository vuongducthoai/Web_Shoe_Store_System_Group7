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
  </style>
</head>
<body>
<section class="hero" id="hero-slider">
  <div class="main" id="Home">
    <div class="slider">
      <div class="slides-container">
        <c:forEach var="promotionProduct" items="${promotionProductDTOList}">
          <c:set var="productName" value="${promotionProduct.product.productName}" />
          <c:set var="nameParts" value="${fn:split(productName, ' ')}" />

          <!-- Set individual parts based on array indices -->
          <c:set var="namePart1" value="${nameParts[0]}" />
          <c:set var="namePart2" value="${nameParts[1]}" />

          <div class="slide">
            <div class="main_content">
              <div>
                <div class="main_text">
                  <h1>${namePart1}<br><span>${namePart2}</span></h1>
                  <p>${promotionProduct.product.description}</p>
                  <button style="margin: 100px 0 0 50px;" class="btn btn-hero">pre-order now</button>
                </div>
                <div style="margin-top: 100px;" class="hero-img-off" data-aos="zoom-in-up">
                  <h3 class="heading-three">${promotionProduct.promotion.promotionName}</h3>
                  <p>StartDate: ${promotionProduct.promotion.startDate}</p>
                  <p>EndDate: ${promotionProduct.promotion.endDate}</p>
                </div>
              </div>
              <div class="main_image">
                <img src="../image/shoes3.png">
              </div>
            </div>
          </div>
        </c:forEach>



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