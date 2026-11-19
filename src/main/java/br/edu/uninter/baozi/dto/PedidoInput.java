package br.edu.uninter.baozi.dto;

import jakarta.validation.constraints.*;

public record PedidoInput(
        @NotNull @Positive Long clienteId,
        @NotNull @Positive Long produtoId,
        @NotNull @Positive Integer quantidade
) {}
