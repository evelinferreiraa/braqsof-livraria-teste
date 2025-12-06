package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.*;
import br.edu.ifsp.bra.livraria.repository.ClienteRepository;
import java.util.List;

/**
 * Service que integra as regras de negócio RN01 e RN02.
 * Usa injeção de dependências para facilitar testes com MOCKS.
 */
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
     * Retorna o pedido com todos os cálculos realizados.
     */
    public Pedido processarPedido(Long clienteId, Endereco enderecoEntrega,
                                  List<ItemCarrinho> itens, String formaPagamento) {

        // 1. Buscar cliente (simulação de banco de dados usando MOCK)
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + clienteId));

        // 2. Calcular valor total dos itens
        double valorItens = calcularValorItens(itens);

        // 3. Aplicar desconto (RN01)
        double desconto = calculadoraDesconto.calcularDesconto(cliente, valorItens);
        double valorComDesconto = valorItens - desconto;

        // 4. Calcular frete (RN02)
        double frete = calculadoraFrete.calcularFrete(enderecoEntrega, valorComDesconto);

        // 5. Calcular valor final
        double valorFinal = valorComDesconto + frete;

        // 6. Criar e retornar pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEnderecoEntrega(enderecoEntrega);
        pedido.setItens(itens);
        pedido.setValorItens(valorItens);
        pedido.setDesconto(desconto);
        pedido.setFrete(frete);
        pedido.setValorTotal(valorFinal);
        pedido.setFormaPagamento(formaPagamento);
        pedido.setStatus("EM_PROCESSAMENTO"); // RN03 - Status inicial

        return pedido;
    }

    private double calcularValorItens(List<ItemCarrinho> itens) {
        if (itens == null || itens.isEmpty()) {
            return 0.0;
        }
        return itens.stream()
                .mapToDouble(ItemCarrinho::getSubtotal)
                .sum();
    }
}