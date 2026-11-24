package leoastray.examencinerest.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import leoastray.examencinerest.model.entity.Pelicula;

public interface IPeliculaRepository extends JpaRepository<Pelicula, Long> {

    List<Pelicula> findByTituloIgnoringCase(String titulo);

    List<Pelicula> findByAnioGreaterThanOrderByAnioAsc(Integer anio);

}
