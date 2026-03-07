package com.springinventarioproductos.repository;

import com.springinventarioproductos.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    List<ProductEntity> id(Long id);

    List<ProductEntity> findByInventoryId(long userId);
}
