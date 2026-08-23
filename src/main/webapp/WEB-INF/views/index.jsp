<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to E-Commerce Market Place</title>
    <style>
        body { font-family: Arial, sans-serif; display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; margin: 0; background-color: #f4f6f8; }
        .dashboard-box { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); text-align: center; width: 400px; }
        h1 { color: #333; margin-bottom: 30px; }
        .btn { display: block; width: 100%; padding: 15px; margin-bottom: 15px; text-decoration: none; font-size: 1.1em; font-weight: bold; color: white; border-radius: 5px; box-sizing: border-box; transition: background 0.3s; }
        .btn-customer { background-color: #1976d2; }
        .btn-customer:hover { background-color: #115293; }
        .btn-seller { background-color: #f57c00; }
        .btn-seller:hover { background-color: #e65100; }
        .btn-admin { background-color: #388e3c; }
        .btn-admin:hover { background-color: #2e7d32; }
    </style>
</head>
<body>

<div class="dashboard-box">
    <h1>Welcome to Ecommerce MarketPlace</h1>
    <p style="margin-bottom: 25px; color: #666;">Please select your portal to continue:</p>

    <!-- Using scriptlets to guarantee the context path is written correctly -->
    <a href="<%= request.getContextPath() %>/login?role=CUSTOMER" class="btn btn-customer">Customer Login</a>
    <a href="<%= request.getContextPath() %>/login?role=SELLER" class="btn btn-seller">Seller Login</a>
    <a href="<%= request.getContextPath() %>/login?role=ADMIN" class="btn btn-admin">Admin Login</a>
</div>

</body>
</html>