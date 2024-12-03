<!DOCTYPE html>
<html lang="en">
<head>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
    <title>Chat Room</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel = "stylesheet" href="${pageContext.request.contextPath}/css/chat.css">
</head>
<body>
<div class="chat-container">
    <div id="customerList" class="customer-list" style="display: none;">
        <!-- Danh sách khách hàng -->
    </div>

    <div class="chat-box-container">
        <h2>Customer Support</h2>

        <div class="chat-box">
            <div id="loadMoreMessages" class="load-more" onclick="loadmore()">
                Xem thêm
            </div>
            <div class="message-wrapper" id="messageList">
                <!-- Tin nhắn sẽ được thêm vào đây -->
            </div>
        </div>

        <div class="input-box">
            <input type="text" id="message" placeholder="Type a message..." required>
            <button onclick="sendMessage()">Send</button>
        </div>
    </div>
</div>
<script>
    var socket;
    var userId = "";
    var currentChatId = 1;
    var loadMoreClicked = false;

    window.onload = function () {

        var userJson = `${userJson}`;

        // Dùng domain hiện tại để tự động xác định đường dẫn WebSocket
        var wsUrl = window.location.protocol === "https:"
            ? "wss://" + window.location.host + "/chat"  // Sử dụng 'wss' cho HTTPS
            : "ws://" + window.location.host + "/chat"; // Sử dụng 'ws' cho HTTP
        socket = new WebSocket(wsUrl);

        socket.onopen = function () {
            console.log("Connected to WebSocket");

            // Gửi chuỗi JSON qua WebSocket
            socket.send("user:" + userJson);
        };


        socket.onmessage = function (event) {
            var messageList = document.getElementById("messageList");

            // If it's a customer list message
            if (event.data.includes("customer-item")) {
                var customerList = document.getElementById("customerList");
                customerList.innerHTML = event.data; // Insert the customer list HTML

                // Display the customer list if it's the admin
                document.getElementById("customerList").style.display = "block";

            } else if (event.data === "noMoreMessages") {
                var noMoreMessageDiv = document.createElement("div");
                noMoreMessageDiv.classList.add("no-more-msg");
                noMoreMessageDiv.innerText = "Bạn đã xem hết tin nhắn!";
                messageList.insertBefore(noMoreMessageDiv, messageList.firstChild);
            } else {
                messageList.innerHTML = event.data;
                var chatBox = document.querySelector('.chat-box');
                if (loadMoreClicked) {
                    loadMoreClicked = false;
                } else {
                    chatBox.scrollTop = chatBox.scrollHeight;
                }
            }
        };

// Handle admin connection
        var userRole = '${role}';

        // Kiểm tra role trong JavaScript
        if (userRole === 1) {
            // Display customer list
            document.getElementById("customerList").style.display = "block";
        }

        socket.onclose = function () {
            console.log("WebSocket connection closed.");
        };

        socket.onerror = function (error) {
            console.log("WebSocket Error: " + error);
        };
    };

    function sendMessage() {
        var message = document.getElementById("message").value;
        if (message) {
            socket.send(message);
            document.getElementById("message").value = "";
        }
    }

    function loadmore() {
        loadMoreClicked = true;
        socket.send("loadMoreMessages");
    }
    function switchChat(customerId) {
        currentChatId = customerId;
        socket.send("switchChat:" + customerId);
    }
</script>
</body>
</html>
