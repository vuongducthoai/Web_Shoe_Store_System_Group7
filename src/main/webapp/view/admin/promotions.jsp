<%@ page import="dto.PromotionDTO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%
    String message = (String) session.getAttribute("message");
    String messageType = (String) session.getAttribute("messageType");
    System.out.println("DEBUG - Message: " + message);
    System.out.println("DEBUG - MessageType: " + messageType);
    if (message == null || message.isEmpty()) {
        message = "";
        messageType = "";
    } else {
        session.removeAttribute("message");
        session.removeAttribute("messageType");
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Promotion Management</title>
    <%--    <link rel="stylesheet" href="../../css/promotionStyle.css" type="text/css ">--%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/promotionStyle.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/promotion.js" defer></script>
</head>
<body>
<h1>Promotion Management</h1>
<div class="button-group" >
    <button type="button" onclick="showSection('promotions')">Promotion</button>
    <button type="button" onclick="showSection('vouchers')">Voucher</button>
</div>

<% if (message != null && !message.isEmpty()) { %>
<div class="modal" id="notificationModal">
    <div class="modal-content <%= messageType %>">
        <p><%= message %></p>
        <button onclick="closeModal()">OK</button>
    </div>
</div>
<% } %>
<!-- The container for promotions and vouchers -->
<div class="promotions-container" id="promotions-container">
    <!-- Promotions Content -->
    <div class="promotions-content">
        <div class="promotions-items">
            <c:forEach var="promotion" items="${promotionDTOList}">
                <c:if test="${promotion.promotionType == 'VOUCHER_PRODUCT'}">
                    <div class="promotions-item"
                         onclick="updateOrderSummary('${promotion.promotionName}', 'Promotion','${promotion.startDate}', '${promotion.endDate}', '${promotion.discountValue} ${promotion.discountType == 'Percentage' ? '%' : 'VND'}','${promotion.promotionId}','${promotion.minimumLoyalty}')">
                    <div class="item-info">
                            <img src="../image/promotion.jpg"  alt="Promotion Image" class="item-img">
                            <div class="item-details">
                                <div class="item-title">Promotion Name: ${promotion.promotionName}</div>
                                <div>Start Date: ${promotion.startDate}</div>
                                <div>End Date: ${promotion.endDate}</div>
                                <div class="item-price">Discount: ${promotion.discountValue} ${promotion.discountType == 'Percentage' ? '%' : 'VND'}</div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>

<div class="vouchers-container" id="vouchers-container" style="display: none;">
    <!-- Vouchers Content -->
    <div class="vouchers-items">
        <c:forEach var="promotion" items="${promotionDTOList}">
            <c:if test="${promotion.promotionType == 'VOUCHER_ORDER'}">
                <div class="voucher-item"
                     onclick="updateOrderSummary('${promotion.promotionName}', 'Voucher','${promotion.startDate}', '${promotion.endDate}', '${promotion.discountValue} ${promotion.discountType == 'Percentage' ? '%' : 'VND'}','${promotion.promotionId}','${promotion.minimumLoyalty}')">
                    <div class="item-info">
                        <img src="../image/voucher.jpg" alt="Promotion Image" class="item-img">
                        <div class="item-details">
                            <div class="item-title">Voucher Name: ${promotion.promotionName}</div>
                            <div>Start Date: ${promotion.startDate}</div>
                            <div>End Date: ${promotion.endDate}</div>
                            <div class="item-price">Discount: ${promotion.discountValue} ${promotion.discountType == 'Percentage' ? '%' : 'VND'}</div>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>

</div>

<!-- Order Summary (This section remains unchanged) -->
<div class="order-summary">
    <div class="summary-title">Information</div>
    <div class="summary-item"><span>Promotion Name:</span> <span id="summary-promotion-name">-</span></div>
    <div class="summary-item"><span>Type: </span> <span id="summary-promotion-type">-</span></div>
    <div class="summary-item"><span>Start Date:</span> <span id="summary-start-date">-</span></div>
    <div class="summary-item"><span>End Date:</span> <span id="summary-end-date">-</span></div>
    <div class="summary-total"><span>Discount:</span> <span id="summary-discount">-</span></div>
    <!-- Checkout Button -->
    <button class="checkout-btn" id="view-btn">View</button>
</div>
<button class="add-promotion-btn" id="add-promotion-btn">Add Promotion</button>

<div class="overlay" id="overlay-add">
    <div id="add-promotion-form">
        <form action="${pageContext.request.contextPath}/admin/promotions/insert" method="POST">
            <!-- Promotion Name -->
            <div>
                <div class="form-group">
                    <label for="promotion-name">Promotion Name: </label>
                    <input type="text" id="promotion-name" name="promotion-name" required>
                </div>
            </div>

            <!-- Start Date and End Date -->
            <div>
                <div class="form-group">
                    <label for="start-date">Start Date: </label>
                    <input type="date" id="start-date" name="start-date" required>
                </div>
            </div>
            <div>
                <div class="form-group">
                    <label for="end-date">End Date: </label>
                    <input type="date" id="end-date" name="end-date" required>
                </div>
            </div>


            <div>
                <div class="form-group">
                    <label for="promotion-type">Promotion Type: </label>
                    <select id="promotion-type" name="promotion-type" onchange="handleTypeChange()" required>
                        <option value="" disabled selected>Select Promotion Type</option>
                        <option value="voucher">Voucher</option>
                        <option value="promotion">Promotion</option>
                    </select>
                </div>
            </div>
            <!-- Application Products -->
            <div class="dropdown" id="select-products" style="display: none;">
                <!-- Button to show the dropdown menu -->
                <label for="start-date">Application Products: </label>
                <button type="button" class="dropdown-button" onclick="toggleDropdown()">Select Products</button>

                <!-- Dropdown menu with checkboxes -->
                <div class="dropdown-menu">
                    <c:forEach var="productName" items="${nameProductList}">
                        <label>
                            <input type="checkbox" name="selectedProducts" value="${productName}">
                                ${productName}
                        </label>
                    </c:forEach>
                </div>
            </div>

            <!-- Submit Button to see selected items -->

            <!-- Display the selected items -->
            <div id="selected-products" style="margin-top: 20px;"></div>

            <!-- Promotion Type -->
            <div id="minimum-loyalty-container" style="display: none;">
                <div class="form-group">
                    <label for="minimum-loyalty">Minimum Loyalty: </label>
                    <input type="number" id="minimum-loyalty" name="minimum-loyalty">
                </div>
            </div>

            <!-- Discount Value and Unit -->
            <div>
                <div class="form-group">
                    <label for="discount-value">Discount Value: </label>
                    <input type="number" id="discount-value" name="discount-value" required>
                    <select id="discount-unit" name="discount-unit" onchange="handleDiscountUnitChange()" required>
                        <option value="" disabled selected>Select Unit</option>
                        <option value="%">%</option>
                        <option value="VND">VND</option>
                    </select>
                </div>
            </div>

            <!-- Minimum Loyalty -->


            <div class="form-group-button">
                <button type="submit">Add</button>
                <button type="button" onclick="toggleForm()">Cancel</button>
            </div>
        </form>

    </div>
</div>
<div class="overlay" id="overlay-view">
    <div id="view-promotion-form">
        <form action="${pageContext.request.contextPath}/admin/promotions/delete" method="POST">
            <!-- Promotion Name -->
            <div>
                <div class="form-group">
                    <label for="promotion-name">Promotion Name: </label>
                    <input type="text" id="view-promotion-name" name="promotion-name" required readonly>
                </div>
            </div>
            <input type="hidden" id="promotion-id" name="promotion-id" value="">
            <!-- Start Date and End Date -->
            <div>
                <div class="form-group">
                    <label for="start-date">Start Date: </label>
                    <input type="date" id="view-start-date" name="start-date" required readonly>
                </div>
            </div>
            <div>
                <div class="form-group">
                    <label for="end-date">End Date: </label>
                    <input type="date" id="view-end-date" name="end-date" required readonly>
                </div>
            </div>

            <!-- Application Products -->
            <div class="dropdown">
                <!-- Button to show the dropdown menu -->
                <label for="start-date">Application Products: </label>
                <button type="button" class="dropdown-button" onclick="toggleDropdown()">Select Products</button>
                <div class="dropdown-menu" disabled></div>
            </div>
            <div id="selected-products1"></div>

            <!-- Display the selected items -->
            <div id="view selected-products" style="margin-top: 20px;"></div>

            <!-- Promotion Type -->
            <div>
                <div class="form-group">
                    <label for="promotion-type">Promotion Type: </label>
                    <select id="view-promotion-type" name="promotion-type" required disabled>
                        <option value="" disabled selected>Select Promotion Type</option>
                        <option value="voucher">Voucher</option>
                        <option value="promotion">Promotion</option>
                    </select>
                </div>
            </div>

            <!-- Discount Value and Unit -->
            <div>
                <div class="form-group">
                    <label for="discount-value">Discount Value: </label>
                    <input type="number" id="view-discount-value" name="discount-value" required readonly>
                    <select id="view-discount-unit" name="discount-unit" required disabled>
                        <option value="" disabled selected>Select Unit</option>
                        <option value="%">%</option>
                        <option value="VND">VND</option>
                    </select>
                </div>
            </div>

            <!-- Minimum Loyalty -->
            <div>
                <div class="form-group">
                    <label for="minimum-loyalty">Minimum Loyalty: </label>
                    <input type="number" id="view-minimum-loyalty" name="minimum-loyalty" required readonly>
                </div>
            </div>
            <div class="form-group-button">
                <button type="submit" id="delete-button">Delete</button>
                <button type="button" onclick="toggleForm()">Cancel</button>
            </div>

        </form>
    </div>
</div>

</body>
</html>