function convertToast(headerContent, bodyContent, toastType) {
    const toastContainer = document.createElement('div');
    toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';

    // Thêm HTML cho toast
    toastContainer.innerHTML = `
      <div id="dynamicToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="5000" data-bs-autohide="true">
          <div class="toast-header ${getToastClass(toastType)}">
              <strong class="me-auto">${headerContent}</strong>
              <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
          </div>
          <div class="toast-body">
              ${bodyContent}
          </div>
      </div>
  `;

    // Thêm vào DOM
    document.body.appendChild(toastContainer);

    // Khởi chạy toast
    const toastElement = document.getElementById('dynamicToast');
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
}

// Hàm xác định class header dựa trên loại toast
function getToastClass(type) {
    switch (type) {
        case 'success': return 'bg-success text-white';
        case 'error': return 'bg-danger text-white';
        case 'warning': return 'bg-warning text-dark';
        case 'info': return 'bg-info text-white';
        default: return 'bg-secondary text-white';
    }
}
function abc(e){
    convertToast('Success', 'This is a success message!', 'success');
}