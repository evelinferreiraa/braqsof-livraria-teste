package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.*;
import br.edu.ifsp.bra.livraria.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes de unidade para PedidoService, integrando RN01 e RN02.
 *
 * - Usa MOCK de ClienteRepository para simular acesso a dados.
 * - Usa implementações reais de CalculadoraDescontoService e CalculadoraFreteService.
 *
 * Os cenários foram derivados do GFC do caso de uso, combinando:
 *   - perfis de RN01 (básico, bronze, prata, ouro)
 *   - regiões de RN02 (SP, Sudeste, outras)
 */
public class PedidoServiceTest {

    private ClienteRepository clienteRepositoryMock;
    private CalculadoraDescontoService descontoService;
    private CalculadoraFreteService freteService;
    private PedidoService pedidoService;

    @BeforeEach
    public void setup() {
        // MOCK: simula o repositório, sem precisar de banco de dados real
        clienteRepositoryMock = Mockito.mock(ClienteRepository.class);

        // Serviços reais de regra de negócio (RN01 e RN02)
        descontoService = new CalculadoraDescontoService();
        freteService = new CalculadoraFreteService();

        pedidoService = new PedidoService(descontoService, freteService, clienteRepositoryMock);
    }

    private Cliente criarClienteComAnosDeVinculo(long anos) {
        Cliente c = new Cliente();
        c.setId(1L);
        c.setDataCadastro(LocalDate.now().minusYears(anos));
        return c;
    }

    private Endereco enderecoEstado(String uf) {
        Endereco e = new Endereco();
        e.setEstado(uf);
        return e;
    }

    private ItemCarrinho item(double preco, int quantidade) {
        ItemCarrinho ic = new ItemCarrinho();
        ic.setPrecoUnitario(preco);
        ic.setQuantidade(quantidade);
        return ic;
    }

    /**
     * Perfil básico + frete SP (RN01 sem desconto, RN02 isento).
     */
    @Test
    public void deveProcessarPedidoPerfilBasicoComFreteSP() {
        Cliente cliente = criarClienteComAnosDeVinculo(0); // básico
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.of(cliente));

        Endereco endereco = enderecoEstado("SP");
        ItemCarrinho item = item(50.0, 2); // valorItens = 100
        Pedido pedido = pedidoService.processarPedido(
                1L,
                endereco,
                Arrays.asList(item),
                "CREDITO"
        );

        assertEquals(100.0, pedido.getValorItens(), 0.0001);
        assertEquals(0.0, pedido.getDesconto(), 0.0001);
        assertEquals(0.0, pedido.getFrete(), 0.0001);
        assertEquals(100.0, pedido.getValorTotal(), 0.0001);
        assertEquals("EM_PROCESSAMENTO", pedido.getStatus());
    }

    /**
     * Perfil bronze + frete Sudeste (3% desconto + 5% frete).
     */
    @Test
    public void deveProcessarPedidoPerfilBronzeComFreteSudeste() {
        Cliente cliente = criarClienteComAnosDeVinculo(2); // bronze (1 a 3 anos)
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.of(cliente));

        Endereco endereco = enderecoEstado("ES"); // Sudeste → 5%
        ItemCarrinho item = item(100.0, 1); // valorItens = 100

        Pedido pedido = pedidoService.processarPedido(
                1L,
                endereco,
                Arrays.asList(item),
                "PIX"
        );

        double valorItens = 100.0;
        double descontoEsperado = valorItens * 0.03; // bronze
        double valorComDesconto = valorItens - descontoEsperado;
        double freteEsperado = valorComDesconto * 0.05; // Sudeste

        assertEquals(valorItens, pedido.getValorItens(), 0.0001);
        assertEquals(descontoEsperado, pedido.getDesconto(), 0.0001);
        assertEquals(freteEsperado, pedido.getFrete(), 0.0001);
        assertEquals(valorComDesconto + freteEsperado, pedido.getValorTotal(), 0.0001);
    }

    /**
     * Perfil prata + frete Sudeste (5% desconto + 5% frete).
     */
    @Test
    public void deveProcessarPedidoPerfilPrataComFreteSudeste() {
        Cliente cliente = criarClienteComAnosDeVinculo(4); // prata (3 a 5 anos)
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.of(cliente));

        Endereco endereco = enderecoEstado("ES"); // Sudeste → 5%
        ItemCarrinho item = item(100.0, 1); // valorItens = 100

        Pedido pedido = pedidoService.processarPedido(
                1L,
                endereco,
                Arrays.asList(item),
                "PIX"
        );

        double valorItens = 100.0;
        double descontoEsperado = valorItens * 0.05; // prata
        double valorComDesconto = valorItens - descontoEsperado;
        double freteEsperado = valorComDesconto * 0.05; // Sudeste

        assertEquals(valorItens, pedido.getValorItens(), 0.0001);
        assertEquals(descontoEsperado, pedido.getDesconto(), 0.0001);
        assertEquals(freteEsperado, pedido.getFrete(), 0.0001);
        assertEquals(valorComDesconto + freteEsperado, pedido.getValorTotal(), 0.0001);
    }

    /**
     * Perfil ouro + frete outras regiões (10% desconto + 8% frete).
     */
    @Test
    public void deveProcessarPedidoPerfilOuroComFreteOutrasRegioes() {
        Cliente cliente = criarClienteComAnosDeVinculo(6); // ouro (>5 anos)
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.of(cliente));

        Endereco endereco = enderecoEstado("SC"); // outras regiões → 8%
        ItemCarrinho item = item(200.0, 1); // valorItens = 200

        Pedido pedido = pedidoService.processarPedido(
                1L,
                endereco,
                Arrays.asList(item),
                "CREDITO"
        );

        double valorItens = 200.0;
        double descontoEsperado = valorItens * 0.10; // ouro
        double valorComDesconto = valorItens - descontoEsperado;
        double freteEsperado = valorComDesconto * 0.08; // outras regiões

        assertEquals(valorItens, pedido.getValorItens(), 0.0001);
        assertEquals(descontoEsperado, pedido.getDesconto(), 0.0001);
        assertEquals(freteEsperado, pedido.getFrete(), 0.0001);
        assertEquals(valorComDesconto + freteEsperado, pedido.getValorTotal(), 0.0001);
    }

    /**
     * Fluxo de exceção (2): carrinho vazio deve lançar IllegalArgumentException.
     */
    @Test
    public void deveLancarExcecaoQuandoCarrinhoEstiverVazio() {
        Cliente cliente = criarClienteComAnosDeVinculo(0);
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.of(cliente));

        Endereco endereco = enderecoEstado("SP");

        assertThrows(IllegalArgumentException.class,
                () -> pedidoService.processarPedido(1L, endereco, Collections.emptyList(), "PIX"),
                "Carrinho vazio deve impedir o processamento do pedido.");
    }
}
