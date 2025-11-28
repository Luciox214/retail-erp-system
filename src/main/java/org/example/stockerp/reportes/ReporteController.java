package org.example.stockerp.reportes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;
    @GetMapping("/factura/{idVenta}")
    public ResponseEntity<byte[]> descargarFactura(@PathVariable Long idVenta) {
        try {
            byte[] reportePdf = reporteService.generarFacturaPdf(idVenta);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=factura_" + idVenta + ".pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(reportePdf);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}