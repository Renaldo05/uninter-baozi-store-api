package br.edu.uninter.baozi.controller;

import br.edu.uninter.baozi.dto.ClienteInput;
import br.edu.uninter.baozi.exception.RecursoNaoEncontradoException;
import br.edu.uninter.baozi.exception.RegraNegocioException;
import br.edu.uninter.baozi.model.Cliente;
import br.edu.uninter.baozi.repository.ClienteRepository;
import br.edu.uninter.baozi.repository.PedidoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteRepository clientes;
    private final PedidoRepository pedidos;
    public ClienteController(ClienteRepository clientes, PedidoRepository pedidos) {
        this.clientes = clientes; this.pedidos = pedidos;
    }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Cliente criar(@Valid @RequestBody ClienteInput input) {
        return clientes.save(new Cliente(input.nome(), input.clienteDesde()));
    }
    @GetMapping public List<Cliente> listar() { return clientes.findAll(); }
    @GetMapping("/{id}") public Cliente buscar(@PathVariable Long id) {
        return clientes.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado: " + id));
    }
    @PutMapping("/{id}") public Cliente atualizar(@PathVariable Long id, @Valid @RequestBody ClienteInput input) {
        Cliente cliente = buscar(id);
        cliente.setNome(input.nome()); cliente.setClienteDesde(input.clienteDesde());
        return clientes.save(cliente);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        if (!clientes.existsById(id)) throw new RecursoNaoEncontradoException("Cliente nao encontrado: " + id);
        if (pedidos.existsByClienteId(id)) throw new RegraNegocioException("Cliente possui pedidos; exclua os pedidos antes");
        clientes.deleteById(id);
    }
}
