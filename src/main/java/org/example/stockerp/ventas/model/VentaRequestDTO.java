package org.example.stockerp.ventas.model;

import java.util.List;

/**
 * @param clienteId opcional (null = consumidor final)
 * @param medioPago opcional, por defecto EFECTIVO
 * @param descuento opcional, en pesos
 * @param nota      opcional
 */
public record VentaRequestDTO(Long clienteId, List<DetalleVentaDTO> items, String medioPago, Double descuento, String nota) {
}
