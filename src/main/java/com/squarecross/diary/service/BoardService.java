package com.squarecross.diary.service;

import com.squarecross.diary.domain.Board;
import com.squarecross.diary.domain.User;
import com.squarecross.diary.dto.BoardDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.mapper.BoardMapper;
import com.squarecross.diary.mapper.UserMapper;
import com.squarecross.diary.repository.BoardRepository;
import com.squarecross.diary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BoardDto> getBoard(String loginId) {
        List<Board> boards = boardRepository.search(loginId);
        return BoardMapper.boardsToDtos(boards);
    }

    public void saveBoard(BoardDto boardDto, UserDto userDto) {
        Board board = BoardMapper.dtoToBoard(boardDto);
        Optional<User> res = userRepository.findByLoginId(userDto.getLoginId());
        User user = res.get();
        user.addBoard(board);
        boardRepository.save(board);
    }
}
