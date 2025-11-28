package org.example.stockerp.productos.controller;
import org.example.stockerp.productos.model.Producto;
import org.example.stockerp.productos.service.ProductoService;
import org.example.stockerp.response.MessageResponse;
import org.example.stockerp.response.ResponseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/productos")
public class ProductoController {
    @Autowired
    ProductoService productoService;

    @PostMapping
    public ResponseEntity<MessageResponse> crearProducto(@RequestBody Producto producto){
        productoService.crearProducto(producto);
        return ResponseEntity.ok(MessageResponse.success("Producto creado con exito"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminarProducto(@PathVariable Long id){
        productoService.eliminarProducto(id);
        return ResponseEntity.ok(MessageResponse.success("Producto eliminado con exito"));
    }
    @PutMapping
    public ResponseEntity<MessageResponse> actualizarProducto(@RequestBody Producto producto){
        productoService.actualizarProducto(producto);
        return ResponseEntity.ok(MessageResponse.success("Producto actualizado con exito"));
    }
    @GetMapping()
    public ResponseEntity<ResponseAPI<List<Producto>>>obtenerProductos(){
        List<Producto> productos=productoService.obtenerProductos();
        ResponseAPI<List<Producto>> responseAPI=new ResponseAPI<>("success","Lista de productos obtenida con exito",productos);
        return ResponseEntity.ok(responseAPI);
    }

}
