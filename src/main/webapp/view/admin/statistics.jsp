<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="container">
  <!-- Thẻ thông tin -->
  <div class="cards">
    <div class="card">
      <h2>1200</h2>
      <p>Tổng số đơn hàng hoàn thành </p>
    </div>
    <div class="card">
      <h2><%= request.getAttribute("inventoryQuantity") %></h2>
      <p>Tồn kho</p>
    </div>
    <div class="card">
      <h2><%= request.getAttribute("totalAmount") %></h2>
      <p>Doanh thu </p>
    </div>
    <div class="card">
      <h2>150 triệu VNĐ</h2>
      <p>Doanh thu tháng này</p>
    </div>
  </div>

  <!-- Biểu đồ doanh thu -->
  <div class="charts">
    <div class="chart-container">
      <select id="year-select" onchange="updateChartData()">
        <option value="2024">2024</option>
        <option value="2025">2025</option>
        <option value="2026">2026</option>
      </select>
      <canvas id="myChart"></canvas>
    </div>

    <!-- Biểu đồ top 10 sản phẩm bán chạy -->
    <div class="horizontal-chart-container">
      <h3 style="text-align: center;">Top 10 Sản phẩm bán chạy</h3>
      <canvas id="horizontalChart"></canvas>
    </div>
  </div>
</div>

<div class="top-customers">
  <h3 style="text-align: center;">Top 10 Khách hàng tiềm năng nhất</h3>
  <table>
    <thead>
    <tr>
      <th>Hạng</th>
      <th>Tên Khách hàng</th>
      <th>Tổng Đơn hàng</th>
      <th>Doanh thu (VNĐ)</th>
    </tr>
    </thead>
    <tbody id="customer-table">
    <!-- Dữ liệu sẽ được thêm vào bằng JavaScript -->
    </tbody>
  </table>
</div>
