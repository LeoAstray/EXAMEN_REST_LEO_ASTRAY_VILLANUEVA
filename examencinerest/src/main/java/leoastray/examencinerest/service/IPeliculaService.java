package leoastray.examencinerest.service;

import java.util.List;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;
import leoastray.examencinerest.model.dto.RepartoRequestDTO;

public interface IPeliculaService {

    PeliculaDetalleDTO crear(PeliculaRequestDTO dto);

    List<PeliculaListadoDTO> leerLista();

    List<PeliculaListadoDTO> buscarPorAnio(Integer anio);

    PeliculaDetalleDTO leerDetalle(Long id);

    PeliculaDetalleDTO agregarReparto(Long id, RepartoRequestDTO dto);

    PeliculaListadoDTO actualizar(Long id, PeliculaRequestDTO dto);

    void eliminar(Long id);
}
