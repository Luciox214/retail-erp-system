package org.example.stockerp.catalogo;

import org.example.stockerp.productos.model.ProductoCatalogoDTO;
import org.example.stockerp.productos.service.ProductoService;
import org.example.stockerp.response.MessageResponse;
import org.example.stockerp.response.ResponseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/catalogo")
public class CatalogoController {
    @Autowired
    ProductoService productoService;
    @GetMapping()
    public ResponseEntity<ResponseAPI<List<ProductoCatalogoDTO>>>obtenerProductosCatalogo(){
        List<ProductoCatalogoDTO> productos=productoService.obtenerProductosCatalogo();
        ResponseAPI<List<ProductoCatalogoDTO>> responseAPI=new ResponseAPI<>("success","Lista de productos obtenida con exito",productos);
        return ResponseEntity.ok(responseAPI);
    }
    @PatchMapping("/cambiar-estado/{id}")
    public ResponseEntity<MessageResponse> cambiarEstadoProducto(@PathVariable Long id, @RequestParam boolean estado){
        productoService.cambiarEstadoProducto(id,estado);
        return ResponseEntity.ok(MessageResponse.success("Estado del producto actualizado con exito"));
    }
    @GetMapping("/ping")
    public ResponseEntity<MessageResponse>ping(){
        return ResponseEntity.ok(MessageResponse.success("Catalogo funcionando correctamente"));
    }
}
