<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 11/17/2024
  Time: 9:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login page</title>
    <link rel="stylesheet" href="https://unpkg.com/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://unpkg.com/bs-brain@2.0.4/components/logins/login-6/assets/css/login-6.css">
    <style>
        .height-100 {
            height: 100vh
        }

        .card {
            width: 550px;
            border: none;
            height: 300px;
            box-shadow: 0px 5px 20px 0px #d2dae3;
            z-index: 1;
            display: flex;
            justify-content: center;
            align-items: center
        }

        .card h6 {
            color: red;
            font-size: 20px
        }

        .inputs input {
            width: 40px;
            height: 40px
        }

        input[type=number]::-webkit-inner-spin-button,
        input[type=number]::-webkit-outer-spin-button {
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
            margin: 0
        }

        .card-2 {
            background-color: #fff;
            padding: 10px;
            width: 350px;
            height: 100px;
            bottom: -50px;
            left: 20px;
            position: absolute;
            border-radius: 5px
        }

        .card-2 .content {
            margin-top: 50px
        }

        .card-2 .content a {
            color: red
        }

        .form-control:focus {
            box-shadow: none;
            border: 2px solid red
        }

        .validate {
            border-radius: 20px;
            height: 40px;
            background-color: red;
            border: 1px solid red;
            width: 140px
        }
    </style>
</head>
<body class="bg-primary"><div class="container height-100 d-flex justify-content-center align-items-center">
    <form action="${pageContext.request.contextPath}/verifyOTP" method="POST">
    <div class="position-relative">
        <div class="card p-2 text-center">
            <h6>Please enter the one time password <br> to verify your account</h6>
            <div> <span>A code has been sent to</span> <small id="maskedNumber">*******9897</small> </div>
            <div id="otp" class="inputs d-flex flex-row justify-content-center mt-2">
                <input class="m-2 text-center form-control rounded" name="num1" type="text" id="first" maxlength="1" />
                <input class="m-2 text-center form-control rounded" name="num2" type="text" id="second" maxlength="1" />
                <input class="m-2 text-center form-control rounded" name="num3" type="text" id="third" maxlength="1" />
                <input class="m-2 text-center form-control rounded" name="num4" type="text" id="fourth" maxlength="1" />
                <input class="m-2 text-center form-control rounded" name="num5" type="text" id="fifth" maxlength="1" />
                <input class="m-2 text-center form-control rounded" name="num6" type="text" id="sixth" maxlength="1" />
            </div>
            <div class="mt-4">
                <button id="validateBtn" class="btn btn-danger px-4 validate">Validate</button>
            </div>
        </div>
        </div>
    </form>
</div>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        function OTPInput() {
            const inputs = document.querySelectorAll('#otp > input');
            for (let i = 0; i < inputs.length; i++) {
                inputs[i].addEventListener('input', function() {
                    if (this.value.length > 1) {
                        this.value = this.value[0]; //
                    }
                    if (this.value !== '' && i < inputs.length - 1) {
                        inputs[i + 1].focus(); //
                    }
                });

                inputs[i].addEventListener('keydown', function(event) {
                    if (event.key === 'Backspace') {
                        this.value = '';
                        if (i > 0) {
                            inputs[i - 1].focus();
                        }
                    }
                });
            }
        }

        OTPInput();

        <%--const validateBtn = document.getElementById('validateBtn');--%>
        <%--validateBtn.addEventListener('click', function() {--%>
        <%--    let otp = '';--%>
        <%--    document.querySelectorAll('#otp > input').forEach(input => otp += input.value);--%>
        <%--    alert(`Entered OTP: ${otp}`);--%>
        <%--});--%>
    });
</script>
</body>
</html>