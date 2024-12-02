const ctx = document.getElementById('myChart').getContext('2d');
const horizontalCtx = document.getElementById('horizontalChart').getContext('2d');





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

const horizontalChart = new Chart(horizontalCtx, {
    type: 'bar',
    data: {
        labels: labels,
        datasets: [{
            label: 'Số lượng bán',
            data: data,
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

