package br.edu.uninter.baozi.controller;

import br.edu.uninter.baozi.dto.PedidoInput;
import br.edu.uninter.baozi.dto.PedidoResponse;
import br.edu.uninter.baozi.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;
    public PedidoController(PedidoService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(@Valid @RequestBody PedidoInput input) { return service.criar(input); }
    @GetMapping public List<PedidoResponse> listar() { return service.listar(); }
    @GetMapping("/{id}") public PedidoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public PedidoResponse atualizar(@PathVariable Long id, @Valid @RequestBody PedidoInput input) {
        return service.atualizar(id, input);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
