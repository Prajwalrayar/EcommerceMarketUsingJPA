<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Crimson E-Commerce</title>
    <style>
        body { font-family: Arial, sans-serif; display: flex; justify-content: center; padding-top: 50px; background-color: #f4f6f8;}
        .login-box { background: white; border: 1px solid #ccc; padding: 30px; border-radius: 8px; width: 320px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 15px; }
        .form-group input { width: 100%; padding: 10px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px;}
        .message { margin-bottom: 15px; font-weight: bold; text-align: center; }
        .error { color: red; }
        .success { color: green; }
        button { background-color: #1976d2; color: white; border: none; padding: 12px; width: 100%; border-radius: 4px; cursor: pointer; font-weight: bold; font-size: 1em; }
        button:hover { background-color: #115293; }
        .back-link { display: block; text-align: center; margin-bottom: 20px; text-decoration: none; color: #666; }
        .back-link:hover { color: #333; text-decoration: underline; }
    </style>
</head>
<body>

<div class="login-box">
    <a href="<%= request.getContextPath() %>/" class="back-link">&larr; Back to Dashboard</a>

    <h2 id="form-title" style="text-align: center; margin-top: 0;">Login</h2>

    <div id="message-container" class="message"></div>

    <form id="loginForm">
        <div class="form-group">
            <input type="email" id="email" placeholder="Email Address" required />
        </div>
        <div class="form-group">
            <input type="password" id="password" placeholder="Password" required />
        </div>

        <button type="submit">Login</button>
    </form>

    <p id="register-text" style="text-align: center; margin-top: 20px;">
        Don't have an account? <a id="register-link" href="<%= request.getContextPath() %>/register">Register here</a>
    </p>
</div>

<script>
    // Grab the dynamic context path using a scriptlet
    const contextPath = '<%= request.getContextPath() %>';

    const urlParams = new URLSearchParams(window.location.search);
    const role = urlParams.get('role') || 'CUSTOMER';

    const formattedRole = role.charAt(0) + role.slice(1).toLowerCase();
    document.getElementById('form-title').innerText = formattedRole + " Login";

    // Dynamically append the role to the registration link
    document.getElementById('register-link').href = contextPath + '/register?role=' + role;

    if (role === 'ADMIN') {
        document.getElementById('register-text').style.display = 'none';
    }

    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const messageContainer = document.getElementById('message-container');
        messageContainer.innerText = "Logging in...";
        messageContainer.className = "message";

        const payload = {
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
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