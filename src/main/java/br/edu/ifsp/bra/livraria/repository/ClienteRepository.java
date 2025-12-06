package br.edu.ifsp.bra.livraria.repository;

import br.edu.ifsp.bra.livraria.entity.Cliente;
import java.util.Optional;

/**
 * Interface que simula um repositório de dados.
 * Em um sistema real, seria implementada por uma classe que acessa banco de dados.
 * Para testes, usaremos MOCKS desta interface.
 */
public interface ClienteRepository {
    Optional<Cliente> findById(Long id);
    Cliente save(Cliente cliente);
}