<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ProductInformation.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body>
<div id="custom-toast"></div>
<header>
    <jsp:include page="./view/header.jsp"></jsp:include>
</header>
<main style="margin-top: 100px;">


    <div class="container">
        <div class="product-detail w-100">
            <div class="row">
                <div class="col-sm-6 d-flex">
                    <div class="product-image-list col-sm-3 d-flex flex-column me-2">

                        <c:forEach var="image" items="${images}">
                            <img src="${image}" alt="" class="product-images-item mb-1 h-auto rounded-4">
                        </c:forEach>
                    </div>
                    <img src="${images[0]}" alt="" class="product-image-focus col-sm-9 img-fluid rounded-4">
                </div>


                <div class="product-describe col-sm-6 d-flex flex-column">
                    <h3 class="product-name fw-bolder fs-1 ">${name}</h3>
                    <div class="rated d-flex">
                        <span class="d-block rated-number ms-2 mb-2">${averageRating != null ? averageRating: 0}/5</span>
                        <i class="fa-solid fa-star me-1 mt-1 ms-2 " style="color: #FF5722;"></i>
                    </div>
                    <c:if test="${not empty promotion.getPromotion()}">
                        <div class="price  d-inline-block ">

                            <c:set var="finalPrice" value="${price}" />

                            <c:if test="${promotion.getPromotion().getDiscountType().name() == 'VND'}">
                                <c:set var="finalPrice" value="${price - promotion.getPromotion().getDiscountValue()}" />
                            </c:if>

                            <c:if test="${promotion.getPromotion().getDiscountType().name() == 'Percentage'}">
                                <c:set var="finalPrice" value="${price - (price * promotion.getPromotion().getDiscountValue() / 100)}" />
                            </c:if>
                            <span class="current-price fw-bold fs-2 ">${finalPrice} VND</span>
                            <span class="old-price fs-2 ms-4">${price} VND</span>

                            <c:if test="${promotion.getPromotion().getDiscountType().name() == 'VND'}">
                                <span class="product-badge badge bg-danger rounded-pill align-items-center ms-3">-${promotion.getPromotion().getDiscountValue()}VND</span>
                            </c:if>
                            <c:if test="${promotion.getPromotion().getDiscountType().name() != 'VND'}">
                            <span class="product-badge badge bg-success rounded-pill align-items-center ms-3">
                                -${promotion.getPromotion().getDiscountValue()}%
                            </span>
                            </c:if>
                            <div class="discount-content"><p>${promotion.getPromotion().getPromotionName()}</p></div>
                                <%--<span class="product-badge badge bg-danger rounded-pill align-items-center ms-3">-40%</span>--%>
                        </div>
                        <div class="time-remaining-promotion p-1"
                             style="color:white;width: 100%; background: linear-gradient(to right, rgb(238, 77, 45), rgb(255, 115, 55));">
                            <div class="ms-2">
                                <i class="fa-regular fa-clock"></i>
                                KẾT THÚC TRONG
                                <div id="days" class="days-remaining fw-bolder"
                                     style="display:inline-block; background-color: black; color: white; border-radius:5px; padding: 1px 4px;">
                                    12
                                </div>
                                <div id="hours" class="hours-remaining fw-bolder"
                                     style="display:inline-block; background-color: black; color: white; border-radius:5px; padding: 1px 4px;">
                                    12
                                </div>
                                <div id="minutes" class="minutes-remaining fw-bolder"
                                     style="display:inline-block; background-color: black; color: white; border-radius:5px; padding: 1px 4px;">
                                    12
                                </div>
                                <div id="seconds" class="secconds-remaining fw-bolder"
                                     style="display:inline-block; background-color: black; color: white; border-radius:5px; padding: 1px 4px;">
                                    12
                                </div>
                            </div>
                            <script>
                                // Thay đổi giá trị deadline tại đây (theo định dạng "YYYY-MM-DD HH:MM:SS")
                                const endDateStr = "${promotion.getPromotion().getEndDate()}"; // Giả sử giá trị được trả về từ server là chuỗi
                                const deadline = new Date(endDateStr).getTime();

                                // Cập nhật đồng hồ đếm ngược mỗi giây
                                const x = setInterval(function () {
                                    const now = new Date().getTime();
                                    const distance = deadline - now;

                                    const days = Math.floor(distance / (1000 * 60 * 60 * 24));
                                    const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                                    const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                                    const seconds = Math.floor((distance % (1000 * 60)) / 1000);

                                    // Hiển thị kết quả lên giao diện
                                    document.getElementById("days").innerHTML = days;
                                    document.getElementById("hours").innerHTML = hours;
                                    document.getElementById("minutes").innerHTML = minutes;
                                    document.getElementById("seconds").innerHTML = seconds;

                                    // Nếu thời gian đã hết
                                    if (distance < 0) {
                                        clearInterval(x);
                                        document.getElementById("days").innerHTML = "00";
                                        document.getElementById("hours").innerHTML = "00";
                                        document.getElementById("minutes").innerHTML = "00";
                                        document.getElementById("seconds").innerHTML = "00";
                                    }
                                }, 1000);
                            </script>
                        </div>
                    </c:if>
                    <c:if test="${ empty promotion.getPromotion()}">
                        <div class="price  d-inline-block ">
                            <span class="current-price fw-bold fs-2 ">${price}VND</span>
                                <%--<span class="product-badge badge bg-danger rounded-pill align-items-center ms-3">-40%</span>--%>
                        </div>

                    </c:if>


                    <span class="product-description mt-2">${description}</span>
                    <hr class="vw-100">
                    <span class="mt-1 fs-5 fw-lighter">Select color</span>
                    <div class="product-colors d-flex mt-2 mb-3">
                        <c:forEach var="color" items="${colors}">
                            <div class="circle me-3" data-color="${color}" style="background-color: ${color}"></div>
                        </c:forEach>
                    </div>
                    <hr class="vw-100">
                    <span class="mt-1 fs-5 fw-lighter">Choose size</span>
                    <div class="d-flex mt-2 mb-3">
                        <c:forEach var="size" items="${sizes}">
                            <button class="d-flex justify-content-center align-items-center size rounded-pill me-2"
                                    data-size="${size}">${size}</button>
                        </c:forEach>
                    </div>
                    <div>
                        <span class="label-quantity-remain">chọn màu và kích thước </span><span
                            id="quantity-display"></span>
                    </div>

                    <hr class="vw-100">
                    <div class="d-flex">
                        <div class="quantity-container rounded-pill d-flex justify-content-around align-items-center  ">
                            <i class="fa-solid fa-minus" style="color:black; cursor:pointer;"></i>
                            <span class="quantity">1</span>
                            <i class="fa-solid fa-plus" style="color:black; cursor:pointer;"></i>
                        </div>
                        <button class="add-to-cart-btn flex-grow-1 rounded-pill ms-5"
                                style="color:white; background-color: #FF5722; border: none;">Add To Cart
                        </button>

                    </div>
                </div>
            </div>
        </div>
        <hr class="vw-100">


        <%--<div class="review-control d-flex justify-content-between">
            <div class="d-flex">
                <span class="fw-bolder fs-3">All Reviews</span>
                <span class="review-number fw-light fs-5 ms-2 ">(${reviews.size()})</span>
            </div>
        </div>
        <c:if test="${empty reviews}">
            <div class="d-flex justify-content-center">
                <p class="fw-bolder fs-3">Không có đánh giá nào</p>
            </div>

        </c:if>
        <div class="reviews d-flex flex-wrap row">
            <c:forEach var="review" items="${reviews}">
                <div class="col-6 mt-3">
                    <div class="reviews-container border rounded-3 container-fluid" style="min-height: 200px; position: relative;">
                        <div class="rated mt-2">
                            <span class="rated-number">${review.ratingValue}</span>
                            <i class="fa-solid fa-star me-1"></i>
                            <span class="rated-writer fw-bolder fs-4 d-block mt-2">${review.customer.getFullName()}</span>
                            <span class="rated-content d-block">${review.comment}<br>
                                <span class="rated-date mb-3 ">${review.date}</span>
                        </div>

                        <c:if test="${review.getResponse().getResponseID() != 0}">
                            <div class="response-container mb-2 d-flex justify-content-between">
                                <div class="m-2">
                                    <i class="fa-solid fa-arrow-right-from-bracket"></i>
                                    <span class="response-name">${review.response.admin.fullName}</span><br>
                                    <span class="response-content">${review.response.content}</span><br>
                                    <span class="response-date fw-lighter" style="font-size: 10px">${review.response.timeStamp}</span><br>
                                        &lt;%&ndash;<input type="hidden"  id="ResponseID" value="${review.getResponse().getResponseID()}">&ndash;%&gt;
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${role == 1}">
                            <button type="button" class="btn bg-dark rounded-pill ms-auto me-2 mb-1 mt-2 " data-bs-toggle="modal" data-bs-target="#responseModal"
                                    onclick="document.getElementById('modalReviewID').value = '${review.getReviewID()}';
                                            document.getElementById('modalResponseID').value = '${review.getResponse().getResponseID()}';"

                                    style="color:white" >Phản hồi</button>
                        </c:if>
                    </div>




                </div>
            </c:forEach>
        </div>--%>

        <c:set var="totalReviews" value="${reviews.size()}"/>

        <c:set var="fiveStars" value="0"/>
        <c:set var="fourStars" value="0"/>
        <c:set var="threeStars" value="0"/>
        <c:set var="twoStars" value="0"/>
        <c:set var="oneStar" value="0"/>

        <c:forEach var="review" items="${reviews}">
            <c:choose>
                <c:when test="${review.ratingValue == 5}">
                    <c:set var="fiveStars" value="${fiveStars + 1}"/>
                </c:when>
                <c:when test="${review.ratingValue == 4}">
                    <c:set var="fourStars" value="${fourStars + 1}"/>
                </c:when>
                <c:when test="${review.ratingValue == 3}">
                    <c:set var="threeStars" value="${threeStars + 1}"/>
                </c:when>
                <c:when test="${review.ratingValue == 2}">
                    <c:set var="twoStars" value="${twoStars + 1}"/>
                </c:when>
                <c:when test="${review.ratingValue == 1}">
                    <c:set var="oneStar" value="${oneStar + 1}"/>
                </c:when>
            </c:choose>
        </c:forEach>

        <%-- Tính tỷ lệ phần trăm cho mỗi mức sao --%>
        <c:set var="fiveStarsPercentage" value="${(fiveStars / totalReviews) * 100}"/>
        <c:set var="fourStarsPercentage" value="${(fourStars / totalReviews) * 100}"/>
        <c:set var="threeStarsPercentage" value="${(threeStars / totalReviews) * 100}"/>
        <c:set var="twoStarsPercentage" value="${(twoStars / totalReviews) * 100}"/>
        <c:set var="oneStarPercentage" value="${(oneStar / totalReviews) * 100}"/>


        <div class="review-box">
            <div class="review-container container-fluid">
                <div class="rating-summary">
                    <div class="average-rating">
                        <h1 class="rating rating-heading">${averageRating != null ? averageRating: 0}<span
                                class="out-of">/5</span></h1>
                    </div>
                    <div class="stars">
                        <span class="filled-stars">★★★★★</span>
                        <span class="total-reviews">(${reviews.size()} lượt đánh giá)</span>
                    </div>
                    <p>Đây là thông tin người mua đánh giá shop bán sản phẩm này có đúng mô tả không.</p>
                    <div class="rating-breakdown">
                        <div class="star-line">
                            <span style="color: red;">★★★★★</span>
                            <div class="bar">
                                <div class="fill" style="width: ${fiveStarsPercentage}%"></div>
                            </div>
                            <span>${fiveStars}</span>
                        </div>
                        <div class="star-line">
                            <span style="color: rgb(246, 115, 68);">★★★★</span>
                            <div class="bar">
                                <div class="fill" style="width: ${fourStarsPercentage}%"></div>
                            </div>
                            <span>${fourStars}</span>
                        </div>
                        <div class="star-line">
                            <span style="color: rgb(229, 108, 2);">★★★</span>
                            <div class="bar">
                                <div class="fill" style="width: ${threeStarsPercentage}%"></div>
                            </div>
                            <span>${threeStars}</span>
                        </div>
                        <div class="star-line">
                            <span style="color: orange;">★★</span>
                            <div class="bar">
                                <div class="fill" style="width: ${twoStarsPercentage}%"></div>
                            </div>
                            <span>${twoStars}</span>
                        </div>
                        <div class="star-line">
                            <span style="color: rgb(255, 187, 0);">★</span>
                            <div class="bar">
                                <div class="fill" style="width: ${oneStarPercentage}%"></div>
                            </div>
                            <span>${oneStar}</span>
                        </div>
                    </div>
                </div>
                <div class="reviews">
                    <c:forEach var="review" items="${reviews}">
                        <div class="review">
                            <div class="user-info">${review.customer.getFullName()}
                                <span class="date">

                                    <fmt:formatDate value="${review.date}" pattern="dd/MM/yyyy" />
                                </span>
                            </div>
                            <div class="d-flex">
                                <c:forEach begin="1" end="${review.ratingValue}">
                                    <div class="rating">★</div>
                                </c:forEach>
                            </div>
                            <p>${review.comment}</p>
                            <img style="width: 100px; border: none;" src="${review.getBase64Image()}" data-bs-toggle="modal" data-bs-target="#imageModal" class="img-thumbnail review-image"><br>
                            <c:if test="${review.getResponse().getResponseID() != 0}">
                                <div class="response-container mb-2 d-flex justify-content-between me-2 mt-2">
                                    <div class="m-2">
                                        <i class="fa-solid fa-arrow-right-from-bracket"></i>
                                        <span class="response-name">${review.response.admin.fullName}</span><br>
                                        <span class="response-content">${review.response.content}</span><br>
                                        <span class="response-date fw-lighter" style="font-size: 10px">
                                            <!-- Định dạng lại thời gian phản hồi -->
                                            <fmt:formatDate value="${review.response.timeStamp}" pattern="dd/MM/yyyy" />
                                        </span><br>
                                        <input type="hidden" id="ResponseID"
                                               value="${review.getResponse().getResponseID()}">
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${role == 1}">
                                <button class="response-btn"
                                        style="color: #FF5722; font-size:12px; border:none; background-color: white; cursor:pointer;"
                                        data-bs-toggle="modal" data-bs-target="#responseModal"
                                        onclick="document.getElementById('modalReviewID').value = '${review.getReviewID()}';
                                                document.getElementById('modalResponseID').value = '${review.getResponse().getResponseID()}';
                                                document.querySelector('.actionUpdate').value = 'updateResponse'">Phản
                                    hồi
                                </button>
                                <button class="delete-btn"
                                        style="color: red; font-size: 12px; border: none; background-color: white; cursor: pointer;"
                                        data-bs-toggle="modal" data-bs-target="#deleteModal"
                                        onclick="document.getElementById('deleteResponseID').value = '${review.response.responseID}';
                                                document.querySelector('.actionDelete').value = 'deleteResponse'">
                                    Xóa
                                </button>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>


        <div class="modal fade" id="imageModal" tabindex="-1" aria-labelledby="imageModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-md">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="imageModalLabel">Phóng to hình ảnh</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <img id="modalImage" src="" class="img-fluid" alt="Review Image" style="height: 500px;">
                    </div>
                </div>
            </div>
        </div>


        <div class="modal fade" id="responseModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="text-center fw-bolder">Gửi phản hồi</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/product/details" method="post"
                              onsubmit="setResponseContentAndResponseID()">
                            <input type="hidden" name="reviewID" id="modalReviewID">
                            <input type="hidden" name="productName" value="${name}">
                            <input type="hidden" name="action" class="actionUpdate">
                            <input type="hidden" name="ResponseID" id="modalResponseID">
                            <label class="fw-bold fs-5 mb-2">Nội dung</label><br>
                            <textarea class="form-control form-control-lg mb-3 rounded-2" id="responseText" rows="3"
                                      placeholder="Nhập nội dung"></textarea>
                            <input type="hidden" name="responseContent" id="responseContent">
                            <div class="d-flex justify-content-end">
                                <button type="submit" class="btn bg-dark rounded-pill ms-2"
                                        style="color:white; background-color: #FF5722;">Gửi
                                </button>
                            </div>
                        </form>

                    </div>

                </div>
            </div>
        </div>

        <!-- Modal xác nhận xóa -->
        <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/product/details" method="post">
                        <input type="hidden" name="productName" value="${name}">
                        <input type="hidden" name="responseID" id="deleteResponseID">
                        <input type="hidden" name="action" class="actionDelete">
                        <div class="modal-header">
                            <h5 class="modal-title" id="deleteModalLabel">Xác nhận xóa</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            Bạn có chắc chắn muốn xóa phản hồi này không?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-danger">Đồng ý xóa</button>
                        </div>
                    </form>

                </div>
            </div>
        </div>


        <h2 class="text-center mt-5 mb-5 fw-bolder">YOU MIGHT ALSO LIKE</h2>
        <div class="d-flex gap-3 ">

            <c:forEach var="recomendProduct" items="${RecommendProducts}">
                <div class="col-3">
                    <a href="${pageContext.request.contextPath}/product/details?productName=${recomendProduct._1().productName}"
                       class="text-decoration-none">
                        <div class="card-product card" style="border:none">
                            <c:choose>
                                <c:when test="${recomendProduct._1().getImage() != null}">
                                    <img src="${recomendProduct._1().getBase64Image()}" alt=""
                                         class="card-img-top rounded-4">
                                </c:when>
                                <c:otherwise>
                                    <p class="card-img-top rounded-4">Image not available</p>
                                </c:otherwise>
                            </c:choose>
                            <div class="card-product-body">
                                <h5 class="card-product-name mt-2">${recomendProduct._1().productName}</h5>
                                <div class="d-flex price d-inline-block">
                                    <span class="d-block rated-number mb-2">${recomendProduct._2() != null ? recomendProduct._2(): 0}/5</span>
                                    <i class="fa-solid fa-star me-1 mt-1 ms-2 " style="color: #FF5722;"></i>

                                </div>
                                <c:if test="${not empty recomendProduct._3()}">
                                    <div class="price  d-inline-block ">

                                        <c:set var="finalPrice" value="${recomendProduct._1().price}" />

                                        <c:if test="${recomendProduct._3().getPromotion().getDiscountType().name() == 'VND'}">
                                            <c:set var="finalPrice" value="${recomendProduct._1().price - recomendProduct._3().getPromotion().getDiscountValue()}" />
                                        </c:if>

                                        <c:if test="${recomendProduct._3().getPromotion().getDiscountType().name() == 'Percentage'}">
                                            <c:set var="finalPrice" value="${recomendProduct._1().price - (recomendProduct._1().price * recomendProduct._3().getPromotion().getDiscountValue() / 100)}" />
                                        </c:if>
                                        <c:if test="${recomendProduct._3().getPromotion().getDiscountType().name() == 'VND'}">
                                            <span class="product-badge badge bg-danger rounded-pill align-items-center">-${recomendProduct._3().getPromotion().getDiscountValue()}VND</span>
                                        </c:if>
                                        <c:if test="${recomendProduct._3().getPromotion().getDiscountType().name() != 'VND'}">
                                            <span class="product-badge badge bg-success rounded-pill align-items-center">
                                                -${recomendProduct._3().getPromotion().getDiscountValue()}%
                                            </span>
                                        </c:if>
                                        <br><span class="current-price fw-bold fs-4 ">${finalPrice} VND</span>
                                        <br><span class="old-price fs-4">${recomendProduct._1().price} VND</span>

                                    </div>

                                </c:if>
                                <c:if test="${empty recomendProduct._3()}">
                                    <span class="current-price fw-bold fs-4">${recomendProduct._1().price} VND</span>
                                </c:if>

                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>


</main>
<footer>
    <jsp:include page="./view/footer.jsp"></jsp:include>
</footer>
<script>
    // Nhận dữ liệu từ backend
    const productDetails = JSON.parse('${productDetails}');
    console.log("Product Details:", productDetails);
</script>
<%--<script src="${pageContext.request.contextPath}/js/toastMessage.js"></script>--%>
<script src="${pageContext.request.contextPath}/js/ProductInformation.js"></script>
</body>
</html>