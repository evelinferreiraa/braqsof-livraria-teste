package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de unidade destinados à validação da RN01
 * implementada em CalculadoraDescontoService.
 *
 * Técnicas aplicadas:
 *
 *  • Partição de Equivalência (PE):
 *      - PE1: < 1 ano        → 0% (BÁSICO)
 *      - PE2: [1, 3) anos    → 3% (BRONZE)
 *      - PE3: [3, 5) anos    → 5% (PRATA)
 *      - PE4: ≥ 5 anos       → 10% (OURO)
 *
 *  • Análise de Valor Limite (AVL):
 *      Testes nos pontos críticos: 1 ano, 3 anos e 5 anos.
 *
 * Cada caso de teste utiliza STUBs de Cliente com datas controladas
 * para simular cenários específicos de tempo de vínculo.
 */
class CalculadoraDescontoServiceTest {

    private CalculadoraDescontoService calculadora;

    @BeforeEach
    void setUp() {
        // Inicializa o serviço real antes de cada teste.
        calculadora = new CalculadoraDescontoService();
    }

    // ========== TESTES DE PARTIÇÃO DE EQUIVALÊNCIA ==========

    /**
     * PE1 – cliente com < 1 ano de vínculo deve receber 0% de desconto.
     * Representa o perfil BÁSICO segundo RN01.
     */
    @Test
    @DisplayName("RN01 - Cliente com menos de 1 ano (BÁSICO) - 0% desconto")
    void testDescontoPerfilBasico() {
        // STUB: Cria cliente com dados controlados para teste
        Cliente cliente = new Cliente(1L, "Ana", "ana@email.com",
                LocalDate.now().minusMonths(6)); // 6 meses de cadastro
        double valorPedido = 100.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(0.0, desconto, 0.001, "Cliente BÁSICO deve ter 0% de desconto");
        assertEquals("BASICO", perfil, "Perfil deve ser identificado como BÁSICO");
    }

    /**
     * PE2 – cliente com 1 a 3 anos deve receber 3% de desconto.
     * Representa o perfil BRONZE.
     */
    @Test
    @DisplayName("RN01 - Cliente com 1 a 3 anos (BRONZE) - 3% desconto")
    void testDescontoPerfilBronze() {
        Cliente cliente = new Cliente(2L, "Evelin", "evelin@email.com",
                LocalDate.now().minusYears(2).minusMonths(3)); // 2 anos e 3 meses
        double valorPedido = 200.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(6.0, desconto, 0.001, "Cliente BRONZE deve ter 3% de desconto (6.00)");
        assertEquals("BRONZE", perfil, "Perfil deve ser identificado como BRONZE");
    }

    /**
     * PE3 – cliente com 3 a 5 anos deve receber 5% de desconto.
     * Representa o perfil PRATA.
     */
    @Test
    @DisplayName("RN01 - Cliente com 3 a 5 anos (PRATA) - 5% desconto")
    void testDescontoPerfilPrata() {
        Cliente cliente = new Cliente(3L, "Giovanna", "giovanna@email.com",
                LocalDate.now().minusYears(4).minusMonths(6)); // 4 anos e 6 meses
        double valorPedido = 300.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(15.0, desconto, 0.001, "Cliente PRATA deve ter 5% de desconto (15.00)");
        assertEquals("PRATA", perfil, "Perfil deve ser identificado como PRATA");
    }

    /**
     * PE4 – cliente com ≥ 5 anos deve receber 10% de desconto.
     * Representa o perfil OURO.
     */
    @Test
    @DisplayName("RN01 - Cliente com mais de 5 anos (OURO) - 10% desconto")
    void testDescontoPerfilOuro() {
        Cliente cliente = new Cliente(4L, "Soraya", "soraya@email.com",
                LocalDate.now().minusYears(7).minusMonths(2)); // 7 anos e 2 meses
        double valorPedido = 400.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(40.0, desconto, 0.001, "Cliente OURO deve ter 10% de desconto (40.00)");
        assertEquals("OURO", perfil, "Perfil deve ser identificado como OURO");
    }

    // ========== TESTES DE ANÁLISE DE VALOR LIMITE (AVL) ==========

    /**
     * AVL – valor limite: exatamente 1 ano.
     * Deve trocar de BÁSICO para BRONZE.
     */
    @Test
    @DisplayName("RN01 - Cliente com exatamente 1 ano (BRONZE)")
    void testValorLimiteExatamente1Ano() {
        Cliente cliente = new Cliente(5L, "Limite1", "limite1@email.com",
                LocalDate.now().minusYears(1)); // Exatamente 1 ano
        double valorPedido = 1000.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(30.0, desconto, 0.001, "1 ano exato: deve ser 3% (30.00)");
        assertEquals("BRONZE", perfil, "Deve ser identificado como BRONZE");
    }

    /**
     * AVL – valor limite: exatamente 3 anos.
     * Deve trocar de BRONZE para PRATA.
     */
    @Test
    @DisplayName("RN01 - Cliente com exatamente 3 anos (PRATA)")
    void testValorLimiteExatamente3Anos() {
        Cliente cliente = new Cliente(6L, "Limite3", "limite3@email.com",
                LocalDate.now().minusYears(3)); // Exatamente 3 anos
        double valorPedido = 1000.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(50.0, desconto, 0.001, "3 anos exato: deve ser 5% (50.00)");
        assertEquals("PRATA", perfil, "Deve ser identificado como PRATA");
    }

    /**
     * AVL – valor limite: exatamente 5 anos.
     * Deve trocar de PRATA para OURO.
     */
    @Test
    @DisplayName("RN01 - Cliente com exatamente 5 anos (OURO)")
    void testValorLimiteExatamente5Anos() {
        Cliente cliente = new Cliente(7L, "Limite5", "limite5@email.com",
                LocalDate.now().minusYears(5)); // Exatamente 5 anos
        double valorPedido = 1000.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(100.0, desconto, 0.001, "5 anos exato: deve ser 10% (100.00)");
        assertEquals("OURO", perfil, "Deve ser identificado como OURO");
    }
}