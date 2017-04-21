package com.api.counter.application;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.api.counter.dto.SearchResult;
import com.api.counter.dto.SearchText;

@RestController
@RequestMapping("/counter-api")
public class WordCounterService {
	private Logger log = Logger.getLogger(WordCounterService.class.getName());

	private Map<String, Long> countOfWordsFromSource = null;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${text.source.file.name}")
	private String sourceFileName;


	@PostConstruct
	private void iniDataForTesting() {
		try {
			loadCountOfWordsFromSource();
			log.info("Initialized WordCount");
		} catch (IOException e) {
			log.severe("Error While inititalizing the Source : ");
			throw new IllegalStateException(e);
		}
	}

	@RequestMapping("/search")
	@PreAuthorize("hasRole('ADMIN')")
	SearchResult getWordCount(@RequestBody SearchText searchText) {
		Map<String, Long> wordCount = new HashMap<String, Long>(countOfWordsFromSource);
		Map<String, Long> searchTextCount = getSearchTextCount(searchText, wordCount);
		SearchResult result = new SearchResult();
		result.setCounts(searchTextCount.entrySet());
		return result;
	}

	@RequestMapping( value = "/top/{count}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
	public String getTopWord(@PathVariable int count) {

		Map<String, Long> wordCount = new HashMap<String, Long>(countOfWordsFromSource);

		Map<String, Long> result = reverseWordByCount(wordCount);

		return formatTopCount(count, result);

	}

	private Map<String, Long> getSearchTextCount(SearchText searchText, Map<String, Long> wordCount) {
		Map<String, Long> counts = new HashMap<>();

		log.info("search texts :" + searchText.getSearchText());

		searchText.getSearchText().forEach(key -> {
			if (wordCount.containsKey(key)) {
				counts.put(key, wordCount.get(key));
			} else {
				counts.put(key, 0L);
			}
		});
		return counts;
	}
	
	private String formatTopCount(int count, Map<String, Long> result) {
		String resultStr = "";
		int i = 0;
		for (Map.Entry<String, Long> entry : result.entrySet()) {
			resultStr += entry.getKey() + "|" + entry.getValue() + " ";
			if (i > count)
				break;
			i++;
		}
		return resultStr.trim();
	}

	private Map<String, Long> reverseWordByCount(Map<String, Long> wordCount) {
		Map<String, Long> result = new LinkedHashMap<>();

		wordCount.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		return result;
	}

	private Map<String, Long> loadCountOfWordsFromSource() throws IOException {
//		ClassPathResource classPathResource = new ClassPathResource(sourceFileName);
//		Path path = Paths.get(classPathResource.getFile().toURI());
		
//		Path path = Paths.get(resourceLoader.getResource(sourceFileName).getFile().toURI());
		
		File f = new File(sourceFileName);
		InputStream is = new FileInputStream(f);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		countOfWordsFromSource = reader.lines()
				.flatMap(line -> Arrays.stream(line.trim().split("\\s")))
				.filter(word -> word.length() > 0)
				.map(word -> new SimpleEntry<>(word, 1))
				.collect(Collectors.groupingBy(SimpleEntry::getKey, Collectors.counting()));
		
		
		log.log(Level.INFO, "Loaded values :", countOfWordsFromSource);
		return countOfWordsFromSource;

	}

}
