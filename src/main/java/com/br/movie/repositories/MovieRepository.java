package com.br.movie.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.movie.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	
}
