package com.ssafy.trip.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.board.model.dto.BoardDto;
import com.ssafy.trip.board.model.dto.BoardListDto;
import com.ssafy.trip.board.model.dto.CommentDto;
import com.ssafy.trip.board.model.dto.CommentListDto;
import com.ssafy.trip.board.model.dto.FileInfoDto;
import com.ssafy.trip.board.model.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	private BoardMapper boardMapper;

	@Autowired
	public BoardServiceImpl(BoardMapper boardMapper) {
		super();
		this.boardMapper = boardMapper;
	}

	@Override
	@Transactional
	public int writeArticle(BoardDto boardDto) throws Exception {
		int res = boardMapper.writeArticle(boardDto);
		List<FileInfoDto> fileInfos = boardDto.getFileInfos();
//		if (fileInfos != null && !fileInfos.isEmpty()) {
//			boardMapper.registerFile(boardDto);
//		}
		return res;
	}

	@Override
	public BoardListDto listArticle(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("keyword", map.get("keyword") == null ? "" : map.get("keyword"));
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = Integer.parseInt(map.get("perPage") == null ? "20" : map.get("perPage"));
		int start = currentPage * sizePerPage - sizePerPage;
		param.put("start", start);
		param.put("listsize", sizePerPage);

		String keywordType = map.get("keywordType");
		param.put("keywordType", keywordType == null ? "" : keywordType);
		if ("nickname".equals(keywordType))
			param.put("keywordType", keywordType == null ? "" : "m.nickname");
		List<BoardDto> list = boardMapper.listArticle(param);

		int totalArticleCount = boardMapper.getTotalArticleCount(param);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		BoardListDto boardListDto = new BoardListDto();
		boardListDto.setArticles(list);
		boardListDto.setCurrentPage(currentPage);
		boardListDto.setTotalPageCount(totalPageCount);

		return boardListDto;
	}

//	@Override
//	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception {
//		PageNavigation pageNavigation = new PageNavigation();
//
//		int naviSize = SizeConstant.NAVIGATION_SIZE;
//		int sizePerPage = SizeConstant.LIST_SIZE;
//		int currentPage = Integer.parseInt(map.get("pgno"));
//
//		pageNavigation.setCurrentPage(currentPage);
//		pageNavigation.setNaviSize(naviSize);
//		Map<String, Object> param = new HashMap<String, Object>();
//		String key = map.get("key");
//		if ("userid".equals(key))
//			key = "user_id";
//		param.put("key", key == null ? "" : key);
//		param.put("word", map.get("word") == null ? "" : map.get("word"));
//		int totalCount = boardMapper.getTotalArticleCount(param);
//		pageNavigation.setTotalCount(totalCount);
//		int totalPageCount = (totalCount - 1) / sizePerPage + 1;
//		pageNavigation.setTotalPageCount(totalPageCount);
//		boolean startRange = currentPage <= naviSize;
//		pageNavigation.setStartRange(startRange);
//		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
//		pageNavigation.setEndRange(endRange);
//		pageNavigation.makeNavigator();
//
//		return pageNavigation;
//	}

	@Override
	public BoardDto getArticle(int no) throws Exception {
		return boardMapper.getArticle(no);
	}

	@Override
	public void updateHit(int no) throws Exception {
		boardMapper.updateHit(no);
	}

	@Override
	public int modifyArticle(BoardDto boardDto) throws Exception {
		// TODO : BoardDaoImpl의 modifyArticle 호출
		return boardMapper.modifyArticle(boardDto);
	}

//	@Override
//	@Transactional
//	public void deleteArticle(int articleNo, String path) throws Exception {
//		// TODO : BoardDaoImpl의 deleteArticle 호출
//		List<FileInfoDto> fileList = boardMapper.fileInfoList(articleNo);
//		boardMapper.deleteFile(articleNo);
//		boardMapper.deleteArticle(articleNo);
//		for(FileInfoDto fileInfoDto : fileList) {
//			File file = new File(path + File.separator + fileInfoDto.getSaveFolder() + File.separator + fileInfoDto.getSaveFile());
//			file.delete();
//		}
//	}

	@Override
	public int deleteArticle(int no) throws Exception {
		// TODO : BoardDaoImpl의 deleteArticle 호출
		return boardMapper.deleteArticle(no);
	}

	@Override
	public int writeArticleComment(CommentDto commentDto) throws Exception {
		return boardMapper.writeArticleComment(commentDto);
	}

	@Override
	public CommentListDto getArticleComments(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		int articleNo = Integer.parseInt(map.get("article_no"));
		int sizePerPage = Integer.parseInt(map.get("perPage") == null ? "20" : map.get("perPage"));
		param.put("article_no", articleNo);
		int totalArticleCount = boardMapper.getTotalArticleCommentsCount(param);
		System.out.println(totalArticleCount);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;
		int currentPage;
		if (totalPageCount == 0) {
			currentPage = 1;
		} else {
			currentPage = map.get("pgno") == null ? totalPageCount : Integer.parseInt(map.get("pgno"));
		}

		int start = currentPage * sizePerPage - sizePerPage;
		param.put("start", start);
		param.put("listsize", sizePerPage);
		param.put("article_no", articleNo);

		List<CommentDto> list = boardMapper.getArticleComments(param);

		CommentListDto commentListDto = new CommentListDto();
		commentListDto.setComments(list);
		commentListDto.setCurrentPage(currentPage);
		commentListDto.setTotalPageCount(totalPageCount);

		return commentListDto;
	}

	@Override
	public int deleteArticleComment(int article_no, int c_id) throws Exception {
		return boardMapper.deleteArticleComment(article_no, c_id);
	}

	@Override
	public List<BoardDto> listTopArticle(){
		return boardMapper.listTopArticle();
	}

}
