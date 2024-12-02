    var startDate = "<%= request.getAttribute('startDate') %>"; // Chuyển từ Java Servlet
    alert(startDate);
    var endDate = "<%= request.getAttribute('endDate') %>"; // Chuyển từ Java Servlet

    if (startDate && endDate) {
    // Chuyển startDate và endDate sang định dạng Date trong JavaScript
    var start = new Date(startDate);  // JavaScript sẽ tự hiểu định dạng ISO 8601
    var end = new Date(endDate);
    // Hàm tính toán đồng hồ đếm ngược
    function updateCountdown() {
        var now = new Date().getTime();
        var distance = end - now;

        // Tính toán thời gian còn lại
        var days = Math.floor(distance / (1000 * 60 * 60 * 24));
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);

        // Hiển thị đồng hồ đếm ngược
        document.getElementById("countdown").innerHTML = days + "d " + hours + "h " + minutes + "m " + seconds + "s ";

        // Khi đồng hồ đếm ngược hết
        if (distance < 0) {
            clearInterval(x);
            document.getElementById("countdown").innerHTML = "EXPIRED";
        }
    }

    // Cập nhật đồng hồ đếm ngược mỗi giây
    var x = setInterval(updateCountdown, 1000);
}
