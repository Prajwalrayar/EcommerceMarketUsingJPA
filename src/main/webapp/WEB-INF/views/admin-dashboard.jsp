<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background-color: #eceff1; }
        .header { display: flex; justify-content: space-between; align-items: center; background: #fff; padding: 15px 20px; border-radius: 8px; margin-bottom: 20px; }
        .stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin-bottom: 20px; }
        .stat-box { background: #1976d2; color: white; padding: 20px; border-radius: 8px; text-align: center; }
        .stat-box h3 { margin: 0; font-size: 2em; }
        .stat-box p { margin: 5px 0 0 0; font-size: 0.9em; text-transform: uppercase; }
        .card { background: white; padding: 20px; border-radius: 8px; max-width: 400px; }
        .form-group input { width: 100%; padding: 8px; box-sizing: border-box; margin-bottom: 10px; }
        button { background-color: #4caf50; color: white; border: none; padding: 10px 15px; border-radius: 4px; cursor: pointer; font-weight: bold; width: 100%; }
        .message { font-size: 0.9em; font-weight: bold; margin-bottom: 10px; }
        .error { color: red; }
        .success { color: green; }
    </style>
</head>
<body>

    <div class="header">
        <h2>Admin Control Center</h2>
        <!-- Fixed logout link with dynamic context path -->
        <a href="<%= request.getContextPath() %>/login" style="color: #d32f2f; font-weight: bold; text-decoration: none;">Logout</a>
    </div>

    <!-- Platform Stats -->
    <div class="stats-grid" id="stats-container">
        <div class="stat-box"><h3>...</h3><p>Total Revenue</p></div>
        <div class="stat-box"><h3>...</h3><p>Active Orders</p></div>
        <div class="stat-box"><h3>...</h3><p>Total Customers</p></div>
        <div class="stat-box"><h3>...</h3><p>Total Sellers</p></div>
    </div>

    <!-- Add Category Form -->
    <div class="card">
        <h3>Create New Category</h3>
        <div id="cat-msg" class="message"></div>
        <form id="addCategoryForm">
            <div class="form-group"><input type="text" id="catName" placeholder="Category Name (e.g., Electronics)" required></div>
            <div class="form-group"><input type="text" id="catDesc" placeholder="Category Description" required></div>
            <button type="submit">Add Category</button>
        </form>
    </div>

    <script>
        const contextPath = '<%= request.getContextPath() %>';

        window.onload = loadStats;

        function loadStats() {
                    fetch(contextPath + '/api/admin/stats')
                        .then(res => res.json())
                        .then(json => {
                            if (json.success && json.data) {
                                const s = json.data;
                                document.getElementById('stats-container').innerHTML =
                                    '<div class="stat-box"><h3>₹' + (s.totalRevenue || 0) + '</h3><p>Total Revenue</p></div>' +
                                    '<div class="stat-box"><h3>' + (s.totalOrders || 0) + '</h3><p>Active Orders</p></div>' +
                                    '<div class="stat-box"><h3>' + (s.totalCustomers || 0) + '</h3><p>Customers</p></div>' +
                                    '<div class="stat-box"><h3>' + (s.totalSellers || 0) + '</h3><p>Sellers</p></div>';
                            }
                        }).catch(err => console.error(err));
        }

        document.getElementById('addCategoryForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const msgBox = document.getElementById('cat-msg');
            msgBox.innerText = "Creating...";

            const payload = {
                name: document.getElementById('catName').value,
                description: document.getElementById('catDesc').value
            };

            fetch(contextPath + '/api/admin/categories/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(res => res.json())
            .then(json => {
                if (json.success) {
                    msgBox.innerText = "Category created successfully!";
                    msgBox.className = "message success";
                    document.getElementById('addCategoryForm').reset();
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