package com.br.movie.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.br.movie.services.MovieService;

@RestController
@RequestMapping("movies")
public class MovieResource {
	
	@Autowired
	private MovieService service;
	
	@PostMapping("/csv")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile uploadFile) {
		if (uploadFile == null || uploadFile.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Select a file");
		if(!uploadFile.getContentType().equals("text/csv"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File extension not supported. Select CSV file");
		
		try {
			String tmpFileName = service.saveTemporaryCSVFile(uploadFile);
			service.asyncReadFileAndSaveData(tmpFileName);
		} catch (Exception e) {
			ResponseEntity.internalServerError();
		}
		
		return ResponseEntity.ok("File uploaded successfully. Wait a moment for the data to be saved");
	}
	
	@GetMapping("/producers/report")
	public ResponseEntity<?> getReport() {
		return ResponseEntity.ok(service.getProducerWinnerReport());
	}
}
