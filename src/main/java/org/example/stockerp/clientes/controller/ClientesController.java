package org.example.stockerp.clientes.controller;

import org.example.stockerp.clientes.model.Cliente;
import org.example.stockerp.clientes.service.ClienteService;
import org.example.stockerp.response.MessageResponse;
import org.example.stockerp.response.ResponseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/clientes")
public class ClientesController {
    @Autowired
    ClienteService clienteService;

    /** Devuelve el cliente creado para poder seleccionarlo directamente en una venta. */
    @PostMapping
    public ResponseEntity<ResponseAPI<Cliente>>registrarCliente(@RequestBody Cliente cliente){
        return ResponseEntity.ok(ResponseAPI.success("Cliente registrado", clienteService.registrarCliente(cliente)));
    }
    @GetMapping
    public ResponseEntity<ResponseAPI<List<Cliente>>>obtenerClientes(){
        return ResponseEntity.ok(ResponseAPI.success("Lista de clientes obtenida con exito", clienteService.obtenerClientes()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse>eliminarCliente(@PathVariable Long id){
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok(MessageResponse.success("Cliente eliminado"));
    }
    @PutMapping
    public ResponseEntity<ResponseAPI<Cliente>>actualizarCliente(@RequestBody Cliente cliente){
        return ResponseEntity.ok(ResponseAPI.success("Cliente actualizado", clienteService.actualizarCliente(cliente)));
    }
}
