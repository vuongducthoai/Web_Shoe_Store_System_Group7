<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Error Page</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f9f9f9;
      text-align: center;
    }
    .error-container {
      background-color: white;
      border: 1px solid #f44336;
      border-radius: 5px;
      display: inline-block;
      height: 100vh;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
    }
    .error-image {
      width: 300px;
      height: 250px;
      margin-bottom: 20px;
    }
    .error-message {
      color: #f44336;
      font-size: 20px;
      font-weight: bold;
    }
  </style>
</head>
<body>
<div class="error-container">
  <img src="../image/error.png" alt="Error Image" class="error-image">
  <div class="error-message">${error}</div>
</div>
</body>
</html>
