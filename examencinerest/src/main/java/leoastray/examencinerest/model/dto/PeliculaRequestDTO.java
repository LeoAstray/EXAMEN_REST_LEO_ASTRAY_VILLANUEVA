package leoastray.examencinerest.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PeliculaRequestDTO(@NotBlank(message = "El título es obligatorio") String titulo, @NotBlank(message = "El director es obligatorio") String director, @Min(value = 1888) Integer anio, @Positive Integer duracion) {

}
