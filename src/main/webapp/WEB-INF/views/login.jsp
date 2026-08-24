<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login </title>
    <style>
    /* =========================================================
       GLOBAL
       ========================================================= */

    * {
        box-sizing: border-box;
    }

    html,
    body {
        margin: 0;
        padding: 0;
        min-height: 100%;
    }

    body {
        font-family: "Segoe UI", Roboto, Arial, sans-serif;
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 40px 20px;

        background:
            radial-gradient(circle at 10% 20%, rgba(37, 99, 235, 0.08), transparent 30%),
            radial-gradient(circle at 90% 80%, rgba(14, 165, 233, 0.08), transparent 30%),
            linear-gradient(135deg, #f8fafc 0%, #eef2f7 100%);

        color: #0f172a;
    }


    /* =========================================================
       LOGIN CARD
       ========================================================= */

    .login-box {
        width: 100%;
        max-width: 470px;

        background: rgba(255, 255, 255, 0.97);

        border: 1px solid rgba(203, 213, 225, 0.9);
        border-radius: 18px;

        padding: 42px 42px 38px;

        box-shadow:
            0 20px 45px rgba(15, 23, 42, 0.10),
            0 4px 12px rgba(15, 23, 42, 0.05);

        position: relative;
        overflow: hidden;

        animation: cardAppear 0.45s ease-out;
    }

    /* Small colored line on top of card */

    .login-box::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;

        width: 100%;
        height: 4px;

        background: linear-gradient(
            90deg,
            #2563eb,
            #3b82f6,
            #0ea5e9
        );
    }


    /* =========================================================
       BACK TO DASHBOARD
       ========================================================= */

    .back-link {
        display: inline-flex;
        align-items: center;

        text-align: center;
        justify-content: center;

        margin: 0 auto 28px;

        text-decoration: none;

        color: #64748b;

        font-size: 15px;
        font-weight: 500;

        transition:
            color 0.2s ease,
            transform 0.2s ease;
    }

    .back-link:hover {
        color: #2563eb;
        transform: translateX(-2px);
        text-decoration: none;
    }


    /* =========================================================
       LOGIN TITLE
       ========================================================= */

    #form-title {
        text-align: center;
        margin: 0 0 32px;

        font-size: 32px;
        line-height: 1.2;

        font-weight: 700;

        letter-spacing: -0.5px;

        color: #0f172a;
    }


    /* =========================================================
       MESSAGE
       ========================================================= */

    .message {
        min-height: 22px;

        margin-bottom: 18px;

        font-size: 14px;
        font-weight: 600;

        text-align: center;

        transition: all 0.2s ease;
    }

    .error {
        color: #dc2626;
    }

    .success {
        color: #16a34a;
    }


    /* =========================================================
       FORM
       ========================================================= */

    .form-group {
        margin-bottom: 20px;
    }


    /* =========================================================
       INPUTS
       ========================================================= */

    .form-group input {
        display: block;

        width: 100%;

        height: 56px;

        padding: 0 17px;

        border: 1px solid #cbd5e1;

        border-radius: 10px;

        background-color: #ffffff;

        color: #0f172a;

        font-family: inherit;
        font-size: 16px;

        outline: none;

        transition:
            border-color 0.2s ease,
            box-shadow 0.2s ease,
            background-color 0.2s ease;
    }

    .form-group input::placeholder {
        color: #94a3b8;
    }

    .form-group input:hover {
        border-color: #94a3b8;
    }

    .form-group input:focus {
        border-color: #2563eb;

        background-color: #ffffff;

        box-shadow:
            0 0 0 3px rgba(37, 99, 235, 0.12);
    }


    /* =========================================================
       INVALID INPUT
       ========================================================= */

    .form-group input.invalid {
        border-color: #ef4444;

        background-color: #fffafa;

        box-shadow:
            0 0 0 3px rgba(239, 68, 68, 0.08);
    }

    .form-group input.invalid:focus {
        border-color: #dc2626;

        box-shadow:
            0 0 0 3px rgba(239, 68, 68, 0.12);
    }


    /* =========================================================
       VALID INPUT
       ========================================================= */

    .form-group input.valid {
        border-color: #22c55e;

        background-color: #fafffb;

        box-shadow:
            0 0 0 3px rgba(34, 197, 94, 0.06);
    }

    .form-group input.valid:focus {
        border-color: #16a34a;

        box-shadow:
            0 0 0 3px rgba(34, 197, 94, 0.10);
    }


    /* =========================================================
       VALIDATION ERROR
       ========================================================= */

    .field-error {
        font-size: 12px;

        line-height: 1.5;

        color: #dc2626;

        margin-top: 7px;
        padding-left: 3px;

        display: none;
    }


    /* =========================================================
       LOGIN BUTTON
       ========================================================= */

    button {
        width: 100%;

        height: 54px;

        margin-top: 4px;

        border: none;
        border-radius: 10px;

        background: linear-gradient(
            135deg,
            #2563eb,
            #1d4ed8
        );

        color: #ffffff;

        font-family: inherit;

        font-size: 16px;
        font-weight: 700;

        letter-spacing: 0.2px;

        cursor: pointer;

        box-shadow:
            0 8px 18px rgba(37, 99, 235, 0.20);

        transition:
            transform 0.2s ease,
            box-shadow 0.2s ease,
            background 0.2s ease;
    }

    button:hover:not(:disabled) {
        background: linear-gradient(
            135deg,
            #1d4ed8,
            #1e40af
        );

        transform: translateY(-2px);

        box-shadow:
            0 12px 24px rgba(37, 99, 235, 0.25);
    }

    button:active:not(:disabled) {
        transform: translateY(0);

        box-shadow:
            0 6px 14px rgba(37, 99, 235, 0.18);
    }


    /* =========================================================
       DISABLED BUTTON
       ========================================================= */

    button:disabled {
        background: #cbd5e1;

        color: #f8fafc;

        cursor: not-allowed;

        box-shadow: none;

        transform: none;
    }


    /* =========================================================
       REGISTER SECTION
       ========================================================= */

    #register-text {
        text-align: center;

        margin: 26px 0 0;

        color: #64748b;

        font-size: 14px;
        line-height: 1.6;
    }

    #register-link {
        color: #2563eb;

        font-weight: 600;

        text-decoration: none;

        margin-left: 4px;

        transition: color 0.2s ease;
    }

    #register-link:hover {
        color: #1d4ed8;

        text-decoration: underline;
    }


    /* =========================================================
       CARD ANIMATION
       ========================================================= */

    @keyframes cardAppear {
        from {
            opacity: 0;
            transform: translateY(12px);
        }

        to {
            opacity: 1;
            transform: translateY(0);
        }
    }


    /* =========================================================
       RESPONSIVE
       ========================================================= */

    @media (max-width: 600px) {

        body {
            padding: 20px 15px;
            align-items: center;
        }

        .login-box {
            max-width: 100%;

            padding: 36px 25px 30px;

            border-radius: 15px;
        }

        #form-title {
            font-size: 28px;
            margin-bottom: 27px;
        }

        .back-link {
            margin-bottom: 24px;
        }

        .form-group input {
            height: 53px;
        }

        button {
            height: 52px;
        }
    }


    @media (max-width: 380px) {

        .login-box {
            padding: 32px 20px 27px;
        }

        #form-title {
            font-size: 25px;
        }

        .form-group input {
            font-size: 15px;
        }
    }
</style>
</head>
<body>

<div class="login-box">
    <a href="<%= request.getContextPath() %>/" class="back-link">&larr; Back to Dashboard</a>

    <h2 id="form-title" style="text-align: center; margin-top: 0;">Login</h2>

    <div id="message-container" class="message"></div>

    <form id="loginForm" novalidate>
        <div class="form-group">
            <input type="email" id="email" placeholder="Email Address *" required />
            <div id="email-error" class="field-error">Email username must follow rules (min 3 letters, no sequences like abc, max 2 repeats).</div>
        </div>
        <div class="form-group">
            <input type="password" id="password" placeholder="Password *" required />
            <div id="password-error" class="field-error">8-20 chars: min 1 uppercase, 1 lowercase, 1 digit, 1 special character.</div>
        </div>

        <button type="submit" id="submitBtn" disabled>Login</button>
    </form>

    <p id="register-text" style="text-align: center; margin-top: 20px;">
        Don't have an account? <a id="register-link" href="<%= request.getContextPath() %>/register">Register here</a>
    </p>
</div>

<script>
    const contextPath = '<%= request.getContextPath() %>';

    const urlParams = new URLSearchParams(window.location.search);
    const role = urlParams.get('role') || 'CUSTOMER';

    const formattedRole = role.charAt(0) + role.slice(1).toLowerCase();
    document.getElementById('form-title').innerText = formattedRole + " Login";

    document.getElementById('register-link').href = contextPath + '/register?role=' + role;

    if (role === 'ADMIN') {
        document.getElementById('register-text').style.display = 'none';
    }

    // --- Validation Logic Imported from Register ---
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const submitBtn = document.getElementById('submitBtn');

    function validateNameString(str) {
        if (!str || str.length < 3 || str.length > 30) return false;
        if (!/^[a-zA-Z]+$/.test(str)) return false;

        for (let i = 0; i < str.length - 2; i++) {
            if (str.charAt(i) === str.charAt(i+1) && str.charAt(i) === str.charAt(i+2)) {
                return false;
            }
        }

        const lower = str.toLowerCase();
        for (let i = 0; i < lower.length - 2; i++) {
            let code1 = lower.charCodeAt(i);
            let code2 = lower.charCodeAt(i+1);
            let code3 = lower.charCodeAt(i+2);
            if (code2 === code1 + 1 && code3 === code2 + 1) {
                return false;
            }
        }
        return true;
    }

    function validateEmailString(emailStr) {
        const parts = emailStr.split('@');
        if (parts.length !== 2) return false;
        const username = parts[0];
        const domain = parts[1];

        const domainRegex = /^[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!domainRegex.test(domain)) return false;

        return validateNameString(username);
    }

    function updateFieldState(inputElement, errorElementId, isValid) {
        const errorDiv = document.getElementById(errorElementId);
        const val = inputElement.value.trim();

        if (val.length === 0) {
            inputElement.classList.remove('invalid', 'valid');
            if (errorDiv) errorDiv.style.display = 'none';
            return false;
        }

        if (isValid) {
            inputElement.classList.remove('invalid');
            inputElement.classList.add('valid');
            if (errorDiv) errorDiv.style.display = 'none';
            return true;
        } else {
            inputElement.classList.remove('valid');
            inputElement.classList.add('invalid');
            if (errorDiv) errorDiv.style.display = 'block';
            return false;
        }
    }

    function checkFormValidity() {
        const isEmailValid = validateEmailString(emailInput.value.trim());
        updateFieldState(emailInput, 'email-error', isEmailValid);

        const passwordVal = passwordInput.value;
        const passRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&*!?_+=-])[A-Za-z\d@#$%^&*!?_+=-]{8,20}$/;
        const isPasswordValid = passRegex.test(passwordVal);
        updateFieldState(passwordInput, 'password-error', isPasswordValid);

        // Master Enable/Disable Button Condition
        if (isEmailValid && isPasswordValid) {
            submitBtn.removeAttribute('disabled');
        } else {
            submitBtn.setAttribute('disabled', 'true');
        }
    }

    // Attach live validation listeners
    [emailInput, passwordInput].forEach(input => {
        if (input) {
            input.addEventListener('input', checkFormValidity);
        }
    });

    // --- Submit Logic ---
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const messageContainer = document.getElementById('message-container');
        messageContainer.innerText = "Logging in...";
        messageContainer.className = "message";

        const payload = {
            email: emailInput.value.trim(),
            password: passwordInput.value
        };

        let apiEndpoint = '';
        if (role === 'CUSTOMER') apiEndpoint = contextPath + '/api/auth/customer/login';
        else if (role === 'SELLER') apiEndpoint = contextPath + '/api/auth/seller/login';
        else if (role === 'ADMIN') apiEndpoint = contextPath + '/api/auth/admin/login';

        fetch(apiEndpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
        .then(response => response.json())
        .then(json => {
            if (json.success) {
                messageContainer.innerText = "Success! Redirecting...";
                messageContainer.className = "message success";

                setTimeout(() => {
                    if (role === 'CUSTOMER') window.location.href = contextPath + '/products';
                    else if (role === 'SELLER') window.location.href = contextPath + '/seller/dashboard';
                    else if (role === 'ADMIN') window.location.href = contextPath + '/admin/dashboard';
                }, 1000);
            } else {
                messageContainer.innerText = json.message;
                messageContainer.className = "message error";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            messageContainer.innerText = "A network error occurred.";
            messageContainer.className = "message error";
        });
    });
</script>

</body>
</html>