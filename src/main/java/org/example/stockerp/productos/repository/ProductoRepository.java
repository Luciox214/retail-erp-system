package org.example.stockerp.productos.repository;
import org.example.stockerp.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // "archivado" puede ser NULL en filas viejas: se trata como false.
    String NO_ARCHIVADO = "(p.archivado IS NULL OR p.archivado = false)";

    @Query("SELECT p FROM Producto p WHERE " + NO_ARCHIVADO + " ORDER BY p.nombre")
    List<Producto> findVigentes();

    @Query("SELECT p FROM Producto p WHERE " + NO_ARCHIVADO + " AND p.activo = true")
    List<Producto> findVisiblesEnWeb();

    @Query("SELECT COUNT(p) FROM Producto p WHERE " + NO_ARCHIVADO)
    long contarVigentes();

    @Query("SELECT COUNT(p) FROM Producto p WHERE " + NO_ARCHIVADO + " AND p.stock > 0")
    long contarConStock();

    @Query("SELECT COUNT(p) FROM Producto p WHERE " + NO_ARCHIVADO + " AND p.activo = true")
    long contarVisiblesEnWeb();

    @Query("SELECT p FROM Producto p WHERE " + NO_ARCHIVADO + " AND p.stock < :limite ORDER BY p.stock, p.nombre")
    List<Producto> findBajoStock(@Param("limite") int limite);
}
