<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Seller Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background-color: #f4f6f8; }
        .header { display: flex; justify-content: space-between; align-items: center; background: #fff; padding: 15px 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-bottom: 20px; }
        .grid { display: grid; grid-template-columns: 1fr 2fr; gap: 20px; }
        .card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 12px; }
        .form-group input, .form-group textarea, .form-group select { width: 100%; padding: 9px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        button { background-color: #1976d2; color: white; border: none; padding: 10px 15px; border-radius: 4px; cursor: pointer; font-weight: bold; }
        button:hover { background-color: #115293; }
        .product-item { border-bottom: 1px solid #eee; padding: 10px 0; display: flex; justify-content: space-between; }
        .message { font-size: 0.9em; font-weight: bold; margin-bottom: 10px; }
        .error { color: red; }
        .success { color: green; }
        .logout-btn { color: red; text-decoration: none; font-weight: bold; cursor: pointer; background: none; border: none; font-size: 1em; padding: 0; }
        .logout-btn:hover { text-decoration: underline; }
    </style>
</head>
<body>

    <div class="header">
        <h2>Seller Portal</h2>
        <button onclick="logoutUser()" class="logout-btn">Logout</button>
    </div>

    <div class="grid">
        <!-- Add Product Form -->
        <div class="card">
            <h3>Add New Product</h3>
            <div id="add-msg" class="message"></div>
            <form id="addProductForm">
                <div class="form-group"><input type="text" id="name" placeholder="Product Name" required></div>
                <div class="form-group"><input type="text" id="brand" placeholder="Brand Name" required></div>
                <div class="form-group"><input type="number" id="price" placeholder="Price (₹)" step="0.01" required></div>

                <!-- Category Dropdown -->
                <div class="form-group">
                    <select id="categoryName" required>
                        <option value="">Select Category Name</option>
                    </select>
                </div>

                <div class="form-group"><textarea id="description" placeholder="Product Description" rows="3"></textarea></div>
                <button type="submit" style="width: 100%;">Publish Product</button>
            </form>
        </div>

        <!-- My Products List -->
        <div class="card">
            <h3>My Inventory Listings</h3>
            <div id="my-products">
                <p>Loading your products...</p>
            </div>
        </div>
    </div>

    <script>
        const contextPath = '<%= request.getContextPath() %>';

        window.addEventListener('DOMContentLoaded', function() {
            loadCategories();
            loadMyProducts();
        });

        function logoutUser() {
            fetch(contextPath + '/api/auth/logout', {
                method: 'POST'
            })
            .catch(err => console.log('Session clear request sent'))
            .finally(() => {
                window.location.href = contextPath + '/login';
            });
        }

        function loadCategories() {
                    fetch(contextPath + '/api/categories')
                        .then(res => {
                            if (!res.ok) {
                                throw new Error("HTTP error! status: " + res.status);
                            }
                            return res.json();
                        })
                        .then(json => {
                            console.log("Categories API Full Response:", json);
                            const select = document.getElementById('categoryName');

                            // Reset options
                            select.innerHTML = '<option value="">Select Category Name</option>';

                            const categories = json.data ? json.data : (Array.isArray(json) ? json : []);

                            categories.forEach(cat => {
                                const option = document.createElement('option');

                                // Fallback across different possible property names for category text
                                const displayName = cat.name || cat.categoryName || cat.title || (typeof cat === 'string' ? cat : '');

                                if (displayName) {
                                    option.value = displayName;
                                    option.textContent = displayName;
                                    select.appendChild(option);
                                }
                            });
                        })
                        .catch(err => console.error('Failed to load categories:', err));
                }

        function loadMyProducts() {
            fetch(contextPath + '/api/products/my-products')
                .then(res => res.json())
                .then(json => {
                    const container = document.getElementById('my-products');
                    container.innerHTML = '';
                    if (json.success && json.data && json.data.length > 0) {
                        json.data.forEach(p => {
                            container.innerHTML += `
                                <div class="product-item">
                                    <div>
                                        <strong>\${p.name}</strong> (\${p.brand})<br>
                                        <small>Price: ₹\${p.price} | Category: \${p.category ? p.category.name : 'N/A'}</small>
                                    </div>
                                </div>
                            `;
                        });
                    } else if (json.success) {
                        container.innerHTML = '<p>You have not listed any products yet.</p>';
                    } else {
                        container.innerHTML = `<p class="error">\${json.message}</p>`;
                    }
                }).catch(err => console.error(err));
        }

        document.getElementById('addProductForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const msgBox = document.getElementById('add-msg');
            msgBox.innerText = "Publishing...";

            const payload = {
                name: document.getElementById('name').value,
                brand: document.getElementById('brand').value,
                price: parseFloat(document.getElementById('price').value),
                categoryName: document.getElementById('categoryName').value,
                description: document.getElementById('description').value
            };

            fetch(contextPath + '/api/products', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(res => res.json())
            .then(json => {
                if (json.success) {
                    msgBox.innerText = "Product published successfully!";
                    msgBox.className = "message success";
                    document.getElementById('addProductForm').reset();
                    loadMyProducts();
                    setTimeout(() => msgBox.innerText = '', 3000);
                } else {
                    msgBox.innerText = json.message;
                    msgBox.className = "message error";
                }
            }).catch(err => {
                msgBox.innerText = "Network error.";
                msgBox.className = "message error";
            });
        });
    </script>
</body>
</html>