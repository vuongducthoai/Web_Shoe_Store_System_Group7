const minPrice = parseInt($('.range-slide').attr('data-min'));  // Giá trị min được truyền từ server hoặc HTML
const maxPrice = parseInt($('.range-slide').attr('data-max'));  // Giá trị max được truyền từ server hoặc HTML
const away = 100000;

let filterData = {
    selectedCategory: null,
    selectedSize: null,
    selectedColor: null,
    minPrice: minPrice,
    maxPrice: maxPrice,
    sortOption : "Phổ biến nhất"
};

var selectedSize = document.getElementById("selectedSize").getAttribute("selectedSize");
var selectedColor = document.getElementById("selectedColor").getAttribute("selectedColor");
var selectedCategory = document.getElementById("selectedCategory").getAttribute("selectedCategory");
var filterMinPrice = parseInt(document.getElementById("filterMinPrice").getAttribute("filterMinPrice"));
var filterMaxPrice = parseInt(document.getElementById("filterMaxPrice").getAttribute("filterMaxPrice"));
var sortOption = document.getElementById("sortOption").getAttribute("sortOption");

// In ra giá trị lấy được từ HTML để kiểm tra
console.log("Selected size from HTML:", selectedSize);
console.log("Selected color from HTML:", selectedColor);
console.log("Selected category from HTML:", selectedCategory);
console.log("Min price from HTML:", filterMinPrice);
console.log("Max price from HTML:", filterMaxPrice);
console.log("SortOption price from HTML:", sortOption);

filterData.selectedSize = selectedSize;
filterData.selectedColor = selectedColor;
filterData.selectedCategory = selectedCategory;
filterData.minPrice = filterMinPrice || minPrice;
filterData.maxPrice = filterMaxPrice || maxPrice;
filterData.sortOption = sortOption || "Phổ biến nhất";

// Kiểm tra nếu có giá trị size đã chọn, và kích hoạt việc chọn size
if (selectedSize) {
    var sizeButtons = document.querySelectorAll('.size-btn');
    sizeButtons.forEach(function(button) {
        if (button.getAttribute('data-size') === selectedSize) {
            selectSize(button);  // Gọi hàm selectSize nếu nút size trùng với giá trị đã chọn
        }
    });
}

// Tương tự, bạn có thể xử lý color và category nếu cần
if (selectedColor) {
    var colorButtons = document.querySelectorAll('.color-btn');
    colorButtons.forEach(function(button) {
        console.log(button);
        if (rgbToHex(button.style.backgroundColor) === selectedColor) {
            selectColor(button);  // Gọi hàm selectColor nếu màu trùng với giá trị đã chọn
        }
    });
}

if (selectedCategory) {
    var categoryButtons = document.querySelectorAll('.category-btn');
    categoryButtons.forEach(function(button) {
        if (button.getAttribute('data-category') === selectedCategory) {
            selectCategory(button);  // Gọi hàm selectCategory nếu thể loại trùng với giá trị đã chọn
        }
    });
}

$('#min').html(formatCurrency(minPrice));
$('#max').html(formatCurrency(maxPrice));

// Gán giá trị hiện tại vào các slider mà không thay đổi min/max
$('#rangeMin').val(filterData.minPrice); // Gán giá trị hiện tại của slider min
$('#rangeMax').val(filterData.maxPrice); // Gán giá trị hiện tại của slider max

// Cập nhật giao diện slider (vị trí thanh trượt và giá trị hiển thị)
requestAnimationFrame(() => {
    $('#thumbMin').css('left', calcLeftPosition(filterData.minPrice) + '%');
    $('#thumbMax').css('left', calcLeftPosition(filterData.maxPrice) + '%');
    $('#min').html(formatCurrency(filterData.minPrice)); // Hiển thị giá trị min
    $('#max').html(formatCurrency(filterData.maxPrice)); // Hiển thị giá trị max
    $('#line').css({
        'left': calcLeftPosition(filterData.minPrice) + '%',
        'right': (100 - calcLeftPosition(filterData.maxPrice)) + '%'
    });
});


if (filterData.sortOption) {
    let options = ["Phổ biến nhất", "Ưu đãi hấp dẫn nhất", "Giá: Thấp đến Cao", "Giá: Cao đến thấp"];

    var dropdownMenu = document.getElementById('dropdownMenu');  // Lấy phần tử ul
    var dropdownButton = document.getElementById('dropdownButton');  // Lấy button

    // Nếu dropdownButton tồn tại, cập nhật nội dung của nó
    if (dropdownButton) {
        options.forEach(function(option) {
            if (option === filterData.sortOption) {
                dropdownButton.innerHTML = `${option} <i class="fas fa-chevron-down ml-2"></i>`;
            } else {
                var newListItem = document.createElement('li');
                newListItem.textContent = option;
                newListItem.onclick = function() {
                    selectOption(option);
                };
                dropdownMenu.appendChild(newListItem);
            }
        });
    }
}


function toggleDropdown() {
    const dropdown = document.getElementById('dropdownMenu');
    dropdown.classList.toggle('hidden'); // Toggle class hidden để hiển thị/ẩn
}

// Hàm để xử lý khi người dùng chọn một mục trong dropdown
function selectOption(option) {
    option = option.trim();
    var button = document.getElementById('dropdownButton');

    // Lấy nội dung text của button
    var textCurrent = button.textContent;
    var dropdownMenu = document.getElementById('dropdownMenu');  // Lấy phần tử ul
    var newListItem = document.createElement('li');               // Tạo một li mới
    newListItem.textContent = textCurrent;                               // Thiết lập nội dung văn bản
    newListItem.onclick = function() {                            // Thêm sự kiện onclick
        selectOption(textCurrent);
    };
    dropdownMenu.appendChild(newListItem);

    const dropdownButton = document.getElementById('dropdownButton');
    dropdownButton.innerHTML = `${option} <i class="fas fa-chevron-down ml-2"></i>`;

    filterData.sortOption = option;
    var items = dropdownMenu.getElementsByTagName('li');

    for (var i = 0; i < items.length; i++) {
        console.log(items[i].textContent);
        if (items[i].textContent.trim() === option) {
            items[i].remove();
            break;
        }
    }

    // Đóng dropdown sau khi chọn
    toggleDropdown();
    apply();
}

// Hàm tính toán vị trí của thumb dựa trên giá trị min và max
const calcLeftPosition = value => ((value - minPrice) / (maxPrice - minPrice)) * 100;

$('#rangeMin').on('input', function(e) {
    const newValue = parseInt(e.target.value);

    // Đảm bảo min + step <= max
    if (newValue + away > filterData.maxPrice) {
        $(this).val(filterData.minPrice); // Reset lại giá trị nếu không hợp lệ
        return;
    }

    filterData.minPrice = newValue;

    // Cập nhật giao diện mượt mà hơn
    requestAnimationFrame(() => {
        $('#thumbMin').css('left', calcLeftPosition(newValue) + '%');
        $('#min').html(formatCurrency(newValue));
        $('#line').css({
            'left': calcLeftPosition(newValue) + '%',
            'right': (100 - calcLeftPosition(filterData.maxPrice)) + '%'
        });
    });
});

$('#rangeMax').on('input', function(e) {
    const newValue = parseInt(e.target.value);

    // Đảm bảo max - step >= min
    if (newValue - away < filterData.minPrice) {
        $(this).val(filterData.maxPrice); // Reset lại giá trị nếu không hợp lệ
        return;
    }

    filterData.maxPrice = newValue;

    // Cập nhật giao diện mượt mà hơn
    requestAnimationFrame(() => {
        $('#thumbMax').css('left', calcLeftPosition(newValue) + '%');
        $('#max').html(formatCurrency(newValue));
        $('#line').css({
            'left': calcLeftPosition(filterData.minPrice) + '%',
            'right': (100 - calcLeftPosition(newValue)) + '%'
        });
    });
});

function selectCategoryAndLoadPage(button) {
    selectCategory(button);
    apply();
}

function selectCategory(button) {
    // Kiểm tra xem nút thể loại đã chọn chưa
    const isSelected = button.classList.contains('bg-blue-500');

    // Xóa lớp đã chọn khỏi tất cả các nút thể loại
    let allButtons = document.querySelectorAll('.category-btn');
    allButtons.forEach(function(btn) {
        btn.classList.remove('bg-blue-500', 'text-white'); // Xóa màu nền và chữ trắng
        btn.classList.add('text-gray-700'); // Đặt lại màu chữ mặc định
    });

    if (isSelected) {
        // Nếu thể loại này đã được chọn, bỏ chọn
        filterData.selectedCategory = null;
        console.log("Bỏ chọn thể loại.");
    } else {
        // Thêm lớp đặc biệt vào thể loại được chọn
        button.classList.add('bg-blue-500', 'text-white'); // Thêm màu nền và chữ trắng
        // Lưu lại thể loại đã chọn
        filterData.selectedCategory = button.getAttribute('data-category');
        console.log("Thể loại đã chọn: " + filterData.selectedCategory);
    }
}

// Hàm xử lý khi click vào màu sắc
function selectColor(button) {
    // Kiểm tra nếu nút đã có lớp 'border-blue-500'
    if (button.classList.contains('border-blue-500')) {
        // Nếu có, bỏ lớp đi để hủy chọn màu
        button.classList.remove('border-blue-500');
        filterData.selectedColor = ""; // Hủy màu đã chọn
    } else {
        let colorButtons = document.querySelectorAll('.color-btn'); // Chỉ chọn nút màu
        colorButtons.forEach(function(btn) {
            btn.classList.remove('border-blue-500'); // Chỉ xóa trạng thái nút màu
        });

        // Thêm lớp đặc biệt vào nút đã chọn
        button.classList.add('border-blue-500'); // Thêm border màu xanh

        // Lưu lại màu sắc đã chọn
        filterData.selectedColor = rgbToHex(button.style.backgroundColor);
    }

    // Ví dụ xử lý: Gửi màu sắc người dùng đã chọn để lọc dữ liệu
    console.log("Màu sắc đã chọn: " + filterData.selectedColor);
}

// Hàm xử lý khi click vào nút size
function selectSize(button) {
    // Kiểm tra nếu nút đã có lớp 'border-blue-500'
    if (button.classList.contains('border-blue-500')) {
        // Nếu có, bỏ lớp đi để hủy chọn size
        button.classList.remove('border-blue-500');
        filterData.selectedSize = ""; // Hủy size đã chọn
    } else {
        // Nếu chưa có lớp 'border-blue-500', xóa lớp đã chọn khỏi tất cả các nút size
        let allButtons = document.querySelectorAll('.size-btn');
        allButtons.forEach(function(btn) {
            btn.classList.remove('border-blue-500');
        });

        // Thêm lớp đặc biệt vào nút được chọn
        button.classList.add('border-blue-500'); // Thêm màu nền và chữ trắng

        // Lưu lại size đã chọn
        filterData.selectedSize = button.getAttribute('data-size');
    }

    // Ví dụ xử lý: Gửi size người dùng đã chọn để lọc dữ liệu
    console.log("Size đã chọn: " + filterData.selectedSize);
}

// Gán sự kiện click cho nút "Áp dụng"
function apply(page = 1) {
    const contextPath = document.getElementById('contextPath').getAttribute('data-contextPath');

    const url = contextPath + '/customer/product/filter';

    // Tạo URL chứa thông tin lọc
    const queryParams = new URLSearchParams({
        selectedCategory: filterData.selectedCategory || "",
        selectedSize: filterData.selectedSize || "",
        selectedColor: filterData.selectedColor || "",
        minPrice: filterData.minPrice,
        maxPrice: filterData.maxPrice,
        sortOption: filterData.sortOption,
        page: page
    }).toString();

    // Redirect đến URL mới
    window.location.href = `${url}?${queryParams}`;
}

const categoryListJson = document.getElementById('categoryListJson').textContent;


// Chuyển chuỗi JSON thành đối tượng JavaScript
const categoryList = JSON.parse(categoryListJson);


// Tìm phần tử <span> bằng ID
const spanElement = document.getElementById("dynamicSpan");
var totalSize = parseInt(document.getElementById("totalSize").getAttribute("totalSize"));

// Cập nhật nội dung của <span>
spanElement.textContent = "Hiển thị 1-" + categoryList.length + " trong số " + totalSize + " sản phẩm";
    
// Hàm render sản phẩm
function renderProductList(products) {
    const container = document.getElementById('product-container'); // Lấy container
    container.innerHTML = ''; // Xóa nội dung cũ

    const MAX_STARS = 5; // Số sao tối đa

    // Lặp qua từng sản phẩm và tạo HTML
    products.forEach(product => {
        // Tính toán giá trị trung bình của đánh giá từ reviewDTOList
        const reviews = product.reviewDTOList || []; // Nếu không có reviewDTOList, mặc định là một mảng rỗng
        const totalRating = reviews.reduce((sum, review) => sum + review.ratingValue, 0); // Tổng giá trị đánh giá
        const averageRating = reviews.length > 0 ? totalRating / reviews.length : 0; // Tính giá trị trung bình

        // Kiểm tra xem trung bình có phải là số nguyên không
        const formattedRating = Number.isInteger(averageRating) ? averageRating : averageRating.toFixed(1);

        // Tính số sao đầy đủ và nửa sao
        const fullStars = Math.floor(averageRating); // Số sao đầy đủ
        const halfStars = (averageRating % 1 >= 0.5) ? 1 : 0; // Kiểm tra xem có nửa sao không
        const emptyStars = MAX_STARS - fullStars - halfStars; // Số sao trống

        // Tạo chuỗi đánh giá sao động
        let starHTML = '';
        for (let i = 0; i < fullStars; i++) {
            starHTML += '<span class="text-yellow-500"><i class="fas fa-star"></i></span>';
        }
        if (halfStars) {
            starHTML += '<span class="text-yellow-500"><i class="fas fa-star-half-alt"></i></span>';
        }
        for (let i = 0; i < emptyStars; i++) {
            starHTML += '<span class="text-gray-300"><i class="fas fa-star"></i></span>';
        }

        // Tính giá cũ và giá mới với giảm giá
        const originalPrice = product.price;
        const discount = product.promotionDTO ? product.promotionDTO.discountValue : 0;
        const discountedPrice = discount ? (originalPrice - (originalPrice * (discount / 100))) : originalPrice;

        // Tính phần trăm giảm giá
        const discountText = discount ? `-${discount}%` : '';

        const productHTML = `
            <div class="bg-white p-4 rounded-lg shadow-sm cursor-pointer">
                <img alt="${product.productName}"
                     class="w-full h-64 object-cover mb-4" height="400"
                     src="data:image/jpeg;base64,${product.imageBase64}" width="300" />
                <h2 class="font-medium">${product.productName}</h2>
                <div class="flex items-center mb-2">
                    ${starHTML} <!-- Hiển thị đánh giá sao động -->
                    <span class="ml-2 text-gray-500">${formattedRating}/5</span> <!-- Hiển thị đánh giá với định dạng số nguyên nếu có -->
                </div>
                <p class="text-lg font-semibold">
                    <span class="text-1.5xl font-bold text-black">${formatCurrency(discountedPrice)}</span> 
                    ${discount > 0 ? `<span class="text-1.5xl font-bold text-gray-400 line-through">${formatCurrency(originalPrice)}</span>` : ''}
                    ${discount > 0 ? `<span class="text-1xl font-bold text-red-500 bg-red-100 rounded-full px-4 py-2">${discountText}</span>` : ''}
                </p>
            </div>
        `;
        container.insertAdjacentHTML('beforeend', productHTML); // Thêm HTML vào container
    });
}


// Gọi hàm render sau khi đã có danh sách sản phẩm
renderProductList(categoryList);

function formatCurrency(value) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
        minimumFractionDigits: 0 // Không hiển thị phần thập phân
    }).format(value);
}


function rgbToHex(rgb) {
    if (!rgb){
        return null;
    }
    const rgbValues = rgb.match(/\d+/g);  // Tách giá trị r, g, b từ chuỗi rgb(r, g, b)
    const hex = rgbValues.map(value => {
        let hexValue = parseInt(value).toString(16);  // Chuyển số thập phân sang hex
        return hexValue.length === 1 ? '0' + hexValue : hexValue;  // Đảm bảo có 2 ký tự
    }).join('');
    return `#${hex.toUpperCase()}`;  // Kết quả là mã màu hex
}