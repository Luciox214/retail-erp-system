package org.example.stockerp.productos.repository;

import org.example.stockerp.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    long countByStockGreaterThan(Integer stock);
    long countByActivoTrue();
    long countByStockLessThan(Integer stock);
}