package com.br.movie.dto;

import java.util.ArrayList;
import java.util.List;

public class ProducerWinnerReport {
	
	private List<ProducerWinnerDetail> min = new ArrayList<>();
	private List<ProducerWinnerDetail> max = new ArrayList<>();
	
	public List<ProducerWinnerDetail> getMin() {
		return min;
	}
	public void setMin(List<ProducerWinnerDetail> min) {
		this.min = min;
	}
	public List<ProducerWinnerDetail> getMax() {
		return max;
	}
	public void setMax(List<ProducerWinnerDetail> max) {
		this.max = max;
	}

}
