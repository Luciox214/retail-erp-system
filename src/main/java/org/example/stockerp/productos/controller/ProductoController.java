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
    public ResponseEntity<ResponseAPI<Producto>> crearProducto(@RequestBody Producto producto){
        Producto creado = productoService.crearProducto(producto);
        return ResponseEntity.ok(ResponseAPI.success("Producto creado", creado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> eliminarProducto(@PathVariable Long id){
        ProductoService.ResultadoEliminacion r = productoService.eliminarProducto(id);
        String mensaje = r == ProductoService.ResultadoEliminacion.ARCHIVADO
                ? "El producto tenía ventas, así que se archivó: ya no aparece en el inventario ni en la web, pero su historial se conserva."
                : "Producto eliminado";
        return ResponseEntity.ok(MessageResponse.success(mensaje));
    }

    @PutMapping
    public ResponseEntity<ResponseAPI<Producto>> actualizarProducto(@RequestBody Producto producto){
        Producto actualizado = productoService.actualizarProducto(producto);
        return ResponseEntity.ok(ResponseAPI.success("Producto actualizado", actualizado));
    }

    /** Ajuste rápido de stock: PATCH /productos/5/stock?delta=-1 */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ResponseAPI<Producto>> ajustarStock(@PathVariable Long id, @RequestParam int delta){
        Producto p = productoService.ajustarStock(id, delta);
        return ResponseEntity.ok(ResponseAPI.success("Stock actualizado", p));
    }

    @GetMapping()
    public ResponseEntity<ResponseAPI<List<Producto>>>obtenerProductos(){
        List<Producto> productos=productoService.obtenerProductos();
        return ResponseEntity.ok(ResponseAPI.success("Lista de productos obtenida con exito", productos));
    }

}
