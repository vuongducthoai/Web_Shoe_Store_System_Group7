function showSuccessToast() {
    alert("123333");
    custom_toast({
        title: "Welcome back!",
        message: "We're so happy you're back! 😊 Let's make today amazing!",
        type: "success",
        duration: 2000
    });
}

function showErrorToast() {
    custom_toast({
        title: "We are very sorry for not satisfying you",
        message: "Please accept my apologies for the lack of deliciousness in the food",
        type: "error",
        duration: 1000
    });
}

function custom_toast({ title = "", message = "", type = "success", duration = 1000 }) {
    const main = document.querySelector("#custom-toast");
    if (main) {
        const toast = document.createElement("div");

        // Auto remove toast
        const autoRemoveId = setTimeout(() => main.removeChild(toast), duration + 300);

        toast.onclick = (e) => {
            if (e.target.closest(".toast__close")) {
                main.removeChild(toast);
                clearTimeout(autoRemoveId);
            }
        };

        alert("123");

        const icons = {
            success: "fas fa-check-circle",
            info: "fas fa-info-circle",
            warning: "fas fa-exclamation-circle",
            error: "fas fa-exclamation-circle"
        };
        const icon = icons[type];
        const delay = (duration / 1000).toFixed(2);

        toast.classList.add("toast", `toast--${type}`);
        toast.style.animation = `slideInLeft ease .3s, fadeOut linear 1s ${delay}s forwards`;

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
