package br.edu.ifsp.bra.livraria.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private Cliente cliente;
    private Endereco enderecoEntrega;
    private List<ItemCarrinho> itens = new ArrayList<>();
    private double valorItens;
    private double desconto;
    private double frete;
    private double valorTotal;
    private String formaPagamento;
    private String status; // RN03: EM_PROCESSAMENTO, CONFIRMADO, CANCELADO, ENTREGUE
    private LocalDateTime dataCriacao;

    public Pedido() {
        this.dataCriacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Endereco getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(Endereco enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public List<ItemCarrinho> getItens() { return itens; }
    public void setItens(List<ItemCarrinho> itens) { this.itens = itens; }

    public double getValorItens() { return valorItens; }
    public void setValorItens(double valorItens) { this.valorItens = valorItens; }

    public double getDesconto() { return desconto; }
    public void setDesconto(double desconto) { this.desconto = desconto; }

    public double getFrete() { return frete; }
    public void setFrete(double frete) { this.frete = frete; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}