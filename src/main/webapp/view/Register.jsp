<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 11/15/2024
  Time: 8:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
  <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://unpkg.com/bs-brain@2.0.4/components/logins/login-9/assets/css/login-9.css">
    <style>
      .card-registration {
        padding-top: 40px;
        max-width: 100%;
        width: 100%;
        margin: auto; /* Căn giữa form */
        box-sizing: border-box;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        /*transition: transform 0.3s ease, box-shadow 0.3s ease;*/
      }

      .error-message {
        margin-top: 5px;
        color: red;
        font-size: 14px;
        display: none;
      }

      input {
        text-transform: none;
      }

      .form-group select {
        margin-left: 12px;
        width: 100px;
        height: 40px;
        border: none;
        padding: .375rem .75rem;
        font-size: 1rem;
        font-weight: 400;
        line-height: 1.5;
        color: #495057;
        background-color: #fff;
        background-clip: padding-box;
        border: 1px solid #ced4da;
        border-radius: .25rem;
        transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
      }

      label {
        font-weight: 500;
      }

      .require {
        margin-left: 4px;
        color: red;
      }

      @media(max-width: 576px){
        .form-group select {
          margin-left: 10px;
        }

        label {
          font-size: 16px;
        }

        .error-message {
          font-size: 12px;
        }
      }
    </style>
</head>
<body>
<jsp:include page="./header.jsp"></jsp:include>
<section class="vh-100 gradient-custom">
  <div class="container h-100">
    <div class="row justify-content-center align-items-center h-100">
      <div class="">
        <div class="card shadow-2-strong card-registration" style="border-radius: 15px;">
          <div class="p-md-5">
            <h3 class="mb-1 pb-2 pb-md-0 mb-md-3">ĐĂNG KÝ TÀI KHOẢN</h3>
            <form onsubmit="return validateForm()" action="register" method="post">

              <div class="row">
                <div class="col-12 col-md-6 mb-2">
                  <div data-mdb-input-init class="form-outline flex-fill mb-0">
                    <label class="form-label" for="fullName">Họ Và Tên</label> <span class="require">* </span><br>
                    <input name="fullName" type="text" id="fullName" class="form-control" value="${customerDTO.getFullName()}"/>
                    <span id="fullname-error" class="error-message">Họ và tên phải có 2 từ trở lên, mỗi từ viết hoa chữ cái đầu tiên</span>

                  </div>
                </div>
                <div class="col-12 col-md-6 mb-2">
                  <div data-mdb-input-init class="form-outline flex-fill mb-0">
                    <label class="form-label" for="phone">Phone</label><span class="require">*</span>
                    <input name="phone" type="tel" id="phone" class="form-control"  value="${customerDTO.getPhone()}"/>
                    <span id="phone-error" class="error-message">Số điện thoại phải có 10 số</span>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-12 col-md-6 mb-2">
                  <label>Ngày sinh</label><span class="require">*</span>
                  <div class="row">
                    <div class="form-group">
                      <!-- Ngày -->
                      <select name="day" >
                        <option value="">Ngày</option>
                        <%
                          String selecteDay = request.getParameter("day");
                          for (int i = 1; i <= 31; i++) {
                        %>
                        <option value="<%=i%>"
                            <%=(selecteDay != null && selecteDay.equals(String.valueOf(i))) ? "selected" : ""%>
                        >

                          <%=i%>
                        </option>
                        <%
                          }
                        %>
                      </select>

                      <!-- Tháng -->
                      <select name="month" >
                        <option value="">Tháng</option>
                        <%
                          String selectMonth = request.getParameter("month");
                          for (int i = 1; i <= 12; i++) {
                        %>
                        <option value="<%=i%>"
                          <%=(selectMonth != null && selectMonth.equals(String.valueOf(i))) ? "selected" : ""%>

                        ><%=i%></option>
                        <%
                          }
                        %>
                      </select>

                      <!-- Năm -->
                      <select name="year">
                        <option value="">Năm</option>
                        <%
                          String selectYear = request.getParameter("year");
                          int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                          for (int i = 1940; i <= currentYear; i++) {
                        %>
                        <option value="<%=i%>"
                          <%=(selectYear != null && selectYear.equals(String.valueOf(i))) ? "selected" : ""%>
                        >
                          <%=i%>
                        </option>
                        <%
                          }
                        %>
                      </select>
                      <span id="birthDay-error" class="error-message" style="margin-left: 12px">Vui lòng chọn đầy đủ Ngày, Tháng, Năm!</span>
                    </div>

                  </div>


                </div>
                <div class="col-12 col-md-6 mb-2">
                  <div class="row">
                    <div data-mdb-input-init class="col-md-6">
                      <label class="form-label" for="houseNumber" >Số Nhà</label><span class="require">*</span>
                      <input type="text" name="houseNumber" id="houseNumber" class="form-control" value="${customerDTO.getAddressDTO().getHouseNumber()}"/>

                    </div>
                    <div data-mdb-input-init class="form-outline flex-fill mb-0 col-md-6">
                      <label class="form-label" for="streetName">Tên Đường</label><span class="require">*</span>
                      <input type="text" name="streetName" id="streetName" class="form-control" value="${customerDTO.getAddressDTO().getStreetName()}"/>

                    </div>
                  </div>
                  <span id="streetName-error" class="error-message">Số nhà / đường không được để trống.</span>
                </div>
              </div>

              <div class="row">
                <div class="col-12 col-md-6 mb-2">

                  <div data-mdb-input-init class="form-outline flex-fill mb-0">
                    <label class="form-label" for="email">Email</label><span class="require">*</span>
                    <input type="email" name="email" id="email" class="form-control" value="${customerDTO.getAccount().getEmail()}"/>
                    <span id="email-error" class="error-message">Email phải đúng định dạng (vd: thoai@gmail.com, ...)</span>
                    <c:if test="${not empty errorEmail}">
                      <div class="error-message" style="display: block">
                          ${errorEmail}
                      </div>
                    </c:if>
                  </div>

                </div>
                <div class="col-12 col-md-6 mb-2">
                  <div data-mdb-input-init class="form-outline flex-fill mb-0">
                    <label class="form-label" for="city">Chọn Tỉnh</label><span class="require">*</span>
                    <select class="form-control" id="city" name="province">
                      <option value="" selected>Chọn tỉnh thành</option>
                    </select>
                    <span id="province-error" class="error-message">Vui lòng chọn tỉnh thành</span>
                  </div>

                </div>
              </div>

              <div class="row">
                <div class="col-12 col-md-6 mb-4">

                  <div data-mdb-input-init class="form-outline flex-fill mb-0">
                    <label class="form-label" for="password">Mật khẩu</label><span class="require">*</span>
                    <input type="password" name="password" id="password" class="form-control" />
                    <span id="password-error" class="error-message">Mật khẩu phải có ít nhất 6 kí tự</span>
                  </div>

                </div>
                <div class="col-12 col-md-6 mb-4">
                  <div data-mdb-input-init class="form-outline flex-fill mb-0">
                    <label class="form-label" for="district">Chọn Quận / Huyện</label><span class="require">*</span>
                    <select class="form-control" id="district" aria-label=".form-select-sm" name="district">
                      <option value="" selected>Chọn quận huyện</option>
                    </select>
                    <span id="district-error" class="error-message">Vui lòng chọn quận / huyện</span>
                  </div>

                </div>
              </div>


              <div class="row">
                <div class="col-12 col-md-6 mb-2">
                  <div data-mdb-input-init class="form-outline flex-fill mb-0">

                    <label class="form-label" for="re-password">Nhập lại mật khẩu</label><span class="require">*</span>
                    <input name="re-password" type="password" id="re-password" class="form-control" name="re-password" />
                    <span id="re-password-error" class="error-message">Xác nhận mật khẩu là bắt buộc</span>
                  </div>

                </div>
                <div class="col-12 col-md-6 mb-2">
                  <div data-mdb-input-init class="form-outline flex-fill mb-0">
                    <label class="form-label" for="ward">Chọn Phường / Xã</label><span class="require">*</span>
                    <select class="form-control" id="ward" name="ward">
                      <option value="" selected>Chọn phường xã</option>
                    </select>
                    <span id="ward-error" class="error-message">Vui lòng chọn Phường / Xã</span>
                  </div>

                </div>
              </div>

              <div class="mt-2 pt-2">
                <input data-mdb-ripple-init class="btn btn-primary btn-lg" type="submit" value="Submit" />
              </div>

              <div class="row mt-1">
                <div class="col-12">
                  <h5 class="mt-2 mb-2 text-center">----------------Hoặc Đăng Ký Bằng-----------------</h5>
                  <div class="d-flex gap-2 gap-sm-3 justify-content-center">
                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid&redirect_uri=http://localhost:8080/login&response_type=code&client_id=347868096033-jmjks5edljqsd2aetc3bnmdpdl7fvrak.apps.googleusercontent.com" class="btn btn-outline-danger bsb-btn-circle bsb-btn-circle-2xl">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-google" viewBox="0 0 16 16">
                        <path d="M15.545 6.558a9.42 9.42 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.689 7.689 0 0 1 5.352 2.082l-2.284 2.284A4.347 4.347 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.792 4.792 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.702 3.702 0 0 0 1.599-2.431H8v-3.08h7.545z" />
                      </svg>
                    </a>
                    <a style="margin-left: 20px" href="https://www.facebook.com/v21.0/dialog/oauth?client_id=958382746118607&redirect_uri=http://localhost:8080/loginFacebook&scope=email" class="btn btn-outline-primary bsb-btn-circle bsb-btn-circle-2xl">
                      <svg  xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-facebook" viewBox="0 0 16 16">
                        <path d="M16 8.049c0-4.446-3.582-8.05-8-8.05C3.58 0-.002 3.603-.002 8.05c0 4.017 2.926 7.347 6.75 7.951v-5.625h-2.03V8.05H6.75V6.275c0-2.017 1.195-3.131 3.022-3.131.876 0 1.791.157 1.791.157v1.98h-1.009c-.993 0-1.303.621-1.303 1.258v1.51h2.218l-.354 2.326H9.25V16c3.824-.604 6.75-3.934 6.75-7.951z" />
                      </svg>
                    </a>
                  </div>
                </div>
              </div>

            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js" integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.26.1/axios.min.js" integrity="sha512-bPh3uwgU5qEMipS/VOmRqynnMXGGSRv+72H/N260MQeXZIK4PG48401Bsby9Nq5P5fz7hy5UGNmC/W1Z51h2GQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="../js/register.js">
</script>
  <script src="../js/APITinhThanh.js"></script>
</body>
</html>
