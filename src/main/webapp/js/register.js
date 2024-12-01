function validateForm() {
    // Ẩn tất cả thông báo lỗi ban đầu
    document.querySelectorAll(".error-message").forEach(element => {
        element.style.display = "none";
    });

    let isValid = true;
    let firstErrorElement = null;

    const fullName = document.getElementById('fullName').value.trim();
    const email = document.getElementById('email').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('re-password').value.trim();

    // Regular Expression
    const fullnameRegex = /^[a-zA-Z\u00C0-\u1EF9\u0300-\u036F\s]+$/i;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d).{6,}$/;
    const phoneRegex = /^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$/;

    // Kiểm tra họ và tên
    const nameParts = fullName.split(" ");
    if (fullName === "") {
        document.getElementById('fullname-error').textContent = 'Họ và Tên không được để trống.';
        document.getElementById('fullname-error').style.display = 'block';
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('fullName');
        }
        isValid = false;
    } else if (nameParts.length < 2 || !nameParts.every(part => fullnameRegex.test(part))) {
        document.getElementById('fullname-error').textContent = 'Họ và tên phải có ít nhất 2 từ, mỗi từ viết hoa chữ cái đầu tiên.';
        document.getElementById('fullname-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('fullName');
        }
    }

    // Kiểm tra email
    if (email === "") {
        document.getElementById('email-error').textContent = 'Email không được để trống.';
        document.getElementById('email-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('email');
        }
    } else if (!emailRegex.test(email)) {
        document.getElementById('email-error').textContent = 'Email phải đúng định dạng (vd: thoai@gmail.com, ...)';
        document.getElementById('email-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('email');
        }
    }

    // Kiểm tra số điện thoại
    if (phone === "") {
        document.getElementById('phone-error').textContent = 'Số điện thoại không được để trống.';
        document.getElementById('phone-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('phone');
        }
    } else if (!phoneRegex.test(phone)) {
        document.getElementById('phone-error').textContent = 'Số điện thoại phải đúng định dạng 10 số (vd: 0859716818, ...)';
        document.getElementById('phone-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('phone');
        }
    }

    // Kiểm tra mật khẩu
    if (password === "") {
        document.getElementById('password-error').textContent = 'Mật khẩu không được để trống.';
        document.getElementById('password-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('password');
        }
    } else if (!passwordRegex.test(password)) {
        document.getElementById('password-error').textContent = 'Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ cái và số.';
        document.getElementById('password-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('password');
        }
    }

    // Kiểm tra xác nhận mật khẩu
    if (confirmPassword === "") {
        document.getElementById('re-password-error').textContent = "Nhập lại mật khẩu không được để trống.";
        document.getElementById('re-password-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('re-password');
        }
    } else if (password !== confirmPassword) {
        document.getElementById('re-password-error').textContent = "Mật khẩu không khớp.";
        document.getElementById('re-password-error').style.display = 'block';
        isValid = false;
        if (!firstErrorElement) {
            firstErrorElement = document.getElementById('re-password');
        }
    }

    // Kiểm tra ngày sinh
    const day = parseInt(document.querySelector('select[name="day"]').value);
    const month = parseInt(document.querySelector('select[name="month"]').value);
    const year = parseInt(document.querySelector('select[name="year"]').value);

    if (!day || !month || !year) {
        document.getElementById('birthDay-error').textContent = "Vui lòng chọn đầy đủ ngày, tháng và năm sinh.";
        document.getElementById('birthDay-error').style.display = 'block';
        if (!firstErrorElement) {
            firstErrorElement = document.querySelector('select[name="day"]');
        }
        isValid = false;
    } else {
        const today = new Date();
        const birthDay = new Date(year, month - 1, day);
        const age = today.getFullYear() - birthDay.getFullYear();
        const monthDiff = today.getMonth() - birthDay.getMonth();
        const isOlderThan16 = age > 16 || (age === 16 && (monthDiff > 0 || (monthDiff === 0 && today.getDate() >= birthDay.getDate())));

        if (!isOlderThan16) {
            document.getElementById('birthDay-error').textContent = "Bạn phải trên 16 tuổi.";
            document.getElementById('birthDay-error').style.display = 'block';
            if (!firstErrorElement) {
                firstErrorElement = document.querySelector('select[name="day"]');
            }
            isValid = false;
        }
    }

    const housrNumber = parseInt(document.getElementById('houseNumber').value);
    const streetName = document.getElementById('streetName').value;
    if(!housrNumber || !streetName){
        document.getElementById('streetName-error').style.display = 'block';
        if(!firstErrorElement) {
            firstErrorElement = document.getElementById('streetName');
        }
        isValid = false;
    }

    const province = document.getElementById('city').value.trim();
    const district = document.getElementById('district').value.trim();
    const ward = document.getElementById('ward').value.trim();

        if(province === ''){
            document.getElementById('province-error').style.display = 'block';
            if(!firstErrorElement) {
                firstErrorElement = document.getElementById('city');
            }
            isValid = false;
        } else if(district === ''){
            document.getElementById('district-error').style.display = 'block';
            if(!firstErrorElement) {
                firstErrorElement = document.getElementById('district');
            }
            isValid = false;
        } else if(ward === ''){
            document.getElementById('ward-error').style.display = 'block';
            if(!firstErrorElement) {
                firstErrorElement = document.getElementById('ward');
            }
            isValid = false;
        }

    if (firstErrorElement) {
        firstErrorElement.focus();
    }

    return isValid;
}
