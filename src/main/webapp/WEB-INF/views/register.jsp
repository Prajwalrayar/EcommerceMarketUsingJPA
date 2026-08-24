<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - Crimson E-Commerce</title>
    <style>
    /* =========================================================
       GLOBAL RESET
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
            radial-gradient(
                circle at 10% 10%,
                rgba(37, 99, 235, 0.08),
                transparent 30%
            ),
            radial-gradient(
                circle at 90% 90%,
                rgba(14, 165, 233, 0.08),
                transparent 30%
            ),
            linear-gradient(
                135deg,
                #f8fafc 0%,
                #eef2f7 100%
            );

        color: #0f172a;
    }


    /* =========================================================
       REGISTRATION CARD
       ========================================================= */

    .register-box {
        width: 100%;
        max-width: 500px;

        background: rgba(255, 255, 255, 0.97);

        border: 1px solid #dbe2ea;

        border-radius: 18px;

        padding: 38px 42px 34px;

        box-shadow:
            0 20px 45px rgba(15, 23, 42, 0.10),
            0 5px 15px rgba(15, 23, 42, 0.05);

        position: relative;

        overflow: hidden;

        animation: registerCardAppear 0.45s ease-out;
    }


    /* Professional top accent */

    .register-box::before {
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
       BACK LINK
       ========================================================= */

    .back-link {
        display: flex;

        align-items: center;
        justify-content: center;

        width: fit-content;

        margin: 0 auto 24px;

        color: #64748b;

        font-size: 15px;

        font-weight: 500;

        text-decoration: none;

        transition:
            color 0.2s ease,
            transform 0.2s ease;
    }

    .back-link:hover {
        color: #2563eb;

        text-decoration: none;

        transform: translateX(-2px);
    }


    /* =========================================================
       TITLE
       ========================================================= */

    #form-title {
        text-align: center;

        margin: 0 0 26px;

        color: #0f172a;

        font-size: 30px;

        font-weight: 700;

        line-height: 1.25;

        letter-spacing: -0.4px;
    }


    /* =========================================================
       MESSAGE
       ========================================================= */

    .message {
        min-height: 22px;

        margin: 0 0 18px;

        text-align: center;

        font-size: 14px;

        font-weight: 600;

        line-height: 1.5;
    }

    .error {
        color: #dc2626;
    }

    .success {
        color: #16a34a;
    }


    /* =========================================================
       FORM GROUP
       ========================================================= */

    .form-group {
        margin-bottom: 17px;
    }


    /* =========================================================
       INPUTS
       ========================================================= */

    .form-group input {
        display: block;

        width: 100%;

        height: 52px;

        padding: 0 16px;

        border: 1px solid #cbd5e1;

        border-radius: 10px;

        background: #ffffff;

        color: #0f172a;

        font-family: inherit;

        font-size: 15px;

        outline: none;

        transition:
            border-color 0.2s ease,
            box-shadow 0.2s ease,
            background-color 0.2s ease;
    }


    /* Placeholder */

    .form-group input::placeholder {
        color: #94a3b8;

        opacity: 1;
    }


    /* Hover */

    .form-group input:hover {
        border-color: #94a3b8;
    }


    /* Focus */

    .form-group input:focus {
        border-color: #2563eb;

        background: #ffffff;

        box-shadow:
            0 0 0 3px rgba(37, 99, 235, 0.11);
    }


    /* =========================================================
       INVALID INPUT
       ========================================================= */

    .form-group input.invalid {
        border-color: #ef4444;

        background: #fffafa;

        box-shadow:
            0 0 0 3px rgba(239, 68, 68, 0.07);
    }

    .form-group input.invalid:focus {
        border-color: #dc2626;

        box-shadow:
            0 0 0 3px rgba(239, 68, 68, 0.11);
    }


    /* =========================================================
       VALID INPUT
       ========================================================= */

    .form-group input.valid {
        border-color: #22c55e;

        background: #fafffb;

        box-shadow:
            0 0 0 3px rgba(34, 197, 94, 0.06);
    }

    .form-group input.valid:focus {
        border-color: #16a34a;

        box-shadow:
            0 0 0 3px rgba(34, 197, 94, 0.10);
    }


    /* =========================================================
       FIELD VALIDATION MESSAGE
       ========================================================= */

    .field-error {
        font-size: 12px;

        line-height: 1.5;

        color: #dc2626;

        margin-top: 6px;

        padding-left: 3px;

        display: none;
    }


    /* =========================================================
       SECTION TITLE
       ========================================================= */

    .section-title {
        margin: 25px 0 14px;

        padding-bottom: 9px;

        border-bottom: 1px solid #e2e8f0;

        color: #334155;

        font-size: 14px;

        font-weight: 700;

        letter-spacing: 0.2px;

        text-transform: uppercase;
    }


    /* =========================================================
       REGISTER BUTTON
       ========================================================= */

    button {
        width: 100%;

        height: 53px;

        margin-top: 8px;

        padding: 0 20px;

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


    /* Hover only when enabled */

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


    /* Click */

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
       LOGIN LINK SECTION
       ========================================================= */

    .register-box > p {
        text-align: center;

        margin: 24px 0 0;

        color: #64748b;

        font-size: 14px;

        line-height: 1.6;
    }

    #login-link {
        color: #2563eb;

        font-weight: 600;

        text-decoration: none;

        margin-left: 4px;

        transition: color 0.2s ease;
    }

    #login-link:hover {
        color: #1d4ed8;

        text-decoration: underline;
    }


    /* =========================================================
       ANIMATION
       ========================================================= */

    @keyframes registerCardAppear {

        from {
            opacity: 0;

            transform: translateY(14px);
        }

        to {
            opacity: 1;

            transform: translateY(0);
        }
    }


    /* =========================================================
       RESPONSIVE - TABLET
       ========================================================= */

    @media (max-width: 600px) {

        body {
            padding: 25px 15px;

            align-items: center;
        }

        .register-box {
            max-width: 100%;

            padding: 34px 25px 30px;

            border-radius: 15px;
        }

        #form-title {
            font-size: 27px;

            margin-bottom: 24px;
        }

        .back-link {
            margin-bottom: 22px;
        }

        .form-group input {
            height: 51px;

            font-size: 15px;
        }

        button {
            height: 51px;
        }
    }


    /* =========================================================
       RESPONSIVE - SMALL MOBILE
       ========================================================= */

    @media (max-width: 380px) {

        body {
            padding: 15px 10px;
        }

        .register-box {
            padding: 30px 18px 26px;
        }

        #form-title {
            font-size: 24px;
        }

        .form-group input {
            height: 49px;

            font-size: 14px;
        }

        .section-title {
            font-size: 13px;
        }

        button {
            height: 50px;

            font-size: 15px;
        }
    }
</style>
</head>
<body>

<div class="register-box">
    <a href="<%= request.getContextPath() %>/" class="back-link">&larr; Back to Dashboard</a>

    <h2 id="form-title" style="text-align: center; margin-top: 0;">Register</h2>

    <div id="message-container" class="message"></div>

    <form id="registerForm" novalidate>
        <div class="form-group">
            <input type="text" id="firstName" placeholder="First Name *" />
            <div id="firstName-error" class="field-error">3-30 letters. Max 2 consecutive repeats. No sequences (abc/xyz).</div>
        </div>

        <div class="form-group">
            <input type="text" id="lastName" placeholder="Last Name (Optional)" />
            <div id="lastName-error" class="field-error">Invalid format. Letters only, 3-30 chars, max 2 repeats.</div>
        </div>

        <div class="form-group">
            <input type="email" id="email" placeholder="Email Address *" />
            <div id="email-error" class="field-error">Email username must follow name rules (min 3 letters, no sequences like abc, max 2 repeats).</div>
        </div>

        <div class="form-group">
            <input type="text" id="phone" placeholder="Phone Number (10 digits starting with 6-9) *" maxlength="10" />
            <div id="phone-error" class="field-error">Must be 10 digits and start with 6, 7, 8, or 9.</div>
        </div>

        <div class="form-group">
            <input type="password" id="password" placeholder="Password *" />
            <div id="password-error" class="field-error">8-20 chars: min 1 uppercase, 1 lowercase, 1 digit, 1 special character.</div>
        </div>

        <div id="extra-fields">
            <div class="section-title" id="extra-title">Address Details (Optional)</div>
            <div class="form-group" id="shop-group" style="display: none;">
                <input type="text" id="shopName" placeholder="Shop Name *" />
                <div id="shopName-error" class="field-error">Shop Name is required for sellers.</div>
            </div>
            <div class="form-group">
                <input type="text" id="street" placeholder="Street Address" />
                <div id="street-error" class="field-error">Street address is required.</div>
            </div>
            <div class="form-group">
                <input type="text" id="city" placeholder="City" />
                <div id="city-error" class="field-error">City is required.</div>
            </div>
            <div class="form-group"><input type="text" id="state" placeholder="State" /></div>
            <div class="form-group"><input type="text" id="zipCode" placeholder="Zip Code" /></div>
            <div class="form-group"><input type="text" id="country" placeholder="Country" /></div>
        </div>

        <button type="submit" id="submitBtn" style="margin-top: 10px;" disabled>Register</button>
    </form>

    <p style="text-align: center; margin-top: 15px;">
        Already have an account? <a id="login-link" href="<%= request.getContextPath() %>/login">Login here</a>
    </p>
</div>

<script>
    const contextPath = '<%= request.getContextPath() %>';

    const urlParams = new URLSearchParams(window.location.search);
    const role = urlParams.get('role') || 'CUSTOMER';

    const formattedRole = role.charAt(0) + role.slice(1).toLowerCase();
    document.getElementById('form-title').innerText = formattedRole + " Registration";
    document.getElementById('login-link').href = contextPath + '/login?role=' + role;

    if (role === 'SELLER') {
        document.getElementById('extra-title').innerText = "Shop & Address Details (Required)";
        document.getElementById('shop-group').style.display = 'block';
    }

    const firstNameInput = document.getElementById('firstName');
    const lastNameInput = document.getElementById('lastName');
    const emailInput = document.getElementById('email');
    const phoneInput = document.getElementById('phone');
    const passwordInput = document.getElementById('password');
    const shopNameInput = document.getElementById('shopName');
    const streetInput = document.getElementById('street');
    const cityInput = document.getElementById('city');
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

    function updateFieldState(inputElement, errorElementId, isValid, isOptional = false) {
        const errorDiv = document.getElementById(errorElementId);
        const val = inputElement.value.trim();

        if (isOptional && val.length === 0) {
            inputElement.classList.remove('invalid', 'valid');
            if (errorDiv) errorDiv.style.display = 'none';
            return true;
        }

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
        const isFirstNameValid = validateNameString(firstNameInput.value.trim());
        updateFieldState(firstNameInput, 'firstName-error', isFirstNameValid);

        const lastNameVal = lastNameInput.value.trim();
        let isLastNameValid = true;
        if (lastNameVal.length > 0) {
            isLastNameValid = validateNameString(lastNameVal);
            updateFieldState(lastNameInput, 'lastName-error', isLastNameValid);
        } else {
            lastNameInput.classList.remove('invalid', 'valid');
            document.getElementById('lastName-error').style.display = 'none';
        }

        const isEmailValid = validateEmailString(emailInput.value.trim());
        updateFieldState(emailInput, 'email-error', isEmailValid);

        const phoneRegex = /^[6-9]\d{9}$/;
        const isPhoneValid = phoneRegex.test(phoneInput.value.trim());
        updateFieldState(phoneInput, 'phone-error', isPhoneValid);

        const passwordVal = passwordInput.value;
        const passRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&*!?_+=-])[A-Za-z\d@#$%^&*!?_+=-]{8,20}$/;
        const isPasswordValid = passRegex.test(passwordVal);
        updateFieldState(passwordInput, 'password-error', isPasswordValid);

        let isSellerValid = true;
        if (role === 'SELLER') {
            const isShopValid = shopNameInput.value.trim().length > 0;
            updateFieldState(shopNameInput, 'shopName-error', isShopValid);

            const isStreetValid = streetInput.value.trim().length > 0;
            updateFieldState(streetInput, 'street-error', isStreetValid);

            const isCityValid = cityInput.value.trim().length > 0;
            updateFieldState(cityInput, 'city-error', isCityValid);

            isSellerValid = isShopValid && isStreetValid && isCityValid;
        }

        if (isFirstNameValid && isLastNameValid && isEmailValid && isPhoneValid && isPasswordValid && isSellerValid) {
            submitBtn.removeAttribute('disabled');
        } else {
            submitBtn.setAttribute('disabled', 'true');
        }
    }

    [firstNameInput, lastNameInput, emailInput, phoneInput, passwordInput, shopNameInput, streetInput, cityInput].forEach(input => {
        if (input) {
            input.addEventListener('input', checkFormValidity);
        }
    });

    document.getElementById('registerForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const messageContainer = document.getElementById('message-container');
        messageContainer.innerText = "Processing registration...";
        messageContainer.className = "message";

        const payload = {
            name: firstNameInput.value.trim(), // Correctly matches DTO field 'name'
            email: emailInput.value.trim(),
            phone: phoneInput.value.trim(),
            password: passwordInput.value
        };

        const street = streetInput.value.trim();
        if (street || role === 'SELLER') {
            payload.address = {
                street: street,
                city: cityInput.value.trim(),
                state: document.getElementById('state').value.trim(),
                zipCode: document.getElementById('zipCode').value.trim(),
                country: document.getElementById('country').value.trim()
            };
        }

        if (role === 'SELLER') {
            payload.shopName = shopNameInput.value.trim();
        }

        const apiEndpoint = role === 'CUSTOMER'
            ? contextPath + '/api/auth/customer/register'
            : contextPath + '/api/auth/seller/register';

        fetch(apiEndpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
        .then(response => response.json())
        .then(json => {
            if (json.success) {
                messageContainer.innerText = "Registration successful! Redirecting...";
                messageContainer.className = "message success";

                setTimeout(() => {
                    window.location.href = contextPath + '/login?role=' + role;
                }, 2000);
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