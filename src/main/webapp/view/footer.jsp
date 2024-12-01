<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 11/20/2024
  Time: 10:11 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Header</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>

<footer>
    <div class="footer_main">
        <div class="tag">
            <h1>Contact</h1>
            <a href="#"><i class="fa-solid fa-house"></i>484 Le Van Viet, TP.HCM</a>
            <a href="#"><i class="fa-solid fa-phone"></i>+84 59716818</a>
            <a href="#"><i class="fa-solid fa-envelope"></i>ShoeStoreVip@gmail.com</a>
            <div class="social_link">
                <a href="#"><i class="fa-brands fa-facebook-f"></i></a>
                <a href="#"><i class="fa-brands fa-twitter"></i></a>
                <a href="#"><i class="fa-brands fa-instagram"></i></a>
                <a href="#"><i class="fa-brands fa-linkedin-in"></i></a>
            </div>
        </div>

        <div class="tag">
            <h1 style="text-align: center;">Get Help</h1>
            <a href="#" class="center">FAQ</a>
            <a href="#" class="center">Shipping</a>
            <a href="#" class="center">Returns</a>
            <a href="#" class="center">Payment Options</a>
        </div>

        <div class="tag">
            <h1>Our Stores</h1>
            <a href="#" class="center">TP.HCM</a>
            <a href="#" class="center">Phu Yen</a>
            <a href="#" class="center">Đa Nang</a>
            <a href="#" class="center">Ha Noi</a>
        </div>

        <div class="tag" style="width: 25%;">
            <div class="map">
                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3918.55596002732!2d106.79674410000003!3d10.845253600000008!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3175273e5b95f75d%3A0x774a073b596e07ab!2zNDg0IEzDqiBWxINuIFZp4buHdCwgVMSDbmcgTmjGoW4gUGjDuiBBLCBRdeG6rW4gOSwgSOG7kyBDaMOtIE1pbmgsIFZp4buHdCBOYW0!5e0!3m2!1svi!2s!4v1731420545448!5m2!1svi!2s" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
            </div>
        </div>
    </div>
    <div class="footer" style="background-color: rgb(6, 71, 82); color: white; text-align: center;">
        © The copyright by ShoeStoreVip
    </div>
</footer>
</body>
</html>