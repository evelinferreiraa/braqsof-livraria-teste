package br.edu.ifsp.bra.livraria.api;

import br.edu.ifsp.bra.livraria.api.dto.PedidoRequestDTO;
import br.edu.ifsp.bra.livraria.entity.Pedido;
import br.edu.ifsp.bra.livraria.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller responsável por expor o caso de uso
 * "Efetuar Pedido de Livro" via API REST.
 *
 * Este endpoint será utilizado para testes de sistema
 * com ferramentas como Thunder Client (VS Code) ou Postman.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * PedidoService é injetado pelo Spring (necessário anotar PedidoService com @Service).
     */
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * Endpoint que processa um pedido completo com base em:
     * - clienteId
     * - endereço de entrega
     * - itens do carrinho
     * - forma de pagamento
     *
     * Fluxo:
     *  - RN01: aplica desconto conforme perfil do cliente
     *  - RN02: calcula frete conforme estado
     *  - RN03: define status inicial do pedido
     */
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoRequestDTO dto) {
        try {
            Pedido pedido = pedidoService.processarPedido(
                    dto.getClienteId(),
                    dto.getEnderecoEntrega(),
                    dto.getItens(),
                    dto.getFormaPagamento()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);

        } catch (RuntimeException ex) {
            // Exemplo: "Cliente não encontrado: X" vindo do PedidoService
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}