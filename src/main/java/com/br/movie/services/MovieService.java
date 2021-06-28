package com.br.movie.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.br.movie.dto.ProducerWinnerReport;

public interface MovieService {
	
	void asyncReadFileAndSaveData(String fileName) throws IOException;

	String saveTemporaryCSVFile(MultipartFile file) throws IllegalStateException, IOException;
	
	ProducerWinnerReport getProducerWinnerReport();

}
