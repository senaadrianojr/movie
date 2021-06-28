package com.br.movie.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Movie implements Serializable {
	
	private static final long serialVersionUID = 7962334093033425347L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotNull
	private Integer year;
	@NotBlank
	private String title;
	@NotBlank
	private String studio;
	@Column(columnDefinition = "boolean default false")
	private Boolean winner = Boolean.FALSE;
	@OrderBy("name ASC")
	@ManyToMany(mappedBy = "movies", cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
	private Set<Producer> producers = new HashSet<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStudio() {
		return studio;
	}
	public void setStudio(String studio) {
		this.studio = studio;
	}
	public Boolean getWinner() {
		return winner;
	}
	public void setWinner(Boolean winner) {
		this.winner = winner;
	}
	public Set<Producer> getProducers() {
		return producers;
	}
	public void setProducers(Set<Producer> producers) {
		this.producers = producers;
	}
	public void addProducer(Producer producer) {
		this.producers.add(producer);
		producer.getMovies().add(this);
	}

}
