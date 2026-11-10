package org.example.stockerp.reportes;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.stockerp.ventas.model.Venta;
import org.example.stockerp.ventas.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReporteService {

    @Autowired
    private VentasRepository ventaRepository;

    public byte[] generarFacturaPdf(Long idVenta) throws JRException {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new org.example.stockerp.exception.NoEncontradoException("La venta no existe o fue anulada."));

        InputStream reportStream = getClass().getResourceAsStream("/reportes/factura.jrxml");

        if (reportStream == null) {
            throw new RuntimeException("No se encontró el archivo /reportes/factura.jrxml");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(venta.getItems());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nroFactura", venta.getId().toString());
        parameters.put("fecha", venta.getFecha().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        parameters.put("clienteNombre", venta.getCliente() != null ? venta.getCliente().getNombre() : "Consumidor Final");
        parameters.put("clienteTelefono", venta.getCliente() != null ? venta.getCliente().getTelefono() : "");
        parameters.put("totalVenta", venta.getTotal());
        parameters.put("descuento", venta.getDescuento());
        parameters.put("medioPago", venta.getMedioPago() == null ? null : switch (venta.getMedioPago()) {
            case "EFECTIVO" -> "Efectivo";
            case "TRANSFERENCIA" -> "Transferencia";
            case "MERCADO_PAGO" -> "Mercado Pago";
            case "TARJETA" -> "Tarjeta";
            default -> "Otro";
        });
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}