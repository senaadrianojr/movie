package com.br.movie.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.br.movie.entities.ProducerMovie;

@Repository
public class ProducerMovieRepository {
	
	@PersistenceContext
    private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public List<ProducerMovie> findAllByWinner() {
		
		Query query = em.createNativeQuery(" select p.id as id, p.name as name, m.year as year from movie m "
			+ " inner join producer_movie mp on mp.movie_id = m.id "
			+ " inner join producer p on mp.producer_id = p.id "
			+ " where m.winner = true "
			+ " order by p.name asc, m.year asc ");
		
		final List<Object[]> resultSet = query.getResultList();
		List<ProducerMovie> result = new ArrayList<>();
		resultSet.forEach(object -> {
			ProducerMovie pm = new ProducerMovie();
			pm.setId(Long.valueOf(object[0].toString()));
			pm.setName(object[1].toString());
			pm.setYear(Integer.valueOf(object[2].toString()));
			result.add(pm);
		});
		
		return result;
	}

}
