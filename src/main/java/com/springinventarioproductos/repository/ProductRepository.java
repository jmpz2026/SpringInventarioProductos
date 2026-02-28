package com.springinventarioproductos.repository;

public class ProductRepository {
    public static final String SELECT_PRODUCT = "SELECT * FROM products WHERE id = ?";
    public static final String INSERT_PRODUCT = "INSERT INTO products (productName, quantity, inventoryId) VALUES (?, ?, ?)";
}
