package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.*;
import br.edu.ifsp.bra.livraria.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service que integra as regras de negócio RN01 (desconto) e RN02 (frete)
 * no contexto do caso de uso "Efetuar Pedido de Livro".
 *
 * Diagrama de Atividades / GFC:
 *  - Nós 1 a 22 (até selecionar forma de pagamento) mapeiam para as ações aqui.
 *
 * Diagrama de Estados:
 *  - Estado inicial: EM_PROCESSAMENTO
 *  - Outras transições (CONFIRMADO, CANCELADO, ENTREGUE) podem ser aplicadas
 *    via métodos auxiliares deste serviço.
 */

@Service
public class PedidoService {

    private final CalculadoraDescontoService calculadoraDesconto;
    private final CalculadoraFreteService calculadoraFrete;
    private final ClienteRepository clienteRepository;

    public PedidoService(CalculadoraDescontoService calculadoraDesconto,
                         CalculadoraFreteService calculadoraFrete,
                         ClienteRepository clienteRepository) {
        this.calculadoraDesconto = calculadoraDesconto;
        this.calculadoraFrete = calculadoraFrete;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Processa um pedido completo aplicando RN01 e RN02.
     *
     * Este método corresponde, de forma agregada, aos passos:
     *  - (4) identificar perfil (RN01)
     *  - (6) calcular frete (RN02)
     *  - (10) registrar pedido e definir status inicial (RN03 - EM_PROCESSAMENTO)
     *
     * @param clienteId        identificador do cliente (será buscado via repositório - MOCK/STUB em testes)
     * @param enderecoEntrega  endereço informado pelo cliente
     * @param itens            itens do carrinho (não pode ser vazio - Fluxo de Exceção 2)
     * @param formaPagamento   forma de pagamento escolhida
     * @return Pedido com todos os cálculos aplicados
     */
    public Pedido processarPedido(Long clienteId, Endereco enderecoEntrega,
                                  List<ItemCarrinho> itens, String formaPagamento) {

        // Validação do fluxo de exceção (2): carrinho vazio
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Carrinho vazio não permitido. Deve haver ao menos um item.");
        }

        // 1. Buscar cliente (simulação de banco de dados usando MOCK)
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + clienteId));

        // 2. Validar carrinho (Fluxo de Exceção 2: carrinho vazio)
        double valorItens = calcularValorItens(itens); // lança exceção se vazio

        // 3. Aplicar desconto (RN01)
        double desconto = calculadoraDesconto.calcularDesconto(cliente, valorItens);
        double valorComDesconto = valorItens - desconto;

        // 4. Calcular frete (RN02)
        double frete = calculadoraFrete.calcularFrete(enderecoEntrega, valorComDesconto);

        // 5. Calcular valor final
        double valorFinal = valorComDesconto + frete;

        // 6. Criar e retornar pedido (estado inicial RN03)
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEnderecoEntrega(enderecoEntrega);
        pedido.setItens(itens);
        pedido.setValorItens(valorItens);
        pedido.setDesconto(desconto);
        pedido.setFrete(frete);
        pedido.setValorTotal(valorFinal);
        pedido.setFormaPagamento(formaPagamento);
        pedido.setStatus("EM_PROCESSAMENTO"); // RN03 - status inicial

        return pedido;
    }

    /**
     * Corresponde ao vértice (2→3) do GFC:
     * - Se o carrinho estiver vazio, o caso de uso segue para o fluxo de exceção.
     */
    private double calcularValorItens(List<ItemCarrinho> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Não é possível processar pedido com carrinho vazio.");
        }
        return itens.stream()
                .mapToDouble(ItemCarrinho::getSubtotal)
                .sum();
    }

    // Métodos auxiliares de RN03 (diagrama de estados / GE).
    // Podem ser testados separadamente, mas não são o foco principal deste trabalho.

    public void confirmarPedido(Pedido pedido) {
        pedido.setStatus("CONFIRMADO");
    }

    public void cancelarPorPagamentoNaoAutorizado(Pedido pedido) {
        pedido.setStatus("CANCELADO");
    }

    public void marcarPedidoComoEntregue(Pedido pedido) {
        pedido.setStatus("ENTREGUE");
    }
}
