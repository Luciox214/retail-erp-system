package org.example.stockerp.productos.service;
import org.example.stockerp.productos.model.Producto;
import org.example.stockerp.productos.model.ProductoCatalogoDTO;
import org.example.stockerp.productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    @Autowired
    ProductoRepository productoRepository;

    public void crearProducto(Producto producto){
        producto.setActivo(true);
        productoRepository.save(producto);
    }
    public void eliminarProducto(Long id){
        productoRepository.deleteById(id);
    }
    public void actualizarProducto(Producto producto){
        productoRepository.save(producto);
    }
    public List<Producto>obtenerProductos(){
        return productoRepository.findAll();
    }
    public List<ProductoCatalogoDTO>obtenerProductosCatalogo(){
        return productoRepository.findAll().stream()
                .filter(Producto::getActivo)
                .map(producto -> new ProductoCatalogoDTO(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getDescripcion(),
                        producto.getUrlImagen()
                )).toList();
    }

    public void cambiarEstadoProducto(Long id, boolean estado){
        Producto producto=productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            producto.setActivo(estado);
            productoRepository.save(producto);
        }
    }
