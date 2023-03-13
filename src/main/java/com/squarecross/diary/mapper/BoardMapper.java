package com.squarecross.diary.mapper;

import com.squarecross.diary.domain.Board;
import com.squarecross.diary.dto.BoardDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class BoardMapper {

    public static List<BoardDto> boardsToDtos(List<Board> boards) {
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            BoardDto boardDto = new BoardDto();
            boardDto.setBoardId(board.getBoardId());
            boardDto.setContent(board.getContent());
            boardDto.setTitle(board.getTitle());
            boardDto.setCreatedDate(board.getCreatedDate());
            boardDto.setModifiedDate(board.getModifiedDate());
            boardDto.setAuthor(board.getAuthor());
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public static BoardDto boardToDto(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardId(board.getBoardId());
        boardDto.setContent(board.getContent());
        boardDto.setTitle(board.getTitle());
        boardDto.setCreatedDate(board.getCreatedDate());
        boardDto.setModifiedDate(board.getModifiedDate());
        boardDto.setAuthor(board.getAuthor());
        return boardDto;
    }

    public static Board dtoToBoard(BoardDto boardDto) {
        Board board = new Board();
        board.setContent(boardDto.getContent());
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setAuthor(boardDto.getAuthor());
        return board;
    }
}
