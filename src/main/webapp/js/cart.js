function showError(errCode,message)
{
    if (errCode == 1) {
        custom_toast({
            title: "Chúng tôi rất xin lỗi vì sự bất tiện",
            message: message,
            type: "error",
            duration: 6000
        });
    }
}

function custom_toast({ title = "", message = "", type = "success", duration = 1000 }) {
    const main = document.querySelector("#custom-toast");
    if (main) {
        const toast = document.createElement("div");
        console.log(duration)
        // Auto remove toast
        const autoRemoveId = setTimeout(() => main.removeChild(toast), duration + 1000);

        toast.onclick = (e) => {
            if (e.target.closest(".toast__close")) {
                main.removeChild(toast);
                clearTimeout(autoRemoveId);
            }
        };


        const icons = {
            success: "fas fa-check-circle",
            info: "fas fa-info-circle",
            warning: "fas fa-exclamation-circle",
            error: "fas fa-exclamation-circle"
        };
        const icon = icons[type];
        const delay = (duration / 1000).toFixed(2);

        toast.classList.add("toast_custom", `toast--${type}`);
        toast.style.animation = `slideInLeft ease .3s, fadeOut linear 1s ${delay}s forwards !important`;

        toast.innerHTML = `
            <div class="toast__icon">
                <i class="${icon}"></i>
            </div>
            <div class="toast__body">
                <h3 class="toast__title">${title}</h3>
                <p class="toast__msg">${message}</p>
            </div>
            <div class="toast__close">
                <i class="fas fa-times"></i>
            </div>
        `;
        main.appendChild(toast);
    }
}

let productID = document.querySelector(".inputID");
let quantity = document.querySelector(".inpQuantity");
let AddItemQuantity=()=>{
    AddItemWithQuantity(productID.value,quantity.value)
}

function ChangeSelect() {
    const selectElement = document.querySelector('select[name="voucher"]');
    const selectedValue = selectElement.value;

    // Tạo form và gửi request đến servlet
    const form = document.createElement('form');
    form.method = 'POST';  // Hoặc 'POST' nếu cần
    form.action = '/Cart'; // URL của servlet

    // Thêm dữ liệu lựa chọn vào form
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'idPr';
    input.value = selectedValue;
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}
