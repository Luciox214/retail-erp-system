package org.example.stockerp.productos.model;
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
    private String descripcion;
    @Column(nullable = false)
    private Double costo;
    @Column(nullable = false)
    private Double precioVenta;
    @Column(nullable = false)
    private Integer stock;
    private String urlImagen;
    private Boolean activo=true;
}
