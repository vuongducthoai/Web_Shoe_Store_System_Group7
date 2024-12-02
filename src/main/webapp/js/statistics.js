const ctx = document.getElementById('myChart').getContext('2d');
const horizontalCtx = document.getElementById('horizontalChart').getContext('2d');




console.log(dataByYear); // Kiểm tra dữ liệu đã được truyền vào đúng

// Tiếp tục xử lý dữ liệu như bình thường
// Ví dụ: Hiển thị dữ liệu từ `dataByYear`
for (let year in dataByYear) {
    console.log(`Dữ liệu cho năm ${year}:`, dataByYear[year]);
}




const topProducts = {
    labels: ['Sản phẩm 1', 'Sản phẩm 2', 'Sản phẩm 3', 'Sản phẩm 4', 'Sản phẩm 5', 'Sản phẩm 6', 'Sản phẩm 7', 'Sản phẩm 8', 'Sản phẩm 9', 'Sản phẩm 10'],
    data: [120, 110, 105, 100, 95, 90, 85, 80, 75, 70]
};
const revenue2024 = [];
for (let month = 1; month <= 12; month++) {
    revenue2024.push(dataByYear["2024"][month] || 0); // Nếu không có dữ liệu thì gán là 0
}
// Biểu đồ dọc
const myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        datasets: []
    },
    options: {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Biểu đồ Doanh Thu', // Thêm tiêu đề
                font: {
                    size: 18, // Kích thước chữ
                    weight: 'bold',
                },
                color: '#333', // Màu chữ
            }
        },
        scales: {
            y: { beginAtZero: true }
        }
    }
});

// Biểu đồ ngang
const horizontalChart = new Chart(horizontalCtx, {
    type: 'bar',
    data: {
        labels: topProducts.labels,
        datasets: [{
            label: 'Số lượng bán',
            data: topProducts.data,
            backgroundColor: 'rgba(75, 192, 75, 0.2)', // Màu xanh lá cây nhạt
            borderColor: 'rgba(75, 192, 75, 1)',
            borderWidth: 2
        }]
    },
    options: {
        indexAxis: 'y',
        responsive: true,
        scales: {
            x: { beginAtZero: true }
        }
    }
});
const currentYear = new Date().getFullYear();

// Tạo các năm từ 4 năm trước đến năm hiện tại
const years = [];
for (let i = 0; i <= 3; i++) {
    years.push(currentYear - i); // Tạo mảng các năm từ 4 năm trước đến năm hiện tại
}

// Lấy dropdown để thêm các option
const yearSelect = document.getElementById('year-select');

// Thêm các năm vào dropdown
years.forEach(year => {
    const option = document.createElement('option');
    option.value = year;
    option.textContent = year;
    yearSelect.appendChild(option);
});

// Hàm cập nhật biểu đồ khi chọn năm
function updateChartData() {
    const selectedYear = document.getElementById('year-select').value;
    updateChart(selectedYear);
}

// Hàm cập nhật biểu đồ dọc
function updateChart(year) {
    const revenue = [];
    for (let month = 1; month <= 12; month++) {
        revenue.push(dataByYear[year][month] || 0); // Nếu không có dữ liệu, gán là 0
    }

    // Cập nhật datasets của biểu đồ
    myChart.data.datasets = [{
        label: `Doanh thu ${year}`,
        data: revenue,
        backgroundColor: 'rgba(75, 192, 192, 0.2)', // Màu nền
        borderColor: 'rgba(75, 192, 192, 1)', // Màu viền
        borderWidth: 1
    }];

    // Cập nhật biểu đồ
    myChart.update();
}

// Lắng nghe sự kiện khi chọn năm từ dropdown
document.getElementById('year-select').addEventListener('change', function() {
    const selectedYear = this.value;  // Lấy năm đã chọn
    updateChart(selectedYear);  // Cập nhật biểu đồ
});

// Gọi hàm để hiển thị biểu đồ cho năm mặc định khi tải trang (ví dụ 2021)
updateChart(currentYear);


document.addEventListener("DOMContentLoaded", () => {
    const topCustomers = [
        { rank: 1, name: "Nguyễn Văn A", orders: 25, revenue: "12 triệu VNĐ" },
        { rank: 2, name: "Trần Thị B", orders: 22, revenue: "10 triệu VNĐ" },
        { rank: 3, name: "Phạm Văn C", orders: 18, revenue: "8 triệu VNĐ" },
        { rank: 4, name: "Lê Thị D", orders: 16, revenue: "7 triệu VNĐ" },
        { rank: 5, name: "Đỗ Văn E", orders: 14, revenue: "6 triệu VNĐ" },
        { rank: 6, name: "Hoàng Thị F", orders: 12, revenue: "5.5 triệu VNĐ" },
        { rank: 7, name: "Ngô Văn G", orders: 10, revenue: "5 triệu VNĐ" },
        { rank: 8, name: "Vũ Thị H", orders: 9, revenue: "4.5 triệu VNĐ" },
        { rank: 9, name: "Bùi Văn I", orders: 8, revenue: "4 triệu VNĐ" },
        { rank: 10, name: "Lý Thị K", orders: 7, revenue: "3.5 triệu VNĐ" },
    ];

    const tableBody = document.getElementById("customer-table");

    topCustomers.forEach((customer, index) => {
        setTimeout(() => {
            const row = document.createElement("tr");
            row.innerHTML = `
        <td>${customer.rank}</td>
        <td>${customer.name}</td>
        <td>${customer.orders}</td>
        <td>${customer.revenue}</td>
      `;
            tableBody.appendChild(row);
        }, index * 100); // Thêm độ trễ 100ms cho từng hàng
    });
});

