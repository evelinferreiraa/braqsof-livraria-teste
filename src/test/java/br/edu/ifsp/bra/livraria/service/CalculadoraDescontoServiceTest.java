package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraDescontoServiceTest {

    private CalculadoraDescontoService calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraDescontoService();
    }

    @Test
    @DisplayName("RN01 - Cliente com menos de 1 ano (BÁSICO) - desconto 0%")
    void testDescontoPerfilBasico() {
        Cliente cliente = new Cliente(1L, "Ana", "ana@email.com",
                LocalDate.now().minusMonths(6));
        double valorPedido = 100.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(0.0, desconto, 0.001);
        assertEquals("BASICO", perfil);
    }

    @Test
    @DisplayName("RN01 - Cliente com 1 a 3 anos (BRONZE) - desconto 3%")
    void testDescontoPerfilBronze() {
        Cliente cliente = new Cliente(2L, "Evelin", "evelin@email.com",
                LocalDate.now().minusYears(2).minusMonths(3));
        double valorPedido = 200.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(6.0, desconto, 0.001);
        assertEquals("BRONZE", perfil);
    }

    @Test
    @DisplayName("RN01 - Cliente com 3 a 5 anos (PRATA) - desconto 5%")
    void testDescontoPerfilPrata() {
        Cliente cliente = new Cliente(3L, "Giovanna", "giovanna@email.com",
                LocalDate.now().minusYears(4).minusMonths(6));
        double valorPedido = 300.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(15.0, desconto, 0.001);
        assertEquals("PRATA", perfil);
    }

    @Test
    @DisplayName("RN01 - Cliente com mais de 5 anos (OURO) - desconto 10%")
    void testDescontoPerfilOuro() {
        Cliente cliente = new Cliente(4L, "Soraya", "soraya@email.com",
                LocalDate.now().minusYears(7).minusMonths(2));
        double valorPedido = 400.0;

        double desconto = calculadora.calcularDesconto(cliente, valorPedido);
        String perfil = calculadora.identificarPerfil(cliente);

        assertEquals(40.0, desconto, 0.001);
        assertEquals("OURO", perfil);
    }
}