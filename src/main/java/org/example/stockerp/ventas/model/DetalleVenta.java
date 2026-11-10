package org.example.stockerp.ventas.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.example.stockerp.productos.model.Producto;

@Entity
@Table(name = "detalle_ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore
    private Venta venta;
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnoreProperties({"costo", "stock", "activo", "descripcion"})
    private Producto producto;
    private Integer cantidad;
    private Double precioUnitario;
    /**
     * Costo del producto al momento de la venta. Así la ganancia de ventas pasadas no cambia
     * si después se actualiza el costo. Null en ventas anteriores (se usa el costo actual).
     */
    @JsonIgnore
    private Double costoUnitario;
}
