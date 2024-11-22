<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<header>
    <jsp:include page="./view/header.jsp"></jsp:include>
</header>
<main>
    <div class="container">
        <div class="product-detail w-100">
            <div class="row">
                <div class="col-sm-6 d-flex">
                    <div class="product-image-list col-sm-3 d-flex flex-column">

                        <c:forEach var="image" items="${images}">
                            <img src="${image}" alt="" class="product-images-item mb-1 h-auto rounded-4">
                        </c:forEach>
                    </div>
                    <img src="${images[0]}" alt="" class="product-image-focus col-sm-9 img-fluid rounded-4">
                </div>
                <div class="product-describe col-sm-6 d-flex flex-column">
                    <h3 class="product-name fw-bolder fs-1 ">${name}</h3>
                    <div class="rated d-flex">
                        <span class="d-block rated-number ms-2 mb-2">5/5</span>
                        <i class="fa-solid fa-star me-1 mt-1 ms-2 "></i>
                    </div>
                    <div class="price  d-inline-block ">
                        <span class="current-price fw-bold fs-2 ">${price}</span>
                        <span class="product-badge badge bg-danger rounded-pill align-items-center ms-3">-40%</span>
                    </div>
                    <span class="product-description mt-2">${description}</span>
                    <hr class="vw-100">
                    <span class="mt-1 fs-5 fw-lighter">Select color</span>
                    <div class="product-colors d-flex mt-2 mb-3">
                        <c:forEach var="color" items="${colors}">
                            <div class="circle me-3" style="background-color: ${color}"></div>
                        </c:forEach>
                    </div>
                    <hr class="vw-100">
                    <span class="mt-1 fs-5 fw-lighter">Choose size</span>
                    <div class="d-flex mt-2 mb-3">
                        <c:forEach var="size" items="${sizes}">
                            <button class="d-flex justify-content-center align-items-center size rounded-pill me-2">${size}</button>
                        </c:forEach>
                    </div>
                    <hr class="vw-100">
                    <div class="d-flex">
                        <div class="quantity-container rounded-pill d-flex justify-content-around align-items-center  ">
                            <i class="fa-solid fa-minus" style="color:black; cursor:pointer;"></i>
                            <span class="quantity">1</span>
                            <i class="fa-solid fa-plus" style="color:black; cursor:pointer;"></i>
                        </div>
                        <button class="add-to-cart-btn flex-grow-1 bg-dark rounded-pill ms-5" style="color:white">Add To Cart</button>

                    </div>
                </div>
            </div>
        </div>
        <hr class="vw-100">
        <div class="review-control d-flex justify-content-between">
            <div class="d-flex">
                <span class="fw-bolder fs-3">All Reviews</span>
            </div>
            <div class="btn-group">
                <div class="dropdown">
                    <button  class="btn-dropdown-menu btn dropdown-toggle rounded-pill ms-2" data-bs-toggle="dropdown" aria-expanded="false">Latest</button>
                    <ul class="dropdown-menu rounded-2 text-center">
                        <li><button class="dropdown-item">All</button></li><hr>
                        <li><button class="dropdown-item">Best</button></li><hr>
                        <li><button class="dropdown-item">Worst</button></li>
                    </ul>
                </div>
                <button type="button" class="btn bg-dark rounded-pill ms-2" data-bs-toggle="modal" data-bs-target=".modal" style="color:white">Write a Review</button>
            </div>
        </div>
        <div class="reviews d-flex flex-wrap row">
            <c:forEach var="review" items="${reviews}">
                    <div class="col-6 mt-3">
                    <div class="reviews-container border rounded-3 container-fluid">
                        <div class="rated mt-2">
                            <span class="rated-number">${review.ratingValue}</span>
                            <i class="fa-solid fa-star me-1"></i>
                            <span class="rated-writer fw-bolder fs-4 d-block mt-2">${review.customer.getFullName()}</span>
                            <span class="rated-content d-block">${review.comment}</span><br>
                        </div>
                        <span class="rated-date mb-3 ">${review.date}</span>
                    </div>
                </div>
                </c:forEach>
        </div>



        <div class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="text-center fw-bolder">Gửi đánh giá của bạn</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <label class="fw-bold fs-5 me-2">Đánh giá sản phẩm</label>
                        <input class="mb-3 rounded-2" type="number" min="1" max="5" step="1" value="5">
                        <i class="fa-solid fa-star me-1"></i><br>
                        <label class="fw-bold fs-5 mb-2">Nội dung</label><br>
                        <textarea class="form-control form-control-lg mb-3 rounded-2" rows="3" placeholder="Nhập nội dung"></textarea>
                        <div class="d-flex justify-content-end">
                            <button type="button" class="btn bg-dark rounded-pill ms-2" style="color:white">Gửi</button>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <h2 class="text-center mt-5 mb-5 fw-bolder">YOU MIGHT ALSO LIKE</h2>
        <div class="d-flex gap-3 ">
            <c:forEach var="recomendProduct" items="${RecommendProducts}">
                <div class="col-3">
                    <div class="card-product card" style="border:none">
                        <c:choose>
                            <c:when test="${recomendProduct.getImage() != null}">
                                <img src="${recomendProduct.getBase64Image()}" alt="" class="card-img-top rounded-4">
                            </c:when>
                            <c:otherwise>
                                <p class="card-img-top rounded-4">Image not available</p>
                            </c:otherwise>
                        </c:choose>
                        <div class="card-product-body">
                            <h5 class="card-product-name mt-2">${recomendProduct.productName}</h5>
                            <div class="price d-inline-block">
                                <span class="current-price fw-bold fs-4">${recomendProduct.price}</span>
                                <span class="product-badge badge bg-danger rounded-pill align-items-center ms-3">-40%</span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

        </div>
    </div>


</main>
<footer>
    <jsp:include page="./view/footer.jsp"></jsp:include>
</footer>
<script src="${pageContext.request.contextPath}/js/ProductInformation.js"></script>
</body>
</html>