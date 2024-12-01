<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Chat Admin</title>
</head>
<body>
<div id="customerList">
  <h2>Danh sách khách hàng</h2>
  <c:forEach var="customer" items="${customerList}">
    <div class="customer-item" onclick="startChat(${customer.id})">
        ${customer.fullName}
    </div>
  </c:forEach>
</div>

<div class="chat-container">
  <div class="chat-box">
    <h2>Chat với khách hàng</h2>
    <div id="messageList" class="message-wrapper">
      <!-- Messages will appear here -->
    </div>
    <div class="input-box">
      <input type="text" id="message" placeholder="Nhập tin nhắn..." required>
      <button onclick="sendMessage()">Gửi</button>
    </div>
  </div>
</div>

<script>
  var socket;
  var userId = ${userId}; // Lấy userId của admin
  var currentChatId = 0; // Biến lưu chatId của khách hàng hiện tại

  window.onload = function() {
    socket = new WebSocket("ws://localhost:8080/JPAExample_war_exploded/chat");
    socket.onopen = function() {
      console.log("Connected to WebSocket");
      socket.send("admin:" + userId);
    };

    socket.onmessage = function(event) {
      var messageList = document.getElementById("messageList");
      messageList.innerHTML = event.data;
      document.querySelector('.chat-box').scrollTop = document.querySelector('.chat-box').scrollHeight;
    };

    socket.onclose = function() {
      console.log("WebSocket connection closed.");
    };
  };

  function startChat(customerId) {
    currentChatId = customerId;
    socket.send("switchChat:" + customerId); // Gửi yêu cầu chuyển chat tới WebSocket
  }

  function sendMessage() {
    var message = document.getElementById("message").value;
    if (message && currentChatId !== 0) {
      socket.send(message);
      document.getElementById("message").value = ""; // Clear input field
    }
  }
</script>
</body>
</html>
