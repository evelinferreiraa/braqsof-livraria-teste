# Sistema de Livraria – Qualidade de Software (IFSP)

Este projeto implementa e testa, com rigor técnico, partes essenciais de um sistema de software para uma livraria.
O foco principal é o caso de uso “Efetuar Pedido de Livro”, bem como a aplicação das regras de negócio RN01, RN02 e RN03.

O repositório contém a implementação do domínio, serviços, repositórios simulados e testes baseados nas técnicas estudadas em aula.

## Objetivos do Projeto

### Implementar o fluxo computacional do caso de uso Efetuar Pedido de Livro.

Aplicar regras de negócio formais:
**RN01** — Perfil de cliente e desconto.
**RN02** — Cálculo de frete por região do Brasil.
**RN03** — Status do pedido.

### Desenvolver testes baseados em modelo (MBT) utilizando:

**GFC** – Grafo de Fluxo de Controle
**GE** – Grafo de Estado

## Diagrama de Atividades

Aplicar técnicas de teste:

- Partição de Equivalência (PE)
- Análise de Valor Limite (AVL)
- Node e Edge Coverage
- Integrar ferramentas de automação:
- JUnit 5
- Mockito
- JaCoCo (relatórios de cobertura)

```
braqsof-livraria-teste/
│
├── src/
│   ├── main/java/br/edu/ifsp/bra/livraria/
│   │   ├── Main.java
│   │   ├── entity/
│   │   │   ├── Cliente.java
│   │   │   ├── Endereco.java
│   │   │   ├── Pedido.java
│   │   │   └── ItemCarrinho.java
│   │   ├── service/
│   │   │   ├── CalculadoraDescontoService.java   # RN01
│   │   │   ├── CalculadoraFreteService.java      # RN02
│   │   │   └── PedidoService.java                # Caso de Uso
│   │   └── repository/
│   │       └── ClienteRepository.java
│   │
│   └── test/java/br/edu/ifsp/bra/livraria/service/
│       ├── CalculadoraDescontoServiceTest.java   # RN01 (PE, AVL)
│       ├── CalculadoraFreteServiceTest.java      # RN02 (MBT)
│       └── PedidoServiceTest.java                # Integração RN01 + RN02
│
├── pom.xml
└── README.md
```

## Regras de Negócio Implementadas

### RN01 – Perfil de Cliente e Desconto

Vínculo	    Perfil	Desconto
< 1 ano	    BÁSICO	0%
1–3 anos	BRONZE	3%
3–5 anos	PRATA	5%
≥ 5 anos	OURO	10%

Implementação: *CalculadoraDescontoService*.

## RN02 – Cálculo de Frete
Região	        UF	        Frete
SP	            SP          0%
Sudeste	        RJ, MG, ES	5%
Outras regiões	demais UFs	8%

Implementação: *CalculadoraFreteService*.

## RN03 – Status do Pedido

Estados possíveis:

- EM_PROCESSAMENTO
- CONFIRMADO
- CANCELADO
- ENTREGUE

Implementação: *PedidoService*.

## Estratégia de Testes

A validação do sistema utiliza várias técnicas de teste:

- Partição de Equivalência (PE)
Aplicada na RN01, garantindo um representante por partição.

- Análise de Valor Limite (AVL)
Testes nos pontos críticos 1, 3 e 5 anos de vínculo.

- Model-Based Testing (MBT)

Com base em:
- Grafo de Fluxo de Controle (GFC) – RN01 e RN02
- Grafo de Estados (GE) – RN03
- Diagrama de Atividades
Caminhos independentes foram derivados e transformados em testes.

- Uso de Mocks
*ClienteRepository* é simulado via Mockito.

### Projeto Acadêmico

Este trabalho foi desenvolvido para a disciplina
Qualidade de Software – IFSP Bragança Paulista,
demonstrando domínio de modelagem, regras de negócio, teste estruturado
e validação de software com ferramentas modernas.