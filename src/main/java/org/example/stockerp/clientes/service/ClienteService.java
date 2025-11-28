package org.example.stockerp.clientes.service;

import org.example.stockerp.clientes.model.Cliente;
import org.example.stockerp.clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;
    public void registrarCliente(Cliente cliente){
        if(clienteRepository.existsByTelefono((cliente.getTelefono()))){
            throw new IllegalArgumentException("El cliente ya está registrado");
        }
        clienteRepository.save(cliente);
    }
    public void eliminarCliente(Long id){
        clienteRepository.deleteById(id);
    }
    public void actualizarCliente(Cliente cliente){
        clienteRepository.save(cliente);
    }
    public List<Cliente>obtenerClientes(){
        return clienteRepository.findAll();
    }
}
