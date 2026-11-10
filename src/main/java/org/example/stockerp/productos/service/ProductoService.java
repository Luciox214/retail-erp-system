package org.example.stockerp.productos.service;
import org.example.stockerp.exception.NegocioException;
import org.example.stockerp.exception.NoEncontradoException;
import org.example.stockerp.productos.model.Producto;
import org.example.stockerp.productos.model.ProductoCatalogoDTO;
import org.example.stockerp.productos.repository.ProductoRepository;
import org.example.stockerp.ventas.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    DetalleVentaRepository detalleVentaRepository;

    /** Resultado de pedir eliminar un producto: se borró o, si tenía ventas, se archivó. */
    public enum ResultadoEliminacion { ELIMINADO, ARCHIVADO }

    public Producto crearProducto(Producto producto) {
        normalizarYValidar(producto);
        producto.setId(null);
        producto.setArchivado(false);
        if (producto.getActivo() == null) producto.setActivo(true);
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Producto cambios) {
        if (cambios.getId() == null) throw new NegocioException("Falta el producto a actualizar.");
        Producto actual = buscar(cambios.getId());
        normalizarYValidar(cambios);
        actual.setNombre(cambios.getNombre());
        actual.setDescripcion(cambios.getDescripcion());
        actual.setCosto(cambios.getCosto());
        actual.setPrecioVenta(cambios.getPrecioVenta());
        actual.setStock(cambios.getStock());
        actual.setUrlImagen(cambios.getUrlImagen());
        if (cambios.getActivo() != null) actual.setActivo(cambios.getActivo());
        return productoRepository.save(actual);
    }

    /**
     * Si el producto nunca se vendió, se borra. Si tiene ventas, se archiva para no
     * romper el historial ni las facturas.
     */
    @Transactional
    public ResultadoEliminacion eliminarProducto(Long id) {
        Producto producto = buscar(id);
        if (detalleVentaRepository.existsByProductoId(id)) {
            producto.setArchivado(true);
            producto.setActivo(false);
            productoRepository.save(producto);
            return ResultadoEliminacion.ARCHIVADO;
        }
        productoRepository.delete(producto);
        return ResultadoEliminacion.ELIMINADO;
    }

    /** Suma o resta unidades de stock (por ejemplo, +5 al reponer). Nunca deja stock negativo. */
    @Transactional
    public Producto ajustarStock(Long id, int delta) {
        Producto producto = buscar(id);
        int nuevo = producto.getStock() + delta;
        if (nuevo < 0) {
            throw new NegocioException("El stock no puede quedar negativo (hay " + producto.getStock() + ").");
        }
        producto.setStock(nuevo);
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerProductos() {
        return productoRepository.findVigentes();
    }

    public List<ProductoCatalogoDTO> obtenerProductosCatalogo() {
        return productoRepository.findVisiblesEnWeb().stream()
                .map(producto -> new ProductoCatalogoDTO(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getDescripcion(),
                        producto.getUrlImagen()
                )).toList();
    }

    public void cambiarEstadoProducto(Long id, boolean estado) {
        Producto producto = buscar(id);
        producto.setActivo(estado);
        productoRepository.save(producto);
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .filter(p -> !p.isArchivadoSeguro())
                .orElseThrow(() -> new NoEncontradoException("El producto no existe o fue eliminado."));
    }

    private void normalizarYValidar(Producto p) {
        p.setNombre(p.getNombre() == null ? "" : p.getNombre().trim().replaceAll("\\s+", " "));
        if (p.getDescripcion() != null) p.setDescripcion(p.getDescripcion().trim());
        if (p.getUrlImagen() != null && p.getUrlImagen().isBlank()) p.setUrlImagen(null);

        if (p.getNombre().isEmpty()) throw new NegocioException("El producto necesita un nombre.");
        if (p.getPrecioVenta() == null || p.getPrecioVenta() <= 0)
            throw new NegocioException("El precio de venta tiene que ser mayor a 0.");
        if (p.getCosto() == null) p.setCosto(0.0);
        if (p.getCosto() < 0) throw new NegocioException("El costo no puede ser negativo.");
        if (p.getStock() == null) p.setStock(0);
        if (p.getStock() < 0) throw new NegocioException("El stock no puede ser negativo.");
    }
}
