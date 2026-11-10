package org.example.stockerp.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private Double ventasHoy;
    private Long cantidadVentasHoy;
    private Double ventasMes;
    private Long cantidadVentasMes;
    /** Ganancia del mes: (precio - costo) de lo vendido, menos los descuentos. */
    private Double gananciaMes;
    /** Ventas del mismo período del mes anterior (del día 1 al día de hoy), para comparar. */
    private Double ventasMesAnteriorMismoPeriodo;
    private Long totalProductos;
    private Long productosEnStock;
    private Long productosWeb;
    private Long productosBajoStock;
    /** Hasta 6 productos con menos stock: id, nombre, stock, urlImagen. */
    private List<Map<String, Object>> bajoStock;
    /** Últimos 7 días (incluye hoy): fecha (yyyy-MM-dd) y total. */
    private List<Map<String, Object>> ultimos7Dias;
    private List<Map<String, Object>> topClientes;
}
