package org.bookbook.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bookbook.domain.BookSearchVO;
import org.bookbook.domain.BookVO;
import org.bookbook.domain.GenreVO;
import org.bookbook.domain.TopicVO;
import org.bookbook.model.Criteria;
import org.bookbook.model.PageMakerDTO;
import org.bookbook.service.BookSearchService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/book")
@Controller
public class BookController {
	@Autowired
	BookSearchService service;

	@ModelAttribute("searchBook")
	public JSONObject searchBookTypes(TopicVO topics, GenreVO genres) {
		List<TopicVO> topicList = service.getTopicList(topics);

		List<GenreVO> genreList = service.getGenreList(genres);

		Map<String, List<String>> genreConvertedMap = convertToMap(genreList);


		Map<String, Map<String, List<String>>> map = new LinkedHashMap<>();


		for (TopicVO topic : topicList) {
			Map<String, List<String>> genreMap = new LinkedHashMap<>();

			String genreToString = topic.getGenres();

			List<String> genreToList = new ArrayList<>(Arrays.asList(genreToString.split(", ")));

			for (String genre : genreToList) {
				List<String> categoriesToList = genreConvertedMap.get(genre);

				if (categoriesToList == null) {
					categoriesToList = new ArrayList<>();
				}

				genreMap.put(genre, categoriesToList);
			}

			map.put(topic.getTopic(), genreMap);
		}

		JSONObject jsonObject = new JSONObject(map);

		return jsonObject;
	}

	public static Map<String, List<String>> convertToMap(List<GenreVO> genreList) {
		Map<String, List<String>> genreMap = new HashMap<>();

		for (GenreVO genreVO : genreList) {
			String genre = genreVO.getGenre();
			String categoriesToString = genreVO.getCategories();

			List<String> categoriesList = new ArrayList<>();

			if (categoriesToString != null) {
				categoriesList = new ArrayList<String>(Arrays.asList(categoriesToString.split(", ")));
			}


			genreMap.put(genre, categoriesList);
		}


		return genreMap;
	}

	@GetMapping("/list")
	public void list(@ModelAttribute("search") BookSearchVO search, Model model, Criteria cri) {

		log.info("list Page");
		log.info(search);
		
        String flaskApiUrl = "http://49.50.166.252:5000/api/list";
//		String flaskApiUrl = "http://127.0.0.1:5000/api/list";

        RestTemplate restTemplate = new RestTemplate();

        log.info("Start!!!!");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(flaskApiUrl)
                .queryParam("keyword", search.getKeywords() != null ? String.join(",", search.getKeywords()) : "")
                .queryParam("topic",
                        search.getTopics() != null ? String.join(",", search.getTopics()) : "")
                .queryParam("genre", search.getBookType() != null ? String.join(",", search.getBookType()) : "")
                .queryParam("category",
                        search.getSelectedCategories() != null ? String.join(",", search.getSelectedCategories()) : "");

        log.info(builder);

        log.info("End!!!!");

        String response = restTemplate.getForObject(builder.toUriString(), String.class);

        log.info("Flask API : " + response);

		
		
		List<BookVO> dataResult = service.getListPaging(cri);

		model.addAttribute("list", dataResult);
		
		log.info("dataResult:"+dataResult);
		
		int total = service.getTotal();
		 
		PageMakerDTO pagemake = new PageMakerDTO(cri, total);

		model.addAttribute("pageMaker", pagemake);
		
	}
}
