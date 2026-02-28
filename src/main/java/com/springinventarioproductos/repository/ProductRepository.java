package com.springinventarioproductos.repository;

public class ProductRepository {
    public static final String SELECT_PRODUCT = "SELECT * FROM products WHERE id = ?";
    public static final String INSERT_PRODUCT = "INSERT INTO products (product_name, quantity, inventory_id) VALUES (?, ?, ?)";
    public static final String UPDATE_PRODUCT = "UPDATE products SET quantity = ? WHERE id = ?";
    public static final String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";
}
