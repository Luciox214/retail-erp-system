package org.example.stockerp.productos.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(length = 2000)
    private String descripcion;
    @Column(nullable = false)
    private Double costo;
    @Column(nullable = false)
    private Double precioVenta;
    @Column(nullable = false)
    private Integer stock;
    private String urlImagen;
    /** Visible en la tienda web. */
    private Boolean activo = true;
    /**
     * Un producto con ventas no se puede borrar (rompería el historial), así que se archiva:
     * deja de aparecer en el inventario y en la web, pero sus ventas pasadas se conservan.
     * Puede ser null en filas creadas antes de agregar esta columna (equivale a false).
     */
    @JsonIgnore
    private Boolean archivado = false;

    @JsonIgnore
    public boolean isArchivadoSeguro() {
        return Boolean.TRUE.equals(archivado);
    }
}
