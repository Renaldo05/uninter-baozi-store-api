package br.edu.uninter.baozi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    @Column(nullable = false)
    private Integer quantidade;

    public Pedido() {}
    public Pedido(Cliente cliente, Produto produto, Integer quantidade) {
        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
    }
    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Produto getProduto() { return produto; }
    public Integer getQuantidade() { return quantidade; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
