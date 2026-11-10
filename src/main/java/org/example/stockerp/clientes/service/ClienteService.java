package org.example.stockerp.clientes.service;

import org.example.stockerp.clientes.model.Cliente;
import org.example.stockerp.clientes.repository.ClienteRepository;
import org.example.stockerp.exception.NegocioException;
import org.example.stockerp.exception.NoEncontradoException;
import org.example.stockerp.ventas.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VentasRepository ventasRepository;

    public Cliente registrarCliente(Cliente cliente){
        normalizarYValidar(cliente);
        cliente.setId(null);
        if (cliente.getTelefono() != null && clienteRepository.existsByTelefono(cliente.getTelefono())) {
            throw new NegocioException("Ya hay un cliente con ese teléfono.");
        }
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(Long id){
        Cliente cliente = buscar(id);
        if (ventasRepository.existsByClienteId(id)) {
            throw new NegocioException("No se puede eliminar a " + cliente.getNombre() +
                    " porque tiene ventas registradas. Podés editar sus datos si hace falta.");
        }
        clienteRepository.delete(cliente);
    }

    public Cliente actualizarCliente(Cliente cambios){
        if (cambios.getId() == null) throw new NegocioException("Falta el cliente a actualizar.");
        Cliente actual = buscar(cambios.getId());
        normalizarYValidar(cambios);
        if (cambios.getTelefono() != null && clienteRepository.existsByTelefonoAndIdNot(cambios.getTelefono(), cambios.getId())) {
            throw new NegocioException("Ya hay otro cliente con ese teléfono.");
        }
        actual.setNombre(cambios.getNombre());
        actual.setTelefono(cambios.getTelefono());
        return clienteRepository.save(actual);
    }

    public List<Cliente>obtenerClientes(){
        return clienteRepository.findAllByOrderByNombreAsc();
    }

    private Cliente buscar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("El cliente no existe o fue eliminado."));
    }

    private void normalizarYValidar(Cliente c) {
        c.setNombre(c.getNombre() == null ? "" : c.getNombre().trim().replaceAll("\\s+", " "));
        if (c.getNombre().isEmpty()) throw new NegocioException("El cliente necesita un nombre.");
        String tel = c.getTelefono() == null ? "" : c.getTelefono().trim();
        c.setTelefono(tel.isEmpty() ? null : tel);
    }
}
