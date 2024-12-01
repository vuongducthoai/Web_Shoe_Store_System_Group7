const ctx = document.getElementById('myChart').getContext('2d');
const horizontalCtx = document.getElementById('horizontalChart').getContext('2d');

// Dữ liệu cho các năm
const dataByYear = {
    2024: [50, 80, 65, 90, 40, 95, 100, 85, 75, 60, 70, 80],
    2025: [60, 70, 80, 95, 50, 105, 110, 95, 85, 65, 80, 90],
    2026: [55, 85, 70, 100, 45, 90, 105, 90, 80, 55, 65, 75]
};

const topProducts = {
    labels: ['Sản phẩm 1', 'Sản phẩm 2', 'Sản phẩm 3', 'Sản phẩm 4', 'Sản phẩm 5', 'Sản phẩm 6', 'Sản phẩm 7', 'Sản phẩm 8', 'Sản phẩm 9', 'Sản phẩm 10'],
    data: [120, 110, 105, 100, 95, 90, 85, 80, 75, 70]
};

// Biểu đồ dọc
const myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        datasets: [{
            label: 'Doanh thu hàng tháng',
            data: dataByYear[2024],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
        }]
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

// Hàm cập nhật biểu đồ dọc
function updateChartData() {
    const selectedYear = document.getElementById('year-select').value;
    myChart.data.datasets[0].data = dataByYear[selectedYear];
    myChart.update();
}


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

