package br.edu.uninter.baozi.controller;

import br.edu.uninter.baozi.dto.ProdutoInput;
import br.edu.uninter.baozi.exception.RecursoNaoEncontradoException;
import br.edu.uninter.baozi.exception.RegraNegocioException;
import br.edu.uninter.baozi.model.Produto;
import br.edu.uninter.baozi.repository.PedidoRepository;
import br.edu.uninter.baozi.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoRepository produtos;
    private final PedidoRepository pedidos;
    public ProdutoController(ProdutoRepository produtos, PedidoRepository pedidos) {
        this.produtos = produtos; this.pedidos = pedidos;
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Produto criar(@Valid @RequestBody ProdutoInput input) {
        return produtos.save(new Produto(input.nome(), input.preco(), input.estoque()));
    }
    @GetMapping public List<Produto> listar() { return produtos.findAll(); }
    @GetMapping("/{id}") public Produto buscar(@PathVariable Long id) {
        return produtos.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontrado: " + id));
    }
    @PutMapping("/{id}") public Produto atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoInput input) {
        Produto produto = buscar(id);
        produto.setNome(input.nome()); produto.setPreco(input.preco()); produto.setEstoque(input.estoque());
        return produtos.save(produto);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        if (!produtos.existsById(id)) throw new RecursoNaoEncontradoException("Produto nao encontrado: " + id);
        if (pedidos.existsByProdutoId(id)) throw new RegraNegocioException("Produto possui pedidos; exclua os pedidos antes");
        produtos.deleteById(id);
    }
}
