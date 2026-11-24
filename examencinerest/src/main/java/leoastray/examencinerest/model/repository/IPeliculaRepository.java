package leoastray.examencinerest.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import leoastray.examencinerest.model.entity.Pelicula;

public interface IPeliculaRepository extends JpaRepository<Pelicula, Long> {

}
