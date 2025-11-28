package org.example.stockerp.ventas.repository;

import org.example.stockerp.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentasRepository extends JpaRepository<Venta,Long> {
}
