<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to E-Commerce Market Place</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: "Segoe UI", Arial, sans-serif;
            min-height: 100vh;
            background: linear-gradient(135deg, #eef2ff 0%, #f8fafc 50%, #e0f2fe 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: #1e293b;
        }

        .page-container {
            width: 100%;
            max-width: 1100px;
            padding: 30px;
        }

        .marketplace-card {
            background: rgba(255, 255, 255, 0.96);
            border-radius: 24px;
            overflow: hidden;
            box-shadow: 0 25px 60px rgba(15, 23, 42, 0.12);
            display: grid;
            grid-template-columns: 1.1fr 1fr;
            min-height: 570px;
        }

        /* =========================================
           LEFT SECTION
           ========================================= */

        .hero-section {
            background: linear-gradient(145deg, #0f172a, #1e3a8a);
            color: white;
            padding: 55px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            position: relative;
            overflow: hidden;
        }

        .hero-section::before {
            content: "";
            position: absolute;
            width: 300px;
            height: 300px;
            border-radius: 50%;
            background: rgba(59, 130, 246, 0.18);
            top: -120px;
            right: -100px;
        }

        .hero-section::after {
            content: "";
            position: absolute;
            width: 220px;
            height: 220px;
            border-radius: 50%;
            background: rgba(96, 165, 250, 0.12);
            bottom: -100px;
            left: -80px;
        }

        .brand {
            position: relative;
            z-index: 1;
            font-size: 14px;
            font-weight: 700;
            letter-spacing: 2px;
            text-transform: uppercase;
            color: #93c5fd;
            margin-bottom: 22px;
        }

        .hero-section h1 {
            position: relative;
            z-index: 1;
            font-size: 46px;
            line-height: 1.12;
            margin-bottom: 22px;
            font-weight: 700;
        }

        .hero-section h1 span {
            color: #60a5fa;
        }

        .hero-description {
            position: relative;
            z-index: 1;
            color: #cbd5e1;
            font-size: 17px;
            line-height: 1.7;
            max-width: 440px;
            margin-bottom: 35px;
        }

        .features {
            position: relative;
            z-index: 1;
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .feature {
            display: flex;
            align-items: center;
            gap: 12px;
            color: #e2e8f0;
            font-size: 14px;
        }

        .feature-icon {
            width: 34px;
            height: 34px;
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.1);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
        }

        /* =========================================
           RIGHT SECTION
           ========================================= */

        .portal-section {
            padding: 55px 50px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .portal-header {
            margin-bottom: 35px;
        }

        .portal-header h2 {
            font-size: 30px;
            color: #0f172a;
            margin-bottom: 10px;
        }

        .portal-header p {
            color: #64748b;
            font-size: 15px;
            line-height: 1.6;
        }

        .portal-options {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .portal-link {
            display: flex;
            align-items: center;
            justify-content: space-between;
            text-decoration: none;
            padding: 20px 22px;
            border: 1px solid #e2e8f0;
            border-radius: 14px;
            background: #ffffff;
            transition: all 0.25s ease;
            color: #0f172a;
        }

        .portal-link:hover {
            transform: translateY(-3px);
            box-shadow: 0 12px 25px rgba(15, 23, 42, 0.10);
        }

        .portal-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .portal-icon {
            width: 48px;
            height: 48px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 21px;
            font-weight: bold;
            color: white;
        }

        .customer-icon {
            background: linear-gradient(135deg, #2563eb, #3b82f6);
        }

        .seller-icon {
            background: linear-gradient(135deg, #ea580c, #f97316);
        }

        .admin-icon {
            background: linear-gradient(135deg, #15803d, #22c55e);
        }

        .portal-text strong {
            display: block;
            font-size: 16px;
            margin-bottom: 4px;
        }

        .portal-text span {
            font-size: 13px;
            color: #64748b;
        }

        .arrow {
            font-size: 22px;
            color: #94a3b8;
            transition: transform 0.25s ease;
        }

        .portal-link:hover .arrow {
            transform: translateX(5px);
        }

        .customer-link:hover {
            border-color: #93c5fd;
            background: #eff6ff;
        }

        .seller-link:hover {
            border-color: #fdba74;
            background: #fff7ed;
        }

        .admin-link:hover {
            border-color: #86efac;
            background: #f0fdf4;
        }

        .footer-text {
            margin-top: 30px;
            text-align: center;
            font-size: 12px;
            color: #94a3b8;
        }

        /* =========================================
           RESPONSIVE DESIGN
           ========================================= */

        @media (max-width: 850px) {

            .marketplace-card {
                grid-template-columns: 1fr;
            }

            .hero-section {
                padding: 40px;
            }

            .hero-section h1 {
                font-size: 36px;
            }

            .portal-section {
                padding: 40px;
            }
        }

        @media (max-width: 500px) {

            body {
                align-items: flex-start;
            }

            .page-container {
                padding: 15px;
            }

            .hero-section {
                padding: 35px 25px;
            }

            .hero-section h1 {
                font-size: 31px;
            }

            .hero-description {
                font-size: 15px;
            }

            .portal-section {
                padding: 30px 22px;
            }

            .portal-header h2 {
                font-size: 25px;
            }
        }
    </style>
</head>

<body>

<div class="page-container">

    <div class="marketplace-card">

        <!-- =========================================
             LEFT: MARKETPLACE INTRODUCTION
             ========================================= -->

        <div class="hero-section">

            <div class="brand">
                E-Commerce Marketplace
            </div>

            <h1>
                Everything you need,<br>
                <span>all in one place.</span>
            </h1>

            <p class="hero-description">
                Welcome to our marketplace platform.
                Shop products, manage your store, and
                control your marketplace from one secure platform.
            </p>

            <div class="features">

                <div class="feature">
                    <div class="feature-icon">✓</div>
                    <span>Secure and reliable marketplace</span>
                </div>

                <div class="feature">
                    <div class="feature-icon">✓</div>
                    <span>Easy product and order management</span>
                </div>

                <div class="feature">
                    <div class="feature-icon">✓</div>
                    <span>Dedicated portals for every user</span>
                </div>

            </div>

        </div>


        <!-- =========================================
             RIGHT: PORTAL SELECTION
             ========================================= -->

        <div class="portal-section">

            <div class="portal-header">

                <h2>Choose your portal</h2>

                <p>
                    Select the appropriate portal to continue
                    to your account.
                </p>

            </div>


            <div class="portal-options">

                <!-- CUSTOMER LOGIN -->
                <a href="<%= request.getContextPath() %>/login?role=CUSTOMER"
                   class="portal-link customer-link">

                    <div class="portal-info">

                        <div class="portal-icon customer-icon">
                            C
                        </div>

                        <div class="portal-text">
                            <strong>Customer Portal</strong>
                            <span>Shop products and manage your orders</span>
                        </div>

                    </div>

                    <div class="arrow">
                        →
                    </div>

                </a>


                <!-- SELLER LOGIN -->
                <a href="<%= request.getContextPath() %>/login?role=SELLER"
                   class="portal-link seller-link">

                    <div class="portal-info">

                        <div class="portal-icon seller-icon">
                            S
                        </div>

                        <div class="portal-text">
                            <strong>Seller Portal</strong>
                            <span>Manage products, inventory and sales</span>
                        </div>

                    </div>

                    <div class="arrow">
                        →
                    </div>

                </a>


                <!-- ADMIN LOGIN -->
                <a href="<%= request.getContextPath() %>/login?role=ADMIN"
                   class="portal-link admin-link">

                    <div class="portal-info">

                        <div class="portal-icon admin-icon">
                            A
                        </div>

                        <div class="portal-text">
                            <strong>Admin Portal</strong>
                            <span>Manage and monitor the marketplace</span>
                        </div>

                    </div>

                    <div class="arrow">
                        →
                    </div>

                </a>

            </div>


            <div class="footer-text">
                Secure access • E-Commerce Marketplace
            </div>

        </div>

    </div>

</div>

</body>
</html>