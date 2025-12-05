// ====================================
// 0. KHỞI TẠO VÀ CSS ANIMATION (GLOBAL SETUP)
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
 * @description Xử lý chạy các hàm khởi tạo khi DOM đã tải xong.
 * (Bao gồm cả việc tải thông tin người dùng nếu đã đăng nhập)
 */
window.addEventListener("DOMContentLoaded", function () {
    checkLoginStatus();
});

// ====================================
// 1. CHỨC NĂNG XÁC THỰC (AUTHENTICATION)
// ====================================

/**
 * @description Kiểm tra trạng thái đăng nhập, cập nhật UI Header
 * và gọi hàm tải thông tin chi tiết nếu người dùng đã đăng nhập.
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

        // **Gọi hàm tải thông tin người dùng chi tiết cho trang MyInfo**
        fetchAndDisplayUserInfo(token);
    }
}

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

            document.getElementById("loginButton").style.display =
                "inline-block";
            document.getElementById("userDropdown").style.display = "none";

            showNotification("Đăng xuất thành công!", "info");
            window.location.href = "/fe/home/home.html";
        } else {
            showNotification(`Lỗi đăng xuất!`, "error");
        }
    } catch (error) {
        console.error("Lỗi mạng khi đăng xuất:", error);
        showNotification(
            "Lỗi kết nối! Vui lòng kiểm tra mạng và thử lại.",
            "error"
        );
    }
}

/**
 * @description Xử lý form Đăng nhập.
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
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email: email, password: password }),
            });

            const data = await response.json();
            console.log(data);

            if (response.ok && data.result) {
                sessionStorage.setItem("token", data.result.token);
                sessionStorage.setItem("lastName", data.result.lastName);

                showNotification("Đăng nhập thành công!", "success");

                document.getElementById("loginButton").style.display = "none";
                document.getElementById("userDropdown").style.display =
                    "inline-block";
                document.getElementById("userName").textContent =
                    data.result.lastName;

                setTimeout(() => {
                    const modal = bootstrap.Modal.getInstance(
                        document.getElementById("authModal")
                    );
                    modal.hide();
                }, 1000);
            } else {
                showNotification("Lỗi: Tài khoản không chính xác!", "error");
            }
        } catch (error) {
            showNotification("Lỗi kết nối! Vui lòng thử lại.", "error");
        }
    });

/**
 * @description Xử lý form Đăng ký.
 */
document
    .getElementById("registerForm")
    ?.addEventListener("submit", async function (e) {
        e.preventDefault();

        const password = this.querySelector('input[name="password"]').value;
        const confirmPassword = this.querySelector(
            'input[name="confirm-password"]'
        ).value;
        const firstName = this.querySelector('input[name="firstName"]').value;
        const lastName = this.querySelector('input[name="lastName"]').value;
        const email = this.querySelector('input[type="email"]').value;
        const phoneNumber = this.querySelector(
            'input[name="phoneNumber"]'
        ).value;

        if (password !== confirmPassword) {
            alert("Mật khẩu xác nhận không khớp!");
            return;
        }

        const userRequest = JSON.stringify({
            email: email,
            password: password,
            firstName: firstName,
            lastName: lastName,
            phoneNumber: phoneNumber,
        });

        const formData = new FormData();
        const userRequestBlob = new Blob([userRequest], {
            type: "application/json",
        });
        formData.append("userRequest", userRequestBlob);

        try {
            const response = await fetch("http://localhost:8000/user", {
                method: "POST",
                body: formData,
            });

            const data = await response.json();
            console.log(data);

            if (response.ok) {
                showNotification("Đăng ký thành công!", "success");
                this.reset();
                setTimeout(() => {
                    switchToLogin();
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

// ====================================
// 2. CHỨC NĂNG THÔNG TIN CÁ NHÂN (USER INFO)
// ====================================

/**
 * @description Gọi API /user/me (GET) để lấy thông tin chi tiết người dùng
 * và sau đó gọi hàm hiển thị data vào form.
 * @param {string} token - Token xác thực được lưu trong sessionStorage.
 */
async function fetchAndDisplayUserInfo(token) {
    const PROFILE_API_URL = "http://localhost:8000/user/me";

    try {
        const response = await fetch(PROFILE_API_URL, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        });

        const data = await response.json();

        if (response.ok && data.code === 1000) {
            displayUserInfo(data.result);
        } else {
            showNotification(
                `Lỗi: ${data.message || "Không thể tải thông tin cá nhân."}`,
                "error"
            );
            // Xử lý trường hợp token hết hạn/không hợp lệ
            if (response.status === 401) {
                // Có thể thêm logic logout cứng ở đây
            }
        }
    } catch (error) {
        console.error("Fetch User Info Error:", error);
        showNotification("Lỗi kết nối khi tải dữ liệu người dùng.", "error");
    }
}

/**
 * @description Hiển thị dữ liệu người dùng vào các trường input trong form MyInfo.
 * @param {object} user - Đối tượng chứa thông tin người dùng từ API.
 */
function displayUserInfo(user) {
    // Điền dữ liệu vào form (Sử dụng Optional Chaining hoặc kiểm tra tồn tại an toàn hơn)
    const fields = ["firstName", "lastName", "email", "phoneNumber", "gender"];

    fields.forEach((id) => {
        const element = document.getElementById(id);
        if (element) {
            element.value = user[id] || "";
        }
    });

    // Xử lý trường email chỉ để đọc
    const emailElement = document.getElementById("email");
    if (emailElement) {
        emailElement.readOnly = true;
    }

    // Xử lý định dạng ngày sinh
    const dobElement = document.getElementById("dob");
    if (dobElement && user.dob) {
        // Chỉ lấy phần ngày (YYYY-MM-DD)
        dobElement.value = user.dob.split("T")[0];
    }
}

/**
 * @description Xử lý form cập nhật thông tin người dùng (userInfoForm).
 */
document
    .getElementById("userInfoForm")
    ?.addEventListener("submit", async function (e) {
        e.preventDefault();
        const token = sessionStorage.getItem("token");
        if (!token) {
            showNotification(
                "Vui lòng đăng nhập để cập nhật thông tin.",
                "error"
            );
            return;
        }

        // 1. Lấy dữ liệu
        const firstName = document.getElementById("firstName").value;
        const lastName = document.getElementById("lastName").value;
        const phoneNumber = document.getElementById("phoneNumber").value;
        const dob = document.getElementById("dob").value;
        const gender = document.getElementById("gender").value;

        // 2. Chuẩn bị userUpdate JSON
        const userUpdate = JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            phoneNumber: phoneNumber,
            dob: dob,
            gender: gender,
        });

        // 3. Tạo FormData
        const formData = new FormData();
        const userUpdateBlob = new Blob([userUpdate], {
            type: "application/json",
        });
        formData.append("userUpdate", userUpdateBlob);
        // (Bổ sung: Nếu có upload file avatar, append nó vào đây)

        try {
            const response = await fetch("http://localhost:8000/user/me", {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                body: formData,
            });

            const data = await response.json();
            console.log("Update Response:", data);

            if (response.ok) {
                showNotification(
                    "Cập nhật thông tin thành công! 🎉",
                    "success"
                );

                // Cập nhật lại lastName trên header ngay lập tức nếu thay đổi
                if (lastName !== sessionStorage.getItem("lastName")) {
                    sessionStorage.setItem("lastName", lastName);
                    document.getElementById("userName").textContent = lastName;
                }
            } else {
                const errorMessage = data.message || "Cập nhật thất bại!";
                showNotification(`Lỗi: ${errorMessage}`, "error");
            }
        } catch (error) {
            console.error("Lỗi cập nhật:", error);
            showNotification("Lỗi kết nối! Vui lòng thử lại.", "error");
        }
    });

// ====================================
// 3. UI/UX VÀ TIỆN ÍCH (UI/UX & UTILITIES)
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
 * @description Hiển thị modal Đăng nhập/Đăng ký.
 */
function showLoginModal() {
    switchToLogin();
    const modal = new bootstrap.Modal(document.getElementById("authModal"));
    modal.show();
}

/**
 * @description Chuyển đổi giao diện trong modal sang form Đăng ký.
 */
function switchToRegister() {
    document.getElementById("loginForm").style.display = "none";
    document.getElementById("registerForm").style.display = "block";
    document.getElementById("modalTitle").innerHTML =
        '<i class="fas fa-user-plus me-2"></i>Đăng ký';
}

/**
 * @description Chuyển đổi giao diện trong modal sang form Đăng nhập.
 */
function switchToLogin() {
    document.getElementById("registerForm").style.display = "none";
    document.getElementById("loginForm").style.display = "block";
    document.getElementById("modalTitle").innerHTML =
        '<i class="fas fa-sign-in-alt me-2"></i>Đăng nhập';
}

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
        notification.style.background =
            "linear-gradient(135deg, #2ecc71 0%, #27ae60 100%)";
    } else if (type === "error") {
        notification.style.background =
            "linear-gradient(135deg, #e74c3c 0%, #c0392b 100%)";
    } else if (type === "info") {
        notification.style.background =
            "linear-gradient(135deg, #3498db 0%, #2980b9 100%)";
    } else {
        notification.style.background =
            "linear-gradient(135deg, #95a5a6 0%, #7f8c8d 100%)";
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
