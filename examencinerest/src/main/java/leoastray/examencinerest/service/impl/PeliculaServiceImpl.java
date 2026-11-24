package leoastray.examencinerest.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;
import leoastray.examencinerest.model.dto.RepartoDTO;
import leoastray.examencinerest.model.dto.RepartoRequestDTO;
import leoastray.examencinerest.model.entity.Actor;
import leoastray.examencinerest.model.entity.Pelicula;
import leoastray.examencinerest.model.entity.Reparto;
import leoastray.examencinerest.model.repository.IActorRepository;
import leoastray.examencinerest.model.repository.IPeliculaRepository;
import leoastray.examencinerest.model.repository.IRepartoRepository;
import leoastray.examencinerest.service.IPeliculaService;

@Service 
public class PeliculaServiceImpl implements IPeliculaService {

    private final IPeliculaRepository peliculaRepository;
    private final IActorRepository actorRepository;
    private final IRepartoRepository repartoRepository;

    public PeliculaServiceImpl(IPeliculaRepository peliculaRepository,
            IActorRepository actorRepository, IRepartoRepository repartoRepository) {
        this.peliculaRepository = peliculaRepository;
        this.actorRepository = actorRepository;
        this.repartoRepository = repartoRepository;
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
        return peliculaRepository.findAll().stream()
                .map(pelicula -> new PeliculaListadoDTO(pelicula.getId(), pelicula.getTitulo(),
                        pelicula.getDirector(), pelicula.getAnio()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeliculaListadoDTO> buscarPorAnio(Integer anio) {
        return peliculaRepository.findByAnioGreaterThanOrderByAnioAsc(anio).stream()
                .map(pelicula -> new PeliculaListadoDTO(pelicula.getId(), pelicula.getTitulo(),
                        pelicula.getDirector(), pelicula.getAnio()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PeliculaDetalleDTO leerDetalle(Long id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pelicula no encontrada"));
        List<RepartoDTO> reparto = pelicula.getRepartos().stream()
                .map(participacion -> {
                    Actor actor = participacion.getActor();
                    return new RepartoDTO(actor != null ? actor.getId() : null,
                            actor != null ? actor.getNombre() : null,
                            participacion.getPersonaje(), participacion.getSalario());
                })
                .toList();
        return new PeliculaDetalleDTO(pelicula.getId(), pelicula.getTitulo(), pelicula.getDirector(),
                pelicula.getAnio(), pelicula.getDuracion(), reparto);
    }

    @Override
    @Transactional
    public PeliculaDetalleDTO agregarReparto(Long id, RepartoRequestDTO dto) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pelicula no encontrada"));
        Actor actor = actorRepository.findById(dto.actorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor no encontrado"));
        if (repartoRepository.existsByPeliculaIdAndActorId(id, dto.actorId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El actor ya forma parte del reparto de la pelicula");
        }
        Reparto reparto = new Reparto(dto.personaje(), dto.salario(), pelicula, actor);
        pelicula.addRepartos(reparto);
        repartoRepository.save(reparto);
        return leerDetalle(id);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pelicula no encontrada"));
        peliculaRepository.delete(pelicula);
    }

    @Override
    @Transactional
    public PeliculaListadoDTO actualizar(Long id, PeliculaRequestDTO dto) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pelicula no encontrada"));
        pelicula.setTitulo(dto.titulo());
        pelicula.setDirector(dto.director());
        pelicula.setAnio(dto.anio());
        pelicula.setDuracion(dto.duracion());
        pelicula = peliculaRepository.save(pelicula);
        return new PeliculaListadoDTO(pelicula.getId(), pelicula.getTitulo(),
                pelicula.getDirector(), pelicula.getAnio());
    }

}
