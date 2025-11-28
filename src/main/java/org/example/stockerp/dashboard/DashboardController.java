package org.example.stockerp.dashboard;
import org.example.stockerp.productos.repository.ProductoRepository;
import org.example.stockerp.ventas.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    @Autowired
    private VentasRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardDTO> obtenerResumen() {
        DashboardDTO dto = new DashboardDTO();


        LocalDateTime inicioDia = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime finDia = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime inicioMes = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime finMes = LocalDateTime.of(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()), LocalTime.MAX);

        Double ventasHoy = ventaRepository.sumarVentasEnRango(inicioDia, finDia);
        dto.setVentasHoy(ventasHoy != null ? ventasHoy : 0.0);
        dto.setCantidadVentasHoy(ventaRepository.contarVentasEnRango(inicioDia, finDia));

        Double ventasMes = ventaRepository.sumarVentasEnRango(inicioMes, finMes);
        dto.setVentasMes(ventasMes != null ? ventasMes : 0.0);

        Double ganancia = ventaRepository.calcularGananciaEnRango(inicioMes, finMes);
        dto.setGananciaMes(ganancia != null ? ganancia : 0.0);

        dto.setTotalProductos(productoRepository.count());
        dto.setProductosEnStock(productoRepository.countByStockGreaterThan(0));
        dto.setProductosWeb(productoRepository.countByActivoTrue());
        dto.setProductosBajoStock(productoRepository.countByStockLessThan(3));

        List<Map<String, Object>> topClientes = ventaRepository.encontrarTopClientes(inicioMes, finMes, PageRequest.of(0, 4));

        String[] colores = {"#f97316", "#3b82f6", "#10b981", "#f43f5e"};
        for (int i = 0; i < topClientes.size(); i++) {
            topClientes.get(i).put("color", colores[i % colores.length]);
        }

        dto.setTopClientes(topClientes);

        return ResponseEntity.ok(dto);
    }
}