<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Cart - Crimson E-Commerce</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; background: white; padding: 15px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
        .nav-links a { margin-right: 15px; text-decoration: none; color: #1976d2; font-weight: bold; }
        .nav-links a:hover { text-decoration: underline; }

        .cart-container { background: white; padding: 20px; border-radius: 8px; border: 1px solid #ddd; max-width: 800px; margin: 0 auto; }
        .cart-item { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eee; padding: 15px 0; }
        .cart-item:last-child { border-bottom: none; }
        .item-details h4 { margin: 0 0 5px 0; color: #333; }
        .item-details p { margin: 0; color: #666; font-size: 0.9em; }

        button { border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; font-weight: bold; }
        .remove-btn { background-color: #f44336; color: white; }
        .remove-btn:hover { background-color: #d32f2f; }

        .cart-footer { margin-top: 20px; display: flex; justify-content: space-between; align-items: center; border-top: 2px solid #ddd; padding-top: 20px; }
        .total-price { font-size: 1.4em; font-weight: bold; color: #2e7d32; }
        .checkout-btn { background-color: #4caf50; color: white; padding: 12px 24px; font-size: 1.1em; text-decoration: none; border-radius: 4px; }
        .checkout-btn:hover { background-color: #388e3c; }
    </style>
</head>
<body>

    <div class="header">
        <h2>My Cart</h2>
        <div class="nav-links">
            <a href="/products">Continue Shopping</a>
            <a href="/profile">My Profile</a>
        </div>
    </div>

    <div class="cart-container">
        <div id="cart-items">
            <p>Loading your cart...</p>
        </div>

        <div class="cart-footer" id="cart-footer" style="display: none;">
            <div class="total-price" id="cart-total">Total: ₹0.00</div>
            <a href="/checkout" class="checkout-btn" id="checkout-link">Proceed to Checkout</a>
        </div>
    </div>

    <script>
        // 1. Fetch cart data when the page loads
        window.onload = function() {
            loadCart();
        };

        function loadCart() {
            fetch('/api/customer/cart')
                .then(response => response.json())
                .then(json => {
                    const container = document.getElementById('cart-items');
                    const footer = document.getElementById('cart-footer');
                    const totalDisplay = document.getElementById('cart-total');

                    container.innerHTML = ''; // Clear loading text

                    if (json.success && json.data && json.data.length > 0) {
                        let total = 0;

                        json.data.forEach(item => {
                            // Calculate total (assuming item has quantity and productPrice fields from your CartResponseDTO)
                            const itemTotal = item.quantity * item.productPrice;
                            total += itemTotal;

                            const div = document.createElement('div');
                            div.className = 'cart-item';
                            div.innerHTML = `
                                <div class="item-details">
                                    <h4>\${item.productName}</h4>
                                    <p>Quantity: \${item.quantity} | Unit Price: ₹\${item.productPrice.toFixed(2)}</p>
                                    <p><strong>Subtotal: ₹\${itemTotal.toFixed(2)}</strong></p>
                                </div>
                                <button class="remove-btn" onclick="removeItem('\${item.id}')">Remove</button>
                            `;
                            container.appendChild(div);
                        });

                        // Update total and show footer
                        totalDisplay.innerText = 'Total: ₹' + total.toFixed(2);
                        footer.style.display = 'flex';

                    } else if (json.success) {
                        container.innerHTML = '<p>Your cart is empty. <a href="/products">Start shopping!</a></p>';
                        footer.style.display = 'none';
                    } else {
                        container.innerHTML = `<p style="color: red;">\${json.message}</p>`;
                    }
                })
                .catch(error => {
                    console.error('Error fetching cart:', error);
                    document.getElementById('cart-items').innerHTML = '<p style="color: red;">Failed to load cart.</p>';
                });
        }

        // 2. Remove an item from the cart
        function removeItem(cartId) {
            if (!confirm('Are you sure you want to remove this item?')) return;

            fetch('/api/customer/cart/remove/' + cartId, {
                method: 'DELETE'
            })
            .then(res => res.json())
            .then(json => {
                if (json.success) {
                    // Reload the cart dynamically to reflect the deletion
                    loadCart();
                } else {
                    alert('Failed to remove item: ' + json.message);
                }
            })
            .catch(err => console.error('Error removing item:', err));
        }
    </script>

</body>
</html>