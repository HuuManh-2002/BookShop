// ====================================
// 1. KHỞI TẠO VÀ KIỂM TRA TRẠNG THÁI (INITIALIZATION & STATE CHECK)
// ====================================

// Thêm CSS Animation cho Notification (Chạy sớm)
const style = document.createElement("style");
style.textContent = `
    @keyframes slideIn {
        from { transform: translateX(400px); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(400px); opacity: 0; }
    }
`;
document.head.appendChild(style);

/**
 * @description Kiểm tra trạng thái đăng nhập của người dùng khi trang được tải.
 * Nếu có token, hiển thị User Dropdown và ẩn nút Đăng nhập.
 */
function checkLoginStatus() {
    const token = sessionStorage.getItem("token");
    const lastName = sessionStorage.getItem("lastName");

    if (token) {
        // User is logged in
        const loginBtn = document.getElementById("loginButton");
        const userDrop = document.getElementById("userDropdown");
        const userName = document.getElementById("userName");

        if (loginBtn) loginBtn.style.display = "none";
        if (userDrop) userDrop.style.display = "inline-block";
        if (userName) userName.textContent = lastName;
    }
}

/**
 * @description Đặt vị trí top động cho thanh điều hướng (navbar) để nó nằm ngay dưới header,
 * phục vụ cho thuộc tính `position: sticky`.
 */
function setNavbarPosition() {
    const mainHeader = document.querySelector(".main-header");
    const mainNav = document.getElementById("mainNav");

    if (mainHeader && mainNav) {
        const headerHeight = mainHeader.offsetHeight;
        mainNav.style.top = headerHeight + "px";
    }
}

// Chạy các hàm khởi tạo khi DOM đã tải xong
window.addEventListener("DOMContentLoaded", function () {
    checkLoginStatus();
    setNavbarPosition();
});

// Cập nhật lại vị trí navbar khi cửa sổ trình duyệt thay đổi kích thước
window.addEventListener("resize", setNavbarPosition);

// ====================================
// 2. XÁC THỰC VÀ XỬ LÝ FORM (AUTHENTICATION & FORM HANDLING)
// ====================================

/**
 * @description Hiển thị modal Đăng nhập/Đăng ký, đảm bảo reset về form Đăng nhập trước.
 */
function showLoginModal() {
    // 1. Reset về form Đăng nhập trước khi hiển thị Modal
    switchToLogin();

    // 2. Hiển thị Modal
    const modalElement = document.getElementById("authModal");
    if (modalElement && typeof bootstrap !== 'undefined' && bootstrap.Modal) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
    }
}

/**
 * @description Chuyển đổi giao diện trong modal sang form Đăng ký.
 */
function switchToRegister() {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");
    const modalTitle = document.getElementById("modalTitle");
    
    if (loginForm) loginForm.style.display = "none";
    if (registerForm) registerForm.style.display = "block";
    if (modalTitle) modalTitle.innerHTML = '<i class="fas fa-user-plus me-2"></i>Đăng ký';
}

/**
 * @description Chuyển đổi giao diện trong modal sang form Đăng nhập.
 */
function switchToLogin() {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");
    const modalTitle = document.getElementById("modalTitle");

    if (registerForm) registerForm.style.display = "none";
    if (loginForm) loginForm.style.display = "block";
    if (modalTitle) modalTitle.innerHTML = '<i class="fas fa-sign-in-alt me-2"></i>Đăng nhập';
}

/**
 * @description Xử lý đăng nhập bằng cách gửi dữ liệu qua API.
 */
document
    .getElementById("loginForm")
    ?.addEventListener("submit", async function (e) {
        e.preventDefault();

        const email = this.querySelector('input[type="email"]').value;
        const password = this.querySelector('input[type="password"]').value;

        try {
            const response = await fetch("http://localhost:8000/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ email: email, password: password }),
            });

            const data = await response.json();
            console.log("Login Response:", data);
            
            if (response.ok && data.result) {
                // Thành công: Lưu session, cập nhật UI và đóng modal
                sessionStorage.setItem("token", data.result.token);
                sessionStorage.setItem("lastName", data.result.lastName);

                showNotification("Đăng nhập thành công!", "success");
                
                checkLoginStatus(); // Cập nhật UI

                // Đóng modal sau 1 giây
                setTimeout(() => {
                    const modalElement = document.getElementById("authModal");
                    if (modalElement && bootstrap.Modal.getInstance(modalElement)) {
                        bootstrap.Modal.getInstance(modalElement).hide();
                    }
                }, 1000);

            } else {
                // Lỗi từ Server (4xx/5xx)
                const errorMessage = data.message || "Tài khoản hoặc mật khẩu không chính xác!";
                showNotification(`Lỗi: ${errorMessage}`, "error");
            }
        } catch (error) {
            // Lỗi mạng
            console.error("Lỗi mạng khi đăng nhập:", error);
            showNotification("Lỗi kết nối! Vui lòng thử lại.", "error");
        }
    });

/**
 * @description Xử lý đăng ký người dùng mới bằng cách gửi FormData có chứa JSON (userRequest).
 */
document
    .getElementById("registerForm")
    ?.addEventListener("submit", async function (e) {
        e.preventDefault();

        const password = this.querySelector('input[name="password"]').value;
        const confirmPassword = this.querySelector('input[name="confirm-password"]').value;
        const firstName = this.querySelector('input[name="firstName"]').value;
        const lastName = this.querySelector('input[name="lastName"]').value;
        const email = this.querySelector('input[type="email"]').value;
        const phoneNumber = this.querySelector('input[name="phoneNumber"]').value;

        if (password !== confirmPassword) {
            showNotification("Mật khẩu xác nhận không khớp!", "error");
            return;
        }

        // 1. Chuẩn bị userRequest JSON
        const userRequest = JSON.stringify({
            email: email,
            password: password,
            firstName: firstName,
            lastName: lastName,
            phoneNumber: phoneNumber,
        });

        // 2. Tạo FormData và Blob
        const formData = new FormData();
        const userRequestBlob = new Blob([userRequest], {
            type: "application/json",
        });

        formData.append("userRequest", userRequestBlob);

        // [Lưu ý: AvatarFile được comment out]

        try {
            const response = await fetch("http://localhost:8000/user", {
                method: "POST",
                body: formData, // Trình duyệt tự đặt Content-Type: multipart/form-data
            });

            const data = await response.json();
            console.log("Register Response:", data);
            
            if (response.ok) {
                showNotification("Đăng ký thành công!", "success");
                this.reset();
                setTimeout(() => {
                    switchToLogin(); // Chuyển sang đăng nhập sau khi đăng ký
                }, 1000);
            } else {
                const errorMessage = data.message || "Tạo tài khoản thất bại!";
                showNotification(`Lỗi: ${errorMessage}`, "error");
            }
        } catch (error) {
            console.error("Lỗi đăng ký:", error);
            showNotification("Lỗi kết nối! Vui lòng thử lại.", "error");
        }
    });

/**
 * @description Thực hiện đăng xuất người dùng bằng cách gọi API và xóa session.
 */
async function logout() {
    const token = sessionStorage.getItem("token");

    try {
        const response = await fetch("http://localhost:8000/auth/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ token: token }),
        });

        const data = await response.json();
        console.log("Logout Response:", data); 

        if (response.ok) {
            // Xóa session và cập nhật UI
            sessionStorage.removeItem("token");
            sessionStorage.removeItem("lastName");

            checkLoginStatus(); // Cập nhật UI (Ẩn dropdown, hiện nút login)

            showNotification("Đăng xuất thành công!", "info");
            // Điều hướng về trang chủ
            window.location.href = "/fe/home/home.html";
        } else {
            showNotification(`Lỗi đăng xuất!`, "error");
        }
    } catch (error) {
        // Lỗi mạng
        console.error("Lỗi mạng khi đăng xuất:", error);
        showNotification(
            "Lỗi kết nối! Vui lòng kiểm tra mạng và thử lại.",
            "error"
        );
    }
}


// ====================================
// 3. UI/UX VÀ HIỆU ỨNG (UI/UX & EFFECTS)
// ====================================

/**
 * @description Chuyển đổi loại input giữa 'password' và 'text' để hiện/ẩn mật khẩu.
 * @param {HTMLElement} icon - Biểu tượng mắt (`<i>`) được click.
 */
function togglePassword(icon) {
    const input = icon.previousElementSibling;
    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove("fa-eye");
        icon.classList.add("fa-eye-slash");
    } else {
        input.type = "password";
        icon.classList.remove("fa-eye-slash");
        icon.classList.add("fa-eye");
    }
}

/**
 * @description Xử lý hành vi "Thêm vào giỏ" ảo (chuyển trạng thái nút tạm thời).
 */
document.querySelectorAll(".add-to-cart-btn").forEach((button) => {
    button.addEventListener("click", function () {
        this.innerHTML = '<i class="fas fa-check me-2"></i>Đã thêm';
        this.style.background = "#2ecc71";
        // Reset sau 2 giây
        setTimeout(() => {
            this.innerHTML = '<i class="fas fa-shopping-cart me-2"></i>Thêm vào giỏ';
            this.style.background = "#667eea";
        }, 2000);
    });
});

/**
 * @description Xử lý cuộn mượt mà đến các section khi click vào liên kết neo (anchor link)
 * và đánh dấu liên kết đó là 'active'.
 */
document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
        const targetId = this.getAttribute("href");
        if (targetId === "#") return;
        
        e.preventDefault(); // Ngăn chặn hành vi cuộn mặc định

        // Cập nhật class 'active'
        document
            .querySelectorAll(".navbar-custom .nav-link")
            .forEach((link) => {
                link.classList.remove("active");
            });
        this.classList.add("active");

        const target = document.querySelector(targetId);
        if (target) {
            // Tính toán offset để tránh header/navbar che mất nội dung
            const headerHeight = document.querySelector(".main-header")?.offsetHeight || 0;
            const navbarHeight = document.querySelector(".navbar-custom")?.offsetHeight || 0;
            const offset = headerHeight + navbarHeight + 20; 
            
            const targetPosition = target.offsetTop - offset;

            window.scrollTo({
                top: targetPosition,
                behavior: "smooth",
            });
        }
    });
});

/**
 * @description Đánh dấu liên kết điều hướng là 'active' dựa trên section đang hiển thị trên màn hình.
 */
window.addEventListener("scroll", function () {
    const sections = document.querySelectorAll("section[id]");
    
    const headerHeight = document.querySelector(".main-header")?.offsetHeight || 0;
    const navbarHeight = document.querySelector(".navbar-custom")?.offsetHeight || 0;
    const offset = headerHeight + navbarHeight + 100; // Offset để cuộn qua header/navbar
    
    const scrollPosition = window.scrollY + offset;

    sections.forEach((section) => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.offsetHeight;
        const sectionId = section.getAttribute("id");

        if (
            scrollPosition >= sectionTop &&
            scrollPosition < sectionTop + sectionHeight
        ) {
            document
                .querySelectorAll(".navbar-custom .nav-link")
                .forEach((link) => {
                    link.classList.remove("active");
                    if (link.getAttribute("href") === `#${sectionId}`) {
                        link.classList.add("active");
                    }
                });
        }
    });
});

// ====================================
// 4. TIỆN ÍCH CHUNG (UTILITIES)
// ====================================

/**
 * @description Hiển thị thông báo nổi (Toast/Notification) tùy chỉnh.
 * @param {string} message - Nội dung thông báo.
 * @param {('success'|'error'|'info')} type - Loại thông báo để xác định màu nền.
 */
function showNotification(message, type) {
    // Tạo element thông báo
    const notification = document.createElement("div");
    
    // Thiết lập style cơ bản
    notification.style.cssText = `
        position: fixed; top: 20px; right: 20px; padding: 15px 25px;
        border-radius: 10px; color: white; font-weight: bold; z-index: 9999;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15); animation: slideIn 0.3s ease-out;
    `;
    notification.textContent = message;

    // Thiết lập màu nền theo loại
    if (type === "success") {
        notification.style.background = "linear-gradient(135deg, #2ecc71 0%, #27ae60 100%)"; // Xanh lá
    } else if (type === "error") {
        notification.style.background = "linear-gradient(135deg, #e74c3c 0%, #c0392b 100%)"; // Đỏ
    } else if (type === "info") {
        notification.style.background = "linear-gradient(135deg, #3498db 0%, #2980b9 100%)"; // Xanh dương
    } else {
        notification.style.background = "linear-gradient(135deg, #95a5a6 0%, #7f8c8d 100%)"; // Xám
    }

    // Thêm vào body
    document.body.appendChild(notification);

    // Tự động xóa sau 2 giây
    setTimeout(() => {
        notification.style.animation = "slideOut 0.3s ease-in";
        notification.addEventListener("animationend", () => {
            if (notification.parentNode) {
                document.body.removeChild(notification);
            }
        });
    }, 2000);
}