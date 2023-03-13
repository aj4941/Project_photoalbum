package com.squarecross.diary.service;

import com.squarecross.diary.domain.Board;
import com.squarecross.diary.domain.User;
import com.squarecross.diary.dto.BoardDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.mapper.BoardMapper;
import com.squarecross.diary.mapper.UserMapper;
import com.squarecross.diary.repository.BoardRepository;
import com.squarecross.diary.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BoardDto> getBoards(String loginId) {
        List<Board> boards = boardRepository.search(loginId);
        return BoardMapper.boardsToDtos(boards);
    }

    public Board findBoard(Long boardId) {
        Optional<Board> res = boardRepository.findById(boardId);
        if (res.isPresent()) {
            return res.get();
        }
        else {
            throw new EntityNotFoundException("게시글이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void saveBoard(BoardDto boardDto, UserDto userDto) {
        Optional<User> res = userRepository.findByLoginId(userDto.getLoginId());
        User user = res.get();
        Board board = BoardMapper.dtoToBoard(boardDto);
        user.addBoard(board);
        boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(Long boardId, BoardDto boardDto) {
        Optional<Board> res = boardRepository.findById(boardId);
        Board board = res.get();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}
