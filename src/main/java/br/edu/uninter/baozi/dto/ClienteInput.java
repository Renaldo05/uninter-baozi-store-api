package br.edu.uninter.baozi.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ClienteInput(
        @NotBlank @Size(max = 120) String nome,
        @NotNull @PastOrPresent LocalDate clienteDesde
) {}
