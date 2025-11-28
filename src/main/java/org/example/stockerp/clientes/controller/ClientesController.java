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

    @PostMapping
    public ResponseEntity<MessageResponse>registrarCliente(@RequestBody Cliente cliente){
        clienteService.registrarCliente(cliente);
        return ResponseEntity.ok(MessageResponse.success("Cliente registrado con exito"));
    }
    @GetMapping
    public ResponseEntity<ResponseAPI<List<Cliente>>>obtenerClientes(){
        List<Cliente> clientes=clienteService.obtenerClientes();
        ResponseAPI<List<Cliente>> responseAPI=new ResponseAPI<>("success","Lista de clientes obtenida con exito",clientes);
        return ResponseEntity.ok(responseAPI);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse>eliminarCliente(@PathVariable Long id){
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok(MessageResponse.success("Cliente eliminado con exito"));
    }
    @PutMapping
    public ResponseEntity<MessageResponse>actualizarCliente(@RequestBody Cliente cliente){
        clienteService.actualizarCliente(cliente);
        return ResponseEntity.ok(MessageResponse.success("Cliente actualizado con exito"));
    }

}
