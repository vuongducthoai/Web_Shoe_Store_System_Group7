const url = window.location.href; // Lấy toàn bộ URL
const emailMatch = url.match(/email=([^&]+)/);
const email = emailMatch ? decodeURIComponent(emailMatch[1]) : null;

if (email) {
    document.getElementById('email').value = email;
} else {
    console.error("Email parameter is missing or invalid");
}
