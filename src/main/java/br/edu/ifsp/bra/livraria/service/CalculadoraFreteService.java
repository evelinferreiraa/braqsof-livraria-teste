package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.Endereco;
import org.springframework.stereotype.Service;

/**
 * Implementação da Regra de Negócio RN02:
 * - SP: frete isento (0%)
 * - Outros estados do Sudeste: 5%
 * - Outras regiões do Brasil: 8%
 */

@Service
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

        // Lista oficial de estados brasileiros
        String[] estadosBrasil = {
                "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS","MG",
                "PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"
        };

        // Verifica se é um estado válido do Brasil
        boolean estadoValido = false;
        for (String uf : estadosBrasil) {
            if (uf.equals(estado)) {
                estadoValido = true;
                break;
            }
        }

        if (!estadoValido) {
            throw new IllegalArgumentException("Estado não reconhecido como brasileiro: " + estado);
        }

        // SP → frete isento
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