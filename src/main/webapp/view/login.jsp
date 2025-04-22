<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Login Page</title>
  <link rel="stylesheet" href="https://unpkg.com/bootstrap@5.3.3/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://unpkg.com/bs-brain@2.0.4/components/logins/login-9/assets/css/login-9.css">
</head>
<style>
  * {
    font-size: 18px;
    font-weight: 500;
  }
  .error-message {
    color: red;
    margin-bottom: 15px;
  }

  input{
    text-transform: none;
  }

  .captcha-message {
    display: none;
    color: red;
  }

  .hero-right {
    background-image: url("../image/logo_login.png");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    height: 100vh;
  }

  @media (max-width: 768px) {
    .hero-right {
      display: none;
    }

    .hero-left {
      margin: auto;
    }
  }
</style>
<body>
<jsp:include page="./header.jsp"></jsp:include>
<!-- Login 9 - Bootstrap Brain Component -->
<section class="py-md-5">
  <div class="container">
    <div class="row">
      <%-- Hero left --%>
      <div class="col-12 col-md-6 hero-left">
        <div class="card border-0 rounded-4">
          <div class="card-body p-md-4">
            <div class="row">
              <div class="col-12">
                <div class="mb-3">
                  <h1 style="text-align: center">Đăng Nhập</h1>
                  <p>Bạn chưa có tài khoản? <a href="Register.jsp">Đăng Ký</a></p>
                </div>
              </div>
            </div>
            <form onsubmit="return validateForm()" action="loginEmail" method="post">
              <span class="error-message">${errorMessage}</span>
              <div class="row gy-3 overflow-hidden">
                <div class="col-12">
                  <div class="form-floating mb-2">
                    <input style="text-transform: none" type="email" class="form-control" name="email" id="email" placeholder="name@example.com" required>
                    <label for="email" class="form-label">Email</label>
                  </div>
                </div>
                <div class="col-12">
                  <div class="form-floating mb-2">
                    <input style="text-transform: none" type="password" class="form-control" name="password" id="password" placeholder="Mật khẩu" required>
                    <label for="password" class="form-label">Password</label>
                  </div>
                </div>
                <div class="col-12">
                  <div class="form-floating mb-2">
                    <div id="captcha" class="g-recaptcha" data-sitekey="6LdFY4EqAAAAACbQyuCRoJDTJHiYPuCsTXng2isF"></div>
                    <span id="captcha-message" class="captcha-message">Vui lòng xác thực 2 bước và thử lại</span>
                  </div>
                </div>

                <div class="col-12">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="remember_me" id="remember_me">
                    <label class="form-check-label text-secondary" for="remember_me">
                     Ghi nhớ đăng nhập
                    </label>
                  </div>
                </div>
                <div class="col-12">
                  <div class="d-grid">
                    <button class="btn btn-primary btn-lg" type="submit">Đăng nhập</button>
                  </div>
                </div>
              </div>
            </form>
            <div class="row">
              <div class="col-12">
                <div class="d-flex gap-2 justify-content-end mt-2">
                  <a href="requestPassword">Quên mật khẩu</a>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-12">
                <p class="mt-2 mb-1" style="font-size: 18px; font-weight: 600">Hoặc tiếp tục với</p>
                <div class="d-flex gap-2 gap-sm-3 justify-content-center">
                  <a href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid&redirect_uri=http://localhost:8080/login&response_type=code&client_id=347868096033-jmjks5edljqsd2aetc3bnmdpdl7fvrak.apps.googleusercontent.com" class="btn btn-outline-danger bsb-btn-circle bsb-btn-circle-2xl">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-google" viewBox="0 0 16 16">
                      <path d="M15.545 6.558a9.42 9.42 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.689 7.689 0 0 1 5.352 2.082l-2.284 2.284A4.347 4.347 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.792 4.792 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.702 3.702 0 0 0 1.599-2.431H8v-3.08h7.545z" />
                    </svg>
                  </a>
                  <a href="https://www.facebook.com/v21.0/dialog/oauth?client_id=958382746118607&redirect_uri=http://localhost:8080/loginFacebook&scope=email" class="btn btn-outline-primary bsb-btn-circle bsb-btn-circle-2xl">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-facebook" viewBox="0 0 16 16">
                      <path d="M16 8.049c0-4.446-3.582-8.05-8-8.05C3.58 0-.002 3.603-.002 8.05c0 4.017 2.926 7.347 6.75 7.951v-5.625h-2.03V8.05H6.75V6.275c0-2.017 1.195-3.131 3.022-3.131.876 0 1.791.157 1.791.157v1.98h-1.009c-.993 0-1.303.621-1.303 1.258v1.51h2.218l-.354 2.326H9.25V16c3.824-.604 6.75-3.934 6.75-7.951z" />
                    </svg>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

        <%-- Hero Right   --%>
        <div class="col-12 col-md-6 hero-right">
          <div class="d-flex text-bg-primary">
            <div class="col-12 col-xl-9">
            </div>
          </div>
        </div>
    </div>
  </div>
</section>

<script src="https://www.google.com/recaptcha/api.js" async defer></script>
<script src="../js/login.js"></script>

</body>
</html>
