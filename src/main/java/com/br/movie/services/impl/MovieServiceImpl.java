package com.br.movie.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.br.movie.dto.ProducerWinnerDetail;
import com.br.movie.dto.ProducerWinnerReport;
import com.br.movie.entities.Movie;
import com.br.movie.entities.Producer;
import com.br.movie.entities.ProducerMovie;
import com.br.movie.repositories.MovieRepository;
import com.br.movie.repositories.ProducerMovieRepository;
import com.br.movie.repositories.ProducerRepository;
import com.br.movie.services.MovieService;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MovieServiceImpl implements MovieService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieServiceImpl.class);
	
	private static final String COMMA_DELIMITER = ";";
	
	private static final Pattern PATTERN_FOR_BOOLEAN = Pattern.compile("^(y|yes|true|1)$", Pattern.CASE_INSENSITIVE);
	
	private static final Pattern PATTERN_FOR_MANY_PRODUCERS = Pattern.compile("(\\s(and)\\s)|(,)", Pattern.CASE_INSENSITIVE);
	
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ProducerRepository producerRepository;
	@Autowired
	private ProducerMovieRepository producerMovieRepository;
	
	@Value("${dir.tmp}")
	private String dirTmp;
	
	@Override
	public String saveTemporaryCSVFile(MultipartFile file) throws IllegalStateException, IOException {
		File tempFile = new File(dirTmp.concat(file.getOriginalFilename()));
		file.transferTo(tempFile);
		return file.getOriginalFilename();
	}
	
	@Override
	@Async("threadPoolTaskExecutor")
	@Transactional
	public void asyncReadFileAndSaveData(String tmpFileName) throws IOException {
		BufferedReader reader = null;
		try {
			File tmpFile = new File(dirTmp.concat(tmpFileName));
			reader = new BufferedReader(new FileReader(tmpFile));
			String line;
			int lineNumer = 0;
			try {
				//Skip the first line (columns titles) - maybe validate title column order
				reader.readLine(); 
				lineNumer++;
				
				while ((line = reader.readLine()) != null) {
					if(!line.isBlank()) {
						String [] values = line.split(COMMA_DELIMITER);
						Movie movie = new Movie();
						movie.setYear(getIntValue(values[0]));
						movie.setTitle(values[1]);
						movie.setStudio(values[2]);
						if(values.length < 5) {
							movie.setWinner(Boolean.FALSE);
						} else {						
							movie.setWinner(getBooleanValue(values[4], Boolean.FALSE));
						}
						
						String producers = values[3];
						Matcher matcher = PATTERN_FOR_MANY_PRODUCERS.matcher(producers);
						if(matcher.find()) {
							populateProducerAndAddToMovie(movie, producers);
						} else {
							findOrCreateProducerAndAddToMovie(movie, producers);
						}
						
						movieRepository.saveAndFlush(movie);
					}
					lineNumer++;
				}
			} catch (Exception e) {
				LOGGER.error("Error one line {}", lineNumer);
			} finally {
				reader.close();
			}
			
		} catch (Exception e) {
			LOGGER.error("Error while reading file {}", e.getMessage());
		} finally {
			if(reader != null)
				reader.close();
		}
		
	}

	private void populateProducerAndAddToMovie(Movie movie, String producers) {
		String [] producerNames = producers.replaceFirst("\\s(and)", ",").split(",\\s");
		for (String name : producerNames) {
			findOrCreateProducerAndAddToMovie(movie, name);
		}
	}

	private void findOrCreateProducerAndAddToMovie(Movie movie, String producersName) {
		Optional<Producer> optProducer = producerRepository.findByName(producersName);
		if(optProducer.isPresent()) {
			Producer producer = optProducer.get();
			movie.addProducer(producer);
		} else {
			Producer producer = new Producer();
			producer.setName(producersName);
			movie.addProducer(producer);
		}
	}
	
	@Override
	public ProducerWinnerReport getProducerWinnerReport() {
		
		ProducerWinnerReport report = new ProducerWinnerReport();
		
		List<ProducerMovie> resultList = producerMovieRepository.findAllByWinner();

		//Recover producers with more than one winning movies
		List<ProducerMovie>  winners = resultList.stream()
				.filter(i -> Collections.frequency(resultList, i) > 1)
				.collect(Collectors.toList());
		
		//Not exist following wins
		if(resultList.isEmpty() || winners.isEmpty()) {
			return report;
		}
		
		//merging previous and following years
		ProducerWinnerDetail aux = null;
		Integer minInterval = null;
		Integer maxInterval = null;
		
		List<ProducerWinnerDetail> producerWinnerDetails = new ArrayList<>();
		for (ProducerMovie item : winners) {
			if(aux == null || !aux.getProducerId().equals(item.getId())) {
				aux = new ProducerWinnerDetail();
				aux.setProducerId(item.getId());
				aux.setProducer(item.getName());
				aux.setPreviousWin(item.getYear());
			} else {
				if(aux.getProducerId().equals(item.getId()) && aux.getFollowingWin() == null) {
					aux.setFollowingWin(item.getYear());
					producerWinnerDetails.add(new ProducerWinnerDetail(aux));
					if(minInterval == null || aux.getInterval().compareTo(minInterval) < 0) 
						minInterval = Integer.valueOf(aux.getInterval());
					if(maxInterval == null || aux.getInterval().compareTo(maxInterval) > 0) 
						maxInterval = Integer.valueOf(aux.getInterval());
				} 
			}
		}
		
		
		report.setMin(filterByInterval(producerWinnerDetails, minInterval));
		report.setMax(filterByInterval(producerWinnerDetails, maxInterval));
		
		return report;
	}
	
	private List<ProducerWinnerDetail> filterByInterval(List<ProducerWinnerDetail> list, final Integer interval) {
		return list.stream().filter(i -> i.getInterval().equals(interval)).collect(Collectors.toList());
	}
	
	private Integer getIntValue(String str) {
		return Integer.parseInt(str);
	}
	
	private Boolean getBooleanValue(String str, Boolean defaultOfBlank) {
		Matcher matcher = PATTERN_FOR_BOOLEAN.matcher(str);
		if (str == null || str.isBlank())
			return defaultOfBlank;
		return matcher.matches();
	}

}
