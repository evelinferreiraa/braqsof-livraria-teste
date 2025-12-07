package br.edu.ifsp.bra.livraria;

import br.edu.ifsp.bra.livraria.entity.*;
import br.edu.ifsp.bra.livraria.service.CalculadoraDescontoService;
import br.edu.ifsp.bra.livraria.service.CalculadoraFreteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

/**
 * Classe principal da aplicação.
 *
 * - Inicializa o Spring Boot (necessário para testes de sistema via Thunder Client).
 * - Executa demonstrações manuais de RN01 e RN02 (comportamento preservado).
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        // Inicializa a API REST
        SpringApplication.run(Application.class, args);

        // Execução opcional da demonstração
        System.out.println("=== SISTEMA DE LIVRARIA - DEMONSTRAÇÃO RN01 e RN02 ===\n");

        System.out.println("--- RN01: Teste de Descontos por Tempo de Vínculo ---");
        testarRN01();

        System.out.println("\n--- RN02: Teste de Cálculo de Frete por Região ---");
        testarRN02();

        System.out.println("\n=== DEMONSTRAÇÃO CONCLUÍDA ===");
    }

    private static void testarRN01() {
        CalculadoraDescontoService calculadora = new CalculadoraDescontoService();

        Cliente clienteBasico = new Cliente(1L, "Novo Cliente", "novo@email.com",
                LocalDate.now().minusMonths(6));
        double descontoBasico = calculadora.calcularDesconto(clienteBasico, 1000.0);
        System.out.println("Cliente Básico (6 meses): R$ " + descontoBasico + " de desconto");

        Cliente clienteBronze = new Cliente(2L, "Cliente Bronze", "bronze@email.com",
                LocalDate.now().minusYears(2));
        double descontoBronze = calculadora.calcularDesconto(clienteBronze, 1000.0);
        System.out.println("Cliente Bronze (2 anos): R$ " + descontoBronze + " de desconto");

        Cliente clientePrata = new Cliente(3L, "Cliente Prata", "prata@email.com",
                LocalDate.now().minusYears(4));
        double descontoPrata = calculadora.calcularDesconto(clientePrata, 1000.0);
        System.out.println("Cliente Prata (4 anos): R$ " + descontoPrata + " de desconto");

        Cliente clienteOuro = new Cliente(4L, "Cliente Ouro", "ouro@email.com",
                LocalDate.now().minusYears(7));
        double descontoOuro = calculadora.calcularDesconto(clienteOuro, 1000.0);
        System.out.println("Cliente Ouro (7 anos): R$ " + descontoOuro + " de desconto");
    }

    private static void testarRN02() {
        CalculadoraFreteService calculadora = new CalculadoraFreteService();

        Endereco enderecoSP = new Endereco("Rua SP", "123", "Centro", "São Paulo", "SP", "01000-000");
        double freteSP = calculadora.calcularFrete(enderecoSP, 1000.0);
        System.out.println("Frete SP: R$ " + freteSP + " (isento)");

        Endereco enderecoRJ = new Endereco("Rua RJ", "456", "Copacabana", "Rio de Janeiro", "RJ", "22000-000");
        double freteRJ = calculadora.calcularFrete(enderecoRJ, 1000.0);
        System.out.println("Frete RJ: R$ " + freteRJ + " (5%)");

        Endereco enderecoRS = new Endereco("Rua RS", "789", "Centro", "Porto Alegre", "RS", "90000-000");
        double freteRS = calculadora.calcularFrete(enderecoRS, 1000.0);
        System.out.println("Frete RS: R$ " + freteRS + " (8%)");
    }
}