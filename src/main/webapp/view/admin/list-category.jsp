<%--
  Created by IntelliJ IDEA.
  User: Asus
  Date: 11/5/2024
  Time: 10:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Category-Admin</title>
</head>
<body>
    <c:forEach items="${categoryList}" var="cate" varStatus="STT">
        <tr class="odd gradeX">
            <td>${STT.index + 1}</td>
            <c:url value="/image?fname=${cate.images }" var="imgUrl"></c:url>
            <td><img height="150" width="200" src="${imgUrl}"></td>
            <td>${cate.cateName}</td>
        </tr>
    </c:forEach>
</body>
</html>
