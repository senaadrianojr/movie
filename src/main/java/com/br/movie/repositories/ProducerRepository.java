package com.br.movie.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.movie.entities.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
	
	Optional<Producer> findByName(String name);

}
