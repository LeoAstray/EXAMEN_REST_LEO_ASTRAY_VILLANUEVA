package leoastray.examencinerest.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;
import leoastray.examencinerest.model.entity.Pelicula;
import leoastray.examencinerest.model.repository.IActorRepository;
import leoastray.examencinerest.model.repository.IPeliculaRepository;
import leoastray.examencinerest.model.repository.IRepartoRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController 
@RequestMapping("/api/peliculas") 
public class PeliculaController {

    @Autowired 
    private IPeliculaRepository peliculaRepository;

    @Autowired 
    private IActorRepository actorRepository;

    @Autowired 
    private IRepartoRepository repartoRepository;

    @PostMapping
    public ResponseEntity<PeliculaDetalleDTO> crear(@RequestBody String entity) {
        Pelicula pelicula = peliculaRepository.save(new Pelicula(
                entity.titulo(), entity.director(), entity.anio(), entity.duracion()));
        PeliculaDetalleDTO detalle = new PeliculaDetalleDTO(
                pelicula.getId(), pelicula.getTitulo(), pelicula.getDirector(),
                pelicula.getAnio(), pelicula.getDuracion(), List.of());
        URI location = URI.create("/api/peliculas/" + pelicula.getId());
        return ResponseEntity.created(location).body(detalle);
    }

    @GetMapping
    public ResponseEntity<List<PeliculaListadoDTO>> getMethodName(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/{id}")
    public String getMethodName(@PathVariable Long id) {
        return new String();
    }
    
    
    
}
