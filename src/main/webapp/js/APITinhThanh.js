var citis = document.getElementById("city");
var districts = document.getElementById("district");
var wards = document.getElementById("ward");
var Parameter = {
    url: "https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json",
    method: "GET",
    responseType: "application/json",
};
var promise = axios(Parameter);
promise.then(function (result) {
    renderCity(result.data);
});

function renderCity(data) {
    var selectedProvince = '<c:out value="${customerDTO.getAddressDTO().getProvince()}" />';
    alert(selectedProvince);
    for (const x of data) {
        var option = new Option(x.Name, x.Name);
        // Kiểm tra xem tỉnh nào được chọn và đánh dấu nó
        if (x.Name === selectedProvince) {
            option.selected = true;  // Chọn tỉnh nếu khớp
        }
        citis.options[citis.options.length] = option;
    }
    citis.onchange = function () {
        district.length = 1;
        ward.length = 1;
        if(this.value != ""){
            const result = data.filter(n => n.Name === this.value);

            for (const k of result[0].Districts) {
                district.options[district.options.length] = new Option(k.Name, k.Name);
            }
        }
    };
    district.onchange = function () {
        ward.length = 1;
        const dataCity = data.filter((n) => n.Name === citis.value);
        if (this.value != "") {
            const dataWards = dataCity[0].Districts.filter(n => n.Name === this.value)[0].Wards;

            for (const w of dataWards) {
                wards.options[wards.options.length] = new Option(w.Name, w.Name);
            }
        }
    };
}