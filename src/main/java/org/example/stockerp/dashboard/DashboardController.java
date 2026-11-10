package org.example.stockerp.dashboard;

import org.example.stockerp.productos.model.Producto;
import org.example.stockerp.productos.repository.ProductoRepository;
import org.example.stockerp.ventas.model.Venta;
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
import java.util.*;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private static final int LIMITE_BAJO_STOCK = 3;
    // Colores de la paleta de la marca para el gráfico de clientes
    private static final String[] COLORES = {"#D6836B", "#F4A261", "#A88B7D", "#5D4D43"};

    @Autowired
    private VentasRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private static double nz(Double d) { return d == null ? 0.0 : d; }

    @GetMapping("/resumen")
    public ResponseEntity<DashboardDTO> obtenerResumen() {
        DashboardDTO dto = new DashboardDTO();
        LocalDate hoy = LocalDate.now();

        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = LocalDateTime.of(hoy, LocalTime.MAX);
        LocalDateTime inicioMes = hoy.withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMes = LocalDateTime.of(hoy.withDayOfMonth(hoy.lengthOfMonth()), LocalTime.MAX);

        dto.setVentasHoy(nz(ventaRepository.sumarVentasEnRango(inicioDia, finDia)));
        dto.setCantidadVentasHoy(ventaRepository.contarVentasEnRango(inicioDia, finDia));
        dto.setVentasMes(nz(ventaRepository.sumarVentasEnRango(inicioMes, finMes)));
        dto.setCantidadVentasMes(ventaRepository.contarVentasEnRango(inicioMes, finMes));
        dto.setGananciaMes(nz(ventaRepository.calcularGananciaEnRango(inicioMes, finMes))
                - nz(ventaRepository.sumarDescuentosEnRango(inicioMes, finMes)));

        // Mismo tramo del mes pasado (ej.: del 1 al 10) para comparar de forma justa
        LocalDate mesPasado = hoy.minusMonths(1);
        LocalDate finComparable = mesPasado.withDayOfMonth(Math.min(hoy.getDayOfMonth(), mesPasado.lengthOfMonth()));
        dto.setVentasMesAnteriorMismoPeriodo(nz(ventaRepository.sumarVentasEnRango(
                mesPasado.withDayOfMonth(1).atStartOfDay(), LocalDateTime.of(finComparable, LocalTime.MAX))));

        dto.setTotalProductos(productoRepository.contarVigentes());
        dto.setProductosEnStock(productoRepository.contarConStock());
        dto.setProductosWeb(productoRepository.contarVisiblesEnWeb());

        List<Producto> bajos = productoRepository.findBajoStock(LIMITE_BAJO_STOCK);
        dto.setProductosBajoStock((long) bajos.size());
        List<Map<String, Object>> bajoStock = new ArrayList<>();
        for (Producto p : bajos.subList(0, Math.min(6, bajos.size()))) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", p.getId());
            m.put("nombre", p.getNombre());
            m.put("stock", p.getStock());
            m.put("urlImagen", p.getUrlImagen());
            bajoStock.add(m);
        }
        dto.setBajoStock(bajoStock);

        // Ventas de los últimos 7 días, con 0 en los días sin ventas
        LocalDate desde = hoy.minusDays(6);
        Map<LocalDate, Double> porDia = new TreeMap<>();
        for (int i = 0; i < 7; i++) porDia.put(desde.plusDays(i), 0.0);
        for (Venta v : ventaRepository.findByFechaBetweenOrderByFechaAsc(desde.atStartOfDay(), finDia)) {
            porDia.merge(v.getFecha().toLocalDate(), v.getTotal(), Double::sum);
        }
        List<Map<String, Object>> ultimos7 = new ArrayList<>();
        porDia.forEach((fecha, total) -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("fecha", fecha.toString());
            m.put("total", total);
            ultimos7.add(m);
        });
        dto.setUltimos7Dias(ultimos7);

        List<Map<String, Object>> topClientes = new ArrayList<>();
        for (Map<String, Object> row : ventaRepository.encontrarTopClientes(inicioMes, finMes, PageRequest.of(0, 4))) {
            Map<String, Object> m = new LinkedHashMap<>(row);
            m.put("color", COLORES[topClientes.size() % COLORES.length]);
            topClientes.add(m);
        }
        dto.setTopClientes(topClientes);

        return ResponseEntity.ok(dto);
    }
}
