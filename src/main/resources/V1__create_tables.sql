DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS products;

CREATE TABLE inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    inventory_id BIGINT NOT NULL,
    CONSTRAINT fk_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE CASCADE
);