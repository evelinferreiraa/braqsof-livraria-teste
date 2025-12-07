package br.edu.ifsp.bra.livraria.api.dto;

import br.edu.ifsp.bra.livraria.entity.Endereco;
import br.edu.ifsp.bra.livraria.entity.ItemCarrinho;

import java.util.List;

public class PedidoRequestDTO {

    private Long clienteId;
    private Endereco enderecoEntrega;
    private List<ItemCarrinho> itens;
    private String formaPagamento;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
