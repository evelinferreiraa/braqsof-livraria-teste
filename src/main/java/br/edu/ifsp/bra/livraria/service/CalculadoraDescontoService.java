package br.edu.ifsp.bra.livraria.service;

import br.edu.ifsp.bra.livraria.entity.Cliente;

/**
 * Serviço responsável por aplicar a Regra de Negócio RN01.
 *
 * RN01 define o percentual de desconto de acordo com o tempo de vínculo
 * do cliente com a loja. A classificação é feita em quatro perfis:
 *
 *  - < 1 ano           → Perfil BÁSICO  → 0% de desconto
 *  - 1 a 3 anos        → Perfil BRONZE  → 3% de desconto
 *  - 3 a 5 anos        → Perfil PRATA   → 5% de desconto
 *  - ≥ 5 anos          → Perfil OURO    → 10% de desconto
 *
 * Essa regra é aplicada durante o fluxo do caso de uso "Efetuar Pedido de Livro",
 * sendo chamada por PedidoService.
 */
public class CalculadoraDescontoService {

    /**
     * Executa o cálculo do valor do desconto a partir do perfil do cliente.
     *
     * O método consulta o tempo de vínculo (anos completos) e aplica
     * a porcentagem correspondente à RN01.
     *
     * @param cliente      cliente cujo perfil deve ser avaliado
     * @param valorPedido  valor bruto do pedido antes da aplicação de desconto
     * @return o valor numérico do desconto calculado
     */
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

    /**
     * Identifica o perfil nominal do cliente (BASICO, BRONZE, PRATA, OURO),
     * conforme a tabela de RN01.
     *
     * Esse método é utilizado principalmente para fins de validação,
     * geração de relatórios e suporte aos testes unitários.
     *
     * @param cliente cliente cujo perfil será classificado
     * @return string representando o perfil definido pela RN01
     */
    public String identificarPerfil(Cliente cliente) {
        long anosVinculo = cliente.getTempoVinculoEmAnos();

        if (anosVinculo < 1) return "BASICO";
        else if (anosVinculo >= 1 && anosVinculo < 3) return "BRONZE";
        else if (anosVinculo >= 3 && anosVinculo < 5) return "PRATA";
        else return "OURO";
    }
}