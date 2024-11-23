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
        <div class="slide">
          <div class="main_content">
            <div class="main_text">
              <h1>NIKE<br><span>Collection</span></h1>
              <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry...</p>
            </div>
            <div class="main_image">
              <img src="../image/shoes.png">
            </div>
          </div>
        </div>
        <div class="slide">
          <div class="main_content">
            <div class="main_text">
              <h1>Adidas<br><span>New Arrivals</span></h1>
              <p>The latest collection of Adidas with innovative designs and styles.</p>
            </div>
            <div class="main_image">
              <img src="../image/shoes2.png" alt="Shoe 2">
            </div>
          </div>
        </div>
        <div class="slide">
          <div class="main_content">
            <div class="main_text">
              <h1>Puma<br><span>Exclusive</span></h1>
              <p>Get the most exclusive Puma shoes available now!</p>
            </div>
            <div class="main_image">
              <img src="../image/shoes3.png" alt="Shoe 3">
            </div>
          </div>
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