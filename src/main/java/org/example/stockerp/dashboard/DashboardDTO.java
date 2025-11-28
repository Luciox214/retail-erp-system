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
    private Double gananciaMes;
    private Long totalProductos;
    private Long productosEnStock;
    private Long productosWeb;
    private Long productosBajoStock;
    private List<Map<String, Object>> topClientes;
}
