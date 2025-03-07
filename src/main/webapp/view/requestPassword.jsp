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
        .error-message{
            color: red;
            margin-bottom: 15px;
        }
    </style>
</head>
<body class="bg-primary">
<section class="p-3 p-md-4 p-xl-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-md-9 col-lg-7 col-xl-6 col-xxl-5">
                <div class="card border-0 shadow-sm rounded-4" style="margin-top: 100px;">
                    <div class="card-body p-3 p-md-4 p-xl-5">
                        <div class="row">
                            <div class="col-12">
                                <div class="mb-5">
                                    <h3>Send to email</h3>
                                </div>
                            </div>
                        </div>
                        <c:if test="${not empty errorEmail}">
                            <div class="error-message" style="display: block">
                                    ${errorEmail}
                            </div>
                        </c:if>
                        <form action="${pageContext.request.contextPath}/requestPassword" method="POST">
                            <div class="row gy-3 overflow-hidden">
                                <div class="col-12">
                                    <div class="form-floating mb-3">
                                        <input type="email" class="form-control"
                                               name="email"
                                               id="email" placeholder="name@example.com" required>
                                        <label for="email" class="form-label">Email</label>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="d-grid">
                                        <button class="btn bsb-btn-2xl btn-primary" type="submit">Reset password</button>
                                    </div>
                                </div>
                            </div>
                        </form>
                        <p class="text-danger">${mess}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>