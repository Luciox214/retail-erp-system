package org.example.stockerp.ventas.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stockerp.clientes.model.Cliente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    /** Total cobrado (subtotal de los productos menos el descuento). */
    private double total;
    /** Descuento en pesos aplicado a la venta. Null en ventas anteriores a este campo. */
    private Double descuento;
    /** EFECTIVO, TRANSFERENCIA, MERCADO_PAGO, TARJETA u OTRO. Null en ventas anteriores. */
    private String medioPago;
    /** Nota libre: seña, envío, encargo, etc. */
    @Column(length = 500)
    private String nota;
    /** Opcional: una venta puede ser a "consumidor final". */
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> items = new ArrayList<>();
    public void agregarItem(DetalleVenta item) {
        items.add(item);
        item.setVenta(this);
    }
}
