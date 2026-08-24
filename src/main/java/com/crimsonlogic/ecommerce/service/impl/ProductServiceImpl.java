    package com.crimsonlogic.ecommerce.service.impl;

    import com.crimsonlogic.ecommerce.dto.product.ProductRequestDTO;
    import com.crimsonlogic.ecommerce.dto.product.ProductResponseDTO;
import com.crimsonlogic.ecommerce.dto.product.ProductUpdateRequestDTO;
import com.crimsonlogic.ecommerce.entity.Category;
    import com.crimsonlogic.ecommerce.entity.Inventory;
    import com.crimsonlogic.ecommerce.entity.Product;
    import com.crimsonlogic.ecommerce.entity.Seller;
    import com.crimsonlogic.ecommerce.enumeration.ProductStatus;
    import com.crimsonlogic.ecommerce.exception.ValidationException;
    import com.crimsonlogic.ecommerce.repository.CategoryRepository;
    import com.crimsonlogic.ecommerce.repository.InventoryRepository;
    import com.crimsonlogic.ecommerce.repository.ProductRepository;
    import com.crimsonlogic.ecommerce.repository.SellerRepository;
    import com.crimsonlogic.ecommerce.service.ProductService;
    import com.crimsonlogic.ecommerce.util.IdGenerator;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    @Transactional
    public class ProductServiceImpl implements ProductService {

        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;
        private final SellerRepository sellerRepository;
        private final InventoryRepository inventoryRepository; // Used directly to prevent circular dependency

        public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                                  SellerRepository sellerRepository, InventoryRepository inventoryRepository) {
            this.productRepository = productRepository;
            this.categoryRepository = categoryRepository;
            this.sellerRepository = sellerRepository;
            this.inventoryRepository = inventoryRepository;
        }

        public ProductResponseDTO addProduct(
                ProductRequestDTO request,
                String userId,
                String role) {

            // ==========================================================
            // 1. CHECK CATEGORY
            // ==========================================================

            String categoryName = request.getCategoryName().trim();

            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() ->
                            new ValidationException(
                                    "Category '" + categoryName + "' not found."
                            )
                    );


            // ==========================================================
            // 2. CHECK DUPLICATE PRODUCT NAME
            // ==========================================================

            String productName = request.getName().trim();
            String brand = request.getBrand().trim();

            if (productRepository.existsByNameIgnoreCaseAndBrandIgnoreCase(
                    productName,
                    brand)) {

                throw new ValidationException(
                        "Product '" + productName +
                                "' with brand '" + brand +
                                "' already exists."
                );
            }


            // ==========================================================
            // 3. CREATE PRODUCT
            // ==========================================================

            Product product = new Product();

            product.setId(IdGenerator.generateId("PRO"));
            product.setName(productName);
            product.setBrand(request.getBrand().trim());
            product.setDescription(request.getDescription().trim());
            product.setPrice(request.getPrice());
            product.setCategory(category);
            product.setCreatedBy(userId);


            // ==========================================================
            // 4. SET PRODUCT OWNER BASED ON ROLE
            // ==========================================================

            if ("SELLER".equalsIgnoreCase(role)) {

                // Seller must exist
                Seller seller = sellerRepository.findById(userId)
                        .orElseThrow(() ->
                                new ValidationException("Seller not found.")
                        );

                // Associate product with logged-in seller
                product.setSeller(seller);

            } else if ("ADMIN".equalsIgnoreCase(role)) {

                // Admin-created product
                // seller remains NULL
                product.setSeller(null);

            } else {

                // Only ADMIN and SELLER are allowed
                throw new ValidationException(
                        "Only Admins and Sellers can add products."
                );
            }


            // ==========================================================
            // 5. SET INITIAL PRODUCT STATUS
            // ==========================================================

            if (request.getQuantity() > 0) {
                product.setStatus(ProductStatus.AVAILABLE);
            } else {
                product.setStatus(ProductStatus.OUT_OF_STOCK);
            }


            // ==========================================================
            // 6. SAVE PRODUCT
            // ==========================================================

            Product savedProduct = productRepository.save(product);


            // ==========================================================
            // 7. CREATE INVENTORY
            // ==========================================================

            Inventory inventory = new Inventory();

            inventory.setId(IdGenerator.generateId("INV"));
            inventory.setProduct(savedProduct);
            inventory.setQuantity(request.getQuantity());

            inventoryRepository.save(inventory);


            // ==========================================================
            // 8. RETURN RESPONSE
            // ==========================================================

            return mapToDTO(savedProduct);
        }

        @Override
        public ProductResponseDTO updateProduct(
                String productId,
                ProductUpdateRequestDTO request,
                String userId,
                String role) {

            // ==========================================================
            // 1. FIND PRODUCT
            // ==========================================================

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ValidationException("Product not found.")
                    );


            // ==========================================================
            // 2. CHECK OWNERSHIP
            // ==========================================================

            if ("SELLER".equalsIgnoreCase(role)) {

                if (product.getSeller() == null
                        || !product.getSeller().getId().equals(userId)) {

                    throw new ValidationException(
                            "You can only modify your own products."
                    );
                }
            }


            // ==========================================================
            // 3. ADMIN CAN MODIFY ONLY ADMIN PRODUCTS
            // ==========================================================

            if ("ADMIN".equalsIgnoreCase(role)
                    && product.getSeller() != null) {

                throw new ValidationException(
                        "Admins can only edit admin-created products."
                );
            }


            // ==========================================================
            // 4. UPDATE NAME
            // ==========================================================

            if (request.getName() != null
                    && !request.getName().trim().isEmpty()) {

                String newName = request.getName().trim();

                String currentBrand = product.getBrand();

                // Check duplicate name + brand
                if (!newName.equalsIgnoreCase(product.getName())
                        && productRepository
                        .existsByNameIgnoreCaseAndBrandIgnoreCase(
                                newName,
                                currentBrand
                        )) {

                    throw new ValidationException(
                            "Product '" + newName
                                    + "' with brand '" + currentBrand
                                    + "' already exists."
                    );
                }

                product.setName(newName);
            }


            // ==========================================================
            // 5. UPDATE BRAND
            // ==========================================================

            if (request.getBrand() != null
                    && !request.getBrand().trim().isEmpty()) {

                String newBrand = request.getBrand().trim();

                String currentName = product.getName();

                // Check duplicate name + brand
                if (!newBrand.equalsIgnoreCase(product.getBrand())
                        && productRepository
                        .existsByNameIgnoreCaseAndBrandIgnoreCase(
                                currentName,
                                newBrand
                        )) {

                    throw new ValidationException(
                            "Product '" + currentName
                                    + "' with brand '" + newBrand
                                    + "' already exists."
                    );
                }

                product.setBrand(newBrand);
            }


            // ==========================================================
            // 6. UPDATE DESCRIPTION
            // ==========================================================

            if (request.getDescription() != null
                    && !request.getDescription().trim().isEmpty()) {

                product.setDescription(
                        request.getDescription().trim()
                );
            }


            // ==========================================================
            // 7. UPDATE PRICE
            // ==========================================================

            if (request.getPrice() != null) {

                product.setPrice(
                        request.getPrice()
                );
            }


            // ==========================================================
            // 8. UPDATE CATEGORY
            // ==========================================================

            if (request.getCategoryName() != null
                    && !request.getCategoryName().trim().isEmpty()) {

                String categoryName =
                        request.getCategoryName().trim();

                Category category =
                        categoryRepository.findByName(categoryName)
                                .orElseThrow(() ->
                                        new ValidationException(
                                                "Category '" +
                                                        categoryName +
                                                        "' not found."
                                        )
                                );

                product.setCategory(category);
            }


            // ==========================================================
            // 9. SAVE PRODUCT
            // ==========================================================

            Product savedProduct =
                    productRepository.save(product);


            // ==========================================================
            // 10. RETURN UPDATED PRODUCT
            // ==========================================================

            return mapToDTO(savedProduct);
        }

        public List<ProductResponseDTO> getAllProducts() {
            return productRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        public List<ProductResponseDTO> getProductsBySeller(String sellerId) {
            return productRepository.findBySellerId(sellerId).stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        private ProductResponseDTO mapToDTO(Product product) {
            ProductResponseDTO dto = new ProductResponseDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setBrand(product.getBrand());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStatus(product.getStatus() != null ? product.getStatus().name() : "N/A");
            dto.setCategoryName(product.getCategory().getName());
            dto.setSellerName(product.getSeller() != null ? product.getSeller().getShopName() : "Admin");
            return dto;
        }

        public String deleteProduct(String productId, String userId, String role) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ValidationException("Product not found."));

            // 1. Verify Ownership
            if ("SELLER".equals(role) && (product.getSeller() == null || !product.getSeller().getId().equals(userId))) {
                throw new ValidationException("You can only delete your own products.");
            }
            if ("ADMIN".equals(role) && product.getSeller() != null) {
                throw new ValidationException("Admins can only delete admin-created products.");
            }

            // 2. Delete Associated Inventory First (to prevent Foreign Key constraint crash)
            inventoryRepository.findByProductId(productId).ifPresent(inventory ->
                    inventoryRepository.delete(inventory)
            );

            // 3. Delete the Product
            // Note: If this product is already inside a customer's Cart or Order, deleting it
            // will crash unless you also clear those carts/orders first!
            productRepository.delete(product);

            return "Product deleted successfully.";
        }

        // --- NEW: Get All Products with Pagination ---
        public Page<ProductResponseDTO> getAllProductsPaginated(int page, int size, String sortBy) {
            Pageable pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));
            return productRepository.findAll(pageable).map(this::mapToDTO);
        }

        // --- NEW: Search Products by Keyword (Name or Brand) ---
        public List<ProductResponseDTO> searchProducts(String keyword) {
            return productRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(keyword, keyword, org.springframework.data.domain.Pageable.unpaged())
                    .stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        // --- NEW: Advanced Search and Filter with Pagination ---
        public org.springframework.data.domain.Page<ProductResponseDTO> filterProducts(
                String keyword, String categoryName, Double minPrice, Double maxPrice, int page, int size, String sortBy) {

            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));

            return productRepository.searchAndFilterProducts(keyword, categoryName, minPrice, maxPrice, pageable)
                    .map(this::mapToDTO);
        }

		
    }