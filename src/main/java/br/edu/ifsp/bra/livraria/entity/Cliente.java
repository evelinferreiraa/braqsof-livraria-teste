package br.edu.ifsp.bra.livraria.entity;

import java.time.LocalDate;

public class Cliente {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataCadastro;

    public Cliente() {}

    public Cliente(Long id, String nome, String email, LocalDate dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataCadastro = dataCadastro;
    }

    public long getTempoVinculoEmAnos() {
        return java.time.temporal.ChronoUnit.YEARS.between(dataCadastro, LocalDate.now());
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
}