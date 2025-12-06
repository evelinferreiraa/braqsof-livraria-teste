package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.Endereco;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes baseados em modelo para RN02 (frete).
 *
 * Trecho do GFC considerado:
 *
 *   15: Calcular frete
 *     ├─(UF = SP)            → 16: SP           → 19: Frete 0%
 *     ├─(UF ∈ {RJ, MG, ES})  → 17: Sudeste      → 20: Frete 5%
 *     └─(demais UFs BR)      → 18: Outras reg.  → 21: Frete 8%
 *
 * Estados inválidos (ex.: "FL") geram exceção.
 *
 * Os quatro testes abaixo garantem:
 *  - cobertura de todos os nós relevantes de RN02 (15,16,17,18,19,20,21);
 *  - cobertura de todos os arcos de decisão (15→16, 15→17, 15→18, 15→erro).
 */

public class CalculadoraFreteServiceTest {

    private final CalculadoraFreteService service = new CalculadoraFreteService();

    private Endereco criarEnderecoComEstado(String uf) {
        Endereco endereco = new Endereco();
        endereco.setEstado(uf);
        return endereco;
    }

    /**
     * CT-RN02-01
     * Caminho 15→16→19 (SP → frete 0%).
     */
    @Test
    public void deveCalcularFreteIsentoParaSaoPaulo() {
        double valorPedido = 100.0;
        Endereco endereco = criarEnderecoComEstado("SP");

        double freteCalculado = service.calcularFrete(endereco, valorPedido);

        assertEquals(0.0, freteCalculado, 0.0001,
                "Para UF = SP o frete deve ser 0% do valor do pedido.");
    }

    /**
     * CT-RN02-02
     * Caminho 15→17→20 (Sudeste → frete 5%).
     */
    @Test
    public void deveCalcularFreteCincoPorCentoParaSudeste() {
        double valorPedido = 200.0;
        Endereco endereco = criarEnderecoComEstado("ES"); // RJ/MG/ES

        double freteCalculado = service.calcularFrete(endereco, valorPedido);

        double freteEsperado = valorPedido * 0.05;
        assertEquals(freteEsperado, freteCalculado, 0.0001,
                "Para UF do Sudeste (RJ/MG/ES) o frete deve ser 5%.");
    }

    /**
     * CT-RN02-03
     * Caminho 15→18→21 (outras regiões → frete 8%).
     */
    @Test
    public void deveCalcularFreteOitoPorCentoParaOutrasRegioes() {
        double valorPedido = 150.0;
        Endereco endereco = criarEnderecoComEstado("SC"); // qualquer UF fora de SP/RJ/MG/ES

        double freteCalculado = service.calcularFrete(endereco, valorPedido);

        double freteEsperado = valorPedido * 0.08;
        assertEquals(freteEsperado, freteCalculado, 0.0001,
                "Para outras regiões do Brasil o frete deve ser 8%.");
    }

    /**
     * CT-RN02-04
     * Caminho 15→erro (estado inválido).
     */
    @Test
    public void deveLancarExcecaoQuandoEstadoForInvalido() {
        double valorPedido = 100.0;
        Endereco endereco = criarEnderecoComEstado("FL"); // fora do domínio da RN02

        try {
            service.calcularFrete(endereco, valorPedido);
            fail("Era esperada IllegalArgumentException para estado inválido.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Estado não reconhecido"),
                    "Mensagem da exceção deve indicar estado não reconhecido.");
        }
    }
}