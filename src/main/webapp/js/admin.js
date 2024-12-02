



var banner = document.getElementById("banner-section");
var productManagement = document.getElementById("product-management");
var categoryManagement = document.getElementById("category-management");
var accountManagement = document.getElementById("account-management");
var orderManagement =document.getElementById("order-management");
var promotionManagement =document.getElementById("promotion-management")
// Lắng nghe sự kiện khi người dùng click vào "Quản lý sản phẩm"
document.getElementById("manage-products-btn").addEventListener("click", function (event) {


    banner.style.display = "none";
    productManagement.style.display = "block";
    accountManagement.style.display = "none";
    categoryManagement.style.display = "none";
    orderManagement.style.display="none";
    promotionManagement.style.display="none";
    // nút thêm sửa xóa
    var addForm = document.querySelector("#add-product-management-form");
    var editForm = document.querySelector("#edit-product-management-form");
    var deleteForm = document.querySelector("#delete-product-management-form");


    document.getElementById("btn-product-management-actions-add").addEventListener("click", function (event) {
        editForm.style.display = "none";
        deleteForm.style.display = "none";
        addForm.style.display = "flex";
    });


    document.getElementById("btn-product-management-actions-edit").addEventListener("click", function (event) {
        addForm.style.display = "none";
        deleteForm.style.display = "none";
        editForm.style.display = "flex";
    });


    document.getElementById("btn-product-management-actions-delete").addEventListener("click", function (event) {
        addForm.style.display = "none";
        editForm.style.display = "none";
        deleteForm.style.display = "flex";
    });
});








// Lắng nghe sự kiện khi người dùng click vào "Quản lý danh mục sản phẩm"
document.getElementById("manage-categrories-btn").addEventListener("click", function (event) {


    banner.style.display = "none";
    categoryManagement.style.display = "block";
    accountManagement.style.display = "none";
    productManagement.style.display = "none";
    orderManagement.style.display="none";
    promotionManagement.style.display="none";






    // nút thêm sửa xóa
    var addForm = document.querySelector("#add-category-management-form");
    var editForm = document.querySelector("#edit-category-management-form");
    var deleteForm = document.querySelector("#delete-category-management-form");


    document.getElementById("btn-category-management-actions-add").addEventListener("click", function (event) {
        editForm.style.display = "none";
        deleteForm.style.display = "none";
        addForm.style.display = "flex";
    });


    document.getElementById("btn-category-management-actions-edit").addEventListener("click", function (event) {
        addForm.style.display = "none";
        deleteForm.style.display = "none";
        editForm.style.display = "flex";
    });


    document.getElementById("btn-category-management-actions-delete").addEventListener("click", function (event) {
        addForm.style.display = "none";
        editForm.style.display = "none";
        deleteForm.style.display = "flex";
    });
});








// Lắng nghe sự kiện khi người dùng click vào "Quản lý tài khoản"
document.getElementById("account-management-btn").addEventListener("click", function(event) {
    banner.style.display = "none";
    productManagement.style.display = "none";
    accountManagement.style.display ="block";
    orderManagement.style.display="none";
    categoryManagement.style.display="none";
    promotionManagement.style.display="none";






    var viewInfor = document.querySelector("#view-account-management-form");
    var editInfor = document.querySelector("#edit-account-management-form");
    var blockInfor = document.querySelector("#block-account-management-form");
    var unBlockInfor = document.querySelector("#unBlock-account-management-form");




    document.getElementById("btn-account-management-actions-view").addEventListener("click", function(event) {
        viewInfor.style.display="flex";
        editInfor.style.display="none";
        blockInfor.style.display="none";
        unBlockInfor.style.display="none";


    });
    document.getElementById("btn-account-management-actions-edit").addEventListener("click", function(event) {
        viewInfor.style.display="none";
        editInfor.style.display="flex";
        blockInfor.style.display="none";
        unBlockInfor.style.display="none";


    });
    document.getElementById("btn-account-management-actions-block").addEventListener("click", function(event) {
        viewInfor.style.display="none";
        editInfor.style.display="none";
        blockInfor.style.display="flex";
        unBlockInfor.style.display="none";
    });
    document.getElementById("btn-account-management-actions-unblock").addEventListener("click", function(event) {
        viewInfor.style.display="none";
        editInfor.style.display="none";
        blockInfor.style.display="none";
        unBlockInfor.style.display="flex";
    });


});


// Lắng nghe sự kiện khi người dùng click vào "Quản lý đơn hàng"
document.getElementById("order-management-btn").addEventListener("click", function(event) {
    banner.style.display = "none";
    productManagement.style.display = "none";
    accountManagement.style.display ="none";
    orderManagement.style.display="block";
    categoryManagement.style.display="none";
    promotionManagement.style.display="none";




    var viewOrder
    var editOrder
    var deleteOrder


})
// Load image lên picturebox


const loadImageBtns = document.querySelectorAll('.loadImageBtn');
const cancelBtns = document.querySelectorAll('.cancelBtn');
const imageInputs = document.querySelectorAll('.imageInput');
const imageDisplays = document.querySelectorAll('.imageDisplay');


// Hàm để tải hình ảnh
loadImageBtns.forEach((btn, index) => {
    btn.addEventListener('click', () => {
        event.preventDefault(); // Ngừng hành động mặc định (reload trang)
        imageInputs[index].click();
    });
});


// Xử lý sự kiện khi người dùng chọn file
imageInputs.forEach((input, index) => {
    input.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                imageDisplays[index].src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });
});


// Hàm để hủy hình ảnh
cancelBtns.forEach((btn, index) => {
    btn.addEventListener('click', () => {
        imageDisplays[index].src = '';
        imageInputs[index].value = ''; // Đặt lại input để có thể chọn cùng hình ảnh lần nữa
    });
});

// hàm xử ly xem lich su don hang cua khach hang
function viewHistory(button) {
    // Lấy ID khách hàng từ thuộc tính data-customer-id
    var customerId = button.getAttribute('data-user-id');
    console.log("User ID: " + customerId);
    // Gửi yêu cầu đến controller bằng cách chuyển hướng
    window.location.href = '/JPAExample_war_exploded/customer/orders?id=' + customerId;
}


// Quản lý khuyến mãi
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
    const overlay = document.getElementById('overlay');
    overlay.style.display = overlay.style.display === 'flex' ? 'none' : 'flex';
}


// Thêm sự kiện cho nút Add Promotion
document.querySelector('.add-promotion-btn').addEventListener('click', toggleForm);
function toggleDropdown() {
    const dropdownMenu = document.querySelector('.dropdown-menu');
    dropdownMenu.style.display = dropdownMenu.style.display === 'block' ? 'none' : 'block';
}


// Close the dropdown if clicked outside
document.addEventListener('click', function(event) {
    const dropdownButton = document.querySelector('.dropdown-button');
    const dropdownMenu = document.querySelector('.dropdown-menu');
    if (!dropdownButton.contains(event.target) && !dropdownMenu.contains(event.target)) {
        dropdownMenu.style.display = 'none';
    }
});


// Function to handle form submission and display selected products


document.getElementById("promotion-management-btn").addEventListener("click", function(event) {
    banner.style.display = "none";
    productManagement.style.display = "none";
    accountManagement.style.display ="none";
    orderManagement.style.display="none";
    categoryManagement.style.display="none";
    promotionManagement.style.display="block";


});


function searchCustomer() {
    // Lấy giá trị tìm kiếm từ input
    var input = document.getElementById("search-input").value.toLowerCase();
    var table = document.getElementById("account-table-body");
    var rows = table.getElementsByTagName("tr");


    // Duyệt qua tất cả các hàng trong bảng và ẩn những hàng không khớp với từ khóa
    for (var i = 0; i < rows.length; i++) {
        var cells = rows[i].getElementsByTagName("td");
        var match = false;


        // Kiểm tra trong mỗi cột, nếu cột nào có dữ liệu khớp với từ khóa tìm kiếm, hiển thị hàng
        for (var j = 0; j < cells.length; j++) {
            if (cells[j].textContent.toLowerCase().includes(input)) {
                match = true;
                break;
            }
        }


        // Nếu có từ khóa trùng, hiển thị hàng, ngược lại ẩn hàng
        if (match) {
            rows[i].style.display = "";
        } else {
            rows[i].style.display = "none";
        }
    }
}


// xem chi tiết account
document.addEventListener('DOMContentLoaded', function () {
    const tableBody = document.getElementById('account-table-body');
    const detailForm = document.getElementById('view-account-management-form');


    // Các trường chi tiết
    const inputID = document.getElementById('view-account-id');
    const inputName = document.getElementById('view-account-name');
    const inputEmail = document.getElementById('view-account-email');
    const inputPassword = document.getElementById('view-account-pass');
    const inputPhone = document.getElementById('view-account-phone');


    // Gắn sự kiện dblclick cho từng dòng trong bảng
    tableBody.addEventListener('dblclick', function (event) {
        const targetRow = event.target.closest('tr'); // Tìm hàng chứa phần tử được nhấp đúp
        if (targetRow) {
            // Lấy dữ liệu từ các cột
            const accountID = targetRow.cells[0].innerText.trim();
            const fullName = targetRow.getAttribute('data-account-name');
            const email = targetRow.getAttribute('data-account-email');


            const password = targetRow.getAttribute('data-account-password');
            const phone = targetRow.cells[4].innerText.trim();


            // Điền dữ liệu vào form
            inputID.value = accountID;
            inputName.value = fullName;
            inputEmail.value = email;
            inputPassword.value = password;
            inputPhone.value = phone;
            inputHistory.value = history;


            // Hiển thị form nếu chưa hiển thị
        }
    });
});
// sửa account
document.addEventListener('DOMContentLoaded', function () {
    const tableBody = document.getElementById('account-table-body');
    const detailForm = document.getElementById('view-account-management-form');


    // Các trường chi tiết
    const inputID = document.getElementById('edit-account-id-display');
    const inputName = document.getElementById('edit-account-name');
    const inputEmail = document.getElementById('edit-account-email');
    const inputPassword = document.getElementById('edit-account-pass');
    const inputPhone = document.getElementById('edit-account-phone');


    // Gắn sự kiện dblclick cho từng dòng trong bảng
    tableBody.addEventListener('dblclick', function (event) {
        const targetRow = event.target.closest('tr'); // Tìm hàng chứa phần tử được nhấp đúp
        if (targetRow) {
            // Lấy dữ liệu từ các cột
            const accountID = targetRow.cells[0].innerText.trim();
            const fullName = targetRow.getAttribute('data-account-name');
            const email = targetRow.getAttribute('data-account-email');


            const password = targetRow.getAttribute('data-account-password');
            const phone = targetRow.cells[4].innerText.trim();


            // Điền dữ liệu vào form
            inputID.value = accountID;
            inputName.value = fullName;
            inputEmail.value = email;
            inputPassword.value = password;
            inputPhone.value = phone;
            inputHistory.value = history;


            // Hiển thị form nếu chưa hiển thị
        }
    });
});
// blockAccount
document.addEventListener('DOMContentLoaded', function () {
    const tableBody = document.getElementById('account-table-body');
    const detailForm = document.getElementById('view-account-management-form');


    // Các trường chi tiết
    const inputID = document.getElementById('block-account-id');


    // Gắn sự kiện dblclick cho từng dòng trong bảng
    tableBody.addEventListener('dblclick', function (event) {
        const targetRow = event.target.closest('tr'); // Tìm hàng chứa phần tử được nhấp đúp
        if (targetRow) {
            // Lấy dữ liệu từ các cột
            const accountID = targetRow.cells[0].innerText.trim();


            // Điền dữ liệu vào form
            inputID.value = accountID;


            // Hiển thị form nếu chưa hiển thị
        }
    });
});
// unblock
document.addEventListener('DOMContentLoaded', function () {
    const tableBody = document.getElementById('account-table-body');
    const detailForm = document.getElementById('view-account-management-form');


    // Các trường chi tiết
    const inputID = document.getElementById('unBlock-account-id');


    // Gắn sự kiện dblclick cho từng dòng trong bảng
    tableBody.addEventListener('dblclick', function (event) {
        const targetRow = event.target.closest('tr'); // Tìm hàng chứa phần tử được nhấp đúp
        if (targetRow) {
            // Lấy dữ liệu từ các cột
            const accountID = targetRow.cells[0].innerText.trim();


            // Điền dữ liệu vào form
            inputID.value = accountID;


            // Hiển thị form nếu chưa hiển thị
        }
    });
});






function editProduct(button) {
    event.preventDefault();
    // Lấy thông tin sản phẩm từ thuộc tính data- của nút sửa
    var productId = button.getAttribute('data-product-id');
    var productName = button.getAttribute('data-product-name');
    var productPrice = button.getAttribute('data-product-price');
    var productColor = button.getAttribute('data-product-color');
    var productSize = button.getAttribute('data-product-size');
    var imgURL = button.getAttribute('data-product-img');
    var productDescription = button.getAttribute('data-product-description');
    var categoryName = button.getAttribute('data-category-name');


    // Điền thông tin vào form sửa sản phẩm
    console.log('imgURL:', imgURL); // Kiểm tra giá trị imgURL
    document.getElementById('edit-product-id').value = productId;
    document.getElementById('edit-product-name').value = productName;
    document.getElementById('edit-product-price').value = productPrice;
    document.getElementById('product-color').value = productColor;
    document.getElementById('edit-product-size').value = productSize;
    document.getElementById('edit-product-description').value = productDescription;
    document.getElementById('edit-product-imageDisplay').src = imgURL;
    // Điền thông tin danh mục vào dropdown
    var categorySelect = document.getElementById('edit-product-category');
    for (var i = 0; i < categorySelect.options.length; i++) {
        if (categorySelect.options[i].text === categoryName) {
            categorySelect.selectedIndex = i;
            break;
        }
    }
}


// thêm biến thể
let colorCounter = 1; // Đếm số lượng màu đã thêm


const addColorBtn = document.getElementById('add-color-btn');
const colorContainer = document.getElementById('color-container');


addColorBtn.addEventListener('click', () => {
    // Sử dụng số thứ tự dựa trên màu hiện tại, không dùng colorCounter
    const colorId = getNextColorId();
    const colorBlock = document.createElement('div');
    colorBlock.classList.add('color-block');
    colorBlock.setAttribute('data-color-id', colorId); // Lưu trữ ID để dễ dàng tìm và cập nhật


    colorBlock.innerHTML = `
       <h4>Màu ${colorId}</h4>
       <label for="color-name-${colorId}">Tên Màu:</label>
       <input type="text" name="color-name-${colorId}" id="color-name-${colorId}" placeholder="Nhập tên màu" required>


       <label for="image-color-${colorId}">Hình ảnh:</label>
       <div class="LoadImageContent">
           <div class="picturebox">
               <img class="imageDisplay" src="" alt="No image" />
           </div>
           <button type="button" class="loadImageBtn">Load Image</button>
           <input type="file" name="image-color-${colorId}" class="imageInput" style="display: none;" accept="image/*">
           <button type="button" class="cancelBtn">Cancel Image</button>
       </div>


       <label>Size và Số lượng:</label>
       <div class="size-container" id="size-container-${colorId}"></div>
       <button type="button" class="add-size-btn" data-color="${colorId}">Thêm Size</button>


       <button type="button" class="remove-color-btn">Xóa biến thể</button>
   `;


    colorContainer.appendChild(colorBlock);
    console.log(colorContainer.innerHTML);
    console.log(`Số lượng color-block hiện tại: `+getColorBlockCount());






    const addSizeBtn = colorBlock.querySelector('.add-size-btn');
    const sizeContainer = colorBlock.querySelector('.size-container');


    addSizeBtn.addEventListener('click', () => {
        const sizeBlock = document.createElement('div');
        sizeBlock.classList.add('size-block');
        sizeBlock.innerHTML = `
           <input type="text" name="size-${colorId}[]" placeholder="Nhập size">
           <input type="text" name="quantity-${colorId}[]" placeholder="Nhập số lượng">
           <button type="button" class="remove-size-btn">Xóa Size</button>
       `;
        sizeContainer.appendChild(sizeBlock);


        sizeBlock.querySelector('.remove-size-btn').addEventListener('click', () => {
            sizeContainer.removeChild(sizeBlock);
        });
    });






    // Load Image Logic
    const loadImageBtn = colorBlock.querySelector('.loadImageBtn');
    const cancelBtn = colorBlock.querySelector('.cancelBtn');
    const imageInput = colorBlock.querySelector('.imageInput');
    const imageDisplay = colorBlock.querySelector('.imageDisplay');


    loadImageBtn.addEventListener('click', () => {
        imageInput.click();
    });


    imageInput.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                imageDisplay.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });


    cancelBtn.addEventListener('click', () => {
        imageDisplay.src = '';
        imageInput.value = '';
    });


    // Remove Color Block and update other color blocks' IDs
    const removeColorBtn = colorBlock.querySelector('.remove-color-btn');
    removeColorBtn.addEventListener('click', () => {
        colorContainer.removeChild(colorBlock);
        updateColorIds();
        console.log(`Số lượng color-block hiện tại: ` + getColorBlockCount());
    });
});


// Lấy số ID tiếp theo dựa trên màu hiện tại
function getNextColorId() {
    const colorBlocks = colorContainer.querySelectorAll('.color-block');
    return colorBlocks.length + 1;
}


// Cập nhật lại ID của các màu còn lại
function updateColorIds() {
    const colorBlocks = colorContainer.querySelectorAll('.color-block');


    colorBlocks.forEach((block, index) => {
        const colorId = index + 1; // ID mới sẽ là vị trí của block trong danh sách
        block.querySelector('h4').textContent = `Màu ${colorId}`;


        // Cập nhật lại các input name và id
        const inputs = block.querySelectorAll('input');
        inputs.forEach(input => {
            const name = input.name;
            const id = input.id;
            if (name) input.name = name.replace(/\d+/, colorId); // Thay đổi tên
            if (id) input.id = id.replace(/\d+/, colorId); // Thay đổi ID
        });


        block.setAttribute('data-color-id', colorId); // Cập nhật lại thuộc tính data-color-id
    });
}

function getColorBlockCount() {
    return document.querySelectorAll('.color-block').length;
}


// //thêm biến thể phần edit sản phẩm
//  let colorCounterEdit = 1; // Đếm số lượng màu đã thêm trong chế độ Edit
//
//  const addColorBtnEdit = document.getElementById('add-color-btnEdit');
//  const colorContainerEdit = document.getElementById('color-container-edit');
//
//  // Lắng nghe sự kiện "click" trên colorContainerEdit
//  colorContainerEdit.addEventListener('click', (event) => {
//
//      // Thêm Size
//      if (event.target && event.target.classList.contains('add-size-btnEdit')) {
//          const colorIdEdit = event.target.getAttribute('data-color');
//          const sizeContainerEdit = document.getElementById(`size-container-${colorIdEdit}`);
//          const sizeBlockEdit = document.createElement('div');
//          sizeBlockEdit.classList.add('size-block');
//          sizeBlockEdit.innerHTML = `
//          <input type="text" name="size-${colorIdEdit}[]" placeholder="Nhập size">
//          <input type="text" name="quantity-${colorIdEdit}[]" placeholder="Nhập số lượng">
//          <button type="button" class="remove-size-btnEdit">Xóa Size</button>
//      `;
//          sizeContainerEdit.appendChild(sizeBlockEdit);
//      }
//
//      // Xóa size
//      if (event.target && event.target.classList.contains('remove-size-btnEdit')) {
//          const sizeBlockEdit = event.target.closest('.size-block');
//          sizeBlockEdit.parentElement.removeChild(sizeBlockEdit);
//      }
//
//      // Xóa biến thể
//      if (event.target && event.target.classList.contains('remove-color-btnEdit')) {
//          const colorBlockEdit = event.target.closest('.color-block');
//          colorContainerEdit.removeChild(colorBlockEdit);
//          updateColorIdsEdit(); // Cập nhật lại ID màu sau khi xóa
//      }
//
//      // Load hình ảnh
//      if (event.target && event.target.classList.contains('loadImageBtnEdit')) {
//          const colorBlockEdit = event.target.closest('.color-block');
//          const imageInputEdit = colorBlockEdit.querySelector('.imageInput');
//          imageInputEdit.click(); // Kích hoạt input file
//      }
//
//      // Hủy hình ảnh
//      if (event.target && event.target.classList.contains('cancelBtnEdit')) {
//          const colorBlockEdit = event.target.closest('.color-block');
//          const imageDisplayEdit = colorBlockEdit.querySelector('.imageDisplay');
//          const imageInputEdit = colorBlockEdit.querySelector('.imageInput');
//          imageDisplayEdit.src = '';
//          imageInputEdit.value = ''; // Reset image
//      }
//  });
//
//  // Lắng nghe sự kiện "change" cho input file (load image)
//  colorContainerEdit.addEventListener('change', (event) => {
//      if (event.target && event.target.classList.contains('imageInput')) {
//          const file = event.target.files[0];
//          if (file) {
//              const reader = new FileReader();
//              reader.onload = (e) => {
//                  const colorBlockEdit = event.target.closest('.color-block');
//                  const imageDisplayEdit = colorBlockEdit.querySelector('.imageDisplay');
//                  imageDisplayEdit.src = e.target.result;
//              };
//              reader.readAsDataURL(file);
//          }
//      }
//  });
//
//  // Khi nhấn nút 'Thêm Biến thể' để thêm một màu mới
//  addColorBtnEdit.addEventListener('click', () => {
//      const colorIdEdit = getNextColorIdEdit();
//      const colorBlockEdit = document.createElement('div');
//      colorBlockEdit.classList.add('color-block');
//      colorBlockEdit.setAttribute('data-color-id', colorIdEdit);
//
//      colorBlockEdit.innerHTML = `
//      <h4>Màu ${colorIdEdit}</h4>
//      <label for="color-name-${colorIdEdit}">Tên Màu:</label>
//      <input type="text" name="color-name-${colorIdEdit}" id="color-name-${colorIdEdit}" placeholder="Nhập tên màu" required>
//
//      <label for="image-color-${colorIdEdit}">Hình ảnh:</label>
//      <div class="LoadImageContent">
//          <div class="picturebox">
//              <img class="imageDisplay" src="" alt="No image" />
//          </div>
//          <button type="button" class="loadImageBtn">Load Image</button>
//          <input type="file" name="image-color-${colorIdEdit}" class="imageInput" style="display: none;" accept="image/*">
//          <button type="button" class="cancelBtn">Cancel Image</button>
//      </div>
//
//      <label>Size và Số lượng:</label>
//      <div class="size-container" id="size-container-${colorIdEdit}"></div>
//      <button type="button" class="add-size-btnEdit" data-color="${colorIdEdit}">Thêm Size</button>
//
//      <button type="button" class="remove-color-btnEdit">Xóa biến thể</button>
//  `;
//
//      colorContainerEdit.appendChild(colorBlockEdit);
//  });
//
//
//
//
//
// // Hàm lấy số ID tiếp theo dựa trên màu hiện tại (Edit)
//  function getNextColorIdEdit() {
//      const colorBlocksEdit = colorContainerEdit.querySelectorAll('.color-block');
//      return colorBlocksEdit.length + 1;
//  }
//
//  // Cập nhật lại ID của các màu còn lại (Edit)
//  function updateColorIdsEdit() {
//      const colorBlocksEdit = colorContainerEdit.querySelectorAll('.color-block');
//
//      colorBlocksEdit.forEach((block, index) => {
//          const colorIdEdit = index + 1;
//          block.querySelector('h4').textContent = `Màu ${colorIdEdit}`;
//          block.setAttribute('data-color-id', colorIdEdit);
//          block.querySelector('label[for^="color-name"]').setAttribute('for', `color-name-${colorIdEdit}`);
//          block.querySelector('input[name^="color-name"]').setAttribute('name', `color-name-${colorIdEdit}`);
//          block.querySelector('input[name^="size-"]').setAttribute('name', `size-${colorIdEdit}[]`);
//          block.querySelector('input[name^="quantity-"]').setAttribute('name', `quantity-${colorIdEdit}[]`);
//      });
//  }
//


let colorCounterEdit = 1; // Đếm số lượng màu đã thêm trong chế độ Edit

const addColorBtnEdit = document.getElementById('add-color-btnEdit');
const colorContainerEdit = document.getElementById('color-container-edit');

// Lắng nghe sự kiện "click" trên colorContainerEdit
colorContainerEdit.addEventListener('click', (event) => {

    // Thêm Size
    if (event.target && event.target.classList.contains('add-size-btnEdit')) {
        const colorIdEdit = event.target.getAttribute('data-color');
        const sizeContainerEdit = document.getElementById(`size-container-${colorIdEdit}`);
        const sizeBlockEdit = document.createElement('div');
        sizeBlockEdit.classList.add('size-block');
        sizeBlockEdit.innerHTML = `
            <input type="text" name="size-${colorIdEdit}[]" placeholder="Nhập size">
            <input type="text" name="quantity-${colorIdEdit}[]" placeholder="Nhập số lượng">
            <button type="button" class="remove-size-btnEdit">Xóa Size</button>
        `;
        sizeContainerEdit.appendChild(sizeBlockEdit);
    }

    // Xóa size
    if (event.target && event.target.classList.contains('remove-size-btnEdit')) {
        const sizeBlockEdit = event.target.closest('.size-block');
        sizeBlockEdit.parentElement.removeChild(sizeBlockEdit);
    }

    // Xóa biến thể
    if (event.target && event.target.classList.contains('remove-color-btnEdit')) {
        const colorBlockEdit = event.target.closest('.color-block');
        colorContainerEdit.removeChild(colorBlockEdit);
        updateColorIdsEdit(); // Cập nhật lại ID màu sau khi xóa
    }

    // Load hình ảnh
    if (event.target && event.target.classList.contains('loadImageBtnEdit')) {
        const colorBlockEdit = event.target.closest('.color-block');
        const imageInputEdit = colorBlockEdit.querySelector('.imageInput');
        imageInputEdit.click(); // Kích hoạt input file
    }

    // Hủy hình ảnh
    if (event.target && event.target.classList.contains('cancelBtnEdit')) {
        const colorBlockEdit = event.target.closest('.color-block');
        const imageDisplayEdit = colorBlockEdit.querySelector('.imageDisplay');
        const imageInputEdit = colorBlockEdit.querySelector('.imageInput');
        imageDisplayEdit.src = '';
        imageInputEdit.value = ''; // Reset image
    }
});

// Lắng nghe sự kiện "change" cho input file (load image)
colorContainerEdit.addEventListener('change', (event) => {
    if (event.target && event.target.classList.contains('imageInput')) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                const colorBlockEdit = event.target.closest('.color-block');
                const imageDisplayEdit = colorBlockEdit.querySelector('.imageDisplay');
                imageDisplayEdit.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    }
});

// Khi nhấn nút 'Thêm Biến thể' để thêm một màu mới
addColorBtnEdit.addEventListener('click', () => {
    const colorIdEdit = getNextColorIdEdit();
    const colorBlockEdit = document.createElement('div');
    colorBlockEdit.classList.add('color-block');
    colorBlockEdit.setAttribute('data-color-id', colorIdEdit);

    colorBlockEdit.innerHTML = `
        <h4>Màu ${colorIdEdit}</h4>
        <label for="color-name-${colorIdEdit}">Tên Màu:</label>
        <input type="text" name="color-name-${colorIdEdit}" id="color-name-${colorIdEdit}" placeholder="Nhập tên màu" required>

        <label for="image-color-${colorIdEdit}">Hình ảnh:</label>
        <div class="LoadImageContent">
            <div class="picturebox">
                <img class="imageDisplay" src="" alt="No image" />
            </div>
            <button type="button" class="loadImageBtnEdit action-btn">Load Image</button>
            <input type="file" name="image-color-${colorIdEdit}" class="imageInput" style="display: none;" accept="image/*">
            <button type="button" class="cancelBtnEdit action-btn">Cancel Image</button>
        </div>

        <label>Size và Số lượng:</label>
        <div class="size-container" id="size-container-${colorIdEdit}"></div>
        <button type="button" class="add-size-btnEdit" data-color="${colorIdEdit}">Thêm Size</button>

        <button type="button" class="remove-color-btnEdit">Xóa biến thể</button>
    `;

    colorContainerEdit.appendChild(colorBlockEdit);
});

// Hàm lấy số ID tiếp theo dựa trên màu hiện tại (Edit)
function getNextColorIdEdit() {
    const colorBlocksEdit = colorContainerEdit.querySelectorAll('.color-block');
    return colorBlocksEdit.length + 1;
}

// Cập nhật lại ID của các màu còn lại (Edit)
function updateColorIdsEdit() {
    const colorBlocksEdit = colorContainerEdit.querySelectorAll('.color-block');

    colorBlocksEdit.forEach((block, index) => {
        const colorIdEdit = index + 1;
        block.querySelector('h4').textContent = `Màu ${colorIdEdit}`;
        block.setAttribute('data-color-id', colorIdEdit);
        block.querySelector('label[for^="color-name"]').setAttribute('for', `color-name-${colorIdEdit}`);
        block.querySelector('input[name^="color-name"]').setAttribute('name', `color-name-${colorIdEdit}`);
        block.querySelector('input[name^="size-"]').setAttribute('name', `size-${colorIdEdit}[]`);
        block.querySelector('input[name^="quantity-"]').setAttribute('name', `quantity-${colorIdEdit}[]`);
    });
}




