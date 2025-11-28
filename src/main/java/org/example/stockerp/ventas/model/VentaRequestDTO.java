package org.example.stockerp.ventas.model;
import java.util.List;

public record VentaRequestDTO(Long clienteId, List<DetalleVentaDTO>items) {
}
