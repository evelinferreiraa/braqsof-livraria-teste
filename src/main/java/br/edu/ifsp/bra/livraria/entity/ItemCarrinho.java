package br.edu.ifsp.bra.livraria.entity;

public class ItemCarrinho {
    private String livroTitulo;
    private int quantidade;
    private double precoUnitario;

    public ItemCarrinho() {}

    public ItemCarrinho(String livroTitulo, int quantidade, double precoUnitario) {
        this.livroTitulo = livroTitulo;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
    public String getLivroTitulo() { return livroTitulo; }
    public void setLivroTitulo(String livroTitulo) { this.livroTitulo = livroTitulo; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }

    public double getSubtotal() {
        return quantidade * precoUnitario;
    }
}