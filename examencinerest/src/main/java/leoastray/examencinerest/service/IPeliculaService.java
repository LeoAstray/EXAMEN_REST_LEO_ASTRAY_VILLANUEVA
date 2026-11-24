package leoastray.examencinerest.service;

import java.util.List;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;

public interface IPeliculaService {

    PeliculaDetalleDTO crear(PeliculaRequestDTO dto);

    List<PeliculaListadoDTO> leerLista();

    PeliculaDetalleDTO leerDetalle(Long id);
    //TODO - Metodo leerDetalle de peliculas

    PeliculaListadoDTO actualizar(PeliculaRequestDTO dto);

    void eliminar(Long id);
}
