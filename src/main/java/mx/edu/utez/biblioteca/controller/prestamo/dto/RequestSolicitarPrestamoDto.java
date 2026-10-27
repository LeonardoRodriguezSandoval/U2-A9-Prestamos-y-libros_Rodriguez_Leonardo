package mx.edu.utez.biblioteca.controller.prestamo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestSolicitarPrestamoDto {
    @NotNull(message = "El lector es obligatorio")
    private Long lectorId;

    @NotNull(message = "El libro es obligatorio")
    private Long libroId;
}
