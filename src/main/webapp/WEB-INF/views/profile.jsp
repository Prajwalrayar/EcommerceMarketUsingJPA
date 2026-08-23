<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile - Crimson E-Commerce</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; background: white; padding: 15px; border-radius: 8px; border: 1px solid #ddd; }
        .nav-links a { margin-right: 15px; text-decoration: none; color: #1976d2; font-weight: bold; }
        .nav-links a:hover { text-decoration: underline; }

        .grid-container { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        .card { background: white; padding: 20px; border-radius: 8px; border: 1px solid #ddd; }

        h3 { border-bottom: 2px solid #eee; padding-bottom: 10px; margin-top: 0; }

        .list-item { border: 1px solid #eee; padding: 10px; margin-bottom: 10px; border-radius: 4px; }

        .form-group { margin-bottom: 15px; }
        .form-group input { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }

        button { background-color: #1976d2; color: white; border: none; padding: 10px 15px; border-radius: 4px; cursor: pointer; font-weight: bold; }
        button:hover { background-color: #115293; }

        .status-badge { display: inline-block; padding: 4px 8px; border-radius: 12px; font-size: 0.8em; font-weight: bold; background: #e0e0e0; }
        .status-DELIVERED { background: #c8e6c9; color: #2e7d32; }
        .status-PENDING { background: #fff9c4; color: #f57f17; }

        .message { font-size: 0.9em; font-weight: bold; margin-bottom: 10px; }
        .error { color: red; }
        .success { color: green; }
    </style>
</head>
<body>

    <div class="header">
        <h2>My Account</h2>
        <div class="nav-links">
            <a href="/products">Shop</a>
            <a href="/cart">My Cart</a>
            <a href="/login" style="color: red;">Logout</a>
        </div>
    </div>

    <div class="grid-container">

        <!-- Left Column: Personal Info & Add Address -->
        <div>
            <div class="card" style="margin-bottom: 20px;">
                <h3>Personal Information</h3>
                <div id="profile-info"><p>Loading profile...</p></div>
            </div>

            <div class="card">
                <h3>Add New Address</h3>
                <div id="address-msg" class="message"></div>
                <form id="addAddressForm">
                    <div class="form-group"><input type="text" id="street" placeholder="Street Address" required></div>
                    <div class="form-group"><input type="text" id="city" placeholder="City" required></div>
                    <div class="form-group"><input type="text" id="state" placeholder="State" required></div>
                    <div class="form-group"><input type="text" id="zipCode" placeholder="Zip Code" required></div>
                    <div class="form-group"><input type="text" id="country" placeholder="Country" required></div>
                    <button type="submit">Save Address</button>
                </form>
            </div>
        </div>

        <!-- Right Column: Saved Addresses & Order History -->
        <div>
            <div class="card" style="margin-bottom: 20px;">
                <h3>Saved Addresses</h3>
                <div id="address-list"><p>Loading addresses...</p></div>
            </div>

            <div class="card">
                <h3>Order History</h3>
                <div id="order-history"><p>Loading orders...</p></div>
            </div>
        </div>

    </div>

    <script>
        window.onload = function() {
            loadProfile();
            loadAddresses();
            loadOrders();
        };

        // 1. Fetch Profile Data
        function loadProfile() {
            fetch('/api/customer/profile')
                .then(res => res.json())
                .then(json => {
                    const container = document.getElementById('profile-info');
                    if (json.success && json.data) {
                        const user = json.data;
                        container.innerHTML = `
                            <p><strong>Name:</strong> \${user.firstName} \${user.lastName}</p>
                            <p><strong>Email:</strong> \${user.email}</p>
                            <p><strong>Phone:</strong> \${user.phone || 'Not provided'}</p>
                        `;
                    } else {
                        container.innerHTML = `<p class="error">\${json.message}</p>`;
                    }
                }).catch(err => console.error(err));
        }

        // 2. Fetch Saved Addresses
        function loadAddresses() {
            fetch('/api/addresses/customer/my-addresses')
                .then(res => res.json())
                .then(json => {
                    const container = document.getElementById('address-list');
                    container.innerHTML = '';
                    if (json.success && json.data && json.data.length > 0) {
                        json.data.forEach(addr => {
                            container.innerHTML += `
                                <div class="list-item">
                                    <strong>\${addr.street}</strong><br>
                                    \${addr.city}, \${addr.state} \${addr.zipCode}<br>
                                    \${addr.country}
                                </div>
                            `;
                        });
                    } else {
                        container.innerHTML = '<p>No addresses found.</p>';
                    }
                }).catch(err => console.error(err));
        }

        // 3. Fetch Order History
        function loadOrders() {
            fetch('/api/orders/history')
                .then(res => res.json())
                .then(json => {
                    const container = document.getElementById('order-history');
                    container.innerHTML = '';
                    if (json.success && json.data && json.data.length > 0) {
                        json.data.forEach(order => {
                            container.innerHTML += `
                                <div class="list-item">
                                    <strong>Order ID:</strong> \${order.id} <br>
                                    <strong>Total:</strong> ₹\${order.totalAmount} <br>
                                    <strong>Status:</strong> <span class="status-badge status-\${order.status}">\${order.status}</span><br>
                                    <small>Date: \${new Date(order.orderDate).toLocaleDateString()}</small>
                                </div>
                            `;
                        });
                    } else {
                        container.innerHTML = '<p>You have no past orders.</p>';
                    }
                }).catch(err => console.error(err));
        }

        // 4. Handle Add Address Form Submit
        document.getElementById('addAddressForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const msgBox = document.getElementById('address-msg');
            msgBox.innerText = "Saving...";
            msgBox.className = "message";

            const payload = {
                street: document.getElementById('street').value,
                city: document.getElementById('city').value,
                state: document.getElementById('state').value,
                zipCode: document.getElementById('zipCode').value,
                country: document.getElementById('country').value
            };

            fetch('/api/addresses/customer/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(res => res.json())
            .then(json => {
                if (json.success) {
                    msgBox.innerText = "Address saved successfully!";
                    msgBox.className = "message success";
                    document.getElementById('addAddressForm').reset();
                    loadAddresses(); // Reload the address list dynamically
                    setTimeout(() => msgBox.innerText = '', 3000);
                } else {
                    msgBox.innerText = json.message;
                    msgBox.className = "message error";
                }
            })
            .catch(err => {
                console.error(err);
                msgBox.innerText = "Network error.";
                msgBox.className = "message error";
            });
        });
    </script>

</body>
</html>