package org.example.stockerp.ventas.service;
import jakarta.transaction.Transactional;
import org.example.stockerp.clientes.repository.ClienteRepository;
import org.example.stockerp.productos.model.Producto;
import org.example.stockerp.productos.repository.ProductoRepository;
import org.example.stockerp.ventas.model.DetalleVenta;
import org.example.stockerp.ventas.model.DetalleVentaDTO;
import org.example.stockerp.ventas.model.Venta;
import org.example.stockerp.ventas.model.VentaRequestDTO;
import org.example.stockerp.ventas.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentasService {
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VentasRepository ventasRepository;

    @Transactional
    public void registrarVenta(VentaRequestDTO ventaDTO) {
        System.out.println("DTO Recibido: " + ventaDTO);
        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(0.0);
        venta.setCliente(clienteRepository.findById(ventaDTO.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        for (DetalleVentaDTO itemDTO : ventaDTO.items()) {
            Producto producto = productoRepository.findById(itemDTO.productoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado (ID inválido)"));
            if (producto.getStock() < itemDTO.cantidad()) {
                throw new RuntimeException("Sin stock");
            }
            producto.setStock(producto.getStock() - itemDTO.cantidad());

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(itemDTO.cantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            venta.agregarItem(detalle);
            venta.setTotal(venta.getTotal() + (detalle.getPrecioUnitario() * detalle.getCantidad()));
        }
        ventasRepository.save(venta);
    }
    public Venta obtenerVentaPorId(Long id){
        return ventasRepository.findById(id).orElse(null);
    }
    public void eliminarVenta(Long id){
        ventasRepository.deleteById(id);
    }
    public List<Venta>obtenerVentas(){
        return ventasRepository.findAll();
    }


}

