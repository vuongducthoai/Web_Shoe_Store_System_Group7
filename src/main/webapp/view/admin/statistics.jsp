<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>



<script>
  // Lấy dữ liệu JSON từ request và chuyển nó thành đối tượng JavaScript
  const dataByYear = JSON.parse('<%= request.getAttribute("dataByYear") %>');
  const topProducts = JSON.parse('<%= request.getAttribute("top10product")%>')
  const labels = Object.keys(topProducts); // Tên sản phẩm
  const data = Object.values(topProducts);

</script>
<div class="container">
  <!-- Thẻ thông tin -->
  <div class="cards">
    <div class="card">
      <h2><%= request.getAttribute("quantityCompleted")%></h2>
      <p>Tổng số đơn hàng hoàn thành </p>
    </div>
    <div class="card">
      <h2><%= request.getAttribute("inventoryQuantity") %></h2>
      <p>Tồn kho</p>
    </div>
    <div class="card">
      <h2><%= request.getAttribute("totalAmount") %></h2>
      <p>Tổng doanh thu</p>
    </div>
    <div class="card">
      <h2><%= request.getAttribute("totalMoth") %></h2>
      <p>Doanh thu tháng này</p>
    </div>
  </div>

  <!-- Biểu đồ doanh thu -->
  <div class="charts">
    <div class="chart-container">
      <select id="year-select" onchange="updateChartData()">
        <!-- Dropdown sẽ được cập nhật bằng JS -->
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
