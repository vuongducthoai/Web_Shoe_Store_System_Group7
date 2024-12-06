function showSection(section) {
    const promotionsContainer = document.getElementById('promotions-container');
    const vouchersContainer = document.getElementById('vouchers-container');

    if (section === 'promotions') {
        promotionsContainer.style.display = 'block';
        vouchersContainer.style.display = 'none';
    } else {
        promotionsContainer.style.display = 'none';
        vouchersContainer.style.display = 'block';
    }
}

showSection('promotions');

function toggleForm() {
    const overlayView = document.getElementById('overlay-view');
    const overlayAdd = document.getElementById('overlay-add');

    // Ẩn tất cả các overlays
    overlayView.style.display = 'none';
    overlayAdd.style.display = 'none';
}



// Thêm sự kiện cho nút Add Promotion
// Hàm hiển thị/ẩn overlay
function showFormWithButtons(type) {
    if (type === 'view') {
        document.getElementById('overlay-view').style.display = 'flex'; // Hiển thị Form View
    } else if (type === 'add') {
        document.getElementById('overlay-add').style.display = 'flex'; // Hiển thị Form Add Promotion
    }

}


// Gắn sự kiện click cho các nút
document.getElementById('view-btn').addEventListener('click', () => showFormWithButtons('view'));
document.getElementById('add-promotion-btn').addEventListener('click', () => showFormWithButtons('add'));

// Hàm hiển thị/ẩn dropdown
function toggleDropdown() {
    const dropdownMenu = document.querySelector('.dropdown-menu');
    dropdownMenu.style.display = dropdownMenu.style.display === 'block' ? 'none' : 'block';
}

// Close the dropdown if clicked outside
document.addEventListener('click', function (event) {
    const dropdownButton = document.querySelector('.dropdown-button');
    const dropdownMenu = document.querySelector('.dropdown-menu');
    if (!dropdownButton.contains(event.target) && !dropdownMenu.contains(event.target)) {
        dropdownMenu.style.display = 'none';
    }
});

function updateOrderSummary(name, type, startDate, endDate, discount, promotionID,minimumLoyalty) {

    document.getElementById('summary-promotion-name').textContent = name;
    document.getElementById('summary-promotion-type').textContent = type;
    document.getElementById('summary-start-date').textContent = startDate;
    document.getElementById('summary-end-date').textContent = endDate;
    document.getElementById('summary-discount').textContent =discount;


    let formattedStartDate = new Date(startDate).toISOString().split('T')[0];  // Chuyển thành YYYY-MM-DD
    let formattedEndDate = new Date(endDate).toISOString().split('T')[0];         // Định dạng ngày


    // Cập nhật thông tin vào overlay
    document.getElementById('view-promotion-name').value = name;
    document.getElementById('view-promotion-type').value = type.toLowerCase();
    document.getElementById('view-start-date').value = formattedStartDate;
    document.getElementById('view-end-date').value = formattedEndDate;
    document.getElementById('view-discount-value').value = discount.split(' ')[0]; // Giá trị giảm giá
    document.getElementById('view-discount-unit').value = discount.split(' ')[1];
    document.getElementById('view-minimum-loyalty').value=minimumLoyalty;
    document.getElementById('promotion-id').value=promotionID;
    var promotionId = document.getElementById('promotion-id').value;

    // Kiểm tra trạng thái khuyến mãi
    let currentDate = new Date(); // Ngày hiện tại

    // Kiểm tra trạng thái dựa trên ngày hiện tại và thời gian khuyến mãi
    let promotionStatus = '';
    if (currentDate < new Date(startDate)) {
        promotionStatus = 'Khuyến Mãi chưa áp dụng'; // Ngày hiện tại chưa đến startDate
    } else if (currentDate >= new Date(startDate) && currentDate <= new Date(endDate)) {
        promotionStatus = 'Khuyến Mãi Đang được áp dụng'; // Trong khoảng startDate và endDate
    } else {
        promotionStatus = 'Đã qua thời gian áp dụng'; // Sau endDate
    }

    // Hiển thị trạng thái khuyến mãi
    document.getElementById('summary-promotion-status').textContent = promotionStatus;
    console.log(promotionId);
    // Hiển thị các sản phẩm đã chọn

    const newUrl = `/Admin`;

// Cập nhật URL mà không làm mới trang
    window.history.pushState(
        {path: newUrl},
        '',
        newUrl
    );

    const requestData = {
        'promotionId': promotionID
    };
    fetch(newUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
    })
        .then(response => response.text()) // Đọc dữ liệu dưới dạng text
        .then(data => {
            console.log('Base64 Response:', data); // In dữ liệu Base64

            try {
                // Decode Base64 sang chuỗi JSON
                const cleanData = data.replace(/({.*})$/, ''); // Loại bỏ chuỗi JSON từ dấu `{` trở về sau
                console.log('Cleaned Base64 Data:', cleanData);

                // Decode Base64 sang chuỗi JSON
                const decodedData = atob(cleanData);
                console.log('Decoded Data:', decodedData);

                // Parse chuỗi JSON sau khi decode
                const jsonData = JSON.parse(decodedData);
                console.log('Parsed JSON:', jsonData);

                const dropdownMenu = document.querySelector('#overlay-view .dropdown-menu');
                if (!dropdownMenu) {
                    console.error('Dropdown menu not found!');
                    return;
                }

                dropdownMenu.innerHTML = ''; // Xóa nội dung cũ

                // Thêm các checkbox vào menu và thiết lập trạng thái
                if (jsonData.length > 0) {
                    jsonData.forEach(productName => {
                        // Tạo ID duy nhất cho checkbox
                        const checkboxId = `checkbox-${productName.replace(/\s+/g, '-')}`;

                        // Tạo phần tử label chứa checkbox và luôn đánh dấu checkbox là checked
                        const label = document.createElement('label');
                        label.innerHTML = `
                            <input type="checkbox" id="${checkboxId}" checked disabled>
                            ${productName}
                            `;

                        // Thêm label vào dropdown menu
                        dropdownMenu.appendChild(label);
                    });
                } else {
                    console.log('No products found');
                    dropdownMenu.innerHTML = '<p>No products available</p>';
                }
            } catch (error) {
                console.error('Error decoding or parsing JSON:', error);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });

}

function closeModal() {
    document.getElementById('notificationModal').style.setProperty("display", "none","important");
}
// Function to handle form submission and display selected products
function handleTypeChange() {
    const type = document.getElementById('promotion-type').value;
    const selectProducts = document.getElementById('select-products');
    const minimumLoyaltyContainer = document.getElementById('minimum-loyalty-container');

    if (type === 'promotion') {
        selectProducts.style.display = 'block';
        minimumLoyaltyContainer.style.display = 'none';
    } else if (type === 'voucher') {
        selectProducts.style.display = 'none';
        minimumLoyaltyContainer.style.display = 'block';
    }
}

function handleDiscountUnitChange() {
    const unit = document.getElementById('discount-unit').value;
    const discountValue = document.getElementById('discount-value');

    discountValue.addEventListener('input', () => {
        const value = parseFloat(discountValue.value);
        if (unit === '%') {
            if (value < 0 || value > 100) {
                discountValue.setCustomValidity('Discount percentage must be between 0 and 100.');
            } else {
                discountValue.setCustomValidity('');
            }
        } else if (unit === 'VND') {
            if (value < 0) {
                discountValue.setCustomValidity('Discount value must be 0 or more.');
            } else {
                discountValue.setCustomValidity('');
            }
        }
    });
}
// Đặt min ngày cho Start Date và End Date
document.addEventListener('DOMContentLoaded', function() {
    const today = new Date().toISOString().split('T')[0]; // Lấy ngày hiện tại theo định dạng yyyy-mm-dd
    document.getElementById('start-date').setAttribute('min', today);
    document.getElementById('end-date').setAttribute('min', today);

    document.getElementById('start-date').addEventListener('change', validateDates);
    document.getElementById('end-date').addEventListener('change', validateDates);
});
document.getElementById('delete-button').addEventListener('click', function (event) {
    const startDate = document.getElementById('view-start-date').value;
    const endDate = document.getElementById('view-end-date').value;

    // Kiểm tra xem start-date và end-date có hợp lệ không
    if (startDate && endDate) {
        const currentDate = new Date();
        const start = new Date(startDate);
        const end = new Date(endDate);

        // Kiểm tra nếu ngày hiện tại nằm trong khoảng thời gian promotion
        if (currentDate >= start && currentDate <= end) {
            // Ngừng gửi form
            event.preventDefault();

            // Hiển thị thông báo
            alert('Promotion is active and cannot be deleted during this period.');
        }
    }
});

function validateDates() {
    const startDate = document.getElementById('start-date').value;
    const endDate = document.getElementById('end-date').value;

    if (startDate && endDate && startDate >= endDate) {
        alert('Start date must be before end date.');
        document.getElementById('end-date').value = ''; // Reset end date
    }
}
