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

    @PostMapping
    public ResponseEntity<MessageResponse>registrarVenta(@RequestBody VentaRequestDTO ventaRequestDTO){
        ventasService.registrarVenta(ventaRequestDTO);
        return ResponseEntity.ok(MessageResponse.success("Venta registrada con exito"));
    }
    @GetMapping
    public ResponseEntity<ResponseAPI<List<Venta>>>obtenerVentas(){
        List<Venta> ventas=ventasService.obtenerVentas();
        ResponseAPI<List<Venta>> responseAPI=new ResponseAPI<>("success","Lista de ventas obtenida con exito",ventas);
        return ResponseEntity.ok(responseAPI);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<Venta>>obtenerVentaPorId(@PathVariable Long id){
        Venta venta=ventasService.obtenerVentaPorId(id);
        ResponseAPI<Venta> responseAPI=new ResponseAPI<>("success","Venta obtenida con exito",venta);
        return ResponseEntity.ok(responseAPI);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse>eliminarVenta(@PathVariable Long id){
        ventasService.eliminarVenta(id);
        return ResponseEntity.ok(MessageResponse.success("Venta eliminada con exito"));
    }


}
