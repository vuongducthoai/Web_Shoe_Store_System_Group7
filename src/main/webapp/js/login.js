function validateForm() {
    const captchaResponse = grecaptcha.getResponse();
    if (captchaResponse.trim() === "") {
        document.getElementById('captcha-message').style.display = 'block';
        return false;
    }
    document.getElementById('captcha-message').style.display = 'none';
    return true;
}


const emailField = document.getElementById("email");
const passwordField = document.getElementById("password");

const errorMessage = document.querySelector(".error-message");

emailField.addEventListener("focus", () => {
    if (errorMessage) {
        errorMessage.style.display = "none"; // Ẩn thông báo lỗi
    }
});

passwordField.addEventListener("focus", () => {
    if (errorMessage) {
        errorMessage.style.display = "none"; // Ẩn thông báo lỗi
    }
});
