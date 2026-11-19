package br.edu.uninter.baozi.service;

import br.edu.uninter.baozi.dto.PedidoInput;
import br.edu.uninter.baozi.dto.PedidoResponse;
import br.edu.uninter.baozi.exception.RecursoNaoEncontradoException;
import br.edu.uninter.baozi.exception.RegraNegocioException;
import br.edu.uninter.baozi.model.Cliente;
import br.edu.uninter.baozi.model.Pedido;
import br.edu.uninter.baozi.model.Produto;
import br.edu.uninter.baozi.repository.ClienteRepository;
import br.edu.uninter.baozi.repository.PedidoRepository;
import br.edu.uninter.baozi.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoService(PedidoRepository pedidos, ClienteRepository clientes, ProdutoRepository produtos) {
        this.pedidos = pedidos; this.clientes = clientes; this.produtos = produtos;
    }
    @Transactional
    public PedidoResponse criar(PedidoInput input) {
        return PedidoResponse.from(pedidos.save(montar(input)));
    }
    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id) {
        return PedidoResponse.from(pedidos.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido nao encontrado: " + id)));
    }
    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidos.findAll().stream().map(PedidoResponse::from).toList();
    }
    @Transactional
    public PedidoResponse atualizar(Long id, PedidoInput input) {
        Pedido existente = pedidos.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido nao encontrado: " + id));
        Pedido novo = montar(input);
        existente.setCliente(novo.getCliente());
        existente.setProduto(novo.getProduto());
        existente.setQuantidade(novo.getQuantidade());
        return PedidoResponse.from(pedidos.save(existente));
    }
    @Transactional
    public void excluir(Long id) {
        if (!pedidos.existsById(id)) throw new RecursoNaoEncontradoException("Pedido nao encontrado: " + id);
        pedidos.deleteById(id);
    }
    private Pedido montar(PedidoInput input) {
        Cliente cliente = clientes.findById(input.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado: " + input.clienteId()));
        Produto produto = produtos.findById(input.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontrado: " + input.produtoId()));
        if (!Boolean.TRUE.equals(produto.getEstoque())) {
            throw new RegraNegocioException("Produto indisponivel para venda");
        }
        return new Pedido(cliente, produto, input.quantidade());
    }
}
