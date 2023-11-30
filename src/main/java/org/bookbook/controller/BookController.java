package org.bookbook.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bookbook.domain.BestVO;
import org.bookbook.domain.BookIdVO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
				
        String flaskApiUrl = "http://49.50.166.252:5000/api/list";

        RestTemplate restTemplate = new RestTemplate();

        String keywordParam = (search.getKeywords() != null) ?
                String.join(",", search.getKeywords()) :
                "";
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(flaskApiUrl)
                .queryParam("keyword", keywordParam)
                ;
        String response = restTemplate.getForObject(builder.toUriString(), String.class);
        

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            JsonNode resultNode = jsonNode.get("result");
                                                
            if (resultNode != null && resultNode.isArray()) {
            	
                List<Long> bookIds = StreamSupport.stream(resultNode.spliterator(), false)
                        .map(JsonNode::asLong)
                        .collect(Collectors.toList());
                
                log.info("bookIds : "+bookIds);
                
                if (!bookIds.isEmpty()) {                	
                	List<BookVO> books = service.getBookListById(bookIds);
                	
                	model.addAttribute("bookByCBF", books);
                }

            }
        } catch (Exception e) {
            System.out.println("------------>Error");
        }

        
    	List<BestVO> bestBooks = service.getBestBookList();
    	
    	model.addAttribute("best", bestBooks);
        
		
		List<BookVO> dataResult = service.getListPaging(cri);

		model.addAttribute("list", dataResult);
				
		int total = service.getTotal();
		 
		PageMakerDTO pagemake = new PageMakerDTO(cri, total);

		model.addAttribute("pageMaker", pagemake); // 키 : 밸류
		
	}
}
