package com.springinventarioproductos.repository;

public class InventoryRepository {

    public static final String INSERT_INVENTORY = "INSERT INTO inventory (name) VALUES (?)";
    public static final String SELECT_INVENTORY = "SELECT * FROM inventory WHERE id = ?";
    public static final String SELECT_PRODUCT_BY_INVENTORY_ID = "SELECT id, productName, quantity FROM products WHERE inventoryId = ?";
}
