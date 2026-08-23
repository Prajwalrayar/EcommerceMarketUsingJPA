<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Catalog - Crimson E-Commerce</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background-color: #f9f9f9; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; background: white; padding: 15px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
        .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap: 20px; }
        .product-card { background: white; border: 1px solid #ddd; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); display: flex; flex-direction: column; justify-content: space-between; }
        .product-card h3 { margin-top: 0; color: #333; }
        .price { font-weight: bold; color: #2e7d32; font-size: 1.2em; margin: 10px 0; }
        .nav-links a { margin-right: 15px; text-decoration: none; color: #1976d2; font-weight: bold; }
        .nav-links a:hover { text-decoration: underline; }
        button { background-color: #1976d2; color: white; border: none; padding: 10px; width: 100%; border-radius: 4px; cursor: pointer; font-weight: bold; }
        button:hover { background-color: #115293; }
        .logout-btn { background-color: #d32f2f; width: auto; padding: 6px 12px; }
        .logout-btn:hover { background-color: #9a0007; }
        .message { margin-top: 10px; font-size: 0.85em; text-align: center; font-weight: bold; }
    </style>
</head>
<body>

    <div class="header">
        <h2>Crimson Store</h2>
        <div class="nav-links">
            <a href="/cart">View Cart</a>
            <a href="/profile">My Profile</a>
            <button class="logout-btn" onclick="logout()">Logout</button>
        </div>
    </div>

    <h3>Available Products</h3>
    <div id="product-container" class="product-grid">
        <p>Loading products...</p>
    </div>

    <script>
        // 1. Fetch products from the backend REST API on page load
        window.onload = function() {
            fetch('/api/products')
                .then(response => response.json())
                .then(json => {
                    const container = document.getElementById('product-container');
                    container.innerHTML = '';

                    // Check your ApiResponse structure
                    if (json.success && json.data && json.data.length > 0) {
                        json.data.forEach(product => {
                            const card = document.createElement('div');
                            card.className = 'product-card';
                            card.innerHTML = `
                                <div>
                                    <h3>\${product.name}</h3>
                                    <p><strong>Brand:</strong> \${product.brand || 'N/A'}</p>
                                    <p class="price">₹\${product.price}</p>
                                    <p>\${product.description || ''}</p>
                                </div>
                                <div>
                                    <button onclick="addToCart('\${product.id}')">Add to Cart</button>
                                    <div id="msg-\${product.id}" class="message"></div>
                                </div>
                            `;
                            container.appendChild(card);
                        });
                    } else if (json.success) {
                        container.innerHTML = '<p>No products available right now.</p>';
                    } else {
                        container.innerHTML = `<p style="color: red;">\${json.message}</p>`;
                    }
                })
                .catch(error => {
                    console.error('Error fetching products:', error);
                    document.getElementById('product-container').innerHTML = '<p style="color: red;">Failed to load products.</p>';
                });
        };

        // 2. Handle adding items to the customer cart
        function addToCart(productId) {
            const msgDiv = document.getElementById(`msg-\${productId}`);
            msgDiv.innerText = "Adding...";
            msgDiv.style.color = "#555";

            const payload = {
                productId: productId,
                quantity: 1
            };

            fetch('/api/customer/cart/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(res => res.json())
            .then(json => {
                if (json.success) {
                    msgDiv.style.color = "green";
                    msgDiv.innerText = json.message; // "Product added to cart successfully"
                    setTimeout(() => { msgDiv.innerText = ""; }, 3000);
                } else {
                    msgDiv.style.color = "red";
                    msgDiv.innerText = json.message;
                }
            })
            .catch(err => {
                console.error('Error:', err);
                msgDiv.style.color = "red";
                msgDiv.innerText = "Network error";
            });
        }

        // 3. Handle session logout
        function logout() {
            fetch('/api/auth/logout', { method: 'POST' })
                .then(res => res.json())
                .then(json => {
                    window.location.href = '/login';
                });
        }
    </script>

</body>
</html>