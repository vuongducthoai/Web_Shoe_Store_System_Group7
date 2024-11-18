const url = window.location.href; // Lấy toàn bộ URL
const emailMatch = url.match(/email=([^&]+)/); // Tìm email trong URL
const email = emailMatch ? decodeURIComponent(emailMatch[1]) : null; // Giải mã và lấy giá trị email

if (email) {
    document.getElementById('email').value = email;
} else {
    console.error("Email parameter is missing or invalid");
}
