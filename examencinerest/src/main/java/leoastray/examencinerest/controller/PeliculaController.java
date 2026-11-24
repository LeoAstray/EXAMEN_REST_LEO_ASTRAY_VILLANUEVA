package leoastray.examencinerest.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;
import leoastray.examencinerest.model.dto.RepartoDTO;
import leoastray.examencinerest.model.dto.RepartoRequestDTO;
import jakarta.validation.Valid;
import leoastray.examencinerest.model.entity.Actor;
import leoastray.examencinerest.model.entity.Pelicula;
import leoastray.examencinerest.model.repository.IActorRepository;
import leoastray.examencinerest.model.repository.IPeliculaRepository;
import leoastray.examencinerest.model.repository.IRepartoRepository;
import leoastray.examencinerest.service.IPeliculaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;


@RestController 
@RequestMapping("/api/peliculas") 
public class PeliculaController {

    @Autowired
    private IPeliculaService peliculaService;

    @Autowired 
    private IPeliculaRepository peliculaRepository;

    @Autowired 
    private IActorRepository actorRepository;

    @Autowired 
    private IRepartoRepository repartoRepository;

    @PostMapping
    public ResponseEntity<PeliculaDetalleDTO> crear(@RequestBody PeliculaRequestDTO dto) {
        Pelicula pelicula = peliculaRepository.save(new Pelicula(dto.titulo(), dto.director(), dto.anio(), dto.duracion()));
        PeliculaDetalleDTO detalle = new PeliculaDetalleDTO(pelicula.getId(), pelicula.getTitulo(), pelicula.getDirector(), pelicula.getAnio(), pelicula.getDuracion(), List.of());
        URI location = URI.create("/api/peliculas/" + pelicula.getId());
        return ResponseEntity.created(location).body(detalle);
    }

    @GetMapping
    public ResponseEntity<List<PeliculaListadoDTO>> leerLista() {
        List<PeliculaListadoDTO> peliculas = peliculaRepository.findAll().stream()
                .map(pelicula -> new PeliculaListadoDTO(pelicula.getId(), pelicula.getTitulo(),
                        pelicula.getDirector(), pelicula.getAnio()))
                .toList();
        return ResponseEntity.ok(peliculas);
    }

    @PostMapping("/{id}/reparto")
    public ResponseEntity<PeliculaDetalleDTO> agregarReparto(@PathVariable Long id,
            @Valid @RequestBody RepartoRequestDTO dto) {
        PeliculaDetalleDTO detalle = peliculaService.agregarReparto(id, dto);
        URI location = URI.create("/api/peliculas/" + id);
        return ResponseEntity.created(location).body(detalle);
    }

    @GetMapping("/buscar/{anio}")
    public ResponseEntity<List<PeliculaListadoDTO>> buscarPorAnio(@PathVariable Integer anio) {
        return ResponseEntity.ok(peliculaService.buscarPorAnio(anio));
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<PeliculaDetalleDTO> leerDetalle(@PathVariable Long id) {
        return peliculaRepository.findById(id)
                .map(pelicula -> {
                    List<RepartoDTO> reparto = pelicula.getRepartos().stream()
                            .map(papel -> {
                                Actor actor = papel.getActor();
                                return new RepartoDTO(actor == null ? null : actor.getId(),
                                        actor == null ? null : actor.getNombre(),
                                        papel.getPersonaje(), papel.getSalario());
                            })
                            .toList();
                    return ResponseEntity.ok(new PeliculaDetalleDTO(pelicula.getId(), pelicula.getTitulo(),
                            pelicula.getDirector(), pelicula.getAnio(), pelicula.getDuracion(), reparto));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PeliculaDetalleDTO> actualizar(@PathVariable Long id,
            @RequestBody PeliculaRequestDTO dto) {
        return peliculaRepository.findById(id)
                .map(pelicula -> {
                    pelicula.setTitulo(dto.titulo());
                    pelicula.setDirector(dto.director());
                    pelicula.setAnio(dto.anio());
                    pelicula.setDuracion(dto.duracion());
                    peliculaRepository.save(pelicula);
                    return leerDetalle(id);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return peliculaRepository.findById(id)
                .map(pelicula -> {
                    peliculaRepository.delete(pelicula);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
