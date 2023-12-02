package org.bookbook.service;

import java.util.List;

import org.bookbook.domain.BestVO;
import org.bookbook.domain.BookSearchVO;
import org.bookbook.domain.BookVO;
import org.bookbook.domain.GenreVO;
import org.bookbook.domain.LikeVO;
import org.bookbook.domain.TopicVO;
import org.bookbook.mapper.BookSearchMapper;
import org.bookbook.model.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class BookSearchServiceImpl implements BookSearchService {

	@Autowired
	BookSearchMapper bookSearchMapper;
	
	@Override
	public List<BestVO> getBestBookList() {
		
		return bookSearchMapper.selectBestBook();
	}

	@Override
	public List<BookVO> getBookListById(List<Long> bookIds) {
		
		return bookSearchMapper.selectBooksByIds(bookIds);
	}

	@Override
	public List<BookVO> getBookList(BookSearchVO bookSearch) {
		log.info(bookSearch);
		return bookSearchMapper.read(bookSearch);
	}

	@Override
	public List<GenreVO> getGenreList(GenreVO genre) {
		log.info(genre);
		return bookSearchMapper.getGenre(genre);
	}

	@Override
	public List<TopicVO> getTopicList(TopicVO Topic) {
		log.info(Topic);
		return bookSearchMapper.getTopic(Topic);
	}

	@Override
	public List<BookVO> getListPaging(Criteria cri) {
		return bookSearchMapper.getListPaging(cri);
	}

	@Override
	public int getTotal() {
		return bookSearchMapper.getTotal();
	}
	
	// 좋아요 추가 부분
	 @Override
	    public void addLike(LikeVO like) {
	        bookSearchMapper.addLike(like);
	    }

	    @Override
	    public List<LikeVO> getLikes(String userId) {
	        return bookSearchMapper.getLikes(userId);
	    }
}
