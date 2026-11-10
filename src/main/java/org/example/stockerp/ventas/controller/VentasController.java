package org.example.stockerp.ventas.controller;
import org.example.stockerp.response.MessageResponse;
import org.example.stockerp.response.ResponseAPI;
import org.example.stockerp.ventas.model.Venta;
import org.example.stockerp.ventas.model.VentaRequestDTO;
import org.example.stockerp.ventas.service.VentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ventas")
public class VentasController {
    @Autowired
    VentasService ventasService;

    /** Devuelve la venta creada (con su id) para poder mostrar el comprobante enseguida. */
    @PostMapping
    public ResponseEntity<ResponseAPI<Venta>> registrarVenta(@RequestBody VentaRequestDTO ventaRequestDTO){
        Venta venta = ventasService.registrarVenta(ventaRequestDTO);
        return ResponseEntity.ok(ResponseAPI.success("Venta registrada", venta));
    }
    @GetMapping
    public ResponseEntity<ResponseAPI<List<Venta>>>obtenerVentas(){
        return ResponseEntity.ok(ResponseAPI.success("Lista de ventas obtenida con exito", ventasService.obtenerVentas()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<Venta>>obtenerVentaPorId(@PathVariable Long id){
        return ResponseEntity.ok(ResponseAPI.success("Venta obtenida con exito", ventasService.obtenerVentaPorId(id)));
    }
    /** Anula la venta: se borra y el stock vendido vuelve al inventario. */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse>eliminarVenta(@PathVariable Long id){
        ventasService.eliminarVenta(id);
        return ResponseEntity.ok(MessageResponse.success("Venta anulada. El stock se devolvió al inventario."));
    }
}
