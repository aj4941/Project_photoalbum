package com.squarecross.diary.mapper;

import com.squarecross.diary.domain.Board;
import com.squarecross.diary.dto.BoardDto;

import java.util.ArrayList;
import java.util.List;

public class BoardMapper {

    public static List<BoardDto> boardsToDtos(List<Board> boards) {
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Board board : boards) {
            BoardDto boardDto = new BoardDto();
            boardDto.setTitle(board.getTitle());
            boardDto.setCreatedDate(board.getCreatedDate());
            boardDto.setModifiedDate(board.getModifiedDate());
            boardDto.setAuthor(board.getAuthor());
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public static Board dtoToBoard(BoardDto boardDto) {
        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setAuthor(boardDto.getAuthor());
        return board;
    }
}
