package org.example.stockerp.ventas.service;
import org.example.stockerp.clientes.repository.ClienteRepository;
import org.example.stockerp.exception.NegocioException;
import org.example.stockerp.exception.NoEncontradoException;
import org.example.stockerp.productos.model.Producto;
import org.example.stockerp.productos.repository.ProductoRepository;
import org.example.stockerp.ventas.model.DetalleVenta;
import org.example.stockerp.ventas.model.DetalleVentaDTO;
import org.example.stockerp.ventas.model.Venta;
import org.example.stockerp.ventas.model.VentaRequestDTO;
import org.example.stockerp.ventas.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class VentasService {
    public static final Set<String> MEDIOS_PAGO = Set.of("EFECTIVO", "TRANSFERENCIA", "MERCADO_PAGO", "TARJETA", "OTRO");

    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VentasRepository ventasRepository;

    @Transactional
    public Venta registrarVenta(VentaRequestDTO dto) {
        if (dto == null || dto.items() == null || dto.items().isEmpty()) {
            throw new NegocioException("Agregá al menos un producto a la venta.");
        }

        // Si el mismo producto vino repetido, se suman las cantidades.
        Map<Long, Integer> cantidades = new LinkedHashMap<>();
        for (DetalleVentaDTO item : dto.items()) {
            if (item.productoId() == null) throw new NegocioException("Hay un producto sin identificar en la venta.");
            if (item.cantidad() == null || item.cantidad() < 1) throw new NegocioException("Las cantidades tienen que ser de 1 o más.");
            cantidades.merge(item.productoId(), item.cantidad(), Integer::sum);
        }

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setNota(dto.nota() == null || dto.nota().isBlank() ? null : dto.nota().trim());

        String medio = dto.medioPago() == null || dto.medioPago().isBlank() ? "EFECTIVO" : dto.medioPago().trim().toUpperCase();
        if (!MEDIOS_PAGO.contains(medio)) throw new NegocioException("Medio de pago no válido: " + dto.medioPago());
        venta.setMedioPago(medio);

        if (dto.clienteId() != null) {
            venta.setCliente(clienteRepository.findById(dto.clienteId())
                    .orElseThrow(() -> new NoEncontradoException("El cliente elegido ya no existe.")));
        }

        double subtotal = 0;
        for (Map.Entry<Long, Integer> e : cantidades.entrySet()) {
            Producto producto = productoRepository.findById(e.getKey())
                    .filter(p -> !p.isArchivadoSeguro())
                    .orElseThrow(() -> new NoEncontradoException("Uno de los productos ya no existe. Actualizá la lista."));
            int cantidad = e.getValue();
            if (producto.getStock() < cantidad) {
                throw new NegocioException(producto.getStock() == 0
                        ? "No queda stock de \"" + producto.getNombre() + "\"."
                        : "No hay stock suficiente de \"" + producto.getNombre() + "\": quedan " + producto.getStock() + ".");
            }
            producto.setStock(producto.getStock() - cantidad);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setCostoUnitario(producto.getCosto());
            venta.agregarItem(detalle);
            subtotal += detalle.getPrecioUnitario() * cantidad;
        }

        double descuento = dto.descuento() == null ? 0 : dto.descuento();
        if (descuento < 0) throw new NegocioException("El descuento no puede ser negativo.");
        if (descuento > subtotal) throw new NegocioException("El descuento no puede ser mayor al total de la venta.");
        venta.setDescuento(descuento);
        venta.setTotal(subtotal - descuento);

        return ventasRepository.save(venta);
    }

    public Venta obtenerVentaPorId(Long id) {
        return ventasRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("La venta no existe o fue anulada."));
    }

    /** Anula la venta y devuelve al stock las unidades vendidas. */
    @Transactional
    public void eliminarVenta(Long id) {
        Venta venta = obtenerVentaPorId(id);
        for (DetalleVenta item : venta.getItems()) {
            Producto p = item.getProducto();
            p.setStock(p.getStock() + item.getCantidad());
            productoRepository.save(p);
        }
        ventasRepository.delete(venta);
    }

    public List<Venta> obtenerVentas() {
        return ventasRepository.findAllByOrderByFechaDesc();
    }
}
