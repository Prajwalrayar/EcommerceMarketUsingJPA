<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - Crimson E-Commerce</title>
    <style>
        body { font-family: Arial, sans-serif; display: flex; justify-content: center; padding-top: 30px; background-color: #f4f6f8; }
        .register-box { background: white; border: 1px solid #ccc; padding: 30px; border-radius: 8px; width: 380px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 12px; }
        .form-group input { width: 100%; padding: 9px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        .form-group input.invalid { border-color: red; background-color: #fff8f8; }
        .form-group input.valid { border-color: green; background-color: #f8fff8; }
        .field-error { font-size: 0.75em; color: red; margin-top: 3px; display: none; }
        .message { margin-bottom: 15px; font-weight: bold; text-align: center; }
        .error { color: red; }
        .success { color: green; }
        button { background-color: #1976d2; color: white; border: none; padding: 12px; width: 100%; border-radius: 4px; cursor: pointer; font-weight: bold; font-size: 1em; }
        button:disabled { background-color: #cccccc; cursor: not-allowed; }
        button:hover:not(:disabled) { background-color: #115293; }
        .back-link { display: block; text-align: center; margin-bottom: 15px; text-decoration: none; color: #666; }
        .back-link:hover { color: #333; text-decoration: underline; }
        .section-title { font-size: 0.95em; font-weight: bold; color: #444; margin: 15px 0 8px 0; border-bottom: 1px solid #eee; padding-bottom: 4px; }
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