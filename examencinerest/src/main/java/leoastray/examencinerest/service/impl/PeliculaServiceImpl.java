package leoastray.examencinerest.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;
import leoastray.examencinerest.model.entity.Pelicula;
import leoastray.examencinerest.model.repository.IPeliculaRepository;
import leoastray.examencinerest.service.IPeliculaService;

@Service 
public class PeliculaServiceImpl implements IPeliculaService {

    private final IPeliculaRepository peliculaRepository;

    public PeliculaServiceImpl(IPeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    @Override
    @Transactional
    public PeliculaDetalleDTO crear(PeliculaRequestDTO dto) {
        Pelicula pelicula = peliculaRepository.save(
                new Pelicula(dto.titulo(), dto.director(), dto.anio(), dto.duracion()));
        return new PeliculaDetalleDTO(pelicula.getId(), pelicula.getTitulo(), pelicula.getDirector(),
                pelicula.getAnio(), pelicula.getDuracion(), List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeliculaListadoDTO> leerLista() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leerLista'");
    }

    @Override
    @Transactional(readOnly = true)
    public PeliculaDetalleDTO leerDetalle(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leerDetalle'");
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
    }

    @Override
    @Transactional
    public PeliculaListadoDTO actualizar(PeliculaRequestDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }

}
