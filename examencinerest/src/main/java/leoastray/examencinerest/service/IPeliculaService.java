package leoastray.examencinerest.service;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;

public interface IPeliculaService {

    PeliculaDetalleDTO crear(PeliculaRequestDTO dto);

    PeliculaListadoDTO leerLista(Long id);

    PeliculaDetalleDTO leerDetalle(Long id);

    PeliculaListadoDTO actualizar(PeliculaRequestDTO dto);

    void eliminar(Long id);
}
