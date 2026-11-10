package org.example.stockerp.ventas.repository;

import org.example.stockerp.ventas.model.Venta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface VentasRepository extends JpaRepository<Venta, Long> {

    List<Venta> findAllByOrderByFechaDesc();

    List<Venta> findByFechaBetweenOrderByFechaAsc(LocalDateTime inicio, LocalDateTime fin);

    boolean existsByClienteId(Long clienteId);

    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    Double sumarVentasEnRango(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    Long contarVentasEnRango(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    /** Ganancia bruta de los productos (usa el costo guardado al vender; si no existe, el actual). */
    @Query("SELECT SUM((i.precioUnitario - COALESCE(i.costoUnitario, p.costo)) * i.cantidad) " +
            "FROM Venta v JOIN v.items i JOIN i.producto p " +
            "WHERE v.fecha BETWEEN :inicio AND :fin")
    Double calcularGananciaEnRango(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(COALESCE(v.descuento, 0)) FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    Double sumarDescuentosEnRango(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT new map(c.nombre as label, SUM(v.total) as value) " +
            "FROM Venta v JOIN v.cliente c " +
            "WHERE v.fecha BETWEEN :inicio AND :fin " +
            "GROUP BY c.nombre " +
            "ORDER BY SUM(v.total) DESC")
    List<Map<String, Object>> encontrarTopClientes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin, Pageable pageable);
}
