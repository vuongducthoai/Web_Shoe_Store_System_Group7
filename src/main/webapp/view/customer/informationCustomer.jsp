<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile</title>
    <link rel="stylesheet" href="<c:url value='/css/info.css' />">
</head>
<body>
<div class="container">
    <div class="header">
        <div class="breadcrumb">
            <a href="home.jsp">Home</a> / <span>My Account</span>
        </div>
        <div class="welcome">Welcome! <span><c:out value="${fullName}" /></span></div>
    </div>
    <div class="main-content">
        <div class="sidebar">
            <h3>Manage My Account</h3>
            <a href="profile.jsp" class="active">My Profile</a>
            <a href="addressBook.jsp">Address Book</a>
            <a href="paymentOptions.jsp">My Payment Options</a>
            <h3>My Orders</h3>
            <a href="returns.jsp">My Returns</a>
            <a href="cancellations.jsp">My Cancellations</a>
            <h3>My Wishlist</h3>
        </div>
        <div class="content">
            <h2>Edit Your Profile</h2>

            <!-- Messages -->
            <div>
                <c:if test="${param.success eq 'true'}">
                    <div class="success-message">Profile updated successfully!</div>
                </c:if>
                <c:if test="${param.error eq 'true'}">
                    <div class="error-message">An error occurred while updating the profile. Please try again.</div>
                </c:if>
            </div>

            <!-- Form -->
            <form action="Information" method="post">
                <!-- Hidden field for userID -->
                <input type="text" name="userID" value="<c:out value='${userID}' />">
                <input type="text" name="accountID" value="<c:out value='${accountID}' />" readonly>
                <input type="text" name="addressID" value="<c:out value='${addressID}' />"
                >

                <!-- Personal Information -->
                <div class="form-group">
                    <label for="full-name">Full Name</label>
                    <input type="text" id="full-name" name="fullName" value="<c:out value='${fullName}' />" required>
                </div>
                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="text" id="phone" name="phone" value="<c:out value='${phone}' />" required>
                </div>
                <div class="form-group">
                    <label for="dateOfBirth">Date of Birth</label>
                    <input type="date" id="dateOfBirth" name="dateOfBirth" value="<c:out value='${dateOfBirth}' />" required>
                </div>

                <!-- Address Information -->
                <h3>Address Details</h3>
                <div class="form-group">
                    <label for="houseNumber">House Number</label>
                    <input type="text" id="houseNumber" name="houseNumber" value="<c:out value='${numberHouse}' />" required>
                </div>
                <div class="form-group">
                    <label for="streetName">Street Name</label>
                    <input type="text" id="streetName" name="streetName" value="<c:out value='${streetName}' />" required>
                </div>
                <div class="form-group">
                    <label for="district">District</label>
                    <input type="text" id="district" name="district" value="<c:out value='${district}' />" required>
                </div>
                <div class="form-group">
                    <label for="city">City</label>
                    <input type="text" id="city" name="city" value="<c:out value='${city}' />" required>
                </div>

                <!-- Account Details -->
                <h3>Account Details</h3>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" value="<c:out value='${email}' />" disabled>
                </div>
                <div class="form-group">
                    <label for="loyalty">Loyalty Points</label>
                    <input type="text" id="loyalty" name="loyalty" value="<c:out value='${loyalty}' />" disabled>
                </div>

                <!-- Button Actions -->
                <div class="button-group">
                    <button type="reset" class="cancel-btn">Cancel</button>
                    <button type="submit" class="save-btn">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
