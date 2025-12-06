package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.Cliente;

/**
 * Implementação da Regra de Negócio RN01:
 * - < 1 ano: 0% (perfil básico)
 * - 1 a 3 anos: 3% (perfil bronze)
 * - 3 a 5 anos: 5% (perfil prata)
 * - > 5 anos: 10% (perfil ouro)
 */
public class CalculadoraDescontoService {

    public double calcularDesconto(Cliente cliente, double valorPedido) {
        long anosVinculo = cliente.getTempoVinculoEmAnos();

        if (anosVinculo < 1) {
            return 0.0; // Perfil básico
        } else if (anosVinculo >= 1 && anosVinculo < 3) {
            return valorPedido * 0.03; // Bronze
        } else if (anosVinculo >= 3 && anosVinculo < 5) {
            return valorPedido * 0.05; // Prata
        } else {
            return valorPedido * 0.10; // Ouro
        }
    }

    public String identificarPerfil(Cliente cliente) {
        long anosVinculo = cliente.getTempoVinculoEmAnos();

        if (anosVinculo < 1) return "BASICO";
        else if (anosVinculo >= 1 && anosVinculo < 3) return "BRONZE";
        else if (anosVinculo >= 3 && anosVinculo < 5) return "PRATA";
        else return "OURO";
    }
}