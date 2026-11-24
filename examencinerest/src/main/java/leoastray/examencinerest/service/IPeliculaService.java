package leoastray.examencinerest.service;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;

public interface IPeliculaService {

    PeliculaDetalleDTO crear(PeliculaRequestDTO);

    PeliculaListadoDTO leerLista(Long id);

    PeliculaDetalleDTO leerDetalle(Long id);

    PeliculaListadoDTO actualizar(PeliculaRequestDTO);

    void eliminar(Long id);
}
