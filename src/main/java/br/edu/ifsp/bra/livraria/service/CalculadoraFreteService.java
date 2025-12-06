package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.Endereco;

/**
 * Implementação da Regra de Negócio RN02:
 * - SP: frete isento (0%)
 * - Outros estados do Sudeste: 5%
 * - Outras regiões do Brasil: 8%
 */
public class CalculadoraFreteService {

    public double calcularFrete(Endereco endereco, double valorPedido) {
        String estado = endereco.getEstado().toUpperCase();
        String regiao = identificarRegiao(estado);

        if (regiao.equals("SP")) {
            return 0.0;
        } else if (regiao.equals("SUDESTE")) {
            return valorPedido * 0.05;
        } else if (regiao.equals("OUTRAS_REGIOES")) {
            return valorPedido * 0.08;
        }
        throw new IllegalArgumentException("Estado não reconhecido: " + estado);
    }

    /**
     * Stub/Mock interno para simular serviço de frete externo.
     * Em produção, isso seria uma chamada HTTP a um Sistema de Frete.
     */
    private String identificarRegiao(String estado) {
        if (estado.equals("SP")) {
            return "SP";
        }

        // Estados do Sudeste (exceto SP)
        String[] sudeste = {"RJ", "MG", "ES"};
        for (String sigla : sudeste) {
            if (estado.equals(sigla)) {
                return "SUDESTE";
            }
        }

        // Demais regiões do Brasil
        return "OUTRAS_REGIOES";
    }
}