<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Checkout - Crimson E-Commerce</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; background: white; padding: 15px; border-radius: 8px; border: 1px solid #ddd; }
        .checkout-container { background: white; padding: 20px; border-radius: 8px; border: 1px solid #ddd; max-width: 600px; margin: 0 auto; }

        .section { margin-bottom: 25px; }
        .section h3 { border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 15px; }

        .address-card { border: 1px solid #ccc; padding: 10px; border-radius: 4px; margin-bottom: 10px; display: flex; align-items: flex-start; gap: 10px; }
        .address-card input { margin-top: 5px; }

        .form-group { margin-bottom: 15px; }
        .form-group select { width: 100%; padding: 10px; border-radius: 4px; border: 1px solid #ccc; }

        .btn-submit { background-color: #4caf50; color: white; padding: 12px; border: none; width: 100%; font-size: 1.1em; border-radius: 4px; cursor: pointer; font-weight: bold; }
        .btn-submit:hover { background-color: #388e3c; }

        .message { margin-bottom: 15px; font-weight: bold; text-align: center; }
        .error { color: red; }
        .success { color: green; }
    </style>
</head>
<body>

    <div class="header">
        <h2>Checkout</h2>
        <a href="/cart" style="color: #1976d2; font-weight: bold; text-decoration: none;">&larr; Back to Cart</a>
    </div>

    <div class="checkout-container">
        <div id="message-container" class="message"></div>

        <form id="checkoutForm">

            <!-- Step 1: Select Address -->
            <div class="section">
                <h3>1. Select Delivery Address</h3>
                <div id="address-list">
                    <p>Loading addresses...</p>
                </div>
            </div>

            <!-- Step 2: Select Payment Method -->
            <div class="section">
                <h3>2. Payment Method</h3>
                <div class="form-group">
                    <select id="paymentMethod" required>
                        <option value="">-- Select Payment Method --</option>
                        <option value="UPI">UPI</option>
                        <option value="CASH_ON_DELIVERY">Cash on Delivery (COD)</option>
                    </select>
                </div>
            </div>

            <button type="submit" class="btn-submit" id="placeOrderBtn">Place Order</button>
        </form>
    </div>

    <script>
        // Load addresses on page load
        window.onload = function() {
            fetch('/api/addresses/customer/my-addresses')
                .then(response => response.json())
                .then(json => {
                    const addressContainer = document.getElementById('address-list');
                    addressContainer.innerHTML = ''; // Clear loading text

                    if (json.success && json.data && json.data.length > 0) {
                        json.data.forEach((address, index) => {
                            // Select the first address by default
                            const isChecked = index === 0 ? 'checked' : '';

                            const div = document.createElement('div');
                            div.className = 'address-card';
                            div.innerHTML = `
                                <input type="radio" name="addressId" value="\${address.id}" id="addr-\${address.id}" \${isChecked} required>
                                <label for="addr-\${address.id}">
                                    <strong>\${address.street}</strong><br>
                                    \${address.city}, \${address.state} \${address.zipCode}<br>
                                    <small>\${address.country}</small>
                                </label>
                            `;
                            addressContainer.appendChild(div);
                        });
                    } else if (json.success) {
                        addressContainer.innerHTML = '<p style="color: red;">No addresses found. Please add an address in your profile first.</p>';
                        document.getElementById('placeOrderBtn').disabled = true;
                    } else {
                        addressContainer.innerHTML = `<p class="error">\${json.message}</p>`;
                    }
                })
                .catch(error => {
                    console.error('Error fetching addresses:', error);
                    document.getElementById('address-list').innerHTML = '<p class="error">Failed to load addresses.</p>';
                });
        };

        // Handle Checkout Submission
        document.getElementById('checkoutForm').addEventListener('submit', function(e) {
            e.preventDefault();

            const messageContainer = document.getElementById('message-container');
            const submitBtn = document.getElementById('placeOrderBtn');

            // Get selected address
            const selectedAddress = document.querySelector('input[name="addressId"]:checked');
            if (!selectedAddress) {
                messageContainer.innerText = "Please select a delivery address.";
                messageContainer.className = "message error";
                return;
            }

            const paymentMethod = document.getElementById('paymentMethod').value;

            const payload = {
                addressId: selectedAddress.value,
                paymentMethod: paymentMethod
            };

            submitBtn.innerText = "Processing...";
            submitBtn.disabled = true;

            // Submit to OrderController
            fetch('/api/orders/checkout', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(res => res.json())
            .then(json => {
                if (json.success) {
                    messageContainer.innerText = "Order placed successfully! Redirecting...";
                    messageContainer.className = "message success";

                    // Redirect to a success page or order history
                    setTimeout(() => {
                        window.location.href = '/products'; // Or route to an order history view
                    }, 2000);
                } else {
                    messageContainer.innerText = json.message;
                    messageContainer.className = "message error";
                    submitBtn.innerText = "Place Order";
                    submitBtn.disabled = false;
                }
            })
            .catch(err => {
                console.error('Error:', err);
                messageContainer.innerText = "Network error occurred.";
                messageContainer.className = "message error";
                submitBtn.innerText = "Place Order";
                submitBtn.disabled = false;
            });
        });
    </script>

</body>
</html>