package leoastray.examencinerest.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import leoastray.examencinerest.model.entity.Actor;

public interface IActorRepository extends JpaRepository<Actor, Long> {

}
