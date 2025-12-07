package br.edu.ifsp.bra.livraria.repository;

import br.edu.ifsp.bra.livraria.entity.Cliente;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação em memória de ClienteRepository.
 *
 * Esta classe é usada apenas para testes/demonstração,
 * permitindo que o PedidoService recupere clientes
 * sem necessidade de banco de dados real.
 */
@Repository
public class ClienteRepositoryInMemory implements ClienteRepository {

    private final Map<Long, Cliente> banco = new ConcurrentHashMap<>();

    public ClienteRepositoryInMemory() {
        // Clientes de exemplo com diferentes tempos de vínculo (para RN01)
        banco.put(1L, new Cliente(1L, "Cliente Básico", "basico@email.com",
                LocalDate.now().minusMonths(6))); // < 1 ano

        banco.put(2L, new Cliente(2L, "Cliente Bronze", "bronze@email.com",
                LocalDate.now().minusYears(2))); // 1 a 3 anos

        banco.put(3L, new Cliente(3L, "Cliente Prata", "prata@email.com",
                LocalDate.now().minusYears(4))); // 3 a 5 anos

        banco.put(4L, new Cliente(4L, "Cliente Ouro", "ouro@email.com",
                LocalDate.now().minusYears(7))); // > 5 anos
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return Optional.ofNullable(banco.get(id));
    }

    @Override
    public Cliente save(Cliente cliente) {
        // Implementação simples para fins de teste:
        // Se não houver id, gera um novo id apenas para armazenamento interno.
        Long id = cliente.getId();
        if (id == null) {
            id = gerarNovoId();
        }
        banco.put(id, cliente);
        return cliente;
    }

    private Long gerarNovoId() {
        return banco.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L) + 1L;
    }
}
