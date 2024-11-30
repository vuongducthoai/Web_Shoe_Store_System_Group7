// Lấy giá trị min/max từ HTML
const minPrice = parseInt($('.custom-slider___1').attr('data-min'));
const maxPrice = parseInt($('.custom-slider___1').attr('data-max'));
const away = 100000;
// Lấy dữ liệu từ thẻ HTML (dùng data attribute)
const jsonProductNames = document.getElementById("jsonProductNames").textContent;
// Chuyển chuỗi JSON thành mảng JavaScript
const productNames = JSON.parse(jsonProductNames);
const searchInput = document.querySelector("input[type='text']");
const suggestionsList = document.getElementById("suggestionsList");
suggestionsList.style.display = "none"; // Ẩn danh sách gợi ý
const searchIcon = document.querySelector(".fas.fa-search");

// Tạo dữ liệu bộ lọc ban đầu
let filterData = {
    selectedPromotion: null,
    selectedCategory: null,
    selectedSize: null,
    selectedColor: null,
    minPrice: minPrice,
    maxPrice: maxPrice,
    sortOption: "Phổ biến nhất",
    searchName: null
};

// Lấy các giá trị từ HTML
var selectedSize = document.getElementById("selectedSize").getAttribute("selectedSize");
var selectedColor = document.getElementById("selectedColor").getAttribute("selectedColor");
var selectedCategory = document.getElementById("selectedCategory").getAttribute("selectedCategory");
var selectedPromotion = document.getElementById("selectedPromotion").getAttribute("selectedPromotion");
var filterMinPrice = parseInt(document.getElementById("filterMinPrice").getAttribute("filterMinPrice"));
var filterMaxPrice = parseInt(document.getElementById("filterMaxPrice").getAttribute("filterMaxPrice"));
var sortOption = document.getElementById("sortOption").getAttribute("sortOption");
var searchName = document.getElementById("searchName").getAttribute("searchName");

// In ra giá trị lấy được từ HTML để kiểm tra
console.log("Selected size from HTML:", selectedSize);
console.log("Selected color from HTML:", selectedColor);
console.log("Selected category from HTML:", selectedCategory);
console.log("Selected selectedPromotion from HTML:", selectedPromotion);
console.log("Min price from HTML:", filterMinPrice);
console.log("Max price from HTML:", filterMaxPrice);
console.log("SortOption price from HTML:", sortOption);
console.log("searchName from HTML:", searchName);

// Gán dữ liệu vào `filterData`
filterData.selectedSize = selectedSize;
filterData.selectedColor = selectedColor;
filterData.selectedCategory = selectedCategory;
filterData.selectedPromotion = selectedPromotion;
filterData.minPrice = filterMinPrice || minPrice;
filterData.maxPrice = filterMaxPrice || maxPrice;
filterData.sortOption = sortOption || "Phổ biến nhất";
filterData.searchName = searchName;

if (searchName){
    searchInput.value = searchName;
}

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
    var categoryButtonsCategory = document.querySelectorAll('.category-btn');
    categoryButtonsCategory.forEach(function(button) {
        if (button.getAttribute('data-category') === selectedCategory) {
            selectCategory(button);  // Gọi hàm selectCategory nếu thể loại trùng với giá trị đã chọn
        }
    });
}

if (selectedPromotion)
{
    var categoryButtonPromotion = document.querySelectorAll('.promotion-btn');
    categoryButtonPromotion.forEach(function(button) {
        if (button.getAttribute('data-promotion') === selectedPromotion) {
            selectPromotion(button);  // Gọi hàm selectCategory nếu thể loại trùng với giá trị đã chọn
        }
    });
}
// Cập nhật giao diện ban đầu
$('#min').html(formatCurrency(filterData.minPrice));
$('#max').html(formatCurrency(filterData.maxPrice));
$('#rangeMin').val(filterData.minPrice);
$('#rangeMax').val(filterData.maxPrice);

// Hàm tính toán vị trí thumb
const calcLeftPosition = value => ((value - minPrice) / (maxPrice - minPrice)) * 100;

// Cập nhật giao diện slider
requestAnimationFrame(() => {
    $('#thumbMin').css('left', calcLeftPosition(filterData.minPrice) + '%');
    $('#thumbMax').css('left', calcLeftPosition(filterData.maxPrice) + '%');
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

// Xử lý sự kiện khi thay đổi giá trị của `rangeMin`
$('#rangeMin').on('input', function (e) {
    const newValue = parseInt(e.target.value);

    // Kiểm tra tính hợp lệ
    if (newValue + away > filterData.maxPrice) {
        $(this).val(filterData.minPrice); // Reset giá trị nếu không hợp lệ
        return;
    }

    filterData.minPrice = newValue;

    // Cập nhật giao diện
    requestAnimationFrame(() => {
        $('#thumbMin').css('left', calcLeftPosition(newValue) + '%');
        $('#line').css('left', calcLeftPosition(newValue) + '%');
        $('#min').html(formatCurrency(newValue));
    });
});

// Xử lý sự kiện khi thay đổi giá trị của `rangeMax`
$('#rangeMax').on('input', function (e) {
    const newValue = parseInt(e.target.value);

    // Kiểm tra tính hợp lệ
    if (newValue - away < filterData.minPrice) {
        $(this).val(filterData.maxPrice); // Reset giá trị nếu không hợp lệ
        return;
    }

    filterData.maxPrice = newValue;

    // Cập nhật giao diện
    requestAnimationFrame(() => {
        $('#thumbMax').css('left', calcLeftPosition(newValue) + '%');
        $('#line').css('right', (100 - calcLeftPosition(newValue)) + '%');
        $('#max').html(formatCurrency(newValue));
    });
});

function selectPromotionAndLoadPage(button) {
    selectPromotion(button);
    apply();
}


function selectPromotion(button) {
    // Kiểm tra xem nút khuyến mãi đã được chọn chưa
    const isSelected = button.classList.contains('text-blue-500');

    // Xóa màu chữ xanh của tất cả các nút khuyến mãi
    let allButtons = document.querySelectorAll('.promotion-btn');
    allButtons.forEach(function(btn) {
        btn.classList.remove('text-blue-500'); // Xóa màu chữ xanh
        btn.classList.add('text-gray-800'); // Đặt lại màu chữ mặc định
    });

    if (isSelected) {
        // Nếu nút này đã được chọn, bỏ chọn
        filterData.selectedPromotion = null;
    } else {
        // Thêm màu chữ xanh vào nút được chọn
        // Thêm màu chữ xanh vào nút được chọn
        button.classList.add('text-blue-500'); // Thêm màu chữ xanh
        button.classList.remove('text-gray-800'); // Đảm bảo màu chữ mặc định bị loại bỏ
        // Lưu lại chương trình khuyến mãi đã chọn
        filterData.selectedPromotion = button.getAttribute('data-promotion');
        console.log(filterData.selectedPromotion);
    }
}



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
        selectedPromotion: filterData.selectedPromotion || "",
        selectedCategory: filterData.selectedCategory || "",
        selectedSize: filterData.selectedSize || "",
        selectedColor: filterData.selectedColor || "",
        minPrice: filterData.minPrice,
        maxPrice: filterData.maxPrice,
        sortOption: filterData.sortOption,
        searchName: filterData.searchName,
        page: page
    }).toString();

    // Redirect đến URL mới
    window.location.href = `${url}?${queryParams}`;
}

const categoryListJson = document.getElementById('categoryListJson').textContent;
const categoryList = JSON.parse(categoryListJson);

const soldQuantityMapJson = document.getElementById('soldQuantityMapJson').textContent;
const soldQuantityMap = JSON.parse(soldQuantityMapJson);

// Tìm phần tử <span> bằng ID
const spanElement = document.getElementById("dynamicSpan");
var totalSize = parseInt(document.getElementById("totalSize").getAttribute("totalSize"));

if (categoryList.length === 0) {
    spanElement.textContent = "Hiện tại chưa có sản phẩm nào trong danh mục này hoặc các sản phẩm đều đã hết hàng";
} else {
    spanElement.textContent = "Hiển thị 1-" + categoryList.filter(product => product.status === true).length + " trong số " + totalSize + " sản phẩm";
}


    
// Hàm render sản phẩm
function renderProductList(products) {
    const container = document.getElementById('product-container'); // Lấy container
    container.innerHTML = ''; // Xóa nội dung cũ
    const MAX_STARS = 5; // Số sao tối đa
    let promotionMessage = '';

    // Lặp qua từng sản phẩm và tạo HTML
    products.forEach(product => {
        if (product.status === false){
            return;
        }
        const averageRating = calculateAverageRating(products, product.productName);

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

        // Kiểm tra nếu có khuyến mãi và kiểm tra thời gian hiện tại có nằm trong khoảng thời gian khuyến mãi không
        const originalPrice = product.price;
        let discountedPrice = originalPrice;
        let discountText = '';
        let discount;

        // Nếu có khuyến mãi, kiểm tra thời gian khuyến mãi
        if (Array.isArray(product.promotionProducts) && product.promotionProducts.length > 0) {
            const now = new Date();

            // Duyệt qua từng phần tử trong mảng promotionProducts
            for (const promotionProduct of product.promotionProducts) {
                const promotion = promotionProduct.promotion; // Lấy thông tin khuyến mãi
                if (!promotion) continue; // Nếu không có promotion, bỏ qua
                if (!promotion.isActive) continue; // Nếu khuyến mãi không hoạt động, bỏ qua

                const startDate = new Date(promotion.startDate);
                const endDate = new Date(promotion.endDate);

                // Kiểm tra nếu khuyến mãi đang hoạt động
                if (now >= startDate && now <= endDate) {
                    discount = promotion.discountValue;
                    if (promotion.discountType === 'Percentage') {
                        // Tính giá giảm theo phần trăm
                        discountedPrice = originalPrice - (originalPrice * (discount / 100));
                        discountText = `-${discount}%`;
                    } else if (promotion.discountType === 'VND') {
                        // Tính giá giảm theo tiền tệ (giảm trực tiếp)
                        discountedPrice = originalPrice - discount;
                        discountText = `-${formatCurrency(discount)}`;
                    }

                    // Tính thời gian còn lại
                    const timeRemaining = endDate - now; // Thời gian còn lại (mili giây)
                    const daysRemaining = Math.floor(timeRemaining / (1000 * 60 * 60 * 24)); // Số ngày còn lại

                    if (daysRemaining > 0) {
                        promotionMessage = `${discountText} còn ${daysRemaining} ngày`;
                    } else if (daysRemaining === 0) {
                        promotionMessage = `${discountText} hết hạn hôm nay`;
                    } else {
                        promotionMessage = `${discountText} đã hết hạn`;
                    }

                    // Nếu đã tìm thấy khuyến mãi hợp lệ, dừng vòng lặp
                    break;
                }
            }
        }

        const soldQuantity = soldQuantityMap[product.productName];

        const productHTML = `
            <div class="bg-white p-4 rounded-lg shadow-sm mb-6 border border-gray-200">

                <img alt="${product.productName}"
                     class="w-full h-64 object-cover mb-4" height="320"
                     src="${product.imageBase64}" width="240" />

                     
                <h2 class="font-medium">${product.productName}</h2>
                <div class="flex items-center mb-2">
                    ${starHTML} <!-- Hiển thị đánh giá sao động -->
                    <span class="ml-2 text-gray-500">${formattedRating}/5</span> <!-- Hiển thị đánh giá với định dạng số nguyên nếu có -->
                </div>

                <p class="text-sm text-gray-700 mb-2">
                    Đã bán: ${soldQuantity} sản phẩm
                </p>
                
                <p class="text-lg font-semibold mb-2">
                    <span class="text-2xl font-bold text-black">
                        ${formatCurrency(discountedPrice)}
                    </span>
                    ${discount > 0 ? `
                    <span class="text-xl font-bold text-gray-400 line-through">
                        ${formatCurrency(originalPrice)}
                    </span>` : ''}
                </p>
                ${discount > 0 ? `
                    <div class="mt-2 text-sm font-bold text-red-500 bg-red-100 rounded-full px-4 py-2 inline-block">
                        ${promotionMessage}
                    </div>` : ''}
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

// Lấy đối tượng của nút toggle và danh sách thể loại
const toggleButton = document.getElementById('toggleCategories');
const categoryListHTML = document.getElementById('category-list');
const toggleIcon = document.getElementById('toggleIcon');
const toggleText = document.getElementById('toggleText');

// Khi nhấn nút toggle
toggleButton.addEventListener('click', () => {
    // Kiểm tra nếu danh sách hiện đang ẩn
    if (categoryListHTML.classList.contains('hidden')) {
        // Hiển thị danh sách
        categoryListHTML.classList.remove('hidden');
        toggleIcon.textContent = '↑'; // Thay mũi tên xuống bằng mũi tên lên
        toggleText.textContent = 'Ẩn bớt';
    } else {
        // Ẩn danh sách
        categoryListHTML.classList.add('hidden');
        toggleIcon.textContent = '↓'; // Thay mũi tên lên bằng mũi tên xuống
        toggleText.textContent = 'Xem tất cả';
    }
});

searchIcon.addEventListener("click", function() {
    handleSearch(searchInput.value); // Gọi hàm khi click vào biểu tượng tìm kiếm
});


searchInput.addEventListener("input", function (event) {
    const query = event.target.value; // Chuỗi người dùng nhập
    const filteredKeyValue = searchProducts(query, productNames);

    displaySuggestions(filteredKeyValue); // Hiển thị kết quả gợi ý
});




function removeAccents(str) {
    return str
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/đ/g, "d")
        .replace(/Đ/g, "D")
        .toLowerCase();
}

function tokenize(str) {
    return removeAccents(str).split(/\s+/); // Loại bỏ dấu và tách chuỗi theo khoảng trắng
}

function calculateMatchScore(userKeywords, productKeywords) {
    let matchCount = 0;

    // Duyệt qua từng từ khóa người dùng nhập
    userKeywords.forEach(userWord => {
        // Kiểm tra nếu từ khóa là một phần của bất kỳ từ nào trong tên sản phẩm
        if (productKeywords.some(productWord => productWord.includes(userWord))) {
            matchCount++; // Nếu từ khóa khớp, tăng số lượng
        }
    });

    return matchCount; // Số lượng từ khóa khớp
}

function searchProducts(query, productNames) {
    const userKeywords = tokenize(query); // Tách từ khóa từ chuỗi người dùng nhập

    const results = Object.entries(productNames).map(([key, value]) => {
        const productKeywords = tokenize(key); // Tách từ khóa từ tên sản phẩm
        const score = calculateMatchScore(userKeywords, productKeywords); // Tính điểm khớp

        return {
            key,
            value,
            score,
        };
    });

    // Lọc các sản phẩm có điểm khớp > 0 và sắp xếp giảm dần theo điểm
    return results
        .filter(result => result.score > 0)
        .sort((a, b) => b.score - a.score); // Sắp xếp điểm cao nhất lên trước
}


searchInput.addEventListener("focus", function() {
    const filteredKeyValue = Object.entries(productNames)
        .map(([key, value]) => ({ key, value }));  // Chuyển đổi sang dạng object { key, value }

    displaySuggestions(filteredKeyValue);
});

// Khi nhấn Enter trong ô tìm kiếm hoặc khi nhấn gửi
searchInput.addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        event.preventDefault(); // Ngừng hành động mặc định (ví dụ, gửi form)
        handleSearch(searchInput.value); // Gọi hàm khi nhấn Enter
    }
});

// Hàm xử lý khi nhấn Enter hoặc gửi form
function handleSearch(query) {
    console.log("Tìm kiếm với từ khóa: " + query);

    filterData.searchName = query;

    apply();
}

// Hàm hiển thị danh sách gợi ý
function displaySuggestions(products) {
    suggestionsList.innerHTML = ""; // Xóa danh sách cũ

    // Kiểm tra nếu có kết quả gợi ý
    if (products.length > 0) {
        suggestionsList.style.display = "block"; // Hiển thị danh sách gợi ý
    } else {
        suggestionsList.style.display = "none"; // Ẩn danh sách nếu không có gợi ý
    }

    // Duyệt qua danh sách sản phẩm và hiển thị gợi ý
    products.forEach(function (product) {
        const li = document.createElement("li");
        li.classList.add("suggestion-item", "flex", "items-center", "gap-2");

        // Kiểm tra nếu giá trị ảnh đã có tiền tố "data:image/jpeg;base64,"
        const imageSrc = product.value;

        // Tạo phần tử ảnh
        const img = document.createElement("img");
        img.src = imageSrc;  // Sử dụng giá trị Base64 đúng
        img.alt = product.key;   // product.key là tên sản phẩm
        img.classList.add("w-8", "h-8", "rounded-md");

        // Tạo phần tử tên
        const span = document.createElement("span");
        span.textContent = product.key; // Tên sản phẩm

        // Thêm ảnh và tên vào mục gợi ý
        li.appendChild(img);
        li.appendChild(span);

        // Xử lý khi click vào mục gợi ý
        li.onclick = function () {
            searchInput.value = product.key;  // Điền tên sản phẩm vào ô tìm kiếm
            suggestionsList.innerHTML = "";   // Xóa gợi ý sau khi chọn
            suggestionsList.style.display = "none"; // Ẩn danh sách gợi ý
            handleSearch(searchInput.value);  // Gọi hàm xử lý tìm kiếm
        };

        suggestionsList.appendChild(li);
    });
}



// Ẩn danh sách gợi ý khi người dùng nhấn ra ngoài
document.addEventListener('click', function(event) {
    if (!event.target.closest('.relative')) {
        suggestionsList.style.display = "none"; // Ẩn danh sách gợi ý khi nhấn ngoài ô tìm kiếm
    }
});

function calculateAverageRating(productList, productName) {
    // Lọc các sản phẩm theo tên
    const productReviews = productList
        .filter(product => product && product.productName && product.productName.toLowerCase() === productName.toLowerCase())
        .flatMap(product => {
            // Trả về ratingValue từ reviewDTO nếu tồn tại và hợp lệ
            return product.reviewDTO && product.reviewDTO.ratingValue !== 0 ? [product.reviewDTO.ratingValue] : [];
        });

    // Tính trung bình của các đánh giá
    const totalRating = productReviews.reduce((sum, value) => sum + value, 0);
    const averageRating = productReviews.length > 0 ? totalRating / productReviews.length : 0;

    return averageRating;
}