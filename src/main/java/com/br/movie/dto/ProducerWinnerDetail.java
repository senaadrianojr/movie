package com.br.movie.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProducerWinnerDetail {
	@JsonIgnore
	private Long producerId;
	private String producer;
	private Integer previousWin;
	private Integer followingWin;
	
	public ProducerWinnerDetail() {}
	
	public ProducerWinnerDetail(Long producerId, String producer, Integer previousWin) {
		super();
		this.producerId = producerId;
		this.producer = producer;
		this.previousWin = previousWin;
	}
	
	public ProducerWinnerDetail(ProducerWinnerDetail object) {
		super();
		if(object != null) {
			this.producerId = object.getProducerId();
			this.producer = object.getProducer();
			this.previousWin = object.getPreviousWin();
			this.followingWin = object.getFollowingWin();
		}
	}

	public Long getProducerId() {
		return producerId;
	}
	public void setProducerId(Long producerId) {
		this.producerId = producerId;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public Integer getPreviousWin() {
		return previousWin;
	}
	public void setPreviousWin(Integer previousWin) {
		this.previousWin = previousWin;
	}
	public Integer getFollowingWin() {
		return followingWin;
	}
	public void setFollowingWin(Integer followingWin) {
		this.followingWin = followingWin;
	}
	public Integer getInterval() {
		if(this.previousWin != null && this.followingWin != null)
			return this.followingWin - this.previousWin;
		return 0;
	}
	
}
